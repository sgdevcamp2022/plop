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

    val viewModel = hiltViewModel<AddPeopleViewModel>()
    val state by viewModel.addPeopleState.collectAsState()
    var searchDisplay : SearchDisplay = when {
        state.query == TextFieldValue("") -> SearchDisplay.Default
        state.searchResult != null -> SearchDisplay.Results
        else -> SearchDisplay.NoResults
    }
    var focusManager = LocalFocusManager.current

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
            query = state.query,
            onQueryChange = viewModel::setQuery,
            searchFocused = state.textFieldFocusState,
            onSearchFocusChange = viewModel::setFocusState,
            onClearQuery = {
                focusManager.clearFocus()
                viewModel.setQuery(TextFieldValue(""))
            },
            modifier = Modifier.padding(vertical = 8.dp)
        )

        when(searchDisplay) {
            SearchDisplay.NoResults -> {
                NoResultScreen()
            }
            SearchDisplay.Default -> {
                NoResultScreen()
            }
            SearchDisplay.Results -> {
                PeopleList(
                    result = state.searchResult,
                    onClick = viewModel::addPeople,
                    onClickedClick = viewModel::deletePeople,
                    checkedPeople = state.checkedPeople
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