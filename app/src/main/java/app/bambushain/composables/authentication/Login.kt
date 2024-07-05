package app.bambushain.composables.authentication

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import app.bambushain.R
import app.bambushain.Screens
import app.bambushain.viewModels.authentication.LoginViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController) {
    val vm = koinViewModel<LoginViewModel>()
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val loginErrorTwoFactorRequest = stringResource(R.string.login_error_two_factor_request)
    val loginErrorLogin = stringResource(R.string.login_error_login)

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.login_caption))
                },
                scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
            )
        }
    ) { contentPadding ->
        Surface(modifier = Modifier.padding(contentPadding)) {
            Column(modifier = Modifier.padding(16.dp)) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    value = vm.userName,
                    label = { Text(stringResource(R.string.login_label_email_or_name)) },
                    placeholder = { Text(stringResource(R.string.login_placeholder_email_or_name)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    enabled = !vm.twoFactorRequested,
                    onValueChange = { vm.userName = it }
                )
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    value = vm.password,
                    label = { Text(stringResource(R.string.login_label_password)) },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    enabled = !vm.twoFactorRequested,
                    onValueChange = { vm.password = it }
                )
                if (vm.twoFactorRequested) {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                        value = vm.twoFactor,
                        label = { Text(stringResource(R.string.login_label_two_factor_code)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        onValueChange = { vm.twoFactor = it },
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Spacer(Modifier.weight(1f))
                    Button(
                        enabled = vm.loginAllowed,
                        onClick = {
                            coroutineScope.launch {
                                if (vm.twoFactorRequested) {
                                    vm.login(
                                        onSuccess = {
                                            navController.navigate(Screens.LoginScreen.name)
                                        },
                                        onError = {
                                            snackbarHostState.showSnackbar(loginErrorLogin)
                                        })
                                } else {
                                    vm.requestTwoFactor(onError = {
                                        snackbarHostState.showSnackbar(loginErrorTwoFactorRequest)
                                    })
                                }
                            }
                        }) {
                        Text(stringResource(R.string.login_button))
                    }
                }
            }
        }
    }
}