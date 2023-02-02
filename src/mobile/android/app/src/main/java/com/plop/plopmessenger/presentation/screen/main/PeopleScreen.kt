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
import com.plop.plopmessenger.R
import com.plop.plopmessenger.domain.model.People
import com.plop.plopmessenger.presentation.component.PeopleItem
import com.plop.plopmessenger.presentation.component.PeopleWithTwoBtnItem
import com.plop.plopmessenger.presentation.component.SubTitle
import com.plop.plopmessenger.presentation.component.TopBarWithProfile
import com.plop.plopmessenger.util.KeyLine


object PeopleScreenValue {
    var SpacerBetweenFriends = 12.dp
    var SpacerBetweenTitleAndRequest = 12.dp
}


@Composable
fun People(
    navigateToAddPeople: () -> Unit
) {
    var friendsRequest by remember{ mutableStateOf(listOf<People>(
    )) }
    var friends by remember{ mutableStateOf(listOf<People>(
    )) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = KeyLine)
    ) {
        TopBarWithProfile(
            onClick = { navigateToAddPeople() },
            profileImage = "",
            content = stringResource(id = R.string.people_title),
            icon = ImageVector.vectorResource(id = R.drawable.ic_person_add)
        )

        LazyColumn() {
            if(friendsRequest.isNotEmpty()) {
                item {
                    SubTitle(
                        content = stringResource(id = R.string.people_friend_request_subtitle)
                    )
                    Spacer(modifier = Modifier.size(PeopleScreenValue.SpacerBetweenTitleAndRequest))
                }
            }

            items(friendsRequest) { request ->
                PeopleWithTwoBtnItem(
                    onLeftClick = { /*TODO*/ },
                    onRightClick = { /*TODO*/ },
                    leftBtnContent = stringResource(id = R.string.people_accept_btn),
                    rightBtnContent = stringResource(id = R.string.people_deny_btn),
                    imageURL = "",
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
                    imageURL = "",
                    nickname = friend.nickname
                )

                Spacer(modifier = Modifier.size(PeopleScreenValue.SpacerBetweenFriends))
            }
        }
    }
}