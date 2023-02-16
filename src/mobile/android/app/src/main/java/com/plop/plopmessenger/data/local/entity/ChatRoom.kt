package com.plop.plopmessenger.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.plop.plopmessenger.data.dto.response.ChatRoom as ChatRoomDto
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

fun ChatRoomDto.toChatRoom() = ChatRoom(
    chatroomId = this.roomId,
    title = this.title?: "",
    unread = 0,
    content = this.lastMessage.content?: "",
    updatedAt = LocalDateTime.now(),
    type = if(members.size == 1) 1 else 2
)