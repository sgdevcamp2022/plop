package com.plop.plopmessenger.data.dto.response.user


import com.google.gson.annotations.SerializedName

data class PostFriendResponseItem(
    @SerializedName("receiver")
    val target: String,
    val sender: String
)