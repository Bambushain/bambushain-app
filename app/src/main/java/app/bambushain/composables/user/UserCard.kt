package app.bambushain.composables.user

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import app.bambushain.R
import app.bambushain.api.apis.UserApi
import app.bambushain.api.auth.AuthenticationSettings
import app.bambushain.api.infrastructure.ApiClient.Companion.defaultBasePath
import app.bambushain.api.models.User
import app.bambushain.composables.utils.ConfirmDialog
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
    onShowSnackBar: (String) -> Unit,
) {
    var model: ImageRequest? by remember { mutableStateOf(null) }
    val coroutineScope = rememberCoroutineScope()
    var expanded by remember { mutableStateOf(false) }
    var showModDialog by remember { mutableStateOf(false) }
    var showPasswordDialog by remember { mutableStateOf(false) }
    var showDeletePandaDialog by remember { mutableStateOf(false) }
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false,
    )
    val snackbarHostState = remember { SnackbarHostState() }

    val modStatusSuccess = stringResource(R.string.user_card_dialog_redact_mod_status_success, user.displayName)
    val modStatusError = stringResource(R.string.user_card_dialog_redact_mod_status_error)
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
            val (menuButton, menu, profilePicture, displayName, email, discord, mod, editButton, deleteButton) = createRefs();
            if (isMod) {
                IconButton(onClick = { expanded = !expanded }, modifier = Modifier.constrainAs(menuButton) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = null
                    )
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.constrainAs(menu) {
                        top.linkTo(menuButton.bottom)
                        end.linkTo(menuButton.end)
                    }
                ) {
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.user_card_dropdown_redact_mod_status)) },
                        onClick = {
                            showModDialog = true
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.user_card_dropdown_reset_password)) },
                        onClick = {
                            showPasswordDialog = true
                        })
                }
            }
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
                modifier = Modifier.padding(16.dp).fillMaxWidth().constrainAs(displayName) {
                    top.linkTo(profilePicture.bottom)
                    start.linkTo(profilePicture.start)
                    end.linkTo(profilePicture.end)
                },
                style = BambooTypography.titleMedium
            )
            Text(
                text = vm.user!!.email,
                modifier = Modifier.padding(top = 8.dp).fillMaxWidth().constrainAs(email) {
                    top.linkTo(displayName.bottom)
                    start.linkTo(displayName.start)
                    end.linkTo(displayName.end)
                },
            )
            if (vm.user!!.discordName.isNotEmpty()) {
                Text(
                    text = stringResource(R.string.user_discord_name, vm.user!!.discordName),
                    modifier = Modifier.padding(top = 8.dp).fillMaxWidth().constrainAs(discord) {
                        top.linkTo(email.bottom)
                        start.linkTo(email.start)
                        end.linkTo(email.end)
                    },
                )
            }
            if (vm.user!!.isMod) {
                Text(
                    text = stringResource(R.string.user_is_mod, vm.user!!.displayName),
                    modifier = Modifier.padding(top = 8.dp).fillMaxWidth().constrainAs(mod) {
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
                IconButton(onClick = {}, modifier = Modifier.constrainAs(editButton) {
                    end.linkTo(deleteButton.start)
                    top.linkTo(deleteButton.top)
                }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = stringResource(R.string.user_card_edit)
                    )
                }
                IconButton(onClick = {
                    showDeletePandaDialog = true
                }, modifier = Modifier.constrainAs(deleteButton) {
                    val linkTop = if (vm.user!!.isMod) {
                        mod
                    } else if (vm.user!!.discordName.isNotEmpty()) {
                        discord
                    } else {
                        email
                    }
                    end.linkTo(parent.end)
                    top.linkTo(linkTop.top)
                }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(R.string.user_card_delete_panda)
                    )
                }
            }
            if (showModDialog) {
                ConfirmDialog(
                    stringResource(R.string.user_card_dialog_redact_mod_status_title),
                    stringResource(R.string.user_card_dialog_redact_mod_status_text, user.displayName),
                    stringResource(R.string.user_card_dialog_redact_mod_status_confirm),
                    stringResource(R.string.user_card_dialog_redact_mod_status_dismiss),
                    {
                        coroutineScope.launch {
                            vm.revokeUserModRights(
                                onSuccess = {
                                    onShowSnackBar(modStatusSuccess)
                                },
                                onError = {
                                    onShowSnackBar(modStatusError)
                                }
                            )
                            showModDialog = false
                        }
                    },
                    {
                        showModDialog = false
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
                ModalBottomSheet(
                    modifier = Modifier.fillMaxHeight(),
                    sheetState = sheetState,
                    onDismissRequest = { showBottomSheet = false }
                ) {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                        value = vm.user!!.displayName,
                        label = { Text(stringResource(R.string.create_panda_name)) },
                        placeholder = { Text(stringResource(R.string.create_panda_placeholder_name)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        enabled = true,
                        onValueChange = { vm.user!!.displayName = it }
                    )
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                        value = vm.user!!.email,
                        label = { Text(stringResource(R.string.create_panda_email)) },
                        placeholder = { Text(stringResource(R.string.create_panda_placeholder_email)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        enabled = true,
                        onValueChange = { vm.user!!.email = it }
                    )
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                        value = vm.user!!.discordName,
                        placeholder = { Text(stringResource(R.string.create_panda_placeholder_discord)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        enabled = true,
                        onValueChange = { vm.user!!.discordName = it }
                    )
                    Button(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                        enabled = vm.user!!.displayName.isNotEmpty() && vm.user!!.email.isNotEmpty(),
                        onClick = {
                            coroutineScope.launch {
                                vm.updateUser(
                                    onSuccess = {

                                    },
                                    onError = {
                                        if(it == 409) {
                                            snackbarHostState.showSnackbar(updateUserUserExists)
                                        }
                                        else {
                                            snackbarHostState.showSnackbar(updateUserError)
                                        }
                                    }
                                )
                            }
                        }) {
                        Text(stringResource(R.string.create_panda_add_panda))
                    }
                }
            }
        }
    }
}
