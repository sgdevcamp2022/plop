package com.plop.plopmessenger.domain.usecase.presence

import android.util.Log
import com.plop.plopmessenger.domain.model.People
import com.plop.plopmessenger.domain.model.toPeople
import com.plop.plopmessenger.domain.repository.FriendRepository
import com.plop.plopmessenger.domain.repository.PresenceRepository
import com.plop.plopmessenger.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.forEach
import javax.inject.Inject

class GetPresenceUserUseCase @Inject constructor(
    private val presenceRepository: PresenceRepository,
    private val friendRepository: FriendRepository
) {
    suspend operator fun invoke(): Flow<Resource<List<People>>> = flow {
        try {
            presenceRepository.getPresenceUser().collect { presence ->
                val res = presence.body()?.members?.map {
                    friendRepository.loadFriendById(it).first().toPeople()
                } ?: emptyList()

                emit(Resource.Success(res))
            }

        } catch (e: Exception) {
            Log.d("GetPresenceUserUseCase", e.message.toString())
            emit(Resource.Error(e.message.toString()))
        }
    }
}