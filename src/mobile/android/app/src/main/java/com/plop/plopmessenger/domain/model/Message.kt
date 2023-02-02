package com.plop.plopmessenger.domain.model

import java.util.*

data class Message(
    val messageId: String,
    val messageFromId: String,
    val chatroomId: String,
    var content: String,
    var createdAt: Date,
    var type: MessageType
)


enum class MessageType {
    IMAGE,
    TEXT,
    VIDEO,
    ENTER
}