package com.plop.plopmessenger.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plop.plopmessenger.domain.model.People
import com.plop.plopmessenger.domain.model.PeopleStatusType
import com.plop.plopmessenger.domain.model.toPeople
import com.plop.plopmessenger.domain.repository.FriendRepository
import com.plop.plopmessenger.domain.usecase.friend.FriendUseCase
import com.plop.plopmessenger.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PeopleViewModel @Inject constructor(
    private val friendUseCase: FriendUseCase
): ViewModel() {

    var peopleState = MutableStateFlow(PeopleState())
        private set

    init {
        getFriendList()
        getFriendLocalList()
        getFriendResponseList()
    }

    private fun getFriendResponseList() {
        viewModelScope.launch {
            friendUseCase.getFriendResponseListUseCase().collect() { result ->
                when (result) {
                    is Resource.Success -> {
                        peopleState.update {
                            it.copy(
                                requests = result.data ?: emptyList(),
                                isLoading = false,
                            )
                        }
                    }
                    else -> {
                        Log.d("GetFriendRequestListUseCase", "실패...실패오..")
                    }
                }
            }
        }
    }

    fun rejectRequest(target: People) {
        viewModelScope.launch {
            friendUseCase.rejectRequestUseCase(target.email).collect() { result ->
                when(result) {
                    is Resource.Success -> {
                        peopleState.update {
                            it.copy(
                                requests= peopleState.value.requests.minus(target)
                            )
                        }
                    }
                    else -> {
                        Log.d("RejectRequest", "실패..")
                    }
                }
            }
        }
    }

    fun acceptRequest(target: People) {
        viewModelScope.launch {
            friendUseCase.acceptRequestUseCase(target.email).collect() { result ->
                when(result) {
                    is Resource.Success -> {
                        peopleState.update {
                            it.copy(
                                requests= peopleState.value.requests.minus(target)
                            )
                        }
                        insertFriend(target)
                    }
                    else -> {
                        Log.d("RejectRequest", "실패..")
                    }
                }
            }
        }
    }

    private fun insertFriend(people: People) {
        viewModelScope.launch {
            people.status = PeopleStatusType.FRIEND
            friendUseCase.addFriendUseCase(people).collect() { result ->
                when(result) {
                    is Resource.Success -> {}
                    else -> {
                        Log.d("InsertFriend", "error")
                    }
                }
            }
        }
    }

    private fun getFriendList() {
        viewModelScope.launch {
            friendUseCase.getRemoteFriendListUseCase().collect() { result ->
                when(result) {
                    is Resource.Success -> {

                    }
                    else -> {
                        Log.d("GetRemoteFriendList", "error")
                    }
                }
            }
        }
    }

    private fun getFriendLocalList() {
        viewModelScope.launch {
            friendUseCase.getFriendListUseCase().collect() { result ->
                when (result) {
                    is Resource.Success -> {
                        peopleState.update {
                            it.copy(
                                friends = result.data ?: emptyList(),
                                isLoading = false,
                            )
                        }
                    }
                    is Resource.Loading -> {
                        peopleState.update {
                            it.copy(isLoading = true)
                        }
                    }
                    is Resource.Error -> {
                        peopleState.update {
                            it.copy(isLoading = false)
                        }
                    }
                }
            }
        }
    }
}

data class PeopleState(
    val friends: List<People> = emptyList(),
    val requests: List<People> = emptyList(),
    val isLoading: Boolean = false
)

