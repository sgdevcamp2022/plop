package com.plop.plopmessenger.data.remote.api

import com.plop.plopmessenger.data.dto.response.presence.GetPresenceUserResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.PUT

interface PresenceApi {
    @GET(Constants.GET_PRESENCE_USER)
    suspend fun getPresenceUser(): Response<GetPresenceUserResponse>

    @PUT(Constants.PUT_OFF)
    suspend fun putOff(): Response<String>

    @PUT(Constants.PUT_ON)
    suspend fun putOn(): Response<String>
}