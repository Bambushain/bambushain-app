package app.bambushain.composables.user

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import app.bambushain.R
import app.bambushain.api.apis.UserApi
import app.bambushain.api.auth.AuthenticationSettings
import app.bambushain.api.infrastructure.ApiClient.Companion.defaultBasePath
import app.bambushain.api.models.User
import app.bambushain.viewModels.user.UserCardViewModel
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf

@Composable
fun UserCard(
    user: User,
    userApi: UserApi,
    context: Context = koinInject()
) {
    var model: ImageRequest? by remember { mutableStateOf(null) }
    var expanded by remember { mutableStateOf(false) }

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

        IconButton(onClick = { expanded = !expanded }) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = null
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text(stringResource(R.string.user_card_dropdown_redact_mod_status)) },
                onClick = {})
            DropdownMenuItem(
                text = { Text(stringResource(R.string.user_card_dropdown_reset_password)) },
                onClick = {})
        }
        AsyncImage(
            model = model,
            contentDescription = vm.user!!.displayName,
            imageLoader = ImageLoader.Builder(context).components {
                add(SvgDecoder.Factory())
            }.build(),
            modifier = Modifier.padding(8.dp)
        )
        Text(
            text = vm.user!!.displayName,
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
        )
        Text(
            text = vm.user!!.email,
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
        )
        if (vm.user!!.discordName.isNotEmpty()) {
            Text(
                text = stringResource(R.string.user_is_mod, vm.user!!.discordName),
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            )
        }
        if (vm.user!!.isMod) {
            Text(
                text = stringResource(R.string.user_is_mod, vm.user!!.displayName),
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            )
        }
        Row(horizontalArrangement = Arrangement.End) {
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = stringResource(R.string.user_card_edit)
                )
            }
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(R.string.user_card_delete_panda)
                )
            }
        }

    }
}