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
 * @param email 
 * @param password 
 * @param twoFactorCode 
 */
@Serializable
data class LoginRequest (

    @SerialName(value = "email")
    val email: kotlin.String,

    @SerialName(value = "password")
    val password: kotlin.String,

    @SerialName(value = "twoFactorCode")
    val twoFactorCode: kotlin.String? = null

)

