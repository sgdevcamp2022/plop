package com.plop.plopmessenger.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plop.plopmessenger.domain.model.People
import com.plop.plopmessenger.domain.model.toPeople
import com.plop.plopmessenger.domain.repository.FriendRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PeopleViewModel @Inject constructor(
    private val friendRepository: FriendRepository
): ViewModel() {

    var peopleState = MutableStateFlow(PeopleState())
        private set

    init {
        getFriendList()
    }

    private fun getFriendList() {
        viewModelScope.launch {
            friendRepository.loadFriend().collect { result ->
                peopleState.update {
                    it.copy(friends = result.map { it.toPeople() })
                }
            }
        }
    }
}

data class PeopleState(
    val friends: List<People> = emptyList(),
    val requests: List<People> = emptyList()
)

