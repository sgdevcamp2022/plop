package com.plop.plopmessenger.di

import android.app.Application
import androidx.room.Room
import com.plop.plopmessenger.data.local.AppDatabase
import com.plop.plopmessenger.data.repository.ChatRoomRepositoryImpl
import com.plop.plopmessenger.data.repository.FriendRepositoryImpl
import com.plop.plopmessenger.data.repository.MemberRepositoryImpl
import com.plop.plopmessenger.data.repository.MessageRepositoryImpl
import com.plop.plopmessenger.domain.repository.ChatRoomRepository
import com.plop.plopmessenger.domain.repository.FriendRepository
import com.plop.plopmessenger.domain.repository.MemberRepository
import com.plop.plopmessenger.domain.repository.MessageRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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
}