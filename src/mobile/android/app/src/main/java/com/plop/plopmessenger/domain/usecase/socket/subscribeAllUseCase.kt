package com.plop.plopmessenger.domain.usecase.socket

import com.plop.plopmessenger.domain.repository.SocketRepository
import javax.inject.Inject

class subscribeAllUseCase @Inject constructor(
    private val socketRepository: SocketRepository
) {
    operator fun invoke() {
        socketRepository.joinAll()
    }
}