package com.plop.plopmessenger.presentation.screen.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.plop.plopmessenger.R
import com.plop.plopmessenger.domain.model.People
import com.plop.plopmessenger.presentation.component.PeopleItem
import com.plop.plopmessenger.presentation.component.PeopleWithTwoBtnItem
import com.plop.plopmessenger.presentation.component.SubTitle
import com.plop.plopmessenger.presentation.component.TopBarWithProfile
import com.plop.plopmessenger.presentation.state.UserState
import com.plop.plopmessenger.presentation.viewmodel.PeopleViewModel
import com.plop.plopmessenger.util.KeyLine


object PeopleScreenValue {
    var SpacerBetweenFriends = 12.dp
    var SpacerBetweenTitleAndRequest = 12.dp
}


@Composable
fun PeopleScreen(
    navigateToAddPeople: () -> Unit
) {
    val viewModel = hiltViewModel<PeopleViewModel>()
    val state by viewModel.peopleState.collectAsState()
    val friends = state.friends
    val requests = state.requests


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = KeyLine)
    ) {
        TopBarWithProfile(
            onClick = { navigateToAddPeople() },
            content = stringResource(id = R.string.people_title),
            icon = ImageVector.vectorResource(id = R.drawable.ic_person_add)
        )

        LazyColumn() {
            if(requests.isNotEmpty()) {
                item {
                    SubTitle(
                        content = stringResource(id = R.string.people_friend_request_subtitle)
                    )
                    Spacer(modifier = Modifier.size(PeopleScreenValue.SpacerBetweenTitleAndRequest))
                }
            }

            items(requests) { request ->
                PeopleWithTwoBtnItem(
                    onLeftClick = { viewModel.acceptRequest(request) },
                    onRightClick = { viewModel.rejectRequest(request) },
                    leftBtnContent = stringResource(id = R.string.people_accept_btn),
                    rightBtnContent = stringResource(id = R.string.people_deny_btn),
                    imageURL = request.profileImg,
                    nickname = request.nickname
                )
                Spacer(modifier = Modifier.size(PeopleScreenValue.SpacerBetweenFriends))
            }

            item {
                SubTitle(
                    content = stringResource(id = R.string.people_friend_subtitle),
                    modifier = Modifier.padding(vertical = 9.dp)
                )
            }

            items(friends) { friend ->
                PeopleItem(
                    imageURL = friend.profileImg,
                    nickname = friend.nickname
                )

                Spacer(modifier = Modifier.size(PeopleScreenValue.SpacerBetweenFriends))
            }
        }
    }
}