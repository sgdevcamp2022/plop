package com.plop.plopmessenger.data.remote.api

import com.plop.plopmessenger.data.dto.request.chat.*
import com.plop.plopmessenger.data.dto.request.fcm.FcmRequest
import com.plop.plopmessenger.data.dto.response.chat.*
import com.plop.plopmessenger.data.dto.response.fcm.FcmResponse
import retrofit2.Response
import retrofit2.http.*

interface FCMApi {

    @POST(Constants.POST_FCM)
    suspend fun postFCM(
        @Body postFCM: FcmRequest
    ): Response<FcmResponse>
}