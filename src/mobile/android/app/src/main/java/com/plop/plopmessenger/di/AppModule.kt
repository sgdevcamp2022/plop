package com.plop.plopmessenger.di

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.room.Room
import com.plop.plopmessenger.data.local.AppDatabase
import com.plop.plopmessenger.data.pref.PrefDataSource
import com.plop.plopmessenger.data.remote.api.*
import com.plop.plopmessenger.data.remote.api.Constants.BASE_URL
import com.plop.plopmessenger.data.remote.stomp.WebSocketListener
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
    @AuthRetrofit
    @Provides
    fun provideRetrofit(
        @AuthOkHttp okHttpClient: OkHttpClient
    ) : Retrofit {
        return Retrofit.Builder().apply {
            addConverterFactory(GsonConverterFactory.create())
            baseUrl(BASE_URL)
            client(okHttpClient)
        }.build()
    }

    @Singleton
    @RefreshRetrofit
    @Provides
    fun provideRefreshRetrofit(
        @RefreshOkHttp okHttpClient: OkHttpClient
    ) : Retrofit {
        return Retrofit.Builder().apply {
            addConverterFactory(GsonConverterFactory.create())
            baseUrl(BASE_URL)
            client(okHttpClient)
        }.build()
    }

    @Singleton
    @Provides
    fun provideChatApi(
        @AuthRetrofit retrofit: Retrofit
    ): ChatApi {
        return retrofit.create(ChatApi::class.java)
    }

    @Singleton
    @Provides
    fun provideRefreshApi(
        @RefreshRetrofit retrofit: Retrofit
    ): RefreshApi {
        return retrofit.create(RefreshApi::class.java)
    }

    @Singleton
    @Provides
    fun provideFriendApi(
        @AuthRetrofit retrofit: Retrofit
    ): FriendApi {
        return retrofit.create(FriendApi::class.java)
    }

    @Singleton
    @Provides
    fun provideUserApi(
        @AuthRetrofit retrofit: Retrofit
    ): UserApi {
        return retrofit.create(UserApi::class.java)
    }

    @Singleton
    @Provides
    fun providePresenceApi(
        @AuthRetrofit retrofit: Retrofit
    ): PresenceApi {
        return retrofit.create(PresenceApi::class.java)
    }

    @Singleton
    @Provides
    fun provideFCMApi(
        @AuthRetrofit retrofit: Retrofit
    ): FCMApi {
        return retrofit.create(FCMApi::class.java)
    }

    @AuthOkHttp
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

    @RefreshOkHttp
    @Singleton
    @Provides
    fun provideRefreshOkHttpClient(
        @RefreshInterceptor interceptor: Interceptor,
        @LoggingInterceptor loggingInterceptor: Interceptor,
        @NetworkInterceptor networkInterceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(loggingInterceptor)
            .addNetworkInterceptor(networkInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideWebSocketListener(
        @AuthOkHttp okHttpClient: OkHttpClient,
        chatRoomRepository: ChatRoomRepository,
        messageRepository: MessageRepository,
        memberRepository: MemberRepository,
        userRepository: UserRepository
    ) : WebSocketListener {
        return WebSocketListener(
            okHttpClient,
            chatRoomRepository,
            messageRepository,
            memberRepository,
            userRepository
        )
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

    @RefreshInterceptor
    @Provides
    fun provideRefreshInterceptor(
        pref: PrefDataSource
    ) : Interceptor = Interceptor{ chain ->

        val token = runBlocking(Dispatchers.IO) {
            pref.getRefreshToken().first().let {
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
    annotation class RefreshInterceptor

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class NetworkInterceptor

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class LoggingInterceptor

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class RefreshRetrofit

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class AuthRetrofit

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class AuthOkHttp

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class RefreshOkHttp


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
    fun provideChatRoomRepository(db: AppDatabase, chatApi: ChatApi): ChatRoomRepository {
        return ChatRoomRepositoryImpl(
            chatRoomDao = db.chatroomDao,
            chatroomMemberImageDao = db.chatroomMemberImageDao,
            chatApi = chatApi
        )
    }

    @Singleton
    @Provides
    fun provideFriendRepository(db: AppDatabase, friendApi: FriendApi): FriendRepository {
        return FriendRepositoryImpl(
            friendDao = db.friendDao,
            friendApi = friendApi
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
    fun provideUserRepository(pref: PrefDataSource, userApi: UserApi, refreshApi: RefreshApi): UserRepository {
        return UserRepositoryImpl(
            pref = pref,
            userApi = userApi,
            refreshApi = refreshApi
        )
    }

    @Singleton
    @Provides
    fun provideSocketRepository(webSocketListener: WebSocketListener): SocketRepository {
        return SocketRepositoryImpl(
            webSocketListener = webSocketListener
        )
    }

    @Singleton
    @Provides
    fun providePresenceRepository(presenceApi: PresenceApi): PresenceRepository {
        return PresenceRepositoryImpl(
            presenceApi = presenceApi
        )
    }

    @Singleton
    @Provides
    fun provideFCMRepository(fcmApi: FCMApi):FCMRepository {
        return FCMRepositoryImpl(
            fcmApi = fcmApi
        )
    }
}