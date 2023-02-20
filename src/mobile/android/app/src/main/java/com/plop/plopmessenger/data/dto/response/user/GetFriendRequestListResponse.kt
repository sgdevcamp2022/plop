package com.plop.plopmessenger.data.dto.response.user

import com.google.gson.annotations.SerializedName

data class GetFriendRequestListResponse(
    @SerializedName("data")
    val profiles: List<FriendDto>,
    val message: String,
    val result: String
)
