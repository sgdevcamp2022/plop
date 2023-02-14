package com.plop.plopmessenger.data.dto.response.user

data class PostFriendAcceptResponse(
    val message: String,
    val result: String,
    val postFriendAcceptData: RequestFriendResponseItem
)
