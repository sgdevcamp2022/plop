package com.plop.plopmessenger.domain.repository

import com.plop.plopmessenger.data.dto.request.user.*
import com.plop.plopmessenger.data.dto.response.user.*
import com.plop.plopmessenger.data.pref.model.UserPref
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Query

interface UserRepository {
    fun getAccessToken(): Flow<String>
    suspend fun setAccessToken(accessToken: String)
    fun getRefreshToken(): Flow<String>
    suspend fun setRefreshToken(refreshToken: String)
    fun getUser(): Flow<UserPref>
    fun getUserId(): Flow<String>
    suspend fun setUserId(userId: String)
    fun getNickname(): Flow<String>
    suspend fun setNickname(nickname: String)
    fun getProfileImg(): Flow<String>
    suspend fun setProfileImg(profileImg: String)
    fun getThemeModel(): Flow<Boolean>
    suspend fun setThemeMode(mode: Boolean)
    fun getAlarmModel(): Flow<Boolean>
    suspend fun setAlarmMode(mode: Boolean)
    fun getActiveModel(): Flow<Boolean>
    suspend fun setActiveMode(mode: Boolean)
    suspend fun logoutUser()
    suspend fun loginUser(userPref: UserPref)

    suspend fun postLogin(loginRequest: PostLoginRequest): Response<PostLoginResponse>
    suspend fun postAutoLogin(postAutoLoginRequest: PostAutoLoginRequest): Response<PostAutoLoginResponse>
    suspend fun deleteLogout(): Response<DeleteLogoutResponse>
    suspend fun postSignUp(postSignUpRequest: PostSignUpRequest): Response<PostSignUpResponse>
    suspend fun postEmailCode(postEmailCodeRequest: PostEmailCodeRequest): Response<PostEmailCodeResponse>
    suspend fun postEmailVerify(postEmailVerifyRequest: PostEmailVerifyRequest): Response<PostEmailVerifyResponse>
    suspend fun deleteWithdrawal(): Response<DeleteWithdrawalResponse>
    suspend fun postPasswordNew(postPasswordNewRequest: PostPasswordNewRequest): Response<PostPasswordNewResponse>
    suspend fun getUserProfile(target: String): Response<GetUserProfileResponse>
    suspend fun putUserProfile(putUserProfileRequest: PutUserProfileRequest): Response<PutUserProfileResponse>
    suspend fun getSearchUser(target: String): Response<GetSearchUserResponse>
}