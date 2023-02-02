package com.plop.plopmessenger.domain.model

import java.util.*

data class ChatRoom(
    val chatroomId: String,
    var title: String,
    var unread: Int,
    var content: String,
    var createdAt: Date,
    var images: List<String>,
    val type: ChatRoomType
)

enum class ChatRoomType {
    ONE, MULTIPLE
}
