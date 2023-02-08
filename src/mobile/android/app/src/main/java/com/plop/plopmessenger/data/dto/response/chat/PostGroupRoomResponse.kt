package com.plop.plopmessenger.data.dto.response.chat


import com.google.gson.annotations.SerializedName
import com.plop.plopmessenger.data.dto.response.Member

data class PostGroupRoomResponse(
    @SerializedName("created_at")
    val createdAt: String,
    val managers: List<String>,
    val members: List<Member>,
    @SerializedName("room_id")
    val roomId: String,
    val title: String,
    val type: String
)