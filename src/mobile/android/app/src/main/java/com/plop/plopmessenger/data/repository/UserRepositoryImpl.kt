package com.plop.plopmessenger.data.repository

import com.plop.plopmessenger.data.pref.PrefDataSource
import com.plop.plopmessenger.data.pref.model.UserPref
import com.plop.plopmessenger.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class UserRepositoryImpl(
    private val pref: PrefDataSource
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
}


