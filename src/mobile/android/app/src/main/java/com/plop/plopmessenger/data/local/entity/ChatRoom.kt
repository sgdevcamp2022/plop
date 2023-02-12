package com.plop.plopmessenger.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.util.*

/**
 * type
 * 1 -> DM
 * 2 -> GROUP
 */
@Entity(tableName = "chatroom")
data class ChatRoom(
    @PrimaryKey
    @ColumnInfo(name = "chatroom_id")
    val chatroomId: String,
    val title: String,
    var unread: Int,
    var content: String = "",
    @ColumnInfo(name = "updated_at")
    var updatedAt: LocalDateTime,
    var type: Int
)
