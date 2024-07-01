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
 * @param district 
 * @param ward 
 * @param plot 
 * @param characterId 
 * @param id 
 */
@Serializable
data class CharacterHousing (

    @SerialName(value = "district")
    val district: CharacterHousing.District,

    @SerialName(value = "ward")
    val ward: kotlin.Int,

    @SerialName(value = "plot")
    val plot: kotlin.Int,

    @SerialName(value = "characterId")
    val characterId: kotlin.Int,

    @SerialName(value = "id")
    val id: kotlin.Int? = null

) {

    /**
     * 
     *
     * Values: theLavenderBeds,mist,theGoblet,shirogane,empyreum
     */
    @Serializable
    enum class District(val value: kotlin.String) {
        @SerialName(value = "TheLavenderBeds") theLavenderBeds("TheLavenderBeds"),
        @SerialName(value = "Mist") mist("Mist"),
        @SerialName(value = "TheGoblet") theGoblet("TheGoblet"),
        @SerialName(value = "Shirogane") shirogane("Shirogane"),
        @SerialName(value = "Empyreum") empyreum("Empyreum");
    }
}

