package com.plop.plopmessenger.data.repository

import com.google.gson.Gson
import com.plop.plopmessenger.data.dto.request.user.*
import com.plop.plopmessenger.data.dto.response.presence.GetPresenceUserResponse
import com.plop.plopmessenger.data.dto.response.user.*
import com.plop.plopmessenger.data.pref.PrefDataSource
import com.plop.plopmessenger.data.pref.model.UserPref
import com.plop.plopmessenger.data.remote.api.PresenceApi
import com.plop.plopmessenger.data.remote.api.RefreshApi
import com.plop.plopmessenger.data.remote.api.UserApi
import com.plop.plopmessenger.domain.repository.PresenceRepository
import com.plop.plopmessenger.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Response
import java.io.File
import javax.inject.Inject

class PresenceRepositoryImpl @Inject constructor(
    private val presenceApi: PresenceApi
): PresenceRepository {
    override suspend fun setOn(): Response<String> {
        return presenceApi.putOn()
    }

    override suspend fun setOff(): Response<String> {
        return presenceApi.putOff()
    }

    override suspend fun getPresenceUser(): Response<GetPresenceUserResponse> {
        return presenceApi.getPresenceUser()
    }

}


