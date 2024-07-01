# ProfileApi

All URIs are relative to *https://pandas.bambushain.app*

Method | HTTP request | Description
------------- | ------------- | -------------
[**changeMyPassword**](ProfileApi.md#changeMyPassword) | **PUT** api/my/profile/password | Changes the current users password
[**enableTotp**](ProfileApi.md#enableTotp) | **POST** api/my/totp | Enables TOTP for the current user
[**getMyProfile**](ProfileApi.md#getMyProfile) | **GET** api/my/profile | Gets the current user
[**updateMyProfile**](ProfileApi.md#updateMyProfile) | **PUT** api/my/profile | Updates the current users profile
[**updateMyProfilePicture**](ProfileApi.md#updateMyProfilePicture) | **PUT** api/my/picture | Updates the current users profile picture
[**validateTotp**](ProfileApi.md#validateTotp) | **PUT** api/my/totp/validate | Validates the given code and marks the TOTP status as enabled



Changes the current users password

Changes the current users password, while checking if the old password is valid

### Example
```kotlin
// Import classes:
//import app.bambushain.api.*
//import app.bambushain.api.infrastructure.*
//import app.bambushain.api.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(ProfileApi::class.java)
val changeMyPassword : ChangeMyPassword =  // ChangeMyPassword | The current and new password

launch(Dispatchers.IO) {
    webService.changeMyPassword(changeMyPassword)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **changeMyPassword** | [**ChangeMyPassword**](ChangeMyPassword.md)| The current and new password |

### Return type

null (empty response body)

### Authorization



### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json


Enables TOTP for the current user



### Example
```kotlin
// Import classes:
//import app.bambushain.api.*
//import app.bambushain.api.infrastructure.*
//import app.bambushain.api.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(ProfileApi::class.java)

launch(Dispatchers.IO) {
    val result : EnableTotpResponse = webService.enableTotp()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**EnableTotpResponse**](EnableTotpResponse.md)

### Authorization



### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


Gets the current user

Gets the current users profile

### Example
```kotlin
// Import classes:
//import app.bambushain.api.*
//import app.bambushain.api.infrastructure.*
//import app.bambushain.api.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(ProfileApi::class.java)

launch(Dispatchers.IO) {
    val result : User = webService.getMyProfile()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**User**](User.md)

### Authorization



### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


Updates the current users profile

Updates the current users profile

### Example
```kotlin
// Import classes:
//import app.bambushain.api.*
//import app.bambushain.api.infrastructure.*
//import app.bambushain.api.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(ProfileApi::class.java)
val updateMyProfile : UpdateMyProfile =  // UpdateMyProfile | The current users profile

launch(Dispatchers.IO) {
    webService.updateMyProfile(updateMyProfile)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **updateMyProfile** | [**UpdateMyProfile**](UpdateMyProfile.md)| The current users profile |

### Return type

null (empty response body)

### Authorization



### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json


Updates the current users profile picture

Updates the current users profile picture

### Example
```kotlin
// Import classes:
//import app.bambushain.api.*
//import app.bambushain.api.infrastructure.*
//import app.bambushain.api.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(ProfileApi::class.java)
val body : java.io.File = BINARY_DATA_HERE // java.io.File | The current users profile picture

launch(Dispatchers.IO) {
    webService.updateMyProfilePicture(body)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | **java.io.File**| The current users profile picture |

### Return type

null (empty response body)

### Authorization



### HTTP request headers

 - **Content-Type**: application/octet-stream
 - **Accept**: application/json


Validates the given code and marks the TOTP status as enabled



### Example
```kotlin
// Import classes:
//import app.bambushain.api.*
//import app.bambushain.api.infrastructure.*
//import app.bambushain.api.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(ProfileApi::class.java)
val validateTotpRequest : ValidateTotpRequest =  // ValidateTotpRequest | 

launch(Dispatchers.IO) {
    webService.validateTotp(validateTotpRequest)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **validateTotpRequest** | [**ValidateTotpRequest**](ValidateTotpRequest.md)|  | [optional]

### Return type

null (empty response body)

### Authorization



### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

