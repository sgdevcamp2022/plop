package com.plop.plopmessenger.domain.model

import com.plop.plopmessenger.data.local.dao.ChatRoomMemberImage
import java.util.*

data class ChatRoom(
    val chatroomId: String,
    var title: String,
    var unread: Int,
    var content: String,
    var createdAt: Date,
    var members: List<Member>,
    val type: ChatRoomType
)

enum class ChatRoomType {
    DM, GROUP
}

fun ChatRoomMemberImage.toChatRoom(): ChatRoom = ChatRoom(
    chatroomId = this.chatroom.chatroomId,
    title = this.chatroom.title,
    unread = this.chatroom.unread,
    content = this.chatroom.content,
    createdAt = this.chatroom.updatedAt,
    members = this.members.map { it.toMember() },
    type = when(this.chatroom.type){
        1 -> ChatRoomType.DM
        else -> ChatRoomType.GROUP
    }
)