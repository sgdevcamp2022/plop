package com.plop.plopmessenger.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.plop.plopmessenger.domain.model.People
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
class AddGroupChatViewModel @Inject constructor(): ViewModel() {
    var addGroupChatState = MutableStateFlow(AddGroupChatState())
        private set


    fun addPeople(people: People) {
        addGroupChatState.update {
            it.copy(checkedPeople = addGroupChatState.value.checkedPeople.plus(people))
        }
        Log.d("가희", addGroupChatState.value.checkedPeople.size.toString())
    }

    fun deletePeople(people: People) {
        addGroupChatState.update {
            it.copy(checkedPeople = addGroupChatState.value.checkedPeople.minusElement(people))
        }
        Log.d("가희", addGroupChatState.value.checkedPeople.size.toString())
    }
}

data class AddGroupChatState(
    val checkedPeople: List<People> = emptyList()
)