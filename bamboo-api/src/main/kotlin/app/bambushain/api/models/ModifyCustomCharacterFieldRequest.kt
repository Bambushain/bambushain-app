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
 * @param propertyValues 
 * @param label 
 * @param position 
 */
@Serializable
data class ModifyCustomCharacterFieldRequest (

    @SerialName(value = "values")
    val propertyValues: kotlin.collections.List<kotlin.String>,

    @SerialName(value = "label")
    val label: kotlin.String,

    @SerialName(value = "position")
    val position: kotlin.Int

)

