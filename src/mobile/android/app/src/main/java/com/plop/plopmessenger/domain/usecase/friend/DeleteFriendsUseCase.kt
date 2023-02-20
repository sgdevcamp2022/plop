package com.plop.plopmessenger.domain.usecase.friend

import android.util.Log
import com.plop.plopmessenger.domain.repository.ChatRoomRepository
import com.plop.plopmessenger.domain.repository.FriendRepository
import com.plop.plopmessenger.domain.util.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DeleteFriendsUseCase @Inject constructor(
    private val friendRepository: FriendRepository
) {
    suspend operator fun invoke() {
        CoroutineScope(Dispatchers.IO).launch {
            friendRepository.deleteFriends()
        }
    }
}