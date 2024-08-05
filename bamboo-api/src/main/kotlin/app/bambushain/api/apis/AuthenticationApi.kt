package app.bambushain.api.apis

import app.bambushain.api.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

import app.bambushain.api.models.ErrorDetails
import app.bambushain.api.models.ForgotPasswordRequest
import app.bambushain.api.models.LoginRequest
import app.bambushain.api.models.LoginResponse

interface AuthenticationApi {
    /**
     * Performs a login or requests the two factor code
     * Performs the login
     * Responses:
     *  - 200: Returned when the username, password and two factor code are valid
     *  - 204: Returned when the request for the two factor code was successful
     *  - 401: The username, password or two factor code are invalid
     *
     * @param loginRequest 
     * @return [LoginResponse]
     */
    @POST("api/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    /**
     * Performs a password forgot
     * Requests a new password for the user
     * Responses:
     *  - 204: Always returned
     *
     * @return [Unit]
     */
    @POST("/api/forgot-password")
    suspend fun forgotPassword(@Body forgotPasswordRequest: ForgotPasswordRequest): Response<Unit>

    /**
     * Performs a logout
     * Performs a logout and deletes the api token from the server
     * Responses:
     *  - 204: The logout was successful
     *  - 401: The api token is invalid
     *
     * @return [Unit]
     */
    @DELETE("api/login")
    suspend fun logout(): Response<Unit>

    /**
     * Validates the api token
     * Checks whether the api token passed in the &#x60;Authorization&#x60;-header is valid
     * Responses:
     *  - 204: The api token is valid
     *  - 401: The api token is invalid
     *
     * @return [Unit]
     */
    @HEAD("api/login")
    suspend fun validateToken(): Response<Unit>

}
