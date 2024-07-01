package app.bambushain.api.apis

import app.bambushain.api.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

import app.bambushain.api.models.ErrorDetails
import app.bambushain.api.models.UpdateMyProfile
import app.bambushain.api.models.User

import okhttp3.ResponseBody

interface UserApi {
    /**
     * Changes the password for the given user
     * Changes the password for the given user. Cannot be used to changes the current users password
     * Responses:
     *  - 204: The users password was changed and all tokens deleted
     *  - 401: The access token is invalid
     *  - 403: The user is no mod
     *  - 404: The user was not found
     *  - 409: You can't change your own password with this method
     *  - 500: Unknown error
     *
     * @param id The id of the user
     * @return [Unit]
     */
    @PUT("api/user/{id}/password")
    suspend fun changePassword(@Path("id") id: java.math.BigDecimal): Response<Unit>

    /**
     * Creates a new user
     * Creates a new user with the given values
     * Responses:
     *  - 201: The user was created successfully
     *  - 400: The data is invalid
     *  - 401: The api token is invalid
     *  - 403: The user is no mod
     *  - 409: A user with the given username or email exists
     *  - 500: Unknown error
     *
     * @param user The data for the new user
     * @return [User]
     */
    @POST("api/user")
    suspend fun createUser(@Body user: User): Response<User>

    /**
     * Deletes the given user
     * Deletes the user with the given username
     * Responses:
     *  - 204: The user was deleted successfully
     *  - 401: The access token is invalid
     *  - 403: The current user is no mod
     *  - 404: The user was not found
     *  - 409: It is not possible to delete yourself
     *  - 500: Unkown error
     *
     * @param id The id of the user
     * @return [Unit]
     */
    @DELETE("api/user/{id}")
    suspend fun deleteUser(@Path("id") id: java.math.BigDecimal): Response<Unit>

    /**
     * Reset totp
     * Resets the totp of the given user
     * Responses:
     *  - 204: The users totp was reset successfully
     *  - 400: The data is invalid
     *  - 401: The access token is invalid
     *  - 403: The user is no mod
     *  - 404: The user was not found
     *  - 409: The email or username exists
     *  - 500: Unknown error
     *
     * @param id The id of the user
     * @return [Unit]
     */
    @DELETE("api/user/{id}/totp")
    suspend fun disableTotp(@Path("id") id: java.math.BigDecimal): Response<Unit>

    /**
     * Gets the given user
     * Gets the user by the given username
     * Responses:
     *  - 200: The user with the given id
     *  - 401: The acccess token is invalid
     *  - 404: The user was not found
     *  - 500: Unknown error
     *
     * @param id The id of the user
     * @return [User]
     */
    @GET("api/user/{id}")
    suspend fun getUser(@Path("id") id: java.math.BigDecimal): Response<User>

    /**
     * Get the profile picture
     * Gets the current users profile picture
     * Responses:
     *  - 200: The user profile was updated successfully
     *  - 401: The access token is invalid
     *  - 404: The user was not found
     *  - 500: Unknown error
     *
     * @param id The id of the user
     * @return [ResponseBody]
     */
    @GET("api/user/{id}/picture")
    suspend fun getUserProfilePicture(@Path("id") id: java.math.BigDecimal): Response<ResponseBody>

    /**
     * Get a list of all users
     * Gets a list with all users
     * Responses:
     *  - 200: A list of users
     *  - 401: The api token is invalid
     *  - 500: Unknown error
     *
     * @return [kotlin.collections.List<User>]
     */
    @GET("api/user")
    suspend fun getUsers(): Response<kotlin.collections.List<User>>

    /**
     * Gives the given user mod status
     * Gives the given user the mod status
     * Responses:
     *  - 204: The user is now mod
     *  - 401: The access token is invalid
     *  - 403: The user is no mod
     *  - 404: The user was not found
     *  - 409: You can't make yourself mod
     *  - 500: Unknown error
     *
     * @param id The id of the user
     * @return [Unit]
     */
    @PUT("api/user/{id}/mod")
    suspend fun makeUserMod(@Path("id") id: java.math.BigDecimal): Response<Unit>

    /**
     * Revokes the mod rights for the given user
     * Revokes the mod rights for the given user
     * Responses:
     *  - 204: The user is no longer a mod
     *  - 401: The access token is invalid
     *  - 403: The user is no mod
     *  - 404: The user was not found
     *  - 409: You can't revoke your own mod rights
     *  - 500: Unknown error
     *
     * @param id The id of the user
     * @return [Unit]
     */
    @DELETE("api/user/{id}/mod")
    suspend fun revokeUserModRights(@Path("id") id: java.math.BigDecimal): Response<Unit>

    /**
     * Update the profile
     * Updates the profile of the given user
     * Responses:
     *  - 204: The user profile was updated successfully
     *  - 400: The data is invalid
     *  - 401: The access token is invalid
     *  - 403: The user is no mod
     *  - 404: The user was not found
     *  - 409: The email or username exists
     *  - 500: Unknown error
     *
     * @param id The id of the user
     * @param updateMyProfile The data to update the user with
     * @return [Unit]
     */
    @PUT("api/user/{id}/profile")
    suspend fun updateUserProfile(@Path("id") id: java.math.BigDecimal, @Body updateMyProfile: UpdateMyProfile): Response<Unit>

}
