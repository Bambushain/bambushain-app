# AuthenticationApi

All URIs are relative to *https://pandas.bambushain.app*

Method | HTTP request | Description
------------- | ------------- | -------------
[**login**](AuthenticationApi.md#login) | **POST** api/login | Performs a login or requests the two factor code
[**logout**](AuthenticationApi.md#logout) | **DELETE** api/login | Performs a logout
[**validateToken**](AuthenticationApi.md#validateToken) | **HEAD** api/login | Validates the api token



Performs a login or requests the two factor code

Performs the login

### Example
```kotlin
// Import classes:
//import app.bambushain.api.*
//import app.bambushain.api.infrastructure.*
//import app.bambushain.api.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(AuthenticationApi::class.java)
val loginRequest : LoginRequest =  // LoginRequest | 

launch(Dispatchers.IO) {
    val result : LoginResponse = webService.login(loginRequest)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **loginRequest** | [**LoginRequest**](LoginRequest.md)|  |

### Return type

[**LoginResponse**](LoginResponse.md)

### Authorization



### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json


Performs a logout

Performs a logout and deletes the api token from the server

### Example
```kotlin
// Import classes:
//import app.bambushain.api.*
//import app.bambushain.api.infrastructure.*
//import app.bambushain.api.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(AuthenticationApi::class.java)

launch(Dispatchers.IO) {
    webService.logout()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

null (empty response body)

### Authorization



### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


Validates the api token

Checks whether the api token passed in the &#x60;Authorization&#x60;-header is valid

### Example
```kotlin
// Import classes:
//import app.bambushain.api.*
//import app.bambushain.api.infrastructure.*
//import app.bambushain.api.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(AuthenticationApi::class.java)

launch(Dispatchers.IO) {
    webService.validateToken()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

null (empty response body)

### Authorization



### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

