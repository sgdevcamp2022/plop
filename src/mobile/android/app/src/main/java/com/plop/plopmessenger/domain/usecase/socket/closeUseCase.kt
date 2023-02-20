package com.plop.plopmessenger.domain.usecase.socket

import com.plop.plopmessenger.data.dto.request.chat.PostMessageRequest
import com.plop.plopmessenger.domain.model.MessageType
import com.plop.plopmessenger.domain.repository.ChatRoomRepository
import com.plop.plopmessenger.domain.repository.SocketRepository
import javax.inject.Inject

class CloseUseCase @Inject constructor(
private val socketRepository: SocketRepository
) {
    operator fun invoke() {
        socketRepository.close()
    }
}