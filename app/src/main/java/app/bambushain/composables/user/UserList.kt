package app.bambushain.composables.user

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import app.bambushain.api.apis.UserApi
import app.bambushain.api.models.User
import org.koin.compose.koinInject

@Composable
fun UserList(navController: NavController, userApi: UserApi = koinInject()) {
    var userListState by remember { mutableStateOf<List<User>>(listOf()) }

    LaunchedEffect(userListState) {
        val response = userApi.getUsers()
        if (response.isSuccessful) {
            userListState = response.body()!!
        }
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(userListState) { user ->
            UserCard(user, userApi)
        }
    }
}