package app.bambushain.viewModels.authentication

import android.content.Context
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import app.bambushain.api.apis.AuthenticationApi
import app.bambushain.api.models.LoginRequest
import app.bambushain.api.auth.AuthenticationSettings
import app.bambushain.api.models.ForgotPasswordRequest

class LoginViewModel(
    private val authenticationApi: AuthenticationApi,
    private val context: Context
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
                val authSettings = AuthenticationSettings(user, token)
                authSettings.save(context)
            }
            onSuccess()
        } else {
            onError()
        }
    }

    suspend fun requestPassword(onSuccess: suspend () -> kotlin.Unit,onError: suspend () -> Unit) {
        val forgotPasswordRequest = ForgotPasswordRequest(userName)
        val response = authenticationApi.forgotPassword(forgotPasswordRequest)
        if (response.isSuccessful) {
            onSuccess()
        } else {
            onError()
        }
    }
}