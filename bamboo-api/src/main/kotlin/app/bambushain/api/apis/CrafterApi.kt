package app.bambushain.api.apis

import app.bambushain.api.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

import app.bambushain.api.models.Crafter
import app.bambushain.api.models.ErrorDetails

interface CrafterApi {
    /**
     * Creates a new crafter for the current user
     * Creates a new crafter for the current user
     * Responses:
     *  - 200: The created crafter
     *  - 401: The api token is invalid
     *  - 409: A crafter with the given job already exists
     *  - 500: Unknown error
     *
     * @param characterId 
     * @param crafter The crafter to create
     * @return [Crafter]
     */
    @POST("api/final-fantasy/character/{character_id}/crafter")
    suspend fun createCrafter(@Path("character_id") characterId: java.math.BigDecimal, @Body crafter: Crafter): Response<Crafter>

    /**
     * Delete the given crafter
     * Deletes the given crafter
     * Responses:
     *  - 204: Successfully deleted the crafter
     *  - 401: The api token is invalid
     *  - 404: A crafter with the given id does not exist
     *  - 500: Unknown error
     *
     * @param characterId The id of the character
     * @param id The id of the crafter
     * @return [Unit]
     */
    @DELETE("api/final-fantasy/character/{character_id}/crafter/{id}")
    suspend fun deleteCrafter(@Path("character_id") characterId: java.math.BigDecimal, @Path("id") id: java.math.BigDecimal): Response<Unit>

    /**
     * Get a crafter by job
     * Gets the crafter with the given job
     * Responses:
     *  - 200: The crafter
     *  - 401: The api token is invalid
     *  - 404: A crafter with the given id does not exist
     *  - 500: Unknown error
     *
     * @param characterId The id of the character
     * @param id The id of the crafter
     * @return [Crafter]
     */
    @GET("api/final-fantasy/character/{character_id}/crafter/{id}")
    suspend fun getCrafter(@Path("character_id") characterId: java.math.BigDecimal, @Path("id") id: java.math.BigDecimal): Response<Crafter>

    /**
     * Get list of crafter
     * Gets a list of all crafters the current user has configured
     * Responses:
     *  - 200: A list of crafters
     *  - 401: The api token is invalid
     *  - 500: Unknown error
     *
     * @param characterId 
     * @return [kotlin.collections.List<Crafter>]
     */
    @GET("api/final-fantasy/character/{character_id}/crafter")
    suspend fun getCrafters(@Path("character_id") characterId: java.math.BigDecimal): Response<kotlin.collections.List<Crafter>>

    /**
     * Updates the given crafter
     * Updates the given crafter with the new values
     * Responses:
     *  - 204: The crafter was updated successfully
     *  - 400: The data is invalid
     *  - 401: The api token is invalid
     *  - 404: A crafter with the given id does not exist
     *  - 409: A crafter with the given job already
     *  - 500: Unknown error
     *
     * @param characterId The id of the character
     * @param id The id of the crafter
     * @param crafter The crafter data to update to
     * @return [Unit]
     */
    @PUT("api/final-fantasy/character/{character_id}/crafter/{id}")
    suspend fun updateCrafter(@Path("character_id") characterId: java.math.BigDecimal, @Path("id") id: java.math.BigDecimal, @Body crafter: Crafter): Response<Unit>

}
