package com.plop.plopmessenger.data.dto.response.user

data class DeleteFriendRejectResponse(
    val message: String,
    val result: String,
    val postFriendAcceptData: RequestFriendResponseItem
)
