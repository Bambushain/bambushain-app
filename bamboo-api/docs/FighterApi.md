# FighterApi

All URIs are relative to *https://pandas.bambushain.app*

Method | HTTP request | Description
------------- | ------------- | -------------
[**createFighter**](FighterApi.md#createFighter) | **POST** api/final-fantasy/character/{character_id}/fighter | Creates a new fighter for the current user
[**deleteFighter**](FighterApi.md#deleteFighter) | **DELETE** api/final-fantasy/character/{character_id}/fighter/{id} | Delete the given fighter
[**getFighter**](FighterApi.md#getFighter) | **GET** api/final-fantasy/character/{character_id}/fighter/{id} | Get a fighter by job
[**getFighters**](FighterApi.md#getFighters) | **GET** api/final-fantasy/character/{character_id}/fighter | Get list of fighter
[**updateFighter**](FighterApi.md#updateFighter) | **PUT** api/final-fantasy/character/{character_id}/fighter/{id} | Updates the given fighter



Creates a new fighter for the current user

Creates a new fighter for the current user

### Example
```kotlin
// Import classes:
//import app.bambushain.api.*
//import app.bambushain.api.infrastructure.*
//import app.bambushain.api.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(FighterApi::class.java)
val characterId : java.math.BigDecimal = 8.14 // java.math.BigDecimal | 
val fighter : Fighter =  // Fighter | The fighter to create

launch(Dispatchers.IO) {
    val result : Fighter = webService.createFighter(characterId, fighter)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **characterId** | **java.math.BigDecimal**|  |
 **fighter** | [**Fighter**](Fighter.md)| The fighter to create |

### Return type

[**Fighter**](Fighter.md)

### Authorization



### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json


Delete the given fighter

Deletes the given fighter

### Example
```kotlin
// Import classes:
//import app.bambushain.api.*
//import app.bambushain.api.infrastructure.*
//import app.bambushain.api.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(FighterApi::class.java)
val characterId : java.math.BigDecimal = 8.14 // java.math.BigDecimal | The id of the character
val id : java.math.BigDecimal = 8.14 // java.math.BigDecimal | The id of the fighter

launch(Dispatchers.IO) {
    webService.deleteFighter(characterId, id)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **characterId** | **java.math.BigDecimal**| The id of the character |
 **id** | **java.math.BigDecimal**| The id of the fighter |

### Return type

null (empty response body)

### Authorization



### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


Get a fighter by job

Gets the fighter with the given job

### Example
```kotlin
// Import classes:
//import app.bambushain.api.*
//import app.bambushain.api.infrastructure.*
//import app.bambushain.api.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(FighterApi::class.java)
val characterId : java.math.BigDecimal = 8.14 // java.math.BigDecimal | The id of the character
val id : java.math.BigDecimal = 8.14 // java.math.BigDecimal | The id of the fighter

launch(Dispatchers.IO) {
    val result : Fighter = webService.getFighter(characterId, id)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **characterId** | **java.math.BigDecimal**| The id of the character |
 **id** | **java.math.BigDecimal**| The id of the fighter |

### Return type

[**Fighter**](Fighter.md)

### Authorization



### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


Get list of fighter

Gets a list of all fighters the current user has configured

### Example
```kotlin
// Import classes:
//import app.bambushain.api.*
//import app.bambushain.api.infrastructure.*
//import app.bambushain.api.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(FighterApi::class.java)
val characterId : java.math.BigDecimal = 8.14 // java.math.BigDecimal | 

launch(Dispatchers.IO) {
    val result : kotlin.collections.List<Fighter> = webService.getFighters(characterId)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **characterId** | **java.math.BigDecimal**|  |

### Return type

[**kotlin.collections.List&lt;Fighter&gt;**](Fighter.md)

### Authorization



### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


Updates the given fighter

Updates the given fighter with the new values

### Example
```kotlin
// Import classes:
//import app.bambushain.api.*
//import app.bambushain.api.infrastructure.*
//import app.bambushain.api.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(FighterApi::class.java)
val characterId : java.math.BigDecimal = 8.14 // java.math.BigDecimal | The id of the character
val id : java.math.BigDecimal = 8.14 // java.math.BigDecimal | The id of the fighter
val fighter : Fighter =  // Fighter | The fighter data to update to

launch(Dispatchers.IO) {
    webService.updateFighter(characterId, id, fighter)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **characterId** | **java.math.BigDecimal**| The id of the character |
 **id** | **java.math.BigDecimal**| The id of the fighter |
 **fighter** | [**Fighter**](Fighter.md)| The fighter data to update to |

### Return type

null (empty response body)

### Authorization



### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

