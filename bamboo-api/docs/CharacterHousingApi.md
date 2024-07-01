# CharacterHousingApi

All URIs are relative to *https://pandas.bambushain.app*

Method | HTTP request | Description
------------- | ------------- | -------------
[**createHousing**](CharacterHousingApi.md#createHousing) | **POST** api/final-fantasy/character/{character_id}/housing | Creates a new housing for the current user
[**deleteHousing**](CharacterHousingApi.md#deleteHousing) | **DELETE** api/final-fantasy/character/{character_id}/housing/{id} | Delete the given housing
[**getHousing**](CharacterHousingApi.md#getHousing) | **GET** api/final-fantasy/character/{character_id}/housing/{id} | Get a housing by job
[**getHousings**](CharacterHousingApi.md#getHousings) | **GET** api/final-fantasy/character/{character_id}/housing | Get list of housing
[**updateHousing**](CharacterHousingApi.md#updateHousing) | **PUT** api/final-fantasy/character/{character_id}/housing/{id} | Updates the given housing



Creates a new housing for the current user

Creates a new housing for the current user

### Example
```kotlin
// Import classes:
//import app.bambushain.api.*
//import app.bambushain.api.infrastructure.*
//import app.bambushain.api.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(CharacterHousingApi::class.java)
val characterId : java.math.BigDecimal = 8.14 // java.math.BigDecimal | 
val characterHousing : CharacterHousing =  // CharacterHousing | The housing to create

launch(Dispatchers.IO) {
    val result : CharacterHousing = webService.createHousing(characterId, characterHousing)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **characterId** | **java.math.BigDecimal**|  |
 **characterHousing** | [**CharacterHousing**](CharacterHousing.md)| The housing to create |

### Return type

[**CharacterHousing**](CharacterHousing.md)

### Authorization



### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json


Delete the given housing

Deletes the given housing

### Example
```kotlin
// Import classes:
//import app.bambushain.api.*
//import app.bambushain.api.infrastructure.*
//import app.bambushain.api.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(CharacterHousingApi::class.java)
val characterId : java.math.BigDecimal = 8.14 // java.math.BigDecimal | The id of the character
val id : java.math.BigDecimal = 8.14 // java.math.BigDecimal | The id of the housing

launch(Dispatchers.IO) {
    webService.deleteHousing(characterId, id)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **characterId** | **java.math.BigDecimal**| The id of the character |
 **id** | **java.math.BigDecimal**| The id of the housing |

### Return type

null (empty response body)

### Authorization



### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


Get a housing by job

Gets the housing with the given job

### Example
```kotlin
// Import classes:
//import app.bambushain.api.*
//import app.bambushain.api.infrastructure.*
//import app.bambushain.api.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(CharacterHousingApi::class.java)
val characterId : java.math.BigDecimal = 8.14 // java.math.BigDecimal | The id of the character
val id : java.math.BigDecimal = 8.14 // java.math.BigDecimal | The id of the housing

launch(Dispatchers.IO) {
    val result : CharacterHousing = webService.getHousing(characterId, id)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **characterId** | **java.math.BigDecimal**| The id of the character |
 **id** | **java.math.BigDecimal**| The id of the housing |

### Return type

[**CharacterHousing**](CharacterHousing.md)

### Authorization



### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


Get list of housing

Gets a list of all housings the current user has configured

### Example
```kotlin
// Import classes:
//import app.bambushain.api.*
//import app.bambushain.api.infrastructure.*
//import app.bambushain.api.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(CharacterHousingApi::class.java)
val characterId : java.math.BigDecimal = 8.14 // java.math.BigDecimal | 

launch(Dispatchers.IO) {
    val result : kotlin.collections.List<CharacterHousing> = webService.getHousings(characterId)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **characterId** | **java.math.BigDecimal**|  |

### Return type

[**kotlin.collections.List&lt;CharacterHousing&gt;**](CharacterHousing.md)

### Authorization



### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


Updates the given housing

Updates the given housing with the new values

### Example
```kotlin
// Import classes:
//import app.bambushain.api.*
//import app.bambushain.api.infrastructure.*
//import app.bambushain.api.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(CharacterHousingApi::class.java)
val characterId : java.math.BigDecimal = 8.14 // java.math.BigDecimal | The id of the character
val id : java.math.BigDecimal = 8.14 // java.math.BigDecimal | The id of the housing
val characterHousing : CharacterHousing =  // CharacterHousing | The housing data to update to

launch(Dispatchers.IO) {
    webService.updateHousing(characterId, id, characterHousing)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **characterId** | **java.math.BigDecimal**| The id of the character |
 **id** | **java.math.BigDecimal**| The id of the housing |
 **characterHousing** | [**CharacterHousing**](CharacterHousing.md)| The housing data to update to |

### Return type

null (empty response body)

### Authorization



### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

