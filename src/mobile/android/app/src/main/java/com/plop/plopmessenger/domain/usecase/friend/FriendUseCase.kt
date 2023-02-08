package com.plop.plopmessenger.domain.usecase.friend

import javax.inject.Inject

data class FriendUseCase @Inject constructor(
    val getFriendListUseCase: GetFriendListUseCase,
    val getFriendByNicknameListUseCase: GetFriendByNicknameListUseCase,
    val getRemoteFriendListUseCase: GetRemoteFriendListUseCase,
    val requestFriendUseCase: RequestFriendUseCase,
    val refuseRequestUseCase: RefuseRequestUseCase,
    val deleteFriendUseCase: DeleteFriendUseCase,
    val getFriendRequestListUseCase: GetFriendRequestListUseCase
)
