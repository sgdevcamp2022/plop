package com.plop.plopmessenger.data.dto.response.chat


import com.google.gson.annotations.SerializedName
import com.plop.plopmessenger.data.dto.response.ChatRoom

data class GetMyRoomResponse(
    @SerializedName("data")
    val getMyRoomDto: List<ChatRoom>,
    val message: String
)