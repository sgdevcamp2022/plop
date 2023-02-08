package com.plop.plopmessenger.data.dto.request


import com.google.gson.annotations.SerializedName

data class ChatRoomId(
    @SerializedName("room_id")
    val roomId: String
)