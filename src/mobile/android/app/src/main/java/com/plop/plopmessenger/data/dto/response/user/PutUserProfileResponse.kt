package com.plop.plopmessenger.data.dto.response.user


import com.google.gson.annotations.SerializedName
import com.plop.plopmessenger.data.dto.response.People

data class PutUserProfileResponse(
    @SerializedName("data")
    val user: People,
    val result: String,
    val message: String,

)