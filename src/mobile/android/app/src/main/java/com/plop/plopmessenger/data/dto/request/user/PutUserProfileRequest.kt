package com.plop.plopmessenger.data.dto.request.user


import com.google.gson.annotations.SerializedName

data class PutUserProfileRequest(
    val img: String,
    val nickname: String
)