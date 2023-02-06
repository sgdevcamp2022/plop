package com.plop.plopmessenger.domain.usecase.friend

import javax.inject.Inject

data class FriendUseCase @Inject constructor(
    val getFriendListUseCase: GetFriendListUseCase,
    val getFriendByNicknameListUseCase: GetFriendByNicknameListUseCase
)
