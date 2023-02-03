package com.plop.plopmessenger.domain.repository

interface SocketRepository {
    fun connect()
    fun join()
}