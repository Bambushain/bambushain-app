package app.bambushain.viewModels.user

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import app.bambushain.api.apis.UserApi
import app.bambushain.api.models.User

class PandaViewModel(
    private val userApi: UserApi,
    private val context: Context
) : ViewModel(){
    var pandaEmail by mutableStateOf("")
    var pandaName by mutableStateOf("")
    var pandaDiscord by mutableStateOf("")
    var pandaMod by mutableStateOf(false)
    var conflict by mutableStateOf(false)

    suspend fun createPanda(onSuccess: suspend () -> Unit, onError: suspend (error : Int) -> Unit) {
        val panda = User(pandaName, pandaEmail, pandaMod, pandaDiscord, false)
        val request = userApi.createUser(panda)
        if(request.isSuccessful) {
            onSuccess()
        } else onError(request.code())
    }
}