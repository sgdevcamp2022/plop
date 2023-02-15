package com.plop.plopmessenger.data.dto.response.user

import com.google.gson.annotations.SerializedName

data class GetFriendListResponse(
    @SerializedName("data")
    val profiles: List<FriendDto>,
    val result: String,
    val message: String
)

