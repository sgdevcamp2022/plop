package com.plop.plopmessenger.data.remote.stomp


data class SocketMessage(
    var command: String? = null,
    val payload: String? = null,
    val headers: Map<String, String> = HashMap()
)
