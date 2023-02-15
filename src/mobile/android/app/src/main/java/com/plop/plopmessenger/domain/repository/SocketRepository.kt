package com.plop.plopmessenger.domain.repository

import com.plop.plopmessenger.domain.model.MessageType

interface SocketRepository {
    fun connect()
    fun joinAll()
}