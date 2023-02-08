package com.plop.plopmessenger.domain.usecase.user

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
    val searchUserUseCase: SearchUserUseCase
)