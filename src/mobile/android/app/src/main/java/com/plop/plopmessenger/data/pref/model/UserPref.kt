package com.plop.plopmessenger.data.pref.model

data class UserPref(
    val accessToken: String,
    val refreshToken: String,
    val id: String,
    val email: String,
    val nickname: String = "",
    val profileImg: String = "",
    val alarm: Boolean = false,
    val themeMode: Boolean = false,
    val active: Boolean = false
)

