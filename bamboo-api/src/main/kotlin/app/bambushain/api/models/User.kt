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
 * @param displayName 
 * @param email 
 * @param isMod 
 * @param discordName 
 * @param appTotpEnabled 
 * @param id 
 */
@Serializable
data class User (

    @SerialName(value = "displayName")
    var displayName: kotlin.String,

    @SerialName(value = "email")
    var email: kotlin.String,

    @SerialName(value = "isMod")
    val isMod: kotlin.Boolean,

    @SerialName(value = "discordName")
    var discordName: kotlin.String,

    @SerialName(value = "appTotpEnabled")
    val appTotpEnabled: kotlin.Boolean,

    @SerialName(value = "id")
    val id: kotlin.Int? = null

)

