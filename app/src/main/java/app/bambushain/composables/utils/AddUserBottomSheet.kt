package app.bambushain.composables.utils

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import app.bambushain.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddUserBottomSheet(
    sheetState: SheetState, onCreateUser: (name: String, email: String, discord: String, isMod: Boolean) -> Boolean
) {
    var showBottomSheet = true
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false,
    )
    var editUserName by remember { mutableStateOf("") }
    var editUserEmail by remember { mutableStateOf("") }
    var editUserDiscord by remember { mutableStateOf("") }
    var editUserIsMod by remember { mutableStateOf(false) }
    var usernameConflict by remember { mutableStateOf(false) }
    var emailConflict by remember { mutableStateOf(false) }

    ModalBottomSheet(
        modifier = Modifier.fillMaxHeight().padding(16.dp),
        sheetState = sheetState,
        onDismissRequest = { showBottomSheet = false }
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            value = editUserName,
            label = { Text(stringResource(R.string.create_panda_name)) },
            placeholder = { Text(stringResource(R.string.create_panda_placeholder_name)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            enabled = true,
            onValueChange = { editUserName = it },
            isError = usernameConflict,
            supportingText = { if (usernameConflict) Text(stringResource(R.string.create_panda_name_conflict)) else null }
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            value = editUserEmail,
            label = { Text(stringResource(R.string.create_panda_email)) },
            placeholder = { Text(stringResource(R.string.create_panda_placeholder_email)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            enabled = true,
            onValueChange = { editUserEmail = it },
            isError = emailConflict,
            supportingText = { if (emailConflict) Text(stringResource(R.string.create_panda_email_conflict)) else null }
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            value = editUserDiscord,
            placeholder = { Text(stringResource(R.string.create_panda_placeholder_discord)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            enabled = true,
            onValueChange = { editUserDiscord = it }
        )
        Row(
            modifier = Modifier.padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Switch(
                checked = editUserIsMod,
                onCheckedChange = { editUserIsMod = it }
            )
            Text(
                stringResource(R.string.create_panda_isMod),
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
        Button(
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            enabled = editUserName.isNotEmpty() && editUserEmail.isNotEmpty(),
            onClick = {
                usernameConflict = false
                emailConflict = false
                if(onCreateUser(editUserName, editUserEmail, editUserDiscord, editUserIsMod)){
                    usernameConflict = true
                    emailConflict = true
                }
            }) {
            Text(stringResource(R.string.create_panda_edit_panda))
        }
    }
}