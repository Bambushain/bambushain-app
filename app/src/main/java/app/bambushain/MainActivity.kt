package app.bambushain

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import app.bambushain.ui.theme.BambushainTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BambushainTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Login()
                }
            }
        }
    }
}

@Composable
fun Login() {
    Column() {
        var loginSuccess by remember { mutableStateOf(false) }
        var isError by remember { mutableStateOf(false) }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
            Text(text = stringResource(R.string.login_caption))
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            OutlinedTextField(
                "",
                label = { Text(text = stringResource(R.string.login_lable_email_or_name)) },
                placeholder = { Text(text = stringResource(R.string.login_placeholder_email_or_name)) },
                isError = isError,
                supportingText = {
                    if (isError) {
                        Text(text = stringResource(R.string.login_error_email_or_name))
                    }
                },
                onValueChange = {},
            )
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            OutlinedTextField(
                value = "",
                label = { Text(text = stringResource(R.string.login_lable_password)) },
                placeholder = { Text(text = stringResource(R.string.login_placeholder_password)) },
                onValueChange = {},
            )
        }

        if (loginSuccess) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                OutlinedTextField(
                    value = "",
                    label = {},
                    placeholder = { Text(text = stringResource(R.string.login_placeholder_two_factor)) },
                    onValueChange = {},
                )
            }
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {

        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            Button(onClick = {}) {
                Text(text = stringResource(R.string.login_button))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    BambushainTheme {
        Login()
    }
}