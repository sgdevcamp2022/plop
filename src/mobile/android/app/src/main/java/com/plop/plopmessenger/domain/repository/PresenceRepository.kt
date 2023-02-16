package com.plop.plopmessenger.domain.repository

import com.plop.plopmessenger.data.dto.request.user.*
import com.plop.plopmessenger.data.dto.response.presence.GetPresenceUserResponse
import com.plop.plopmessenger.data.dto.response.user.*
import com.plop.plopmessenger.data.pref.model.UserPref
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Query
import java.io.File

interface PresenceRepository {
    suspend fun setOn(): Response<String>
    suspend fun setOff(): Response<String>
    suspend fun getPresenceUser(): Response<GetPresenceUserResponse>
}