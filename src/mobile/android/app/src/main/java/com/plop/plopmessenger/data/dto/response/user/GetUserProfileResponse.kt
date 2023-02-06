package com.plop.plopmessenger.data.dto.response.user

data class GetUserProfileResponse(
    val email: String,
    val userid: String,
    val profile: UserProfileDto,
    val message: String
)
