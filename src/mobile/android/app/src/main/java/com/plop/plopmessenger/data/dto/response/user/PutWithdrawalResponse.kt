package com.plop.plopmessenger.data.dto.response.user

import com.google.gson.annotations.SerializedName


data class PutWithdrawalResponse(
    @SerializedName("data")
    val putWithdrawalDto: WithdrawalDto,
    val message: String,
    val result: String
)