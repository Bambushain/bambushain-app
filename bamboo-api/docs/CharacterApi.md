# CharacterApi

All URIs are relative to *https://pandas.bambushain.app*

Method | HTTP request | Description
------------- | ------------- | -------------
[**createCharacter**](CharacterApi.md#createCharacter) | **POST** api/final-fantasy/character | Create a new character
[**createCustomField**](CharacterApi.md#createCustomField) | **POST** api/final-fantasy/character/custom-field | Creates a new character custom field
[**createCustomFieldOption**](CharacterApi.md#createCustomFieldOption) | **POST** api/final-fantasy/character/custom-field/{field_id}/option | Creates a new option for the given custom field
[**createFreeCompany**](CharacterApi.md#createFreeCompany) | **POST** api/final-fantasy/free-company | Creates a new free company
[**deleteCharacter**](CharacterApi.md#deleteCharacter) | **DELETE** api/final-fantasy/character/{id} | Deletes the given character
[**deleteCustomField**](CharacterApi.md#deleteCustomField) | **DELETE** api/final-fantasy/character/custom-field/{id} | Deletes the given custom field
[**deleteCustomFieldOption**](CharacterApi.md#deleteCustomFieldOption) | **DELETE** api/final-fantasy/character/custom-field/{field_id}/option/{id} | Deletes the given option
[**deleteFreeCompany**](CharacterApi.md#deleteFreeCompany) | **DELETE** api/final-fantasy/free-company/{id} | Deletes the given free company
[**getCharacterById**](CharacterApi.md#getCharacterById) | **GET** api/final-fantasy/character/{id} | Gets the character by id
[**getCharacters**](CharacterApi.md#getCharacters) | **GET** api/final-fantasy/character | Get a list of all characters for the current user
[**getCustomFieldById**](CharacterApi.md#getCustomFieldById) | **GET** api/final-fantasy/character/custom-field/{id} | Gets the custom field with the given 
[**getCustomFieldOptions**](CharacterApi.md#getCustomFieldOptions) | **GET** api/final-fantasy/character/custom-field/{field_id}/option | Gets all options for the given custom field
[**getCustomFields**](CharacterApi.md#getCustomFields) | **GET** api/final-fantasy/character/custom-field | Gets a list of all character custom fields
[**getFreeCompanies**](CharacterApi.md#getFreeCompanies) | **GET** api/final-fantasy/free-company | Gets all free companies
[**getFreeCompanyById**](CharacterApi.md#getFreeCompanyById) | **GET** api/final-fantasy/free-company/{id} | Get free company by id
[**moveCustomField**](CharacterApi.md#moveCustomField) | **PUT** api/final-fantasy/character/custom-field/{id}/{position} | Moves the given custom field to the new position
[**updateCharacter**](CharacterApi.md#updateCharacter) | **PUT** api/final-fantasy/character/{id} | Updates the given character
[**updateCustomField**](CharacterApi.md#updateCustomField) | **PUT** api/final-fantasy/character/custom-field/{id} | Updates the given custom field
[**updateCustomFieldOption**](CharacterApi.md#updateCustomFieldOption) | **PUT** api/final-fantasy/character/custom-field/{field_id}/option/{id} | Updates the given option
[**updateFreeCompany**](CharacterApi.md#updateFreeCompany) | **PUT** api/final-fantasy/free-company/{id} | Updates the given free company



Create a new character



### Example
```kotlin
// Import classes:
//import app.bambushain.api.*
//import app.bambushain.api.infrastructure.*
//import app.bambushain.api.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(CharacterApi::class.java)
val character : Character =  // Character | 

launch(Dispatchers.IO) {
    val result : Character = webService.createCharacter(character)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **character** | [**Character**](Character.md)|  | [optional]

### Return type

[**Character**](Character.md)

### Authorization



### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json


Creates a new character custom field



### Example
```kotlin
// Import classes:
//import app.bambushain.api.*
//import app.bambushain.api.infrastructure.*
//import app.bambushain.api.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(CharacterApi::class.java)
val modifyCustomCharacterFieldRequest : ModifyCustomCharacterFieldRequest =  // ModifyCustomCharacterFieldRequest | 

launch(Dispatchers.IO) {
    val result : CustomCharacterField = webService.createCustomField(modifyCustomCharacterFieldRequest)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **modifyCustomCharacterFieldRequest** | [**ModifyCustomCharacterFieldRequest**](ModifyCustomCharacterFieldRequest.md)|  | [optional]

### Return type

[**CustomCharacterField**](CustomCharacterField.md)

### Authorization



### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json


Creates a new option for the given custom field



### Example
```kotlin
// Import classes:
//import app.bambushain.api.*
//import app.bambushain.api.infrastructure.*
//import app.bambushain.api.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(CharacterApi::class.java)
val fieldId : java.math.BigDecimal = 8.14 // java.math.BigDecimal | The id of the custom field
val body : kotlin.String = body_example // kotlin.String | 

launch(Dispatchers.IO) {
    val result : CustomCharacterFieldOption = webService.createCustomFieldOption(fieldId, body)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **fieldId** | **java.math.BigDecimal**| The id of the custom field |
 **body** | **kotlin.String**|  | [optional]

### Return type

[**CustomCharacterFieldOption**](CustomCharacterFieldOption.md)

### Authorization



### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json


Creates a new free company



### Example
```kotlin
// Import classes:
//import app.bambushain.api.*
//import app.bambushain.api.infrastructure.*
//import app.bambushain.api.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(CharacterApi::class.java)

launch(Dispatchers.IO) {
    val result : FreeCompany = webService.createFreeCompany()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**FreeCompany**](FreeCompany.md)

### Authorization



### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


Deletes the given character



### Example
```kotlin
// Import classes:
//import app.bambushain.api.*
//import app.bambushain.api.infrastructure.*
//import app.bambushain.api.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(CharacterApi::class.java)
val id : java.math.BigDecimal = 8.14 // java.math.BigDecimal | The id of the character

launch(Dispatchers.IO) {
    webService.deleteCharacter(id)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **java.math.BigDecimal**| The id of the character |

### Return type

null (empty response body)

### Authorization



### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


Deletes the given custom field



### Example
```kotlin
// Import classes:
//import app.bambushain.api.*
//import app.bambushain.api.infrastructure.*
//import app.bambushain.api.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(CharacterApi::class.java)
val id : java.math.BigDecimal = 8.14 // java.math.BigDecimal | The id of the custom field

launch(Dispatchers.IO) {
    webService.deleteCustomField(id)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **java.math.BigDecimal**| The id of the custom field |

### Return type

null (empty response body)

### Authorization



### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


Deletes the given option



### Example
```kotlin
// Import classes:
//import app.bambushain.api.*
//import app.bambushain.api.infrastructure.*
//import app.bambushain.api.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(CharacterApi::class.java)
val fieldId : java.math.BigDecimal = 8.14 // java.math.BigDecimal | The id of the custom field
val id : java.math.BigDecimal = 8.14 // java.math.BigDecimal | The id of the custom field option

launch(Dispatchers.IO) {
    webService.deleteCustomFieldOption(fieldId, id)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **fieldId** | **java.math.BigDecimal**| The id of the custom field |
 **id** | **java.math.BigDecimal**| The id of the custom field option |

### Return type

null (empty response body)

### Authorization



### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


Deletes the given free company



### Example
```kotlin
// Import classes:
//import app.bambushain.api.*
//import app.bambushain.api.infrastructure.*
//import app.bambushain.api.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(CharacterApi::class.java)
val id : java.math.BigDecimal = 8.14 // java.math.BigDecimal | The id of the free company

launch(Dispatchers.IO) {
    webService.deleteFreeCompany(id)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **java.math.BigDecimal**| The id of the free company |

### Return type

null (empty response body)

### Authorization



### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


Gets the character by id



### Example
```kotlin
// Import classes:
//import app.bambushain.api.*
//import app.bambushain.api.infrastructure.*
//import app.bambushain.api.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(CharacterApi::class.java)
val id : java.math.BigDecimal = 8.14 // java.math.BigDecimal | The id of the character

launch(Dispatchers.IO) {
    val result : Character = webService.getCharacterById(id)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **java.math.BigDecimal**| The id of the character |

### Return type

[**Character**](Character.md)

### Authorization



### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


Get a list of all characters for the current user



### Example
```kotlin
// Import classes:
//import app.bambushain.api.*
//import app.bambushain.api.infrastructure.*
//import app.bambushain.api.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(CharacterApi::class.java)

launch(Dispatchers.IO) {
    val result : kotlin.collections.List<Character> = webService.getCharacters()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**kotlin.collections.List&lt;Character&gt;**](Character.md)

### Authorization



### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


Gets the custom field with the given 



### Example
```kotlin
// Import classes:
//import app.bambushain.api.*
//import app.bambushain.api.infrastructure.*
//import app.bambushain.api.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(CharacterApi::class.java)
val id : java.math.BigDecimal = 8.14 // java.math.BigDecimal | The id of the custom field

launch(Dispatchers.IO) {
    val result : ErrorDetails = webService.getCustomFieldById(id)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **java.math.BigDecimal**| The id of the custom field |

### Return type

[**ErrorDetails**](ErrorDetails.md)

### Authorization



### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


Gets all options for the given custom field



### Example
```kotlin
// Import classes:
//import app.bambushain.api.*
//import app.bambushain.api.infrastructure.*
//import app.bambushain.api.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(CharacterApi::class.java)
val fieldId : java.math.BigDecimal = 8.14 // java.math.BigDecimal | The id of the custom field

launch(Dispatchers.IO) {
    val result : kotlin.collections.List<CustomCharacterFieldOption> = webService.getCustomFieldOptions(fieldId)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **fieldId** | **java.math.BigDecimal**| The id of the custom field |

### Return type

[**kotlin.collections.List&lt;CustomCharacterFieldOption&gt;**](CustomCharacterFieldOption.md)

### Authorization



### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


Gets a list of all character custom fields



### Example
```kotlin
// Import classes:
//import app.bambushain.api.*
//import app.bambushain.api.infrastructure.*
//import app.bambushain.api.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(CharacterApi::class.java)

launch(Dispatchers.IO) {
    val result : kotlin.collections.List<CustomCharacterField> = webService.getCustomFields()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**kotlin.collections.List&lt;CustomCharacterField&gt;**](CustomCharacterField.md)

### Authorization



### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


Gets all free companies



### Example
```kotlin
// Import classes:
//import app.bambushain.api.*
//import app.bambushain.api.infrastructure.*
//import app.bambushain.api.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(CharacterApi::class.java)

launch(Dispatchers.IO) {
    val result : kotlin.collections.List<FreeCompany> = webService.getFreeCompanies()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**kotlin.collections.List&lt;FreeCompany&gt;**](FreeCompany.md)

### Authorization



### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


Get free company by id



### Example
```kotlin
// Import classes:
//import app.bambushain.api.*
//import app.bambushain.api.infrastructure.*
//import app.bambushain.api.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(CharacterApi::class.java)
val id : java.math.BigDecimal = 8.14 // java.math.BigDecimal | The id of the free company

launch(Dispatchers.IO) {
    val result : FreeCompany = webService.getFreeCompanyById(id)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **java.math.BigDecimal**| The id of the free company |

### Return type

[**FreeCompany**](FreeCompany.md)

### Authorization



### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


Moves the given custom field to the new position



### Example
```kotlin
// Import classes:
//import app.bambushain.api.*
//import app.bambushain.api.infrastructure.*
//import app.bambushain.api.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(CharacterApi::class.java)
val id : java.math.BigDecimal = 8.14 // java.math.BigDecimal | The id of the custom field
val position : java.math.BigDecimal = 8.14 // java.math.BigDecimal | The new position

launch(Dispatchers.IO) {
    webService.moveCustomField(id, position)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **java.math.BigDecimal**| The id of the custom field |
 **position** | **java.math.BigDecimal**| The new position |

### Return type

null (empty response body)

### Authorization



### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


Updates the given character



### Example
```kotlin
// Import classes:
//import app.bambushain.api.*
//import app.bambushain.api.infrastructure.*
//import app.bambushain.api.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(CharacterApi::class.java)
val id : java.math.BigDecimal = 8.14 // java.math.BigDecimal | The id of the character
val character : Character =  // Character | 

launch(Dispatchers.IO) {
    webService.updateCharacter(id, character)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **java.math.BigDecimal**| The id of the character |
 **character** | [**Character**](Character.md)|  | [optional]

### Return type

null (empty response body)

### Authorization



### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json


Updates the given custom field



### Example
```kotlin
// Import classes:
//import app.bambushain.api.*
//import app.bambushain.api.infrastructure.*
//import app.bambushain.api.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(CharacterApi::class.java)
val id : java.math.BigDecimal = 8.14 // java.math.BigDecimal | The id of the custom field
val modifyCustomCharacterFieldRequest : ModifyCustomCharacterFieldRequest =  // ModifyCustomCharacterFieldRequest | 

launch(Dispatchers.IO) {
    webService.updateCustomField(id, modifyCustomCharacterFieldRequest)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **java.math.BigDecimal**| The id of the custom field |
 **modifyCustomCharacterFieldRequest** | [**ModifyCustomCharacterFieldRequest**](ModifyCustomCharacterFieldRequest.md)|  | [optional]

### Return type

null (empty response body)

### Authorization



### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json


Updates the given option



### Example
```kotlin
// Import classes:
//import app.bambushain.api.*
//import app.bambushain.api.infrastructure.*
//import app.bambushain.api.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(CharacterApi::class.java)
val fieldId : java.math.BigDecimal = 8.14 // java.math.BigDecimal | The id of the custom field
val id : java.math.BigDecimal = 8.14 // java.math.BigDecimal | The id of the custom field option
val body : kotlin.String = body_example // kotlin.String | 

launch(Dispatchers.IO) {
    webService.updateCustomFieldOption(fieldId, id, body)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **fieldId** | **java.math.BigDecimal**| The id of the custom field |
 **id** | **java.math.BigDecimal**| The id of the custom field option |
 **body** | **kotlin.String**|  | [optional]

### Return type

null (empty response body)

### Authorization



### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json


Updates the given free company



### Example
```kotlin
// Import classes:
//import app.bambushain.api.*
//import app.bambushain.api.infrastructure.*
//import app.bambushain.api.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(CharacterApi::class.java)
val id : java.math.BigDecimal = 8.14 // java.math.BigDecimal | The id of the free company
val freeCompany : FreeCompany =  // FreeCompany | 

launch(Dispatchers.IO) {
    webService.updateFreeCompany(id, freeCompany)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **java.math.BigDecimal**| The id of the free company |
 **freeCompany** | [**FreeCompany**](FreeCompany.md)|  | [optional]

### Return type

null (empty response body)

### Authorization



### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

