package com.plop.plopmessenger.domain.model

import java.util.*
import com.plop.plopmessenger.data.local.entity.Message as MessageEntity

data class Message(
    val messageId: String,
    val messageFromId: String,
    val chatroomId: String,
    var content: String,
    var createdAt: Date,
    var type: MessageType
)

fun MessageEntity.toMessage() = Message(
    messageId = this.messageId,
    messageFromId = this.messageFromID,
    chatroomId = this.chatroomId,
    content = this.content,
    createdAt = this.createdAt,
    type = when(this.type) {
        1 -> MessageType.TEXT
        2 -> MessageType.IMAGE
        3 -> MessageType.VIDEO
        else -> MessageType.ENTER
    }
)


enum class MessageType {
    IMAGE,
    TEXT,
    VIDEO,
    ENTER
}