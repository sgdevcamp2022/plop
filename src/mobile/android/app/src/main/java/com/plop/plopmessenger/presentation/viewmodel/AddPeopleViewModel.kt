package com.plop.plopmessenger.presentation.viewmodel

import android.util.Log
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plop.plopmessenger.domain.model.People
import com.plop.plopmessenger.domain.usecase.friend.FriendUseCase
import com.plop.plopmessenger.domain.usecase.user.UserUseCase
import com.plop.plopmessenger.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddPeopleViewModel @Inject constructor(
    private val userUseCase: UserUseCase,
    private val friendUseCase: FriendUseCase
): ViewModel() {
    var addPeopleState = MutableStateFlow(AddPeopleState())
        private set

    private fun searchPeople() {
        viewModelScope.launch {
            userUseCase.searchUserUseCase(addPeopleState.value.query.text).collect() { result ->
                when(result){
                    is Resource.Success -> {
                        addPeopleState.update {
                            it.copy(searchResult = result.data?: emptyList())
                        }
                        Log.d("SearchPeople", "success")
                    }
                    else -> {
                        Log.d("SearchPeople", "error")
                    }
                }
            }
        }
    }


    fun addPeople(people: People) {
        viewModelScope.launch {
            friendUseCase.requestFriendUseCase(people.email).collect(){ result ->
                when(result) {
                    is Resource.Success -> {
                        addPeopleState.update {
                            it.copy(checkedPeople = addPeopleState.value.checkedPeople.plus(people))
                        }
                    }
                    else -> {
                        Log.d("AddPeopleRequest", "실패...실패요..")
                    }
                }
            }
        }
    }

    fun deletePeople(people: People) {
        addPeopleState.update {
            it.copy(checkedPeople = addPeopleState.value.checkedPeople.minusElement(people))
        }
    }

    fun setQuery(query: TextFieldValue) {
        addPeopleState.update {
            it.copy(query = query)
        }
        if(query != TextFieldValue("")) searchPeople()
    }

    fun setFocusState(isFocus: Boolean) {
        addPeopleState.update {
            it.copy(textFieldFocusState = isFocus)
        }
    }
}

data class AddPeopleState(
    val friendList: List<People> = emptyList(),
    val checkedPeople: List<People> = emptyList(),
    val query: TextFieldValue = TextFieldValue(""),
    val textFieldFocusState: Boolean = false,
    val searchResult: List<People> = emptyList()
)