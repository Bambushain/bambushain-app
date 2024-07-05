package app.bambushain.viewModels.authentication

import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.*
import androidx.datastore.core.DataStore
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import app.bambushain.api.apis.AuthenticationApi
import app.bambushain.api.models.LoginRequest
import app.bambushain.settings.AuthenticationSettings
import org.koin.compose.koinInject

class LoginViewModel(
    val authenticationApi: AuthenticationApi,
    val authenticationSettings: DataStore<AuthenticationSettings>
) : ViewModel() {
    private var _twoFactor by mutableStateOf("")

    var userName by mutableStateOf("")
    var password by mutableStateOf("")
    var twoFactor
        get() = _twoFactor
        set(value) {
            if (value.length <= 6) {
                this._twoFactor = value
            }
        }
    var twoFactorRequested by mutableStateOf(false)
        private set

    val loginAllowed by derivedStateOf {
        if (twoFactorRequested) {
            twoFactor.length == 6
        } else {
            userName.length > 3 && password.length > 3
        }
    }

    suspend fun requestTwoFactor(onError: suspend () -> Unit) {
        val loginRequest = LoginRequest(userName, password)
        val response = authenticationApi.login(loginRequest)
        if (response.isSuccessful) {
            twoFactorRequested = true
        } else {
            onError()
        }
    }

    suspend fun login(onSuccess: suspend () -> Unit, onError: suspend () -> Unit) {
        val loginRequest = LoginRequest(userName, password, twoFactor)
        val response = authenticationApi.login(loginRequest)
        if (response.isSuccessful) {
            response.body()?.run {
                authenticationSettings.updateData {
                    AuthenticationSettings(user, token)
                }
            }

            onSuccess()
        } else {
            onError()
        }
    }
}