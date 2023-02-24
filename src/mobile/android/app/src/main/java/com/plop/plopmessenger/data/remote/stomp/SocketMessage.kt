package com.plop.plopmessenger.data.remote.stomp


data class SocketMessage(
    var command: String? = null,
    val headers: Map<String, String> = HashMap(),
    val payload: String? = null,
)
