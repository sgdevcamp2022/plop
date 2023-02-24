package com.plop.plopmessenger.domain.usecase.user

import android.net.Uri
import android.util.Log
import com.plop.plopmessenger.domain.repository.UserRepository
import com.plop.plopmessenger.domain.util.Resource
import com.plop.plopmessenger.presentation.state.UserState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import java.io.File
import javax.inject.Inject

class ModifyUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(
        nickname: String,
        image: File,
        img: Uri
    ): Flow<Resource<Boolean>> = flow {
        try {
            val response = userRepository.putUserProfile(target = UserState.userId, nickname = nickname, img = image)
            if(response.isSuccessful) {
                userRepository.setNickname(nickname)
                userRepository.setProfileImg(nickname)
                UserState.nickname = nickname
                UserState.profileImg = img
                Log.d("ModifyUserUseCase", "성공..성공이오..")
                emit(Resource.Success(true))
            } else {
                Log.d("ModifyUserUseCase", "error")
                emit(Resource.Error("error"))
            }
        }catch (e: Exception) {
            Log.d(" ModifyUserUseCase", "error + ${e.message}")
            emit(Resource.Error("error + ${e.message}"))
        }
    }
}