package app.bambushain.api.apis

import app.bambushain.api.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

import app.bambushain.api.models.CharacterHousing
import app.bambushain.api.models.ErrorDetails

interface CharacterHousingApi {
    /**
     * Creates a new housing for the current user
     * Creates a new housing for the current user
     * Responses:
     *  - 200: The created housing
     *  - 401: The api token is invalid
     *  - 409: A housing with the given data already exists
     *  - 500: Unknown error
     *
     * @param characterId 
     * @param characterHousing The housing to create
     * @return [CharacterHousing]
     */
    @POST("api/final-fantasy/character/{character_id}/housing")
    suspend fun createHousing(@Path("character_id") characterId: java.math.BigDecimal, @Body characterHousing: CharacterHousing): Response<CharacterHousing>

    /**
     * Delete the given housing
     * Deletes the given housing
     * Responses:
     *  - 204: Successfully deleted the housing
     *  - 401: The api token is invalid
     *  - 404: A housing with the given id does not exist
     *  - 500: Unknown error
     *
     * @param characterId The id of the character
     * @param id The id of the housing
     * @return [Unit]
     */
    @DELETE("api/final-fantasy/character/{character_id}/housing/{id}")
    suspend fun deleteHousing(@Path("character_id") characterId: java.math.BigDecimal, @Path("id") id: java.math.BigDecimal): Response<Unit>

    /**
     * Get a housing by job
     * Gets the housing with the given job
     * Responses:
     *  - 200: The housing
     *  - 401: The api token is invalid
     *  - 404: A housing with the given id does not exist
     *  - 500: Unknown error
     *
     * @param characterId The id of the character
     * @param id The id of the housing
     * @return [CharacterHousing]
     */
    @GET("api/final-fantasy/character/{character_id}/housing/{id}")
    suspend fun getHousing(@Path("character_id") characterId: java.math.BigDecimal, @Path("id") id: java.math.BigDecimal): Response<CharacterHousing>

    /**
     * Get list of housing
     * Gets a list of all housings the current user has configured
     * Responses:
     *  - 200: A list of housings
     *  - 401: The api token is invalid
     *  - 500: Unknown error
     *
     * @param characterId 
     * @return [kotlin.collections.List<CharacterHousing>]
     */
    @GET("api/final-fantasy/character/{character_id}/housing")
    suspend fun getHousings(@Path("character_id") characterId: java.math.BigDecimal): Response<kotlin.collections.List<CharacterHousing>>

    /**
     * Updates the given housing
     * Updates the given housing with the new values
     * Responses:
     *  - 204: The housing was updated successfully
     *  - 400: The data is invalid
     *  - 401: The api token is invalid
     *  - 404: A housing with the given id does not exist
     *  - 409: A housing with the given dara already exists
     *  - 500: Unknown error
     *
     * @param characterId The id of the character
     * @param id The id of the housing
     * @param characterHousing The housing data to update to
     * @return [Unit]
     */
    @PUT("api/final-fantasy/character/{character_id}/housing/{id}")
    suspend fun updateHousing(@Path("character_id") characterId: java.math.BigDecimal, @Path("id") id: java.math.BigDecimal, @Body characterHousing: CharacterHousing): Response<Unit>

}
