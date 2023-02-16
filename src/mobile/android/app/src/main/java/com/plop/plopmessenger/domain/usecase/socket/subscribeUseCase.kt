package com.plop.plopmessenger.domain.usecase.socket

import com.plop.plopmessenger.domain.repository.SocketRepository
import javax.inject.Inject

class subscribeUseCase @Inject constructor(
    private val socketRepository: SocketRepository
) {
    operator fun invoke(roomId: String) {
        socketRepository.join(roomId)
    }
}