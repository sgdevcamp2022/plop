package com.plop.plopmessenger.data.dto.request.chat


import com.google.gson.annotations.SerializedName

data class PostGroupRoomRequest(
    val members: List<String>
)