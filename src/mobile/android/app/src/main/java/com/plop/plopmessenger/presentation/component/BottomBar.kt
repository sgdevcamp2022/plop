package com.plop.plopmessenger.presentation.component

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.plop.plopmessenger.R
import com.plop.plopmessenger.presentation.navigation.BottomBarDestinations
import com.plop.plopmessenger.presentation.theme.Gray600


@Composable
fun PlopBottomBar(
    tabs: Array<BottomBarTabs>,
    currentRoute: String? = null,
    navigateToRoute: (String) -> Unit = {},
    unSelectedColor: Color = Gray600,
    selectedColor: Color = MaterialTheme.colors.primary
) {
    BottomNavigation(
        backgroundColor = MaterialTheme.colors.background,
        modifier = Modifier
            .navigationBarsPadding()
            .fillMaxWidth()
            .height(56.dp)
    ) {
        tabs.forEach { section ->

            var selected = section.route == currentRoute
            BottomNavigationItem(
                selected = selected,
                onClick = bottomNavigateAction(section, navigateToRoute),
                label = {
                    Text(
                        text = stringResource(id = section.title),
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp
                    )
                },
                icon = {
                    Image(
                        painter = painterResource(id = if(selected) section.selectedIcon else section.icon),
                        contentDescription = section.route,
                        modifier = Modifier.size(27.dp)
                    )
                },
                selectedContentColor = selectedColor,
                unselectedContentColor = unSelectedColor
            )
        }
    }
}



@Composable
private fun bottomNavigateAction(
    destination: BottomBarTabs,
    navigateToRoute: (String) -> Unit
):()-> Unit = { navigateToRoute(destination.route) }


enum class BottomBarTabs(
    @StringRes val title: Int,
    @DrawableRes val icon: Int,
    @DrawableRes val selectedIcon: Int,
    val route: String
) {
    Chats(R.string.bottom_chats, R.drawable.ic_bottombar_chat, R.drawable.ic_bottombar_chat_selected, BottomBarDestinations.CHATS_ROUTE),
    People(R.string.bottom_people, R.drawable.ic_bottombar_people, R.drawable.ic_bottombar_people_selected, BottomBarDestinations.PEOPLE_ROUTE),
    Setting(R.string.bottom_setting, R.drawable.ic_bottombar_setting, R.drawable.ic_bottombar_setting_selected, BottomBarDestinations.SETTING_ROUTE);
}