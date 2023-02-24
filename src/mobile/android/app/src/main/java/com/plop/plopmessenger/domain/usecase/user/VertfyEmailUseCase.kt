package com.plop.plopmessenger.domain.usecase.user

import android.util.Log
import com.plop.plopmessenger.data.dto.request.user.PostEmailVerifyRequest
import com.plop.plopmessenger.domain.repository.UserRepository
import com.plop.plopmessenger.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class VerifyEmailUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(
        email: String,
        userId: String,
        code: String
    ): Flow<Resource<Boolean>> = flow {
        try {
            val response = userRepository.postEmailVerify(PostEmailVerifyRequest(email, userId, code))
            when(response.code()) {
                200 -> {
                    emit(Resource.Success(true))
                }
                else -> {
                    Log.d("VertifyEmailUseCase", "error")
                    emit(Resource.Error("error"))
                }
            }
        }catch (e: Exception) {
            Log.d(" VertifyEmailUseCase", "error")
            emit(Resource.Error("error"))
        }
    }
}