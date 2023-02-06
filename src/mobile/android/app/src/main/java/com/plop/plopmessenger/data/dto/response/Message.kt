package com.plop.plopmessenger.data.dto.response


import com.google.gson.annotations.SerializedName

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