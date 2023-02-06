package com.plop.plopmessenger.data.dto.response.user

import com.google.gson.annotations.SerializedName


data class PostLogoutResponse(
    @SerializedName("data")
    val postLogoutDto: LogoutDto,
    val message: String,
    val result: String
)