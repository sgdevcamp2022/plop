package com.plop.plopmessenger.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import com.plop.plopmessenger.data.dto.response.ChatRoom as ChatRoomDto

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
    title = this.title?: this.members.firstOrNull()?.userId.toString(),
    unread = 0,
    content = this.lastMessage.content?: "",
    updatedAt = if(this.lastMessage.createdAt != null) LocalDateTime.parse(this.lastMessage.createdAt)
                else LocalDateTime.parse(this.members.firstOrNull()?.enteredAt),
    type = if(members.size == 2) 1 else 2
)