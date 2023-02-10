package com.plop.plopmessenger.domain.usecase.user.pref

import com.plop.plopmessenger.domain.repository.UserRepository
import javax.inject.Inject

class SetProfileImgUseCase @Inject constructor(
    private val userRepository: UserRepository
){
    suspend operator fun invoke(profileImg: String) {
        userRepository.setProfileImg(profileImg)
    }
}