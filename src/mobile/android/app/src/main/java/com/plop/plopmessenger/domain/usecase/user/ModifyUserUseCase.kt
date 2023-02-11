package com.plop.plopmessenger.domain.usecase.user

import android.net.Uri
import android.util.Log
import com.plop.plopmessenger.data.dto.request.user.PutUserProfileRequest
import com.plop.plopmessenger.domain.repository.UserRepository
import com.plop.plopmessenger.domain.util.Resource
import com.plop.plopmessenger.presentation.state.UserState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ModifyUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        nickname: String,
        image: Uri?
    ) {
        try {
            Log.d("ㅁㄴㅇㄹ", nickname)
            userRepository.setNickname(nickname)
            userRepository.setProfileImg(image.toString())
            UserState.nickname = nickname
            UserState.profileImg = image
//            val response = userRepository.putUserProfile(PutUserProfileRequest("image", nickname))
//            when(response.code()) {
//                200 -> {
//                    userRepository.setNickname(nickname)
//                    userRepository.setProfileImg(nickname)
//                    emit(Resource.Success(true))
//                }
//                else -> {
//                    Log.d("ModifyUserUseCase", "error")
//                    emit(Resource.Error("error"))
//                }
//            }
        }catch (e: Exception) {
            Log.d(" ModifyUserUseCase", "error")
            //emit(Resource.Error("error"))
        }
    }
}