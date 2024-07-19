package app.bambushain.composables.user

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import app.bambushain.R
import app.bambushain.api.apis.UserApi
import app.bambushain.api.auth.AuthenticationSettings
import app.bambushain.api.models.User
import app.bambushain.viewModels.user.PandaViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

enum class UserCardMessage {
    ResetPassword,
    Error
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserList(navController: NavController, userApi: UserApi = koinInject(), context: Context = koinInject()) {
    val vm = koinViewModel<PandaViewModel>()
    val coroutineScope = rememberCoroutineScope()
    var userListState by remember { mutableStateOf<List<User>>(listOf()) }
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false,
    )

    val snackbarHostState = remember { SnackbarHostState() }

    val isMod = AuthenticationSettings.get(context)!!.user!!.isMod

    LaunchedEffect(userListState) {
        val response = userApi.getUsers()
        if (response.isSuccessful) {
            userListState = response.body()!!
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.user_list_top_bar))
                },
                actions = {
                    IconButton(onClick = { showBottomSheet = true }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = stringResource(R.string.user_list_add_user),
                        )
                    }
                },
                scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
            )
        }
    ) { contentPadding ->
        Surface(modifier = Modifier.padding(contentPadding)) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(userListState) { user ->
                    UserCard(user, isMod, userApi) {
                        coroutineScope.launch { snackbarHostState.showSnackbar(it) }
                    }
                }
            }
            if (showBottomSheet) {
                ModalBottomSheet(
                    modifier = Modifier.fillMaxHeight(),
                    sheetState = sheetState,
                    onDismissRequest = { showBottomSheet = false }
                ) {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                        value = vm.pandaName,
                        label = { Text(stringResource( R.string.create_panda_name)) },
                        placeholder = { Text(stringResource(R.string.create_panda_placeholder_name)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        enabled = true,
                        onValueChange = { vm.pandaName = it }
                    )
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                        value = vm.pandaEmail,
                        label = { Text(stringResource( R.string.create_panda_email)) },
                        placeholder = { Text(stringResource(R.string.create_panda_placeholder_email)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        enabled = true,
                        onValueChange = { vm.pandaEmail = it }
                    )
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                        value = vm.pandaDiscord,
                        placeholder = { Text(stringResource(R.string.create_panda_placeholder_discord)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        enabled = true,
                        onValueChange = { vm.pandaDiscord = it }
                    )
                    Row(modifier = Modifier.padding(bottom = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,) {
                        Switch(
                            checked = vm.pandaMod,
                            onCheckedChange = { vm.pandaMod = it }
                        )
                        Text(stringResource(R.string.create_panda_isMod), modifier = Modifier.padding(horizontal = 8.dp))
                    }
                }
            }
        }
    }

}