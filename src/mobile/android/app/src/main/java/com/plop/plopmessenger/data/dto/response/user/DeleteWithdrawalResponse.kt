package com.plop.plopmessenger.data.dto.response.user

import com.google.gson.annotations.SerializedName


data class DeleteWithdrawalResponse(
    @SerializedName("data")
    val deleteWithdrawalDto: WithdrawalDto,
    val message: String,
    val result: String
)