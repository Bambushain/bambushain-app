package app.bambushain.composables.utils

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import app.bambushain.R
import app.bambushain.Screens

@Composable
fun NavBar(navController: NavController, selectedItem: Int) {
    NavigationBar {
        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(R.drawable.calendar_text),
                    contentDescription = stringResource(R.string.navbar_calendar)
                )
            },
            label = { Text(stringResource(R.string.navbar_calendar)) },
            selected = selectedItem == 0,
            onClick = {},
        )
        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(R.drawable.account_multiple),
                    contentDescription = stringResource(R.string.navbar_pandas)
                )
            },
            label = { Text(stringResource(R.string.navbar_pandas)) },
            selected = selectedItem == 1,
            onClick = {
                navController.navigate(Screens.PandasScreen.name)
            },
        )
        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(R.drawable.hiking),
                    contentDescription = stringResource(R.string.navbar_characters)
                )
            },
            label = { Text(stringResource(R.string.navbar_characters)) },
            selected = selectedItem == 2,
            onClick = { },
        )
        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(R.drawable.account),
                    contentDescription = stringResource(R.string.navbar_profile)
                )
            },
            label = { Text(stringResource(R.string.navbar_profile)) },
            selected = selectedItem == 3,
            onClick = { },
        )
    }
}