package com.plop.plopmessenger.domain.usecase.user

import android.util.Log
import com.plop.plopmessenger.data.dto.request.user.PostEmailCodeRequest
import com.plop.plopmessenger.domain.repository.ChatRoomRepository
import com.plop.plopmessenger.domain.repository.FriendRepository
import com.plop.plopmessenger.domain.repository.UserRepository
import com.plop.plopmessenger.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WithdrawalUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val friendRepository: FriendRepository,
    private val chatRoomsRepository: ChatRoomRepository
) {
    operator fun invoke(): Flow<Resource<Boolean>> = flow {
        try {
            val response = userRepository.deleteWithdrawal()
            if(response.isSuccessful) {
                userRepository.logoutUser()
                chatRoomsRepository.deleteChatRoomData()
                friendRepository.deleteFriends()
                emit(Resource.Success(true))
            }
            else {
                Log.d("WithdrawalUseCase", "error")
                emit(Resource.Error("error"))
            }
        }catch (e: Exception) {
            Log.d(" WithdrawalUseCase", "error")
            emit(Resource.Error("error"))
        }
    }
}