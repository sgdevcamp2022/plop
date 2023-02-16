package com.plop.plopmessenger.data.repository

import com.plop.plopmessenger.data.remote.stomp.WebSocketListener
import com.plop.plopmessenger.domain.model.MessageType
import com.plop.plopmessenger.domain.repository.SocketRepository
import javax.inject.Inject

class SocketRepositoryImpl @Inject constructor(
    private val webSocketListener: WebSocketListener
): SocketRepository {
    override fun connect() {
        webSocketListener.connect()
    }

    override fun joinAll() {
        webSocketListener.joinAll()
    }

    override fun join(roomId: String) {
        webSocketListener.join("/chatting/topic/room/${roomId}")
    }

}