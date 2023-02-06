package com.plop.plopmessenger.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plop.plopmessenger.domain.model.People
import com.plop.plopmessenger.domain.model.toPeople
import com.plop.plopmessenger.domain.repository.FriendRepository
import com.plop.plopmessenger.domain.usecase.friend.FriendUseCase
import com.plop.plopmessenger.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
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
    }

    private fun getFriendList() {
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

