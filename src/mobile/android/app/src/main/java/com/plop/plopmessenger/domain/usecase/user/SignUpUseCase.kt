package com.plop.plopmessenger.domain.usecase.user

import android.util.Log
import com.plop.plopmessenger.data.dto.request.user.PostLoginRequest
import com.plop.plopmessenger.data.dto.request.user.PostSignUpRequest
import com.plop.plopmessenger.data.pref.model.UserPref
import com.plop.plopmessenger.domain.repository.UserRepository
import com.plop.plopmessenger.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(
        email: String,
        nickname: String,
        password: String,
        userId: String
    ): Flow<Resource<Boolean>> = flow {
        try {
            val response = userRepository.postSignUp(PostSignUpRequest(email, nickname, password, userId))
            if(response.isSuccessful) {
                emit(Resource.Success(true))
            } else {
                emit(Resource.Error("${response.code()} :${response.message()}"))
            }
        }catch (e: Exception) {
            Log.d(" SignUpUseCase", "error")
            emit(Resource.Error("error"))
        }
    }
}