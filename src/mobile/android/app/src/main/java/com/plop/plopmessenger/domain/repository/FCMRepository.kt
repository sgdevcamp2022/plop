package com.plop.plopmessenger.domain.repository

import com.plop.plopmessenger.data.dto.request.fcm.FcmRequest
import com.plop.plopmessenger.data.dto.response.fcm.FcmResponse
import retrofit2.Response

interface FCMRepository {
    suspend fun postFCM(fcmRequest: FcmRequest): Response<FcmResponse>
}