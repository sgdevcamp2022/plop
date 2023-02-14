package com.plop.plopmessenger.data.pref

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.plop.plopmessenger.data.pref.model.UserPref
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_setting")

class PrefDataSource @Inject constructor(@ApplicationContext val context: Context) {
    companion object {
        val LOGIN_USER_ACCESS_TOKEN = stringPreferencesKey("login_user_access_token")
        val LOGIN_USER_REFRESH_TOKEN = stringPreferencesKey("login_user_refresh_token")
        val LOGIN_USER_NICKNAME = stringPreferencesKey("login_user_nickname")
        val LOGIN_USER_EMAIL = stringPreferencesKey("login_user_email")
        val LOGIN_USER_ID = stringPreferencesKey("login_user_id")
        val LOGIN_USER_PROFILE_IMG = stringPreferencesKey("login_user_profile_img")

        val THEME_MODE = booleanPreferencesKey("theme_mode")
        val ALARM_MODE = booleanPreferencesKey("alarm_mode")
        val ACTIVE_MODE = booleanPreferencesKey("active_mode")
    }

    fun getAccessToken(): Flow<String> {
        return context.dataStore.data
            .map { it[LOGIN_USER_ACCESS_TOKEN]?: ""}
    }

    suspend fun setAccessToken(accessToken: String) {
        context.dataStore.edit {
            it[LOGIN_USER_ACCESS_TOKEN] = accessToken
        }
    }

    fun getRefreshToken(): Flow<String> {
        return context.dataStore.data
            .map { it[LOGIN_USER_REFRESH_TOKEN]?: ""}
    }

    suspend fun setRefreshToken(refreshToken: String) {
        context.dataStore.edit {
            it[LOGIN_USER_REFRESH_TOKEN] = refreshToken
        }
    }

    fun getUser(): Flow<UserPref> {
        return context.dataStore.data.map { preference ->
            val accessToken = preference[LOGIN_USER_ACCESS_TOKEN] ?: ""
            val refreshToken = preference[LOGIN_USER_REFRESH_TOKEN] ?: ""
            val id = preference[LOGIN_USER_ID] ?: ""
            val email = preference[LOGIN_USER_EMAIL] ?: ""
            val nickname = preference[LOGIN_USER_NICKNAME] ?: ""
            val profileImg = preference[LOGIN_USER_PROFILE_IMG] ?: ""
            val alarm = preference[ALARM_MODE] ?: false
            val mode = preference[THEME_MODE] ?: false
            val active = preference[ACTIVE_MODE] ?: false

            UserPref(accessToken, refreshToken, id, email, nickname, profileImg, alarm, mode, active)
        }
    }

    fun getUserId(): Flow<String> {
        return context.dataStore.data
            .map { it[LOGIN_USER_ID]?: ""}
    }

    suspend fun setUserId(userId: String) {
        context.dataStore.edit {
            it[LOGIN_USER_ID] = userId
        }
    }

    fun getNickname(): Flow<String> {
        return context.dataStore.data
            .map { it[LOGIN_USER_NICKNAME]?: ""}
    }

    suspend fun setNickname(nickname: String) {
        context.dataStore.edit {
            it[LOGIN_USER_NICKNAME] = nickname
        }
    }

    fun getProfileImg(): Flow<String> {
        return context.dataStore.data
            .map { it[LOGIN_USER_PROFILE_IMG]?: ""}
    }

    suspend fun setProfileImg(profileImg: String) {
        context.dataStore.edit {
            it[LOGIN_USER_PROFILE_IMG] = profileImg
        }
    }


    fun getThemeMode(): Flow<Boolean> {
        return context.dataStore.data
            .map { it[THEME_MODE]?: false}
    }

    suspend fun setThemeMode(mode: Boolean) {
        context.dataStore.edit {
            it[THEME_MODE] = mode
        }
    }

    fun getAlarmMode(): Flow<Boolean> {
        return context.dataStore.data
            .map { it[ALARM_MODE]?: false}
    }

    suspend fun setAlarmMode(mode: Boolean) {
        context.dataStore.edit {
            it[ALARM_MODE] = mode
        }
    }

    fun getActiveMode(): Flow<Boolean> {
        return context.dataStore.data
            .map { it[ACTIVE_MODE]?: false}
    }

    suspend fun setActiveMode(mode: Boolean) {
        context.dataStore.edit {
            it[ACTIVE_MODE] = mode
        }
    }

    suspend fun logoutUser() {
        context.dataStore.edit { settings ->
            settings[LOGIN_USER_ACCESS_TOKEN] = ""
            settings[LOGIN_USER_REFRESH_TOKEN] = ""
            settings[LOGIN_USER_ID] = ""
            settings[LOGIN_USER_EMAIL] = ""
            settings[LOGIN_USER_NICKNAME] = ""
            settings[LOGIN_USER_PROFILE_IMG] = ""
            settings[THEME_MODE] = false
            settings[ALARM_MODE] = true
            settings[ACTIVE_MODE] = true
        }
    }

    suspend fun loginUser(userPref: UserPref) {
        context.dataStore.edit { settings ->
            settings[LOGIN_USER_ACCESS_TOKEN] = userPref.accessToken
            settings[LOGIN_USER_REFRESH_TOKEN] = userPref.refreshToken
            settings[LOGIN_USER_ID] = userPref.id
            settings[LOGIN_USER_EMAIL] = userPref.email
            settings[LOGIN_USER_NICKNAME] = userPref.nickname
            settings[LOGIN_USER_PROFILE_IMG] = userPref.profileImg
            settings[THEME_MODE] = userPref.themeMode
            settings[ALARM_MODE] = userPref.alarm
            settings[ACTIVE_MODE] = userPref.active
        }
    }

}
