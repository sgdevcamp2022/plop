package com.plop.plopmessenger.data.remote.api

import com.plop.plopmessenger.data.dto.request.user.PostAutoLoginRequest
import com.plop.plopmessenger.data.dto.response.user.PostAutoLoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RefreshApi {

    @POST(Constants.POST_AUTO_LOGIN)
    suspend fun postAutoLogin(
        @Body postAutoLoginRequest: PostAutoLoginRequest
    ): Response<PostAutoLoginResponse>
}