package com.plop.plopmessenger.data.dto.response


import com.google.gson.annotations.SerializedName

data class ChatRoomAndMember(
    val members: List<String>,
    @SerializedName("room_id")
    val roomId: String
)