package app.bambushain.settings

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import app.bambushain.api.models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

@Serializable
data class AuthenticationSettings(val user: User?, val token: String)

class AuthenticationSettingsSerializer : Serializer<AuthenticationSettings> {
    override val defaultValue: AuthenticationSettings
        get() = AuthenticationSettings(null, "")

    override suspend fun readFrom(input: InputStream): AuthenticationSettings {
        try {
            return Json.decodeFromString(
                AuthenticationSettings.serializer(),
                input.readBytes().toString(Charsets.UTF_8)
            )
        } catch (serializationException: SerializationException) {
            throw CorruptionException("Unable to read settings", serializationException)
        }
    }

    override suspend fun writeTo(t: AuthenticationSettings, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(Json.encodeToString(AuthenticationSettings.serializer(), t).toByteArray(Charsets.UTF_8))
        }
    }
}