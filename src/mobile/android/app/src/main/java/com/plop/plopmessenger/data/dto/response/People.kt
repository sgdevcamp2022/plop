package com.plop.plopmessenger.data.dto.response


import com.google.gson.annotations.SerializedName

data class People(
    val email: String,
    val profile: Profile,
    val userId: String
)
