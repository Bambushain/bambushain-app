/**
 * Bambushain
 *
 * Bambushain Server API
 *
 * The version of the OpenAPI document: 3.1.0
 * Contact: panda.helferlein@bambushain.app
 *
 * Please note:
 * This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * Do not edit this file manually.
 */

@file:Suppress(
    "ArrayInDataClass",
    "EnumEntryName",
    "RemoveRedundantQualifierName",
    "UnusedImport"
)

package app.bambushain.api.models


import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Contextual

/**
 * 
 *
 * @param entityType 
 * @param errorType 
 * @param message 
 */
@Serializable
data class ErrorDetails (

    @SerialName(value = "entityType")
    val entityType: ErrorDetails.EntityType,

    @SerialName(value = "errorType")
    val errorType: ErrorDetails.ErrorType,

    @SerialName(value = "message")
    val message: kotlin.String

) {

    /**
     * 
     *
     * Values: user,character,crafter,customField,event,fighter,freeCompany
     */
    @Serializable
    enum class EntityType(val value: kotlin.String) {
        @SerialName(value = "user") user("user"),
        @SerialName(value = "character") character("character"),
        @SerialName(value = "crafter") crafter("crafter"),
        @SerialName(value = "custom_field") customField("custom_field"),
        @SerialName(value = "event") event("event"),
        @SerialName(value = "fighter") fighter("fighter"),
        @SerialName(value = "free_company") freeCompany("free_company");
    }
    /**
     * 
     *
     * Values: notFound,existsAlready,invalidData,io,db,serialization,validation,insufficientRights,unauthorized,unknown,crypto
     */
    @Serializable
    enum class ErrorType(val value: kotlin.String) {
        @SerialName(value = "NotFound") notFound("NotFound"),
        @SerialName(value = "ExistsAlready") existsAlready("ExistsAlready"),
        @SerialName(value = "InvalidData") invalidData("InvalidData"),
        @SerialName(value = "Io") io("Io"),
        @SerialName(value = "Db") db("Db"),
        @SerialName(value = "Serialization") serialization("Serialization"),
        @SerialName(value = "Validation") validation("Validation"),
        @SerialName(value = "InsufficientRights") insufficientRights("InsufficientRights"),
        @SerialName(value = "Unauthorized") unauthorized("Unauthorized"),
        @SerialName(value = "Unknown") unknown("Unknown"),
        @SerialName(value = "Crypto") crypto("Crypto");
    }
}
