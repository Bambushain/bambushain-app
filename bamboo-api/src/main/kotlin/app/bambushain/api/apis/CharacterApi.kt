package app.bambushain.api.apis

import app.bambushain.api.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

import app.bambushain.api.models.Character
import app.bambushain.api.models.CustomCharacterField
import app.bambushain.api.models.CustomCharacterFieldOption
import app.bambushain.api.models.ErrorDetails
import app.bambushain.api.models.FreeCompany
import app.bambushain.api.models.ModifyCustomCharacterFieldRequest

interface CharacterApi {
    /**
     * Create a new character
     * 
     * Responses:
     *  - 201: Character was created
     *  - 400: The data is invalid
     *  - 401: The access token is invalid
     *  - 409: The character already exists
     *  - 500: Unkown error
     *
     * @param character  (optional)
     * @return [Character]
     */
    @POST("api/final-fantasy/character")
    suspend fun createCharacter(@Body character: Character? = null): Response<Character>

    /**
     * Creates a new character custom field
     * 
     * Responses:
     *  - 201: Created the custom field
     *  - 400: The data is invalid
     *  - 401: The access token is invalid
     *  - 409: The custom field already exists
     *  - 500: Unkown error
     *
     * @param modifyCustomCharacterFieldRequest  (optional)
     * @return [CustomCharacterField]
     */
    @POST("api/final-fantasy/character/custom-field")
    suspend fun createCustomField(@Body modifyCustomCharacterFieldRequest: ModifyCustomCharacterFieldRequest? = null): Response<CustomCharacterField>

    /**
     * Creates a new option for the given custom field
     * 
     * Responses:
     *  - 201: Successfully created the option
     *  - 400: The data is invalid
     *  - 401: The access token is invalid
     *  - 404: The custom field was not found
     *  - 500: Unknown error
     *
     * @param fieldId The id of the custom field
     * @param body  (optional)
     * @return [CustomCharacterFieldOption]
     */
    @POST("api/final-fantasy/character/custom-field/{field_id}/option")
    suspend fun createCustomFieldOption(@Path("field_id") fieldId: java.math.BigDecimal, @Body body: kotlin.String? = null): Response<CustomCharacterFieldOption>

    /**
     * Creates a new free company
     * 
     * Responses:
     *  - 201: Successfully created the free company
     *  - 400: The data is invalid
     *  - 401: The access token is invalid
     *  - 409: The free company already exists
     *  - 500: Unkown error
     *
     * @return [FreeCompany]
     */
    @POST("api/final-fantasy/free-company")
    suspend fun createFreeCompany(): Response<FreeCompany>

    /**
     * Deletes the given character
     * 
     * Responses:
     *  - 204: The character was deleted
     *  - 404: The character was not found
     *  - 500: Unkown error
     *
     * @param id The id of the character
     * @return [Unit]
     */
    @DELETE("api/final-fantasy/character/{id}")
    suspend fun deleteCharacter(@Path("id") id: java.math.BigDecimal): Response<Unit>

    /**
     * Deletes the given custom field
     * 
     * Responses:
     *  - 204: The field was deleted
     *  - 401: The access token is invalid
     *  - 404: The custom field was not found
     *  - 500: Unkown error
     *
     * @param id The id of the custom field
     * @return [Unit]
     */
    @DELETE("api/final-fantasy/character/custom-field/{id}")
    suspend fun deleteCustomField(@Path("id") id: java.math.BigDecimal): Response<Unit>

    /**
     * Deletes the given option
     * 
     * Responses:
     *  - 204: Successfully deleted the option
     *  - 401: The access token is invalid
     *  - 404: The custom field was not found
     *  - 500: Unknown error
     *
     * @param fieldId The id of the custom field
     * @param id The id of the custom field option
     * @return [Unit]
     */
    @DELETE("api/final-fantasy/character/custom-field/{field_id}/option/{id}")
    suspend fun deleteCustomFieldOption(@Path("field_id") fieldId: java.math.BigDecimal, @Path("id") id: java.math.BigDecimal): Response<Unit>

    /**
     * Deletes the given free company
     * 
     * Responses:
     *  - 204: Successfully deleted the free company
     *  - 401: The access token is invalid
     *  - 404: The free company was not found
     *  - 500: Unknown error
     *
     * @param id The id of the free company
     * @return [Unit]
     */
    @DELETE("api/final-fantasy/free-company/{id}")
    suspend fun deleteFreeCompany(@Path("id") id: java.math.BigDecimal): Response<Unit>

    /**
     * Gets the character by id
     * 
     * Responses:
     *  - 200: The character
     *  - 401: The access token is invalid
     *  - 404: The character was not found
     *
     * @param id The id of the character
     * @return [Character]
     */
    @GET("api/final-fantasy/character/{id}")
    suspend fun getCharacterById(@Path("id") id: java.math.BigDecimal): Response<Character>

    /**
     * Get a list of all characters for the current user
     * 
     * Responses:
     *  - 200: A list with all characters
     *  - 401: The access token is invalid
     *
     * @return [kotlin.collections.List<Character>]
     */
    @GET("api/final-fantasy/character")
    suspend fun getCharacters(): Response<kotlin.collections.List<Character>>

