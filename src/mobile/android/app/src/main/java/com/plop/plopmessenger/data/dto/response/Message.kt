package com.plop.plopmessenger.data.dto.response


import com.google.gson.annotations.SerializedName
import java.util.*
import com.plop.plopmessenger.data.local.entity.Message as MessageEntity

data class Message(
    val content: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("message_id")
    val messageId: String,
    @SerializedName("message_type")
    val messageType: String,
    @SerializedName("room_id")
    val roomId: String,
    @SerializedName("sender_id")
    val senderId: String
)

fun Message.toMessage() = MessageEntity(
    messageId = this.messageId,
    messageFromID = this.senderId,
    chatroomId = this.roomId,
    content = this.content,
    createdAt = Date(),
    type = MessageTypeConverter(this.messageType)
)

private fun MessageTypeConverter(type: String): Int {
    return when(type) {
        "TEXT" -> 1
        "IMG" -> 2
        "VIDEO" -> 3
        "ENTER" -> 4
        else -> 1
    }
}