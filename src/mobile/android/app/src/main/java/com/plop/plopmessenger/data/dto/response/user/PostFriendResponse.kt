package com.plop.plopmessenger.data.dto.response.user

import com.google.gson.annotations.SerializedName

data class PostFriendResponse(
    @SerializedName("data")
    val requestFriendList: RequestFriendResponseItem,
    val result: String,
    val message: String
)
