package com.plop.plopmessenger.data.repository

import com.plop.plopmessenger.data.remote.stomp.WebSocketListener
import com.plop.plopmessenger.domain.repository.SocketRepository

class SocketRepositoryImpl(
    private val webSocketListener: WebSocketListener
): SocketRepository {
    override fun connect() {
        webSocketListener.connect()
    }

    override fun join() {
        webSocketListener.join("topic")
    }

}