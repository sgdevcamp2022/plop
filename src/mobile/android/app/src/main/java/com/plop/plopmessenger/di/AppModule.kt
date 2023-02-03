package com.plop.plopmessenger.di

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.room.Room
import com.plop.plopmessenger.data.local.AppDatabase
import com.plop.plopmessenger.data.pref.PrefDataSource
import com.plop.plopmessenger.data.repository.*
import com.plop.plopmessenger.domain.repository.*
import com.plop.plopmessenger.util.Constants
import com.plop.plopmessenger.util.NetworkInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient) : Retrofit {
        return Retrofit.Builder().apply {
            addConverterFactory(GsonConverterFactory.create())
            //baseUrl(BuildConfig.API_BASE_URL)
        }.build()
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        @AuthInterceptor interceptor: Interceptor,
        @LoggingInterceptor loggingInterceptor: Interceptor,
        @NetworkInterceptor networkInterceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(loggingInterceptor)
            .addNetworkInterceptor(networkInterceptor)
            .build()
    }

    @AuthInterceptor
    @Provides
    fun provideAuthInterceptor(
        pref: PrefDataSource
    ) : Interceptor = Interceptor{ chain ->

        val token = runBlocking(Dispatchers.IO) {
            pref.getAccessToken().first().let {
                if (it.isNotEmpty()) "Bearer $it" else ""
            }
        }

        val newRequest = chain
            .request()
            .newBuilder()
            .apply {
                if (token.isNotBlank()) {
                    addHeader(Constants.AUTHORIZATION, token)
                }
            }
            .build()


        return@Interceptor chain
            .proceed(newRequest)
    }


    @NetworkInterceptor
    @Provides
    fun provideNetworkConnectionInterceptor(
        @ApplicationContext context: Context
    ): Interceptor = NetworkInterceptor(context)

    @LoggingInterceptor
    @Provides
    fun provideLoggingInterceptor(): Interceptor =
        HttpLoggingInterceptor {
            Log.d("Logging", "Logging $it")
        }.setLevel(HttpLoggingInterceptor.Level.HEADERS)

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class AuthInterceptor

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class NetworkInterceptor

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class LoggingInterceptor


    @Singleton
    @Provides
    fun provideAppDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        ).build()
    }

    @Singleton
    @Provides
    fun provideChatRoomRepository(db: AppDatabase): ChatRoomRepository {
        return ChatRoomRepositoryImpl(
            chatRoomDao = db.chatroomDao,
            chatroomMemberImageDao = db.chatroomMemberImageDao
        )
    }

    @Singleton
    @Provides
    fun provideFriendRepository(db: AppDatabase): FriendRepository {
        return FriendRepositoryImpl(
            friendDao = db.friendDao
        )
    }

    @Singleton
    @Provides
    fun provideMemberRepository(db: AppDatabase): MemberRepository {
        return MemberRepositoryImpl(
            memberDao = db.memberDao
        )
    }

    @Singleton
    @Provides
    fun provideMessageRepository(db: AppDatabase): MessageRepository {
        return MessageRepositoryImpl(
            messageDao = db.messageDao
        )
    }

    @Singleton
    @Provides
    fun provideUserRepository(pref: PrefDataSource): UserRepository {
        return UserRepositoryImpl(
            pref = pref
        )
    }
}