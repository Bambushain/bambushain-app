# CrafterApi

All URIs are relative to *https://pandas.bambushain.app*

Method | HTTP request | Description
------------- | ------------- | -------------
[**createCrafter**](CrafterApi.md#createCrafter) | **POST** api/final-fantasy/character/{character_id}/crafter | Creates a new crafter for the current user
[**deleteCrafter**](CrafterApi.md#deleteCrafter) | **DELETE** api/final-fantasy/character/{character_id}/crafter/{id} | Delete the given crafter
[**getCrafter**](CrafterApi.md#getCrafter) | **GET** api/final-fantasy/character/{character_id}/crafter/{id} | Get a crafter by job
[**getCrafters**](CrafterApi.md#getCrafters) | **GET** api/final-fantasy/character/{character_id}/crafter | Get list of crafter
[**updateCrafter**](CrafterApi.md#updateCrafter) | **PUT** api/final-fantasy/character/{character_id}/crafter/{id} | Updates the given crafter



Creates a new crafter for the current user

Creates a new crafter for the current user

### Example
```kotlin
// Import classes:
//import app.bambushain.api.*
//import app.bambushain.api.infrastructure.*
//import app.bambushain.api.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(CrafterApi::class.java)
val characterId : java.math.BigDecimal = 8.14 // java.math.BigDecimal | 
val crafter : Crafter =  // Crafter | The crafter to create

launch(Dispatchers.IO) {
    val result : Crafter = webService.createCrafter(characterId, crafter)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **characterId** | **java.math.BigDecimal**|  |
 **crafter** | [**Crafter**](Crafter.md)| The crafter to create |

### Return type

[**Crafter**](Crafter.md)

### Authorization



### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json


Delete the given crafter

Deletes the given crafter

### Example
```kotlin
// Import classes:
//import app.bambushain.api.*
//import app.bambushain.api.infrastructure.*
//import app.bambushain.api.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(CrafterApi::class.java)
val characterId : java.math.BigDecimal = 8.14 // java.math.BigDecimal | The id of the character
val id : java.math.BigDecimal = 8.14 // java.math.BigDecimal | The id of the crafter

launch(Dispatchers.IO) {
    webService.deleteCrafter(characterId, id)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **characterId** | **java.math.BigDecimal**| The id of the character |
 **id** | **java.math.BigDecimal**| The id of the crafter |

### Return type

null (empty response body)

### Authorization



### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


Get a crafter by job

Gets the crafter with the given job

### Example
```kotlin
// Import classes:
//import app.bambushain.api.*
//import app.bambushain.api.infrastructure.*
//import app.bambushain.api.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(CrafterApi::class.java)
val characterId : java.math.BigDecimal = 8.14 // java.math.BigDecimal | The id of the character
val id : java.math.BigDecimal = 8.14 // java.math.BigDecimal | The id of the crafter

launch(Dispatchers.IO) {
    val result : Crafter = webService.getCrafter(characterId, id)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **characterId** | **java.math.BigDecimal**| The id of the character |
 **id** | **java.math.BigDecimal**| The id of the crafter |

### Return type

[**Crafter**](Crafter.md)

### Authorization



### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


Get list of crafter

Gets a list of all crafters the current user has configured

### Example
```kotlin
// Import classes:
//import app.bambushain.api.*
//import app.bambushain.api.infrastructure.*
//import app.bambushain.api.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(CrafterApi::class.java)
val characterId : java.math.BigDecimal = 8.14 // java.math.BigDecimal | 

launch(Dispatchers.IO) {
    val result : kotlin.collections.List<Crafter> = webService.getCrafters(characterId)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **characterId** | **java.math.BigDecimal**|  |

### Return type

[**kotlin.collections.List&lt;Crafter&gt;**](Crafter.md)

### Authorization



### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


Updates the given crafter

Updates the given crafter with the new values

### Example
```kotlin
// Import classes:
//import app.bambushain.api.*
//import app.bambushain.api.infrastructure.*
//import app.bambushain.api.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(CrafterApi::class.java)
val characterId : java.math.BigDecimal = 8.14 // java.math.BigDecimal | The id of the character
val id : java.math.BigDecimal = 8.14 // java.math.BigDecimal | The id of the crafter
val crafter : Crafter =  // Crafter | The crafter data to update to

launch(Dispatchers.IO) {
    webService.updateCrafter(characterId, id, crafter)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **characterId** | **java.math.BigDecimal**| The id of the character |
 **id** | **java.math.BigDecimal**| The id of the crafter |
 **crafter** | [**Crafter**](Crafter.md)| The crafter data to update to |

### Return type

null (empty response body)

### Authorization



### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

