package com.plop.plopmessenger.domain.usecase.message

import android.util.Log
import com.plop.plopmessenger.domain.model.Message
import com.plop.plopmessenger.domain.model.toMessage
import com.plop.plopmessenger.domain.repository.ChatRoomRepository
import com.plop.plopmessenger.domain.repository.MemberRepository
import com.plop.plopmessenger.domain.repository.MessageRepository
import com.plop.plopmessenger.domain.util.Resource
import com.plop.plopmessenger.presentation.state.UserState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class GetFirstMessageUseCase @Inject constructor(
    private val messageRepository: MessageRepository,
    private val memberRepository: MemberRepository,
    private val chatRoomRepository: ChatRoomRepository
) {
    operator fun invoke(chatroomId: String): Flow<Resource<Message?>> = flow {
        try {
            emit(Resource.Loading())
            messageRepository.loadChatFirstMessage(chatroomId).collect() { result ->
                if(result == null) emit(Resource.Success(null))
                else {
                    memberRepository.updateMemberLastRead("1234", result.messageId)
                    chatRoomRepository.updateChatRoomUnreadById(chatroomId,0)
                    emit(Resource.Success(result.toMessage()))
                }
            }
        } catch (e: IOException) {
            Log.d("GetFirstMessageListUseCase", "IOException")
        } catch (e: Exception) {
            Log.d("GetFirstMessageListUseCase", e.message.toString())
        }
    }
}