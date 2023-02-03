package com.plop.plopmessenger.domain.repository

import com.plop.plopmessenger.data.pref.model.UserPref
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getAccessToken(): Flow<String>
    suspend fun setAccessToken(accessToken: String)
    fun getRefreshToken(): Flow<String>
    fun getUser(): Flow<UserPref>
    fun getUserId(): Flow<String>
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
}