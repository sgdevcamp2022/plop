package com.plop.plopmessenger.data.repository

import com.plop.plopmessenger.data.dto.request.fcm.FcmRequest
import com.plop.plopmessenger.data.dto.response.fcm.FcmResponse
import com.plop.plopmessenger.data.remote.api.FCMApi
import com.plop.plopmessenger.domain.repository.FCMRepository
import retrofit2.Response
import javax.inject.Inject

class FCMRepositoryImpl @Inject constructor(
    private val fcmApi: FCMApi
): FCMRepository {
    override suspend fun postFCM(fcmRequest: FcmRequest): Response<FcmResponse> {
        return fcmApi.postFCM(fcmRequest)
    }
}