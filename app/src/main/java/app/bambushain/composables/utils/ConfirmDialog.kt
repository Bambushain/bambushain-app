package app.bambushain.composables.utils

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import kotlinx.coroutines.Job

@Composable
fun ConfirmDialog(
    title: String,
    message: String,
    confirmButtonText: String,
    cancelButtonText: String,
    onConfirm: Job,
    onDismiss: () -> Unit
) {
    AlertDialog(
        title = {
            Text(text = title)
        },
        text = {
            Text(text = message)
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = { onConfirm }
            ) {
                Text(confirmButtonText)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(cancelButtonText)
            }
        }
    )
}