package com.plop.plopmessenger.util

import java.io.IOException

class UnAuthorizationError : IOException() {
    override fun getLocalizedMessage(): String {
        return "401 Error, unAuthorization token"
    }
}

class NoConnectivityException : IOException() {
    override val message: String
        get() = "인터넷 연결이 끊겼습니다. WIFI나 데이터 연결을 확인해주세요"
}