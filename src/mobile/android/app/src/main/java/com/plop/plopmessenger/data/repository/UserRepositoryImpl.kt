package com.plop.plopmessenger.data.repository

import com.plop.plopmessenger.data.dto.request.user.*
import com.plop.plopmessenger.data.dto.response.user.*
import com.plop.plopmessenger.data.pref.PrefDataSource
import com.plop.plopmessenger.data.pref.model.UserPref
import com.plop.plopmessenger.data.remote.api.RefreshApi
import com.plop.plopmessenger.data.remote.api.UserApi
import com.plop.plopmessenger.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val pref: PrefDataSource,
    private val userApi: UserApi,
    private val refreshApi: RefreshApi
): UserRepository {
    override fun getAccessToken(): Flow<String> {
        return pref.getAccessToken()
    }

    override suspend fun setAccessToken(accessToken: String) {
        pref.setAccessToken(accessToken)
    }

    override fun getRefreshToken(): Flow<String> {
        return pref.getRefreshToken()
    }

    override suspend fun setRefreshToken(refreshToken: String) {
        pref.setRefreshToken(refreshToken)
    }

    override fun getUser(): Flow<UserPref> {
        return pref.getUser()
    }

    override fun getUserId(): Flow<String> {
        return pref.getUserId()
    }

    override fun getNickname(): Flow<String> {
        return pref.getNickname()
    }

    override suspend fun setNickname(nickname: String) {
        return pref.setNickname(nickname)
    }

    override fun getProfileImg(): Flow<String> {
        return pref.getProfileImg()
    }

    override suspend fun setProfileImg(profileImg: String) {
        return pref.setProfileImg(profileImg)
    }

    override fun getThemeModel(): Flow<Boolean> {
        return pref.getThemeMode()
    }

    override suspend fun setThemeMode(mode: Boolean) {
        pref.setThemeMode(mode)
    }

    override fun getAlarmModel(): Flow<Boolean> {
        return pref.getAlarmMode()
    }

    override suspend fun setAlarmMode(mode: Boolean) {
        pref.setAlarmMode(mode)
    }

    override fun getActiveModel(): Flow<Boolean> {
        return pref.getActiveMode()
    }

    override suspend fun setActiveMode(mode: Boolean) {
        pref.setActiveMode(mode)
    }

    override suspend fun logoutUser() {
        pref.logoutUser()
    }

    override suspend fun loginUser(userPref: UserPref) {
        pref.loginUser(userPref = userPref)
    }

    override suspend fun postLogin(loginRequest: PostLoginRequest): Response<PostLoginResponse> {
        return userApi.postLogin(loginRequest)
    }

    override suspend fun postAutoLogin(postAutoLoginRequest: PostAutoLoginRequest): Response<PostAutoLoginResponse> {
        return refreshApi.postAutoLogin(postAutoLoginRequest)
    }

    override suspend fun deleteLogout(): Response<DeleteLogoutResponse> {
        return userApi.deleteLogout()
    }

    override suspend fun postSignUp(postSignUpRequest: PostSignUpRequest): Response<PostSignUpResponse> {
        return userApi.postSignUp(postSignUpRequest)
    }

    override suspend fun postEmailCode(postEmailCodeRequest: PostEmailCodeRequest): Response<PostEmailCodeResponse> {
        return userApi.postEmailCode(postEmailCodeRequest)
    }

    override suspend fun postEmailVerify(postEmailVerifyRequest: PostEmailVerifyRequest): Response<PostEmailVerifyResponse> {
        return userApi.postEmailVerify(postEmailVerifyRequest)
    }

    override suspend fun deleteWithdrawal(): Response<DeleteWithdrawalResponse> {
        return userApi.deleteWithdrawal()
    }

    override suspend fun postPasswordNew(postPasswordNewRequest: PostPasswordNewRequest): Response<PostPasswordNewResponse> {
        return userApi.postPasswordNew(postPasswordNewRequest)
    }

    override suspend fun getUserProfile(email: String): Response<GetUserProfileResponse> {
        return userApi.getUserProfile(email = email)
    }

    override suspend fun putUserProfile(putUserProfileRequest: PutUserProfileRequest): Response<PutUserProfileResponse> {
        return userApi.putUserProfile(putUserProfileRequest)
    }

    override suspend fun getSearchUser(getSearchUserRequest: GetSearchUserRequest): Response<GetSearchUserResponse> {
        return userApi.getSearchUser(getSearchUserRequest)
    }
}


