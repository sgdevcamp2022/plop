package com.plop.plopmessenger.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.plop.plopmessenger.domain.model.People
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AddPeopleViewModel @Inject constructor(): ViewModel() {
    var addPeopleState = MutableStateFlow(AddPeopleState())
        private set


    fun addPeople(people: People) {
        addPeopleState.update {
            it.copy(checkedPeople = addPeopleState.value.checkedPeople.plus(people))
        }
    }

    fun deletePeople(people: People) {
        addPeopleState.update {
            it.copy(checkedPeople = addPeopleState.value.checkedPeople.minusElement(people))
        }
    }
}

data class AddPeopleState(
    val checkedPeople: List<People> = emptyList()
)