# app.bambushain.api - Kotlin client library for Bambushain

## Requires

* Kotlin 1.4.30
* Gradle 6.8.3

## Build

First, create the gradle wrapper script:

```
gradle wrapper
```

Then, run:

```
./gradlew check assemble
```

This runs all tests and packages the library.

## Features/Implementation Notes

* Supports JSON inputs/outputs, File inputs, and Form inputs.
* Supports collection formats for query parameters: csv, tsv, ssv, pipes.
* Some Kotlin and Java types are fully qualified to avoid conflicts with types defined in OpenAPI definitions.
* Implementation of ApiClient is intended to reduce method counts, specifically to benefit Android targets.

<a name="documentation-for-api-endpoints"></a>
## Documentation for API Endpoints

All URIs are relative to *https://pandas.bambushain.app*

Class | Method | HTTP request | Description
------------ | ------------- | ------------- | -------------
*AuthenticationApi* | [**login**](docs/AuthenticationApi.md#login) | **POST** api/login | Performs a login or requests the two factor code
*AuthenticationApi* | [**logout**](docs/AuthenticationApi.md#logout) | **DELETE** api/login | Performs a logout
*AuthenticationApi* | [**validateToken**](docs/AuthenticationApi.md#validatetoken) | **HEAD** api/login | Validates the api token
*CalendarApi* | [**createEvent**](docs/CalendarApi.md#createevent) | **POST** api/bamboo-grove/event | Creates a new event
*CalendarApi* | [**deleteEvent**](docs/CalendarApi.md#deleteevent) | **DELETE** api/bamboo-grove/event/{id} | Deletes the given event
*CalendarApi* | [**getEvents**](docs/CalendarApi.md#getevents) | **GET** api/bamboo-grove/event | Gets all events for the given date range
*CalendarApi* | [**updateEvent**](docs/CalendarApi.md#updateevent) | **PUT** api/bamboo-grove/event/{id} | Updates the given event
*CharacterApi* | [**createCharacter**](docs/CharacterApi.md#createcharacter) | **POST** api/final-fantasy/character | Create a new character
*CharacterApi* | [**createCustomField**](docs/CharacterApi.md#createcustomfield) | **POST** api/final-fantasy/character/custom-field | Creates a new character custom field
*CharacterApi* | [**createCustomFieldOption**](docs/CharacterApi.md#createcustomfieldoption) | **POST** api/final-fantasy/character/custom-field/{field_id}/option | Creates a new option for the given custom field
*CharacterApi* | [**createFreeCompany**](docs/CharacterApi.md#createfreecompany) | **POST** api/final-fantasy/free-company | Creates a new free company
*CharacterApi* | [**deleteCharacter**](docs/CharacterApi.md#deletecharacter) | **DELETE** api/final-fantasy/character/{id} | Deletes the given character
*CharacterApi* | [**deleteCustomField**](docs/CharacterApi.md#deletecustomfield) | **DELETE** api/final-fantasy/character/custom-field/{id} | Deletes the given custom field
*CharacterApi* | [**deleteCustomFieldOption**](docs/CharacterApi.md#deletecustomfieldoption) | **DELETE** api/final-fantasy/character/custom-field/{field_id}/option/{id} | Deletes the given option
*CharacterApi* | [**deleteFreeCompany**](docs/CharacterApi.md#deletefreecompany) | **DELETE** api/final-fantasy/free-company/{id} | Deletes the given free company
*CharacterApi* | [**getCharacterById**](docs/CharacterApi.md#getcharacterbyid) | **GET** api/final-fantasy/character/{id} | Gets the character by id
*CharacterApi* | [**getCharacters**](docs/CharacterApi.md#getcharacters) | **GET** api/final-fantasy/character | Get a list of all characters for the current user
*CharacterApi* | [**getCustomFieldById**](docs/CharacterApi.md#getcustomfieldbyid) | **GET** api/final-fantasy/character/custom-field/{id} | Gets the custom field with the given 
*CharacterApi* | [**getCustomFieldOptions**](docs/CharacterApi.md#getcustomfieldoptions) | **GET** api/final-fantasy/character/custom-field/{field_id}/option | Gets all options for the given custom field
*CharacterApi* | [**getCustomFields**](docs/CharacterApi.md#getcustomfields) | **GET** api/final-fantasy/character/custom-field | Gets a list of all character custom fields
*CharacterApi* | [**getFreeCompanies**](docs/CharacterApi.md#getfreecompanies) | **GET** api/final-fantasy/free-company | Gets all free companies
*CharacterApi* | [**getFreeCompanyById**](docs/CharacterApi.md#getfreecompanybyid) | **GET** api/final-fantasy/free-company/{id} | Get free company by id
*CharacterApi* | [**moveCustomField**](docs/CharacterApi.md#movecustomfield) | **PUT** api/final-fantasy/character/custom-field/{id}/{position} | Moves the given custom field to the new position
*CharacterApi* | [**updateCharacter**](docs/CharacterApi.md#updatecharacter) | **PUT** api/final-fantasy/character/{id} | Updates the given character
*CharacterApi* | [**updateCustomField**](docs/CharacterApi.md#updatecustomfield) | **PUT** api/final-fantasy/character/custom-field/{id} | Updates the given custom field
*CharacterApi* | [**updateCustomFieldOption**](docs/CharacterApi.md#updatecustomfieldoption) | **PUT** api/final-fantasy/character/custom-field/{field_id}/option/{id} | Updates the given option
*CharacterApi* | [**updateFreeCompany**](docs/CharacterApi.md#updatefreecompany) | **PUT** api/final-fantasy/free-company/{id} | Updates the given free company
*CharacterHousingApi* | [**createHousing**](docs/CharacterHousingApi.md#createhousing) | **POST** api/final-fantasy/character/{character_id}/housing | Creates a new housing for the current user
*CharacterHousingApi* | [**deleteHousing**](docs/CharacterHousingApi.md#deletehousing) | **DELETE** api/final-fantasy/character/{character_id}/housing/{id} | Delete the given housing
*CharacterHousingApi* | [**getHousing**](docs/CharacterHousingApi.md#gethousing) | **GET** api/final-fantasy/character/{character_id}/housing/{id} | Get a housing by job
*CharacterHousingApi* | [**getHousings**](docs/CharacterHousingApi.md#gethousings) | **GET** api/final-fantasy/character/{character_id}/housing | Get list of housing
*CharacterHousingApi* | [**updateHousing**](docs/CharacterHousingApi.md#updatehousing) | **PUT** api/final-fantasy/character/{character_id}/housing/{id} | Updates the given housing
*CrafterApi* | [**createCrafter**](docs/CrafterApi.md#createcrafter) | **POST** api/final-fantasy/character/{character_id}/crafter | Creates a new crafter for the current user
*CrafterApi* | [**deleteCrafter**](docs/CrafterApi.md#deletecrafter) | **DELETE** api/final-fantasy/character/{character_id}/crafter/{id} | Delete the given crafter
*CrafterApi* | [**getCrafter**](docs/CrafterApi.md#getcrafter) | **GET** api/final-fantasy/character/{character_id}/crafter/{id} | Get a crafter by job
*CrafterApi* | [**getCrafters**](docs/CrafterApi.md#getcrafters) | **GET** api/final-fantasy/character/{character_id}/crafter | Get list of crafter
*CrafterApi* | [**updateCrafter**](docs/CrafterApi.md#updatecrafter) | **PUT** api/final-fantasy/character/{character_id}/crafter/{id} | Updates the given crafter
*FighterApi* | [**createFighter**](docs/FighterApi.md#createfighter) | **POST** api/final-fantasy/character/{character_id}/fighter | Creates a new fighter for the current user
*FighterApi* | [**deleteFighter**](docs/FighterApi.md#deletefighter) | **DELETE** api/final-fantasy/character/{character_id}/fighter/{id} | Delete the given fighter
*FighterApi* | [**getFighter**](docs/FighterApi.md#getfighter) | **GET** api/final-fantasy/character/{character_id}/fighter/{id} | Get a fighter by job
*FighterApi* | [**getFighters**](docs/FighterApi.md#getfighters) | **GET** api/final-fantasy/character/{character_id}/fighter | Get list of fighter
*FighterApi* | [**updateFighter**](docs/FighterApi.md#updatefighter) | **PUT** api/final-fantasy/character/{character_id}/fighter/{id} | Updates the given fighter
*ProfileApi* | [**changeMyPassword**](docs/ProfileApi.md#changemypassword) | **PUT** api/my/profile/password | Changes the current users password
*ProfileApi* | [**enableTotp**](docs/ProfileApi.md#enabletotp) | **POST** api/my/totp | Enables TOTP for the current user
*ProfileApi* | [**getMyProfile**](docs/ProfileApi.md#getmyprofile) | **GET** api/my/profile | Gets the current user
*ProfileApi* | [**updateMyProfile**](docs/ProfileApi.md#updatemyprofile) | **PUT** api/my/profile | Updates the current users profile
*ProfileApi* | [**updateMyProfilePicture**](docs/ProfileApi.md#updatemyprofilepicture) | **PUT** api/my/picture | Updates the current users profile picture
*ProfileApi* | [**validateTotp**](docs/ProfileApi.md#validatetotp) | **PUT** api/my/totp/validate | Validates the given code and marks the TOTP status as enabled
*UserApi* | [**changePassword**](docs/UserApi.md#changepassword) | **PUT** api/user/{id}/password | Changes the password for the given user
*UserApi* | [**createUser**](docs/UserApi.md#createuser) | **POST** api/user | Creates a new user
*UserApi* | [**deleteUser**](docs/UserApi.md#deleteuser) | **DELETE** api/user/{id} | Deletes the given user
*UserApi* | [**disableTotp**](docs/UserApi.md#disabletotp) | **DELETE** api/user/{id}/totp | Reset totp
*UserApi* | [**getUser**](docs/UserApi.md#getuser) | **GET** api/user/{id} | Gets the given user
*UserApi* | [**getUserProfilePicture**](docs/UserApi.md#getuserprofilepicture) | **GET** api/user/{id}/picture | Get the profile picture
*UserApi* | [**getUsers**](docs/UserApi.md#getusers) | **GET** api/user | Get a list of all users
*UserApi* | [**makeUserMod**](docs/UserApi.md#makeusermod) | **PUT** api/user/{id}/mod | Gives the given user mod status
*UserApi* | [**revokeUserModRights**](docs/UserApi.md#revokeusermodrights) | **DELETE** api/user/{id}/mod | Revokes the mod rights for the given user
*UserApi* | [**updateUserProfile**](docs/UserApi.md#updateuserprofile) | **PUT** api/user/{id}/profile | Update the profile


<a name="documentation-for-models"></a>
## Documentation for Models

 - [app.bambushain.api.models.ChangeMyPassword](docs/ChangeMyPassword.md)
 - [app.bambushain.api.models.Character](docs/Character.md)
 - [app.bambushain.api.models.CharacterHousing](docs/CharacterHousing.md)
 - [app.bambushain.api.models.Crafter](docs/Crafter.md)
 - [app.bambushain.api.models.CustomCharacterField](docs/CustomCharacterField.md)
 - [app.bambushain.api.models.CustomCharacterFieldOption](docs/CustomCharacterFieldOption.md)
 - [app.bambushain.api.models.EnableTotpResponse](docs/EnableTotpResponse.md)
 - [app.bambushain.api.models.ErrorDetails](docs/ErrorDetails.md)
 - [app.bambushain.api.models.Event](docs/Event.md)
 - [app.bambushain.api.models.Fighter](docs/Fighter.md)
 - [app.bambushain.api.models.FreeCompany](docs/FreeCompany.md)
 - [app.bambushain.api.models.LoginRequest](docs/LoginRequest.md)
 - [app.bambushain.api.models.LoginResponse](docs/LoginResponse.md)
 - [app.bambushain.api.models.ModifyCustomCharacterFieldRequest](docs/ModifyCustomCharacterFieldRequest.md)
 - [app.bambushain.api.models.UpdateMyProfile](docs/UpdateMyProfile.md)
 - [app.bambushain.api.models.User](docs/User.md)
 - [app.bambushain.api.models.ValidateTotpRequest](docs/ValidateTotpRequest.md)


<a name="documentation-for-authorization"></a>
## Documentation for Authorization

<a name="Panda"></a>
### Panda

- **Type**: API key
- **API key parameter name**: Authorization
- **Location**: HTTP header

