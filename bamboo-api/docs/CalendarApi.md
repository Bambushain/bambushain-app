# CalendarApi

All URIs are relative to *https://pandas.bambushain.app*

Method | HTTP request | Description
------------- | ------------- | -------------
[**createEvent**](CalendarApi.md#createEvent) | **POST** api/bamboo-grove/event | Creates a new event
[**deleteEvent**](CalendarApi.md#deleteEvent) | **DELETE** api/bamboo-grove/event/{id} | Deletes the given event
[**getEvents**](CalendarApi.md#getEvents) | **GET** api/bamboo-grove/event | Gets all events for the given date range
[**updateEvent**](CalendarApi.md#updateEvent) | **PUT** api/bamboo-grove/event/{id} | Updates the given event



Creates a new event



### Example
```kotlin
// Import classes:
//import app.bambushain.api.*
//import app.bambushain.api.infrastructure.*
//import app.bambushain.api.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(CalendarApi::class.java)
val event : Event =  // Event | 

launch(Dispatchers.IO) {
    val result : ErrorDetails = webService.createEvent(event)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **event** | [**Event**](Event.md)|  | [optional]

### Return type

[**ErrorDetails**](ErrorDetails.md)

### Authorization



### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json


Deletes the given event



### Example
```kotlin
// Import classes:
//import app.bambushain.api.*
//import app.bambushain.api.infrastructure.*
//import app.bambushain.api.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(CalendarApi::class.java)
val id : java.math.BigDecimal = 8.14 // java.math.BigDecimal | The id of the event

launch(Dispatchers.IO) {
    webService.deleteEvent(id)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **java.math.BigDecimal**| The id of the event |

### Return type

null (empty response body)

### Authorization



### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


Gets all events for the given date range



### Example
```kotlin
// Import classes:
//import app.bambushain.api.*
//import app.bambushain.api.infrastructure.*
//import app.bambushain.api.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(CalendarApi::class.java)
val start : java.time.LocalDate = 2013-10-20 // java.time.LocalDate | 
val end : java.time.LocalDate = 2013-10-20 // java.time.LocalDate | 

launch(Dispatchers.IO) {
    val result : kotlin.collections.List<Event> = webService.getEvents(start, end)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **start** | **java.time.LocalDate**|  |
 **end** | **java.time.LocalDate**|  |

### Return type

[**kotlin.collections.List&lt;Event&gt;**](Event.md)

### Authorization



### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


Updates the given event



### Example
```kotlin
// Import classes:
//import app.bambushain.api.*
//import app.bambushain.api.infrastructure.*
//import app.bambushain.api.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(CalendarApi::class.java)
val id : java.math.BigDecimal = 8.14 // java.math.BigDecimal | The id of the event
val event : Event =  // Event | 

launch(Dispatchers.IO) {
    webService.updateEvent(id, event)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **java.math.BigDecimal**| The id of the event |
 **event** | [**Event**](Event.md)|  | [optional]

### Return type

null (empty response body)

### Authorization



### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

