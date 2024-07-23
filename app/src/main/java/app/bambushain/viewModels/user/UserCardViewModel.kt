package app.bambushain.viewModels.user

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import app.bambushain.api.apis.UserApi
import app.bambushain.api.models.UpdateMyProfile
import app.bambushain.api.models.User

class UserCardViewModel(
    user: User,
    private val userApi: UserApi,
) : ViewModel() {
    var user: User? by mutableStateOf(user)

    suspend fun resetPassword(onSuccess: suspend () -> Unit, onError: suspend () -> Unit) {
        val response = userApi.changePassword(user?.id!!)
        if (response.isSuccessful) {
            onSuccess()
        } else {
            onError()
        }
    }

    suspend fun revokeUserModRights(onSuccess: suspend () -> Unit, onError: suspend () -> Unit) {
        val response = userApi.revokeUserModRights(user?.id!!)
        if (response.isSuccessful) {
            onSuccess()
        } else {
            onError()
        }
    }

    suspend fun setUserModRights(onSuccess: suspend () -> Unit, onError: suspend () -> Unit) {
        val response = userApi.makeUserMod(user?.id!!)
        if (response.isSuccessful) {
            onSuccess()
        } else {
            onError()
        }
    }

    suspend fun deleteUser(onSuccess: suspend () -> Unit, onError: suspend () -> Unit) {
        val response = userApi.deleteUser(user?.id!!)
        if (response.isSuccessful) {
            onSuccess()
        } else {
            onError()
        }
    }

    suspend fun updateUser(onSuccess: suspend () -> Unit, onError: suspend (error: Int) -> Unit) {
        val updateUser = UpdateMyProfile(user!!.email, user!!.displayName, user!!.discordName)
        val response = userApi.updateUserProfile(user!!.id!!, updateUser)
        if (response.isSuccessful) {
            onSuccess()
        } else {
            onError(response.code())
        }
    }

}