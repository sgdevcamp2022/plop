package com.plop.plopmessenger.domain.usecase.user.pref

import android.net.Uri
import com.plop.plopmessenger.domain.repository.UserRepository
import com.plop.plopmessenger.presentation.state.UserState
import javax.inject.Inject

class SetProfileImgUseCase @Inject constructor(
    private val userRepository: UserRepository
){
    suspend operator fun invoke(profileImg: String) {
        userRepository.setProfileImg(profileImg)
        UserState.profileImg = Uri.parse(profileImg)
    }
}