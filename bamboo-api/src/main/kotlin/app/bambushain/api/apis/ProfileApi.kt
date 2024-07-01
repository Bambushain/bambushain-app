package app.bambushain.api.apis

import app.bambushain.api.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

import app.bambushain.api.models.ChangeMyPassword
import app.bambushain.api.models.EnableTotpResponse
import app.bambushain.api.models.ErrorDetails
import app.bambushain.api.models.UpdateMyProfile
import app.bambushain.api.models.User
import app.bambushain.api.models.ValidateTotpRequest

import okhttp3.MultipartBody

interface ProfileApi {
    /**
     * Changes the current users password
     * Changes the current users password, while checking if the old password is valid
     * Responses:
     *  - 204: The password was changed successfully
     *  - 400: The data is invalid
     *  - 401: The api token is invalid
     *  - 403: The current password is wrong
     *  - 404: The current user seems to have been deleted
     *  - 500: Unknown error
     *
     * @param changeMyPassword The current and new password
     * @return [Unit]
     */
    @PUT("api/my/profile/password")
    suspend fun changeMyPassword(@Body changeMyPassword: ChangeMyPassword): Response<Unit>

    /**
     * Enables TOTP for the current user
     * 
     * Responses:
     *  - 200: TOTP enabled, the code needs to be validated now
     *  - 401: The access token is invalid
     *  - 500: Unkown error
     *
     * @return [EnableTotpResponse]
     */
    @POST("api/my/totp")
    suspend fun enableTotp(): Response<EnableTotpResponse>

    /**
     * Gets the current user
     * Gets the current users profile
     * Responses:
     *  - 200: The current users profile
     *  - 401: The api token is invalid
     *
     * @return [User]
     */
    @GET("api/my/profile")
    suspend fun getMyProfile(): Response<User>

    /**
     * Updates the current users profile
     * Updates the current users profile
     * Responses:
     *  - 204: The profile was updated successfully
     *  - 400: The data is invalid
     *  - 401: The api token is invalid
     *  - 500: Unknown error
     *
     * @param updateMyProfile The current users profile
     * @return [Unit]
     */
    @PUT("api/my/profile")
    suspend fun updateMyProfile(@Body updateMyProfile: UpdateMyProfile): Response<Unit>

    /**
     * Updates the current users profile picture
     * Updates the current users profile picture
     * Responses:
     *  - 204: The profile was updated successfully
     *  - 401: The api token is invalid
     *  - 500: Unknown error
     *
     * @param body The current users profile picture
     * @return [Unit]
     */
    @PUT("api/my/picture")
    suspend fun updateMyProfilePicture(@Body body: java.io.File): Response<Unit>

    /**
     * Validates the given code and marks the TOTP status as enabled
     * 
     * Responses:
     *  - 204: Successfully validated
     *  - 400: Already validated
     *  - 401: The access token is invalid
     *  - 403: The code is invalid
     *  - 500: Unkown error
     *
     * @param validateTotpRequest  (optional)
     * @return [Unit]
     */
    @PUT("api/my/totp/validate")
    suspend fun validateTotp(@Body validateTotpRequest: ValidateTotpRequest? = null): Response<Unit>

}
