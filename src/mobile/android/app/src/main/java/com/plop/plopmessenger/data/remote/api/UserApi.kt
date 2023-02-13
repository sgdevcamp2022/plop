package com.plop.plopmessenger.data.remote.api

import com.plop.plopmessenger.data.dto.request.user.*
import com.plop.plopmessenger.data.dto.response.user.*
import com.plop.plopmessenger.data.remote.api.Constants.DELETE_LOGOUT
import com.plop.plopmessenger.data.remote.api.Constants.DELETE_WITHDRAWAL
import com.plop.plopmessenger.data.remote.api.Constants.GET_SEARCH_USER
import com.plop.plopmessenger.data.remote.api.Constants.GET_USER_PROFILE
import com.plop.plopmessenger.data.remote.api.Constants.POST_EMAIL_CODE
import com.plop.plopmessenger.data.remote.api.Constants.POST_EMAIL_VERIFY
import com.plop.plopmessenger.data.remote.api.Constants.POST_LOGIN
import com.plop.plopmessenger.data.remote.api.Constants.POST_PASSWORD_NEW
import com.plop.plopmessenger.data.remote.api.Constants.POST_SIGN_UP
import com.plop.plopmessenger.data.remote.api.Constants.PUT_USER_PROFILE
import retrofit2.Response
import retrofit2.http.*

interface UserApi {
    @POST(POST_LOGIN)
    suspend fun postLogin(
        @Body loginRequest: PostLoginRequest
    ): Response<PostLoginResponse>

    @DELETE(DELETE_LOGOUT)
    suspend fun deleteLogout(): Response<DeleteLogoutResponse>

    @POST(POST_SIGN_UP)
    suspend fun postSignUp(
        @Body postSignUpRequest: PostSignUpRequest
    ): Response<PostSignUpResponse>

    @POST(POST_EMAIL_CODE)
    suspend fun postEmailCode(
        @Body postEmailCodeRequest: PostEmailCodeRequest
    ): Response<PostEmailCodeResponse>

    @POST(POST_EMAIL_VERIFY)
    suspend fun postEmailVerify(
        @Body postEmailVerifyRequest: PostEmailVerifyRequest
    ): Response<PostEmailVerifyResponse>

    @DELETE(DELETE_WITHDRAWAL)
    suspend fun deleteWithdrawal(): Response<DeleteWithdrawalResponse>

    @POST(POST_PASSWORD_NEW)
    suspend fun postPasswordNew(
        @Body postPasswordNewRequest: PostPasswordNewRequest
    ): Response<PostPasswordNewResponse>

    @GET(GET_USER_PROFILE)
    suspend fun getUserProfile(
        @Query("email") email: String
    ): Response<GetUserProfileResponse>

    @PUT(PUT_USER_PROFILE)
    suspend fun putUserProfile(
        @Body putUserProfileRequest: PutUserProfileRequest
    ): Response<PutUserProfileResponse>

    @GET(GET_SEARCH_USER)
    suspend fun getSearchUser(
        @Body getSearchUserRequest: GetSearchUserRequest
    ): Response<GetSearchUserResponse>
}