package com.plop.plopmessenger.domain.usecase.user

import android.util.Log
import com.plop.plopmessenger.data.dto.request.user.PostPasswordNewRequest
import com.plop.plopmessenger.domain.repository.UserRepository
import com.plop.plopmessenger.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FindPasswordUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(
        email: String,
        userId: String,
        password: String
    ): Flow<Resource<Boolean>> = flow {
        try {
            val response = userRepository.postPasswordNew(PostPasswordNewRequest(email, password,userId))
            when(response.code()) {
                200 -> {
                    emit(Resource.Success(true))
                }
                else -> {
                    Log.d("FinePasswordUseCase", "error")
                    emit(Resource.Error("error"))
                }
            }
        }catch (e: Exception) {
            Log.d(" FinePasswordUseCase", "error")
            emit(Resource.Error("error"))
        }
    }
}