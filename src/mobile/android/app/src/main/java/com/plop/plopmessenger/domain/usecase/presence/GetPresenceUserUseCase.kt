package com.plop.plopmessenger.domain.usecase.presence

import android.util.Log
import com.plop.plopmessenger.domain.model.People
import com.plop.plopmessenger.domain.model.toPeople
import com.plop.plopmessenger.domain.repository.FriendRepository
import com.plop.plopmessenger.domain.repository.PresenceRepository
import com.plop.plopmessenger.domain.util.Resource
import javax.inject.Inject

class GetPresenceUserUseCase @Inject constructor(
    private val presenceRepository: PresenceRepository,
    private val friendRepository: FriendRepository
) {
    suspend operator fun invoke(): Resource<List<People>>{
        try {
            val response = presenceRepository.getPresenceUser()


            return Resource.Success(
                response.body()?.members?.map {
                    friendRepository.loadFriendById(it).first().toPeople()
                }?: emptyList())

        } catch (e: Exception) {
            Log.d("GetPresenceUserUseCase", e.message.toString())
            return Resource.Error(e.message.toString())
        }
    }
}