package com.plop.plopmessenger.data.dto.response.user

import com.google.gson.annotations.SerializedName
import com.plop.plopmessenger.data.dto.response.People

data class GetUserProfileResponse(
    @SerializedName("data")
    val user: People,
    val message: String,
    val result: String
)
