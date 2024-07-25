package app.bambushain.composables.user

import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import app.bambushain.R
import app.bambushain.api.apis.UserApi
import app.bambushain.api.auth.AuthenticationSettings
import app.bambushain.api.models.User
import app.bambushain.composables.utils.AddUserBottomSheet
import app.bambushain.composables.utils.NavBar
import app.bambushain.viewModels.user.PandaViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserList(navController: NavController, userApi: UserApi = koinInject(), context: Context = koinInject()) {
    val vm = koinViewModel<PandaViewModel>()
    val coroutineScope = rememberCoroutineScope()
    var myList by remember { mutableStateOf(arrayOf<User>()) }
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false,
    )


    val createPandaSuccess = stringResource(R.string.create_panda_success)
    val createPandaPandaExists = stringResource(R.string.create_panda_panda_exists)
    val createPandaError = stringResource(R.string.create_panda_error)

    val snackbarHostState = remember { SnackbarHostState() }

    val isMod = AuthenticationSettings.get(context)!!.user!!.isMod

    LaunchedEffect(myList) {
        val response = userApi.getUsers()
        if (response.isSuccessful) {
            myList = response.body()!!.toTypedArray()
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
        },
        bottomBar = { NavBar(navController, 1) }
    ) { contentPadding ->
        Surface(modifier = Modifier.padding(contentPadding)) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(myList) { user ->
                    UserCard(user, isMod, userApi, onUpdateUsers = {
                        var index = 0
                        for (item in myList) {
                            if (item.id == it.id) index = myList.indexOf(item)
                        }
                        myList[index] = myList[index].copy(
                            displayName = it.displayName,
                            email = it.email,
                            discordName = it.discordName,
                            isMod = it.isMod
                        )
                    }, onShowSnackBar = { coroutineScope.launch { snackbarHostState.showSnackbar(it) } })
                }
            }
            if (showBottomSheet) {
                AddUserBottomSheet(sheetState, onCreateUser = { displayName, email, discordName, isMod ->
                    coroutineScope.launch {
                        vm.pandaName = displayName
                        vm.pandaEmail = email
                        vm.pandaDiscord = discordName
                        vm.pandaMod = isMod
                        vm.createPanda(
                            onSuccess = {
                                showBottomSheet = false
                                val response = userApi.getUsers()
                                snackbarHostState.showSnackbar(createPandaSuccess)
                                if (response.isSuccessful) {
                                    myList = response.body()!!.toTypedArray()
                                }
                                vm.conflict = false
                            },
                            onError = {
                                if (it == 409) {
                                    snackbarHostState.showSnackbar(createPandaPandaExists)
                                    vm.conflict = true
                                } else {
                                    snackbarHostState.showSnackbar(createPandaError)
                                    vm.conflict = false
                                }
                            }
                        )
                    }
                    return@AddUserBottomSheet vm.conflict
                })
            }
        }
    }
}