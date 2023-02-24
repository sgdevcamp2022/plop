package com.plop.plopmessenger.domain.usecase.friend

import javax.inject.Inject

data class FriendUseCase @Inject constructor(
    val getFriendListUseCase: GetFriendListUseCase,
    val getFriendByNicknameListUseCase: GetFriendByNicknameListUseCase,
    val getRemoteFriendListUseCase: GetRemoteFriendListUseCase,
    val requestFriendUseCase: RequestFriendUseCase,
    val rejectRequestUseCase: RejectRequestUseCase,
    val acceptRequestUseCase: AcceptRequestUseCase,
    val deleteFriendUseCase: DeleteFriendUseCase,
    val getFriendRequestListUseCase: GetFriendRequestListUseCase,
    val getFriendResponseListUseCase: GetFriendResponseListUseCase,
    val addFriendUseCase: AddFriendUseCase,
    val friendsUseCase: DeleteFriendsUseCase
)
