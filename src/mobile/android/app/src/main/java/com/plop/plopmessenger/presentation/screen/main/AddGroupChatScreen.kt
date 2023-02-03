package com.plop.plopmessenger.presentation.screen.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.plop.plopmessenger.R
import com.plop.plopmessenger.presentation.component.*
import com.plop.plopmessenger.presentation.viewmodel.AddGroupChatViewModel
import com.plop.plopmessenger.util.KeyLine
import com.plop.plopmessenger.util.SearchDisplay

object AddGroupChatValue {
    val BetweenFriendPaddingSize = 10.dp
    val BetweenFriendAndCheckedSize = 10.dp
}

@Composable
fun AddGroupChatScreen(
    upPress: () -> Unit,
    navigateToNewChat: () -> Unit,
) {

    val viewModel = hiltViewModel<AddGroupChatViewModel>()
    val state by viewModel.addGroupChatState.collectAsState()
    var query = state.query
    val result = state.result
    var focusManager = LocalFocusManager.current

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
            rightVisible = state.checkedPeople.size >= 2
        )

        SearchBar(
            query = query,
            onQueryChange = viewModel::setQuery,
            searchFocused = state.textFieldFocusState,
            onSearchFocusChange = viewModel::setFocusState,
            onClearQuery = {
                focusManager.clearFocus()
                viewModel.setQuery(TextFieldValue(""))
            },
            modifier = Modifier.padding(vertical = 8.dp)
        )


        LazyRow(
            modifier = Modifier.padding(vertical = AddGroupChatValue.BetweenFriendAndCheckedSize),
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            items(state.checkedPeople) { friend ->
                ProfileWithDeleteBtn(
                    imageURL = friend.profileImg,
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
                    items(state.friends) { friend ->
                        PeopleWithCheckItem(
                            onClick = {
                                if(friend in state.checkedPeople) viewModel.deletePeople(people = friend)
                                else viewModel.addPeople(people = friend)
                            },
                            imageURL = friend.profileImg,
                            nickname = friend.nickname,
                            isChecked = state.checkedPeople.contains(friend)
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
                                if(friend in state.checkedPeople) viewModel.deletePeople(people = friend)
                                else viewModel.addPeople(people = friend)
                            },
                            imageURL = friend.profileImg,
                            nickname = friend.nickname,
                            isChecked = state.checkedPeople.contains(friend)
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