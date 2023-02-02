package com.plop.plopmessenger.presentation.screen.main

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import com.plop.plopmessenger.presentation.component.MainTopBarWithLeftBtn
import com.plop.plopmessenger.presentation.component.PeopleWithSingleBtnItem
import com.plop.plopmessenger.presentation.component.SearchBar
import com.plop.plopmessenger.presentation.component.SubTitle
import com.plop.plopmessenger.util.KeyLine
import com.plop.plopmessenger.util.SearchDisplay
import com.plop.plopmessenger.R
import com.plop.plopmessenger.presentation.viewmodel.AddPeopleViewModel

object AddPeopleScreenValue {
    const val ProfileSize = 56
    val ProfileDividerPadding = 2.dp
}

@Composable
fun AddPeopleScreen(
    upPress: () -> Unit
) {
    var query by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }
    //검색결과
    val result by remember{ mutableStateOf(listOf<People>()) }
    //모든 친구 목록
    val default by remember{ mutableStateOf(listOf<People>(
    )) }
    var textFieldFocusState by remember { mutableStateOf(false) }
    var focusManager = LocalFocusManager.current
    var searchDisplay : SearchDisplay = when {
        query == TextFieldValue("") -> SearchDisplay.Default
        result.isNotEmpty() -> SearchDisplay.Results
        else -> SearchDisplay.NoResults
    }

    val viewModel = hiltViewModel<AddPeopleViewModel>()
    val checkedPeople = viewModel.addPeopleState.collectAsState()

    Column(
        modifier = Modifier
            .padding(horizontal = KeyLine)
    ) {
        MainTopBarWithLeftBtn(
            onLeftBtnClick = upPress,
            content = stringResource(id = R.string.add_friend_top_bar),
            leftContent = stringResource(id = R.string.add_friend_cancel_btn)
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

        when(searchDisplay) {
            SearchDisplay.NoResults -> {
                NoResultScreen()
            }
            SearchDisplay.Default -> {
                PeopleList(
                    result = default,
                    onClick = viewModel::addPeople,
                    onClickedClick = viewModel::deletePeople,
                    checkedPeople = checkedPeople.value.checkedPeople
                ) {
                    SubTitle(content = stringResource(id = R.string.add_chat_friend_subtitle))
                }
            }
            SearchDisplay.Results -> {
                PeopleList(
                    result = result,
                    onClick = viewModel::addPeople,
                    onClickedClick = viewModel::deletePeople,
                    checkedPeople = checkedPeople.value.checkedPeople
                ){}
            }
        }
    }
}



@Composable
private fun PeopleList(
    result: List<People>,
    onClick: (People) -> Unit,
    onClickedClick: (People) -> Unit,
    checkedPeople: List<People>,
    content: @Composable () -> Unit
) {
    LazyColumn(
    ) {
        item { content() }

        items(result) { friend ->

            PeopleWithSingleBtnItem(
                onClick = { onClick(friend) },
                onClickedClick = { onClickedClick(friend) },
                btnContent = stringResource(id = R.string.add_friend_request_btn),
                imageURL = friend.profileImg,
                nickname = friend.nickname,
                clickedContent = stringResource(id = R.string.add_friend_requested_btn),
                isClicked = checkedPeople.contains(friend)
            )
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = AddPeopleScreenValue.ProfileSize.dp,
                        top = AddPeopleScreenValue.ProfileDividerPadding,
                        bottom = AddPeopleScreenValue.ProfileDividerPadding
                    )
            )
        }
    }
}