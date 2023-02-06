package com.plop.plopmessenger.data.dto.response.user

data class GetFriendListResponse(
    val profiles: List<FriendDto>,
    val message: String
)
