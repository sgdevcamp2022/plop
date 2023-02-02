package com.plop.plopmessenger.presentation.screen.main

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.plop.plopmessenger.domain.model.People
import com.plop.plopmessenger.presentation.component.*
import com.plop.plopmessenger.util.KeyLine
import com.plop.plopmessenger.util.SearchDisplay
import com.plop.plopmessenger.R
import com.plop.plopmessenger.presentation.viewmodel.AddGroupChatViewModel

object AddGroupChatValue {
    val BetweenFriendPaddingSize = 10.dp
    val BetweenFriendAndCheckedSize = 10.dp
}

@Composable
fun AddGroupChatScreen(
    upPress: () -> Unit,
    navigateToNewChat: () -> Unit,
) {
    var query by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }

    val viewModel = hiltViewModel<AddGroupChatViewModel>()

    //검색결과
    val result by remember{ mutableStateOf(listOf<People>()) }
    //모든 친구 목록
    val default by remember{ mutableStateOf(listOf<People>()) }

    var textFieldFocusState by remember { mutableStateOf(false) }
    var focusManager = LocalFocusManager.current

    //추가할 멤버
    val checkedPeople by viewModel.addGroupChatState.collectAsState()

    var searchDisplay : SearchDisplay = when {
        query == TextFieldValue("") -> SearchDisplay.Default
        result.isNotEmpty() -> SearchDisplay.Results
        else -> SearchDisplay.NoResults
    }

    Column(
        modifier = Modifier
            .padding(horizontal = KeyLine)
    ) {
        MainTopBarWithBothBtn(
            onLeftBtnClick = upPress,
            onRightBtnClick = navigateToNewChat,
            content = stringResource(id = R.string.add_chat_group_top_bar),
            leftContent = stringResource(id = R.string.add_chat_group_cancel_btn),
            rightContent = stringResource(id = R.string.add_chat_group_add_btn),
            rightVisible = checkedPeople.checkedPeople.size >= 2
        )

        SearchBar(
            query = query,
            onQueryChange = { query = it },
            searchFocused = textFieldFocusState,
            onSearchFocusChange = { textFieldFocusState = it },
            onClearQuery = {
                focusManager.clearFocus()
                query = TextFieldValue("")
            },
            modifier = Modifier.padding(vertical = 8.dp)
        )

        LazyRow(
            modifier = Modifier.padding(vertical = AddGroupChatValue.BetweenFriendAndCheckedSize),
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            items(checkedPeople.checkedPeople) { friend ->
                ProfileWithDeleteBtn(
                    imageURL = "",
                    onClick = { viewModel.deletePeople(people = friend) }
                )
            }
        }

        when(searchDisplay) {
            SearchDisplay.NoResults -> {
                NoResultScreen()
            }
            SearchDisplay.Default -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(AddGroupChatValue.BetweenFriendPaddingSize)
                ) {
                    item {
                        SubTitle(content = stringResource(id = R.string.add_chat_group_friend_subtitle))
                    }
                    items(default) { friend ->
                        PeopleWithCheckItem(
                            onClick = {
                                if(friend in checkedPeople.checkedPeople) viewModel.deletePeople(people = friend)
                                else viewModel.addPeople(people = friend)
                            },
                            imageURL = friend.profileImg,
                            nickname = friend.nickname,
                            isChecked = checkedPeople.checkedPeople.contains(friend)
                        )
                        Divider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    start = PeopleItemValue.ProfileSize.dp
                                )
                        )
                    }
                }
            }
            SearchDisplay.Results -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(AddGroupChatValue.BetweenFriendPaddingSize)
                ) {
                    items(result) { friend ->
                        PeopleWithCheckItem(
                            onClick = {
                                if(friend in checkedPeople.checkedPeople) viewModel.deletePeople(people = friend)
                                else viewModel.addPeople(people = friend)
                            },
                            imageURL = friend.profileImg,
                            nickname = friend.nickname,
                            isChecked = checkedPeople.checkedPeople.contains(friend)
                        )
                        Divider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    start = PeopleItemValue.ProfileSize.dp,
                                )
                        )
                    }
                }
            }
        }
    }

}