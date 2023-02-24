package com.plop.plopmessenger.data.dto.response


import com.google.gson.annotations.SerializedName

data class ChatRoom(
    val members: List<Member>,
    @SerializedName("room_id")
    val roomId: String,
    val title: String?,
    @SerializedName("last_message")
    val lastMessage: LastMessage
)