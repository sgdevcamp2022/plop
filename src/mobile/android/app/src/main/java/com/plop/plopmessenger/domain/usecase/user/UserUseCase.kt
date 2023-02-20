package com.plop.plopmessenger.domain.usecase.user

import com.plop.plopmessenger.domain.usecase.user.pref.*
import javax.inject.Inject

data class UserUseCase @Inject constructor(
    val loginUseCase: LoginUseCase,
    val autoLoginUseCase: AutoLoginUseCase,
    val logoutUseCase: LogoutUseCase,
    val signUpUseCase: SignUpUseCase,
    val requestEmailCodeUseCase: RequestEmailCodeUseCase,
    val verifyEmailUseCase: VerifyEmailUseCase,
    val withdrawalUseCase: WithdrawalUseCase,
    val findPasswordUseCase: FindPasswordUseCase,
    val getUserProfileUseCase: GetUserProfileUseCase,
    val modifyUserUseCase: ModifyUserUseCase,
    val searchUserUseCase: SearchUserUseCase,
    val getUserInfoUseCase: GetUserInfoUseCase,
    val setAccessTokenUseCase: SetAccessTokenUseCase,
    val setRefreshTokenUseCase: SetRefreshTokenUseCase,
    val setNicknameUseCase: SetNicknameUseCase,
    val setUserIdUseCase: SetUserIdUseCase,
    val setProfileImgUseCase: SetProfileImgUseCase,
    val setThemeUseCase: SetThemeUseCase,
    val setAlarmUseCase: SetAlarmUseCase,
    val setActiveUseCase: SetActiveUseCase
)