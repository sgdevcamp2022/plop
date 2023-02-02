package com.plop.plopmessenger.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.plop.plopmessenger.domain.model.People
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
class AddChatMemberViewModel @Inject constructor(): ViewModel() {

    var addChatMemberState = MutableStateFlow(AddChatMemeberState())
        private set


    fun addPeople(people: People) {
        addChatMemberState.update {
            it.copy(checkedPeople = addChatMemberState.value.checkedPeople.plus(people))
        }
    }

    fun deletePeople(people: People) {
        addChatMemberState.update {
            it.copy(checkedPeople = addChatMemberState.value.checkedPeople.minusElement(people))
        }
    }
}

data class AddChatMemeberState(
    val checkedPeople: List<People> = emptyList()
)