    /**
     * Gets the custom field with the given 
     * 
     * Responses:
     *  - 200: The custom field
     *  - 401: The access token ist invalid
     *  - 404: The custom field was not found
     *
     * @param id The id of the custom field
     * @return [ErrorDetails]
     */
    @GET("api/final-fantasy/character/custom-field/{id}")
    suspend fun getCustomFieldById(@Path("id") id: java.math.BigDecimal): Response<ErrorDetails>

    /**
     * Gets all options for the given custom field
     * 
     * Responses:
     *  - 200: A list of all options
     *  - 401: The access token is invalid
     *  - 404: The custom field was not found
     *  - 500: Unkown error
     *
     * @param fieldId The id of the custom field
     * @return [kotlin.collections.List<CustomCharacterFieldOption>]
     */
    @GET("api/final-fantasy/character/custom-field/{field_id}/option")
    suspend fun getCustomFieldOptions(@Path("field_id") fieldId: java.math.BigDecimal): Response<kotlin.collections.List<CustomCharacterFieldOption>>

    /**
     * Gets a list of all character custom fields
     * 
     * Responses:
     *  - 200: A list with all custom fields
     *  - 401: The access token is invalid
     *  - 500: Unknown error
     *
     * @return [kotlin.collections.List<CustomCharacterField>]
     */
    @GET("api/final-fantasy/character/custom-field")
    suspend fun getCustomFields(): Response<kotlin.collections.List<CustomCharacterField>>

    /**
     * Gets all free companies
     * 
     * Responses:
     *  - 200: A list with all free companies
     *  - 401: The access token is invalid
     *
     * @return [kotlin.collections.List<FreeCompany>]
     */
    @GET("api/final-fantasy/free-company")
    suspend fun getFreeCompanies(): Response<kotlin.collections.List<FreeCompany>>

    /**
     * Get free company by id
     * 
     * Responses:
     *  - 200: The free company
     *  - 401: The access token is invalid
     *  - 404: The free company was not found
     *
     * @param id The id of the free company
     * @return [FreeCompany]
     */
    @GET("api/final-fantasy/free-company/{id}")
    suspend fun getFreeCompanyById(@Path("id") id: java.math.BigDecimal): Response<FreeCompany>

    /**
     * Moves the given custom field to the new position
     * 
     * Responses:
     *  - 204: Successfully moved the custom field
     *  - 401: The access token is invalid
     *  - 404: The custom field was not found
     *  - 500: Unknown error
     *
     * @param id The id of the custom field
     * @param position The new position
     * @return [Unit]
     */
    @PUT("api/final-fantasy/character/custom-field/{id}/{position}")
    suspend fun moveCustomField(@Path("id") id: java.math.BigDecimal, @Path("position") position: java.math.BigDecimal): Response<Unit>

    /**
     * Updates the given character
     * 
     * Responses:
     *  - 204: Character was successfully updated
     *  - 400: The data is invalid
     *  - 401: The access token is invalid
     *  - 404: The character was not found
     *
     * @param id The id of the character
     * @param character  (optional)
     * @return [Unit]
     */
    @PUT("api/final-fantasy/character/{id}")
    suspend fun updateCharacter(@Path("id") id: java.math.BigDecimal, @Body character: Character? = null): Response<Unit>

    /**
     * Updates the given custom field
     * 
     * Responses:
     *  - 204: Updated successfully
     *  - 400: The data is invalid
     *  - 401: The access token ist invalid
     *  - 404: The custom field was not found
     *
     * @param id The id of the custom field
     * @param modifyCustomCharacterFieldRequest  (optional)
     * @return [Unit]
     */
    @PUT("api/final-fantasy/character/custom-field/{id}")
    suspend fun updateCustomField(@Path("id") id: java.math.BigDecimal, @Body modifyCustomCharacterFieldRequest: ModifyCustomCharacterFieldRequest? = null): Response<Unit>

    /**
     * Updates the given option
     * 
     * Responses:
     *  - 204: Successfully updated the option
     *  - 400: The data is invalid
     *  - 401: The access token is invalid
     *  - 404: The custom field was not found
     *  - 500: Unkown error
     *
     * @param fieldId The id of the custom field
     * @param id The id of the custom field option
     * @param body  (optional)
     * @return [Unit]
     */
    @PUT("api/final-fantasy/character/custom-field/{field_id}/option/{id}")
    suspend fun updateCustomFieldOption(@Path("field_id") fieldId: java.math.BigDecimal, @Path("id") id: java.math.BigDecimal, @Body body: kotlin.String? = null): Response<Unit>

    /**
     * Updates the given free company
     * 
     * Responses:
     *  - 204: Successfully updates the free company
     *  - 400: The data is invalid
     *  - 401: The access token is invalid
     *  - 404: The free company was not found
     *  - 500: Unkown error
     *
     * @param id The id of the free company
     * @param freeCompany  (optional)
     * @return [Unit]
     */
    @PUT("api/final-fantasy/free-company/{id}")
    suspend fun updateFreeCompany(@Path("id") id: java.math.BigDecimal, @Body freeCompany: FreeCompany? = null): Response<Unit>

}
