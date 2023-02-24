package com.plop.plopmessenger.data.dto.response.chat


import com.google.gson.annotations.SerializedName
import com.plop.plopmessenger.data.dto.response.Member

data class GetChatRoomInfoResponse(
    val managers: List<String>,
    val members: List<Member>,
    @SerializedName("room_id")
    val roomId: String,
    val title: String,
    val type: String,
    val createdAt: String
)