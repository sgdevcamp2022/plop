package com.plop.plopmessenger.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.plop.plopmessenger.data.local.dao.*
import com.plop.plopmessenger.local.dao.*
import com.plop.plopmessenger.data.local.entity.ChatRoom
import com.plop.plopmessenger.data.local.entity.Friend
import com.plop.plopmessenger.data.local.entity.Member
import com.plop.plopmessenger.data.local.entity.Message


@Database(entities = [ChatRoom::class, Friend::class, Member::class, Message::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract val chatroomDao: ChatRoomDao
    abstract val chatroomMemberImageDao: ChatRoomMemberImageDao
    abstract val friendDao: FriendDao
    abstract val memberDao: MemberDao
    abstract val messageDao: MessageDao

    companion object {
        const val DATABASE_NAME = "plop-db"
    }
}