package app.bambushain.api.apis

import app.bambushain.api.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

import app.bambushain.api.models.ErrorDetails
import app.bambushain.api.models.Event

interface CalendarApi {
    /**
     * Creates a new event
     * 
     * Responses:
     *  - 201: Event was created
     *  - 400: The data is invalid
     *  - 401: The access token is invalid
     *  - 500: Unkown error
     *
     * @param event  (optional)
     * @return [ErrorDetails]
     */
    @POST("api/bamboo-grove/event")
    suspend fun createEvent(@Body event: Event? = null): Response<ErrorDetails>

    /**
     * Deletes the given event
     * 
     * Responses:
     *  - 204: Successfully deleted
     *  - 401: The access token is invalid
     *  - 500: Unknown error
     *
     * @param id The id of the event
     * @return [Unit]
     */
    @DELETE("api/bamboo-grove/event/{id}")
    suspend fun deleteEvent(@Path("id") id: java.math.BigDecimal): Response<Unit>

    /**
     * Gets all events for the given date range
     * 
     * Responses:
     *  - 200: All events
     *  - 400: The start date cannot be after the end date
     *  - 401: The access token is invalid
     *  - 500: Unkown error
     *
     * @param start 
     * @param end 
     * @return [kotlin.collections.List<Event>]
     */
    @GET("api/bamboo-grove/event")
    suspend fun getEvents(@Query("start") start: java.time.LocalDate, @Query("end") end: java.time.LocalDate): Response<kotlin.collections.List<Event>>

    /**
     * Updates the given event
     * 
     * Responses:
     *  - 204: Successfully updated the event
     *  - 400: The data is invalid
     *  - 401: The access token is invalid
     *  - 500: Unkown error
     *
     * @param id The id of the event
     * @param event  (optional)
     * @return [Unit]
     */
    @PUT("api/bamboo-grove/event/{id}")
    suspend fun updateEvent(@Path("id") id: java.math.BigDecimal, @Body event: Event? = null): Response<Unit>

}
