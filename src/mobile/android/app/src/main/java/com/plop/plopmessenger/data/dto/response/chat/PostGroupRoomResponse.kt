package com.plop.plopmessenger.data.dto.response.chat


import com.google.gson.annotations.SerializedName
import com.plop.plopmessenger.data.dto.response.Member
import java.time.LocalDateTime

data class PostGroupRoomResponse(
    @SerializedName("created_at")
    val createdAt: LocalDateTime,
    val managers: List<String>,
    val members: List<Member>,
    @SerializedName("room_id")
    val roomId: String,
    val title: String,
    val type: String
)