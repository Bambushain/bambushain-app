package app.bambushain.api.auth

import android.content.Context
import app.bambushain.api.models.User
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class AuthenticationSettings(val user: User?, val token: String) {
    fun save(context: Context) {
        val sharedPrefs = context.getSharedPreferences("bamboo-settings", Context.MODE_PRIVATE)
        sharedPrefs.edit()
            .putString("auth-settings", Json.encodeToString(this))
            .apply()
    }

    companion object {
        fun get(context: Context): AuthenticationSettings? {
            val sharedPrefs = context.getSharedPreferences("bamboo-settings", Context.MODE_PRIVATE)
            val settings = sharedPrefs.getString("auth-settings", null)

            return if (settings != null) {
                Json.decodeFromString(settings)
            } else {
                null
            }
        }
    }
}