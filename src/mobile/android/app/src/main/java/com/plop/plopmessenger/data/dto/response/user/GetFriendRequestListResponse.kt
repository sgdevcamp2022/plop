package com.plop.plopmessenger.data.dto.response.user

data class GetFriendRequestListResponse(
    val profiles: List<FriendDto>,
    val message: String
)
