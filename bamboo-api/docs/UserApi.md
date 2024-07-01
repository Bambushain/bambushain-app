# UserApi

All URIs are relative to *https://pandas.bambushain.app*

Method | HTTP request | Description
------------- | ------------- | -------------
[**changePassword**](UserApi.md#changePassword) | **PUT** api/user/{id}/password | Changes the password for the given user
[**createUser**](UserApi.md#createUser) | **POST** api/user | Creates a new user
[**deleteUser**](UserApi.md#deleteUser) | **DELETE** api/user/{id} | Deletes the given user
[**disableTotp**](UserApi.md#disableTotp) | **DELETE** api/user/{id}/totp | Reset totp
[**getUser**](UserApi.md#getUser) | **GET** api/user/{id} | Gets the given user
[**getUserProfilePicture**](UserApi.md#getUserProfilePicture) | **GET** api/user/{id}/picture | Get the profile picture
[**getUsers**](UserApi.md#getUsers) | **GET** api/user | Get a list of all users
[**makeUserMod**](UserApi.md#makeUserMod) | **PUT** api/user/{id}/mod | Gives the given user mod status
[**revokeUserModRights**](UserApi.md#revokeUserModRights) | **DELETE** api/user/{id}/mod | Revokes the mod rights for the given user
[**updateUserProfile**](UserApi.md#updateUserProfile) | **PUT** api/user/{id}/profile | Update the profile



Changes the password for the given user

Changes the password for the given user. Cannot be used to changes the current users password

### Example
```kotlin
// Import classes:
//import app.bambushain.api.*
//import app.bambushain.api.infrastructure.*
//import app.bambushain.api.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(UserApi::class.java)
val id : java.math.BigDecimal = 8.14 // java.math.BigDecimal | The id of the user

launch(Dispatchers.IO) {
    webService.changePassword(id)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **java.math.BigDecimal**| The id of the user |

### Return type

null (empty response body)

### Authorization



### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


Creates a new user

Creates a new user with the given values

### Example
```kotlin
// Import classes:
//import app.bambushain.api.*
//import app.bambushain.api.infrastructure.*
//import app.bambushain.api.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(UserApi::class.java)
val user : User =  // User | The data for the new user

launch(Dispatchers.IO) {
    val result : User = webService.createUser(user)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **user** | [**User**](User.md)| The data for the new user |

### Return type

[**User**](User.md)

### Authorization



### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json


Deletes the given user

Deletes the user with the given username

### Example
```kotlin
// Import classes:
//import app.bambushain.api.*
//import app.bambushain.api.infrastructure.*
//import app.bambushain.api.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(UserApi::class.java)
val id : java.math.BigDecimal = 8.14 // java.math.BigDecimal | The id of the user

launch(Dispatchers.IO) {
    webService.deleteUser(id)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **java.math.BigDecimal**| The id of the user |

### Return type

null (empty response body)

### Authorization



### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


Reset totp

Resets the totp of the given user

### Example
```kotlin
// Import classes:
//import app.bambushain.api.*
//import app.bambushain.api.infrastructure.*
//import app.bambushain.api.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(UserApi::class.java)
val id : java.math.BigDecimal = 8.14 // java.math.BigDecimal | The id of the user

launch(Dispatchers.IO) {
    webService.disableTotp(id)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **java.math.BigDecimal**| The id of the user |

### Return type

null (empty response body)

### Authorization



### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


Gets the given user

Gets the user by the given username

### Example
```kotlin
// Import classes:
//import app.bambushain.api.*
//import app.bambushain.api.infrastructure.*
//import app.bambushain.api.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(UserApi::class.java)
val id : java.math.BigDecimal = 8.14 // java.math.BigDecimal | The id of the user

launch(Dispatchers.IO) {
    val result : User = webService.getUser(id)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **java.math.BigDecimal**| The id of the user |

### Return type

[**User**](User.md)

### Authorization



### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


Get the profile picture

Gets the current users profile picture

### Example
```kotlin
// Import classes:
//import app.bambushain.api.*
//import app.bambushain.api.infrastructure.*
//import app.bambushain.api.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(UserApi::class.java)
val id : java.math.BigDecimal = 8.14 // java.math.BigDecimal | The id of the user

launch(Dispatchers.IO) {
    val result : java.io.File = webService.getUserProfilePicture(id)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **java.math.BigDecimal**| The id of the user |

### Return type

[**java.io.File**](java.io.File.md)

### Authorization



### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/octet-stream, application/json


Get a list of all users

Gets a list with all users

### Example
```kotlin
// Import classes:
//import app.bambushain.api.*
//import app.bambushain.api.infrastructure.*
//import app.bambushain.api.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(UserApi::class.java)

launch(Dispatchers.IO) {
    val result : kotlin.collections.List<User> = webService.getUsers()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**kotlin.collections.List&lt;User&gt;**](User.md)

### Authorization



### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


Gives the given user mod status

Gives the given user the mod status

### Example
```kotlin
// Import classes:
//import app.bambushain.api.*
//import app.bambushain.api.infrastructure.*
//import app.bambushain.api.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(UserApi::class.java)
val id : java.math.BigDecimal = 8.14 // java.math.BigDecimal | The id of the user

launch(Dispatchers.IO) {
    webService.makeUserMod(id)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **java.math.BigDecimal**| The id of the user |

### Return type

null (empty response body)

### Authorization



### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


Revokes the mod rights for the given user

Revokes the mod rights for the given user

### Example
```kotlin
// Import classes:
//import app.bambushain.api.*
//import app.bambushain.api.infrastructure.*
//import app.bambushain.api.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(UserApi::class.java)
val id : java.math.BigDecimal = 8.14 // java.math.BigDecimal | The id of the user

launch(Dispatchers.IO) {
    webService.revokeUserModRights(id)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **java.math.BigDecimal**| The id of the user |

### Return type

null (empty response body)

### Authorization



### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


Update the profile

Updates the profile of the given user

### Example
```kotlin
// Import classes:
//import app.bambushain.api.*
//import app.bambushain.api.infrastructure.*
//import app.bambushain.api.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(UserApi::class.java)
val id : java.math.BigDecimal = 8.14 // java.math.BigDecimal | The id of the user
val updateMyProfile : UpdateMyProfile =  // UpdateMyProfile | The data to update the user with

launch(Dispatchers.IO) {
    webService.updateUserProfile(id, updateMyProfile)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **java.math.BigDecimal**| The id of the user |
 **updateMyProfile** | [**UpdateMyProfile**](UpdateMyProfile.md)| The data to update the user with |

### Return type

null (empty response body)

### Authorization



### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

