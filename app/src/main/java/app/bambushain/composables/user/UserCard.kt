package app.bambushain.composables.user

import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import app.bambushain.R
import app.bambushain.api.apis.UserApi
import app.bambushain.api.auth.AuthenticationSettings
import app.bambushain.api.infrastructure.ApiClient.Companion.defaultBasePath
import app.bambushain.api.models.User
import app.bambushain.composables.utils.ConfirmDialog
import app.bambushain.composables.utils.EditUserBottomSheet
import app.bambushain.ui.theme.BambooTypography
import app.bambushain.viewModels.user.UserCardViewModel
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserCard(
    user: User,
    isMod: Boolean,
    userApi: UserApi,
    context: Context = koinInject(),
    onUpdateUsers: (User) -> Unit,
    onShowSnackBar: (String) -> Unit,
) {
    var model: ImageRequest? by remember { mutableStateOf(null) }
    val coroutineScope = rememberCoroutineScope()
    var expanded by remember { mutableStateOf(false) }
    var showRedactModDialog by remember { mutableStateOf(false) }
    var showSetModDialog by remember { mutableStateOf(false) }
    var showPasswordDialog by remember { mutableStateOf(false) }
    var showDeletePandaDialog by remember { mutableStateOf(false) }
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false,
    )
    val snackbarHostState = remember { SnackbarHostState() }

    val modStatusRedactedSuccess = stringResource(R.string.user_card_dialog_redact_mod_status_success, user.displayName)
    val modStatusRedactedError = stringResource(R.string.user_card_dialog_redact_mod_status_error)
    val modStatusSetSuccess = stringResource(R.string.user_card_dialog_set_mod_status_success, user.displayName)
    val modStatusSetError = stringResource(R.string.user_card_dialog_set_mod_status_error)
    val passwordResetSuccess = stringResource(R.string.user_card_dialog_password_success)
    val passwordResetError = stringResource(R.string.user_card_dialog_password_error)
    val deletePandaSuccess = stringResource(R.string.user_card_dialog_delete_panda_success, user.displayName)
    val deletePandaError = stringResource(R.string.user_card_dialog_delete_panda_error)
    val updateUserSuccess = stringResource(R.string.user_card_edit_success)
    val updateUserUserExists = stringResource(R.string.user_card_edit_user_exists)
    val updateUserError = stringResource(R.string.user_card_edit_error)

    LaunchedEffect(model) {
        if (model == null) {
            val authData = AuthenticationSettings.get(context)!!
            model = ImageRequest
                .Builder(context)
                .data("$defaultBasePath/api/user/${user.id}/picture")
                .setHeader("Authorization", "Panda ${authData.token}")
                .fallback(R.drawable.default_profile_picture)
                .build()
        }
    }

    val vm = koinViewModel<UserCardViewModel>(key = user.id.toString()) {
        parametersOf(user, userApi)
    }

    OutlinedCard(
        modifier = Modifier.padding(16.dp)
    ) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (menuButton, menu, profilePicture, displayName, email, discord, mod, passwordButton, editButton, deleteButton) = createRefs();
            AsyncImage(
                model = model,
                contentDescription = vm.user!!.displayName,
                imageLoader = ImageLoader.Builder(context).components {
                    add(SvgDecoder.Factory())
                }.build(),
                modifier = Modifier.padding(bottom = 16.dp).constrainAs(profilePicture) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }
            )
            Text(
                text = vm.user!!.displayName,
                modifier = Modifier.padding(top = 16.dp, start = 16.dp).fillMaxWidth().constrainAs(displayName) {
                    top.linkTo(profilePicture.bottom)
                    start.linkTo(profilePicture.start)
                    end.linkTo(profilePicture.end)
                },
                style = BambooTypography.titleMedium
            )
            Text(
                text = vm.user!!.email,
                modifier = Modifier.padding(top = 8.dp, start = 16.dp).fillMaxWidth().constrainAs(email) {
                    top.linkTo(displayName.bottom)
                    start.linkTo(displayName.start)
                },
            )
            if (vm.user!!.discordName.isNotEmpty()) {
                Text(
                    text = stringResource(R.string.user_discord_name, vm.user!!.discordName),
                    modifier = Modifier.padding(top = 8.dp, start = 16.dp).fillMaxWidth().constrainAs(discord) {
                        top.linkTo(email.bottom)
                        start.linkTo(email.start)
                        end.linkTo(email.end)
                    },
                )
            }
            if (vm.user!!.isMod) {
                Text(
                    text = stringResource(R.string.user_is_mod, vm.user!!.displayName),
                    modifier = Modifier.padding(top = 8.dp, start = 16.dp).fillMaxWidth().constrainAs(mod) {
                        val constraintTo = if (vm.user!!.discordName.isNotEmpty()) {
                            discord
                        } else {
                            email
                        }
                        top.linkTo(constraintTo.bottom)
                        start.linkTo(constraintTo.start)
                        end.linkTo(constraintTo.end)
                    },
                )
            }
            if (isMod) {
                IconButton(
                    onClick = { showPasswordDialog = true },
                    modifier = Modifier.padding(top = 8.dp).constrainAs(passwordButton) {
                        val linkTop = if (vm.user!!.isMod) {
                            mod
                        } else if (vm.user!!.discordName.isNotEmpty()) {
                            discord
                        } else {
                            email
                        }
                        start.linkTo(parent.start)
                        top.linkTo(linkTop.bottom)
                    }) {
                    Icon(
                        painter = painterResource(R.drawable.lock_reset),
                        contentDescription = stringResource(R.string.user_card_reset_password)
                    )
                }
                IconButton(
                    onClick = { showBottomSheet = true },
                    modifier = Modifier.padding(top = 8.dp).constrainAs(editButton) {
                        start.linkTo(passwordButton.end)
                        top.linkTo(passwordButton.top)
                    }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = stringResource(R.string.user_card_edit)
                    )
                }
                IconButton(onClick = {
                    showDeletePandaDialog = true
                }, modifier = Modifier.padding(top = 8.dp).constrainAs(deleteButton) {

                    start.linkTo(editButton.end)
                    top.linkTo(editButton.top)
                }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(R.string.user_card_delete_panda)
                    )
                }
                IconButton(
                    onClick = { expanded = !expanded },
                    modifier = Modifier.padding(top = 8.dp).constrainAs(menuButton) {
                        top.linkTo(editButton.top)
                        end.linkTo(parent.end)
                    }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = null
                    )
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.constrainAs(menu) {
                            top.linkTo(menu.bottom)
                            end.linkTo(menu.end)
                        }
                    ) {
                        DropdownMenuItem(
                            text = {
                                if (user.isMod) {
                                    Text(stringResource(R.string.user_card_dropdown_redact_mod_status))
                                } else {
                                    Text(stringResource(R.string.user_card_dropdown_set_mod_status))
                                }
                            },
                            onClick = {
                                if (user.isMod) {
                                    showRedactModDialog = true
                                } else {
                                    showSetModDialog = true
                                }
                            }
                        )
                    }
                }
            }
            if (showRedactModDialog) {
                ConfirmDialog(
                    stringResource(R.string.user_card_dialog_redact_mod_status_title),
                    stringResource(R.string.user_card_dialog_redact_mod_status_text, user.displayName),
                    stringResource(R.string.user_card_dialog_redact_mod_status_confirm),
                    stringResource(R.string.user_card_dialog_redact_mod_status_dismiss),
                    {
                        coroutineScope.launch {
                            vm.revokeUserModRights(
                                onSuccess = {
                                    onShowSnackBar(modStatusRedactedSuccess)
                                    onUpdateUsers(vm.user!!)
                                    showRedactModDialog = false
                                },
                                onError = {
                                    onShowSnackBar(modStatusRedactedError)
                                    showRedactModDialog = false
                                }
                            )
                        }
                    },
                    {
                        showRedactModDialog = false
                    }
                )
            }
            if (showSetModDialog) {
                ConfirmDialog(
                    stringResource(R.string.user_card_dialog_set_mod_status_title),
                    stringResource(R.string.user_card_dialog_set_mod_status_text, user.displayName),
                    stringResource(R.string.user_card_dialog_set_mod_status_confirm),
                    stringResource(R.string.user_card_dialog_set_mod_status_dismiss),
                    {
                        coroutineScope.launch {
                            vm.setUserModRights(
                                onSuccess = {
                                    onShowSnackBar(modStatusSetSuccess)
                                    onUpdateUsers(vm.user!!)
                                    showSetModDialog = false
                                },
                                onError = {
                                    onShowSnackBar(modStatusSetError)
                                    showSetModDialog = false
                                }
                            )
                        }
                    },
                    {
                        showSetModDialog = false
                    }
                )
            }
            if (showPasswordDialog) {
                ConfirmDialog(
                    stringResource(R.string.user_card_dialog_password_title),
                    stringResource(R.string.user_card_dialog_password_text, user.displayName),
                    stringResource(R.string.user_card_dialog_password_confirm),
                    stringResource(R.string.user_card_dialog_password_dismiss),
                    {
                        coroutineScope.launch {
                            vm.resetPassword(
                                onSuccess = {
                                    onShowSnackBar(passwordResetSuccess)
                                    onUpdateUsers(vm.user!!)
                                },
                                onError = {
                                    onShowSnackBar(passwordResetError)
                                }
                            )
                            showPasswordDialog = false
                        }
                    },
                    {
                        showPasswordDialog = false
                    }
                )
            }
            if (showDeletePandaDialog) {
                ConfirmDialog(
                    stringResource(R.string.user_card_dialog_delete_panda_title),
                    stringResource(R.string.user_card_dialog_delete_panda_text, user.displayName),
                    stringResource(R.string.user_card_dialog_delete_panda_confirm),
                    stringResource(R.string.user_card_dialog_delete_panda_dismiss),
                    {
                        coroutineScope.launch {
                            vm.deleteUser(
                                onSuccess = {
                                    onShowSnackBar(deletePandaSuccess)
                                    onUpdateUsers(vm.user!!)
                                },
                                onError = {
                                    onShowSnackBar(deletePandaError)
                                }
                            )
                            showDeletePandaDialog = false
                        }
                    },
                    {
                        showDeletePandaDialog = false
                    }
                )
            }
            if (showBottomSheet) {
                EditUserBottomSheet(
                    sheetState,
                    user.displayName,
                    user.email,
                    user.discordName,
                    onUpdateUser = { displayName, email, discordName ->
                        vm.user!!.displayName = displayName
                        vm.user!!.email = email
                        vm.user!!.discordName = discordName
                        coroutineScope.launch {
                            vm.updateUser(
                                onSuccess = {
                                    showBottomSheet = false
                                    onUpdateUsers(vm.user!!)
                                    snackbarHostState.showSnackbar(updateUserSuccess)
                                },
                                onError = {
                                    if (it == 409) {
                                        snackbarHostState.showSnackbar(updateUserUserExists)
                                    } else {
                                        snackbarHostState.showSnackbar(updateUserError)
                                    }
                                }
                            )
                        }
                    })
            }
        }
    }
}
