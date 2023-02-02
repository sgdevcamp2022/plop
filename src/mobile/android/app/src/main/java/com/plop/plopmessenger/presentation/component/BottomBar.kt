package com.plop.plopmessenger.presentation.component

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
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


@Composable
fun SmgBottomBar(
    tabs: Array<BottomBarTabs>,
    currentRoute: String? = null,
    navigateToRoute: (String) -> Unit = {},
    unSelectedColor: Color = MaterialTheme.colors.background,
    selectedColor: Color = MaterialTheme.colors.onPrimary
) {
    BottomNavigation(
        backgroundColor = MaterialTheme.colors.background,
        modifier = Modifier
            .navigationBarsPadding()
            .height(56.dp)
    ) {
        tabs.forEach { section ->
            BottomNavigationItem(
                selected = section.route == currentRoute,
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
                        painter = painterResource(id = section.icon),
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
    val route: String
) {
    Chats(R.string.bottom_chats, R.drawable.ic_bottombar_chat, BottomBarDestinations.CHATS_ROUTE),
    People(R.string.bottom_people, R.drawable.ic_bottombar_people, BottomBarDestinations.PEOPLE_ROUTE),
    Setting(R.string.bottom_setting, R.drawable.ic_bottombar_setting, BottomBarDestinations.SETTING_ROUTE);
}