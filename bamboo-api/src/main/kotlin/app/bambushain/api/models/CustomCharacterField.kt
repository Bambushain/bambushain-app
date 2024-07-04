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

import app.bambushain.api.models.CustomCharacterFieldOption

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Contextual

/**
 * 
 *
 * @param id 
 * @param label 
 * @param position 
 * @param options 
 */
@Serializable
data class CustomCharacterField (

    @SerialName(value = "id")
    val id: kotlin.Int,

    @SerialName(value = "label")
    val label: kotlin.String,

    @SerialName(value = "position")
    val position: kotlin.Int,

    @SerialName(value = "options")
    val options: kotlin.collections.List<CustomCharacterFieldOption>

)
