package com.plop.plopmessenger.data.remote.stomp

data class SocketState(
    val type: Type? = null,
    val exception: Throwable? = null
)


enum class Type {
    OPENED,
    CLOSED,
    ERROR
}
