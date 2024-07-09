package app.bambushain.api.apis

import app.bambushain.api.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

import app.bambushain.api.models.ErrorDetails
import app.bambushain.api.models.Fighter

interface FighterApi {
    /**
     * Creates a new fighter for the current user
     * Creates a new fighter for the current user
     * Responses:
     *  - 200: The created fighter
     *  - 401: The api token is invalid
     *  - 409: A fighter with the given job already exists
     *  - 500: Unknown error
     *
     * @param characterId 
     * @param fighter The fighter to create
     * @return [Fighter]
     */
    @POST("api/final-fantasy/character/{character_id}/fighter")
    suspend fun createFighter(@Path("character_id") characterId: Int, @Body fighter: Fighter): Response<Fighter>

    /**
     * Delete the given fighter
     * Deletes the given fighter
     * Responses:
     *  - 204: Successfully deleted the fighter
     *  - 401: The api token is invalid
     *  - 404: A fighter with the given id does not exist
     *  - 500: Unknown error
     *
     * @param characterId The id of the character
     * @param id The id of the fighter
     * @return [Unit]
     */
    @DELETE("api/final-fantasy/character/{character_id}/fighter/{id}")
    suspend fun deleteFighter(@Path("character_id") characterId: Int, @Path("id") id: Int): Response<Unit>

    /**
     * Get a fighter by job
     * Gets the fighter with the given job
     * Responses:
     *  - 200: The fighter
     *  - 401: The api token is invalid
     *  - 404: A fighter with the given id does not exist
     *  - 500: Unknown error
     *
     * @param characterId The id of the character
     * @param id The id of the fighter
     * @return [Fighter]
     */
    @GET("api/final-fantasy/character/{character_id}/fighter/{id}")
    suspend fun getFighter(@Path("character_id") characterId: Int, @Path("id") id: Int): Response<Fighter>

    /**
     * Get list of fighter
     * Gets a list of all fighters the current user has configured
     * Responses:
     *  - 200: A list of fighters
     *  - 401: The api token is invalid
     *  - 500: Unknown error
     *
     * @param characterId 
     * @return [kotlin.collections.List<Fighter>]
     */
    @GET("api/final-fantasy/character/{character_id}/fighter")
    suspend fun getFighters(@Path("character_id") characterId: Int): Response<kotlin.collections.List<Fighter>>

    /**
     * Updates the given fighter
     * Updates the given fighter with the new values
     * Responses:
     *  - 204: The fighter was updated successfully
     *  - 400: The data is invalid
     *  - 401: The api token is invalid
     *  - 404: A fighter with the given id does not exist
     *  - 409: A fighter with the given job already
     *  - 500: Unknown error
     *
     * @param characterId The id of the character
     * @param id The id of the fighter
     * @param fighter The fighter data to update to
     * @return [Unit]
     */
    @PUT("api/final-fantasy/character/{character_id}/fighter/{id}")
    suspend fun updateFighter(@Path("character_id") characterId: Int, @Path("id") id: Int, @Body fighter: Fighter): Response<Unit>

}
