package app.bambushain.api;

import app.bambushain.models.authentication.ForgotPasswordRequest;
import app.bambushain.models.authentication.LoginRequest;
import app.bambushain.models.authentication.LoginResponse;
import app.bambushain.models.authentication.TwoFactorRequest;
import app.bambushain.models.bamboo.Event;
import app.bambushain.models.bamboo.User;
import app.bambushain.models.finalfantasy.Character;
import app.bambushain.models.finalfantasy.*;
import app.bambushain.models.my.ChangeMyPassword;
import app.bambushain.models.my.UpdateMyProfile;
import app.bambushain.models.pandas.UpdateUserProfile;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.*;

import java.time.LocalDate;
import java.util.List;

public interface BambooApi {
    /**
     * Performs a login
     * Performs the login
     *
     * @param loginRequest (required)
     * @return Observable&lt;LoginResponse&gt;
     */
    @Headers({
            "Content-Type:application/json"
    })
    @POST("api/login")
    Observable<LoginResponse> login(
            @retrofit2.http.Body LoginRequest loginRequest
    );

    /**
     * Requests the two factor code
     *
     * @param twoFactorRequest (required)
     * @return Observable&lt;LoginResponse&gt;
     */
    @Headers({
            "Content-Type:application/json"
    })
    @POST("api/login")
    Completable requestTwoFactorCode(
            @retrofit2.http.Body TwoFactorRequest twoFactorRequest
    );

    /**
     * Performs a logout
     * Performs a logout and deletes the api token from the server
     *
     * @return Completable
     */
    @DELETE("api/login")
    Completable logout();

    /**
     * Validates the api token
     * Checks whether the api token passed in the &#x60;Authorization&#x60;-header is valid
     *
     * @return Completable
     */
    @HEAD("api/login")
    Completable validateToken();

    /**
     * Forgot password
     * Sends an email to all mods requesting a password change
     *
     * @return Completable
     */
    @POST("api/forgot-password")
    Completable forgotPassword(@retrofit2.http.Body ForgotPasswordRequest forgotPasswordRequest);

    /**
     * Creates a new event
     *
     * @param event (optional)
     * @return Observable&lt;Event&gt;
     */
    @Headers({
            "Content-Type:application/json"
    })
    @POST("api/bamboo-grove/event")
    Observable<Event> createEvent(
            @retrofit2.http.Body Event event
    );

    /**
     * Deletes the given event
     *
     * @param id The id of the event (required)
     * @return Completable
     */
    @DELETE("api/bamboo-grove/event/{id}")
    Completable deleteEvent(
            @retrofit2.http.Path("id") long id
    );

    /**
     * Gets all events for the given date range
     *
     * @param start (required)
     * @param end   (required)
     * @return Observable&lt;List&lt;Event&gt;&gt;
     */
    @GET("api/bamboo-grove/event")
    Observable<List<Event>> getEvents(
            @retrofit2.http.Query("start") LocalDate start, @retrofit2.http.Query("end") LocalDate end
    );

    /**
     * Updates the given event
     *
     * @param id    The id of the event (required)
     * @param event (optional)
     * @return Completable
     */
    @Headers({
            "Content-Type:application/json"
    })
    @PUT("api/bamboo-grove/event/{id}")
    Completable updateEvent(
            @retrofit2.http.Path("id") long id, @retrofit2.http.Body Event event
    );

    /**
     * Create a new character
     *
     * @param character (optional)
     * @return Observable&lt;Character&gt;
     */
    @Headers({
            "Content-Type:application/json"
    })
    @POST("api/final-fantasy/character")
    Observable<Character> createCharacter(
            @retrofit2.http.Body Character character
    );

    /**
     * Creates a new character custom field
     *
     * @param modifyCustomCharacterFieldRequest (optional)
     * @return Observable&lt;CustomCharacterField&gt;
     */
    @Headers({
            "Content-Type:application/json"
    })
    @POST("api/final-fantasy/character/custom-field")
    Observable<CustomCharacterField> createCustomField(
            @retrofit2.http.Body ModifyCustomCharacterFieldRequest modifyCustomCharacterFieldRequest
    );

    /**
     * Creates a new option for the given custom field
     *
     * @param fieldId The id of the custom field (required)
     * @param body    (optional)
     * @return Completable
     */
    @Headers({
            "Content-Type:application/json"
    })
    @POST("api/final-fantasy/character/custom-field/{field_id}/option")
    Completable createCustomFieldOption(
            @retrofit2.http.Path("field_id") long fieldId, @retrofit2.http.Body String body
    );

    /**
     * Creates a new free company
     *
     * @return Observable&lt;FreeCompany&gt;
     */
    @POST("api/final-fantasy/free-company")
    Observable<FreeCompany> createFreeCompany();


    /**
     * Deletes the given character
     *
     * @param id The id of the character (required)
     * @return Completable
     */
    @DELETE("api/final-fantasy/character/{id}")
    Completable deleteCharacter(
            @retrofit2.http.Path("id") long id
    );

    /**
     * Deletes the given custom field
     *
     * @param id The id of the custom field (required)
     * @return Completable
     */
    @DELETE("api/final-fantasy/character/custom-field/{id}")
    Completable deleteCustomField(
            @retrofit2.http.Path("id") long id
    );

    /**
     * Deletes the given option
     *
     * @param fieldId The id of the custom field (required)
     * @param id      The id of the custom field option (required)
     * @return Completable
     */
    @DELETE("api/final-fantasy/character/custom-field/{field_id}/option/{id}")
    Completable deleteCustomFieldOption(
            @retrofit2.http.Path("field_id") long fieldId, @retrofit2.http.Path("id") long id
    );

    /**
     * Deletes the given free company
     *
     * @param id The id of the free company (required)
     * @return Completable
     */
    @DELETE("api/final-fantasy/free-company/{id}")
    Completable deleteFreeCompany(
            @retrofit2.http.Path("id") long id
    );

    /**
     * Gets the character by id
     *
     * @param id The id of the character (required)
     * @return Observable&lt;Character&gt;
     */
    @GET("api/final-fantasy/character/{id}")
    Observable<Character> getCharacterById(
            @retrofit2.http.Path("id") long id
    );

    /**
     * Get a list of all characters for the current user
     *
     * @return Observable&lt;List&lt;Character&gt;&gt;
     */
    @GET("api/final-fantasy/character")
    Observable<List<Character>> getCharacters();

    /**
     * Gets the custom field with the given id
     *
     * @param id The id of the custom field (required)
     * @return Observable&lt;CustomCharacterField&gt;
     */
    @GET("api/final-fantasy/character/custom-field/{id}")
    Observable<CustomCharacterField> getCustomFieldById(
            @retrofit2.http.Path("id") long id
    );

    /**
     * Gets all options for the given custom field
     *
     * @param fieldId The id of the custom field (required)
     * @return Observable&lt;List&lt;CustomCharacterFieldOption&gt;&gt;
     */
    @GET("api/final-fantasy/character/custom-field/{field_id}/option")
    Observable<List<CustomCharacterFieldOption>> getCustomFieldOptions(
            @retrofit2.http.Path("field_id") long fieldId
    );

    /**
     * Gets a list of all character custom fields
     *
     * @return Observable&lt;List&lt;CustomCharacterField&gt;&gt;
     */
    @GET("api/final-fantasy/character/custom-field")
    Observable<List<CustomCharacterField>> getCustomFields();

    /**
     * Gets all free companies
     *
     * @return Observable&lt;List&lt;FreeCompany&gt;&gt;
     */
    @GET("api/final-fantasy/free-company")
    Observable<List<FreeCompany>> getFreeCompanies();

    /**
     * Get free company by id
     *
     * @param id The id of the free company (required)
     * @return Observable&lt;FreeCompany&gt;
     */
    @GET("api/final-fantasy/free-company/{id}")
    Observable<FreeCompany> getFreeCompanyById(
            @retrofit2.http.Path("id") long id
    );

    /**
     * Moves the given custom field to the new position
     *
     * @param id       The id of the custom field (required)
     * @param position The new position (required)
     * @return Completable
     */
    @PUT("api/final-fantasy/character/custom-field/{id}/{position}")
    Completable moveCustomField(
            @retrofit2.http.Path("id") long id, @retrofit2.http.Path("position") long position
    );

    /**
     * Updates the given character
     *
     * @param id        The id of the character (required)
     * @param character (optional)
     * @return Completable
     */
    @Headers({
            "Content-Type:application/json"
    })
    @PUT("api/final-fantasy/character/{id}")
    Completable updateCharacter(
            @retrofit2.http.Path("id") long id, @retrofit2.http.Body Character character
    );

    /**
     * Updates the given custom field
     *
     * @param id                                The id of the custom field (required)
     * @param modifyCustomCharacterFieldRequest (optional)
     * @return Completable
     */
    @Headers({
            "Content-Type:application/json"
    })
    @PUT("api/final-fantasy/character/custom-field/{id}")
    Completable updateCustomField(
            @retrofit2.http.Path("id") long id, @retrofit2.http.Body ModifyCustomCharacterFieldRequest modifyCustomCharacterFieldRequest
    );

    /**
     * Updates the given option
     *
     * @param fieldId The id of the custom field (required)
     * @param id      The id of the custom field option (required)
     * @param body    (optional)
     * @return Completable
     */
    @Headers({
            "Content-Type:application/json"
    })
    @PUT("api/final-fantasy/character/custom-field/{field_id}/option/{id}")
    Completable updateCustomFieldOption(
            @retrofit2.http.Path("field_id") long fieldId, @retrofit2.http.Path("id") long id, @retrofit2.http.Body String body
    );

    /**
     * Updates the given free company
     *
     * @param id          The id of the free company (required)
     * @param freeCompany (optional)
     * @return Completable
     */
    @Headers({
            "Content-Type:application/json"
    })
    @PUT("api/final-fantasy/free-company/{id}")
    Completable updateFreeCompany(
            @retrofit2.http.Path("id") long id, @retrofit2.http.Body FreeCompany freeCompany
    );

    /**
     * Creates a new crafter for the current user
     * Creates a new crafter for the current user
     *
     * @param characterId (required)
     * @param crafter     The crafter to create (required)
     * @return Observable&lt;Crafter&gt;
     */
    @Headers({
            "Content-Type:application/json"
    })
    @POST("api/final-fantasy/character/{character_id}/crafter")
    Observable<Crafter> createCrafter(
            @retrofit2.http.Path("character_id") long characterId, @retrofit2.http.Body Crafter crafter
    );

    /**
     * Delete the given crafter
     * Deletes the given crafter
     *
     * @param characterId The id of the character (required)
     * @param id          The id of the crafter (required)
     * @return Completable
     */
    @DELETE("api/final-fantasy/character/{character_id}/crafter/{id}")
    Completable deleteCrafter(
            @retrofit2.http.Path("character_id") long characterId, @retrofit2.http.Path("id") long id
    );

    /**
     * Get a crafter by job
     * Gets the crafter with the given job
     *
     * @param characterId The id of the character (required)
     * @param id          The id of the crafter (required)
     * @return Observable&lt;Crafter&gt;
     */
    @GET("api/final-fantasy/character/{character_id}/crafter/{id}")
    Observable<Crafter> getCrafter(
            @retrofit2.http.Path("character_id") long characterId, @retrofit2.http.Path("id") long id
    );

    /**
     * Get list of crafter
     * Gets a list of all crafters the current user has configured
     *
     * @param characterId (required)
     * @return Observable&lt;List&lt;Crafter&gt;&gt;
     */
    @GET("api/final-fantasy/character/{character_id}/crafter")
    Observable<List<Crafter>> getCrafters(
            @retrofit2.http.Path("character_id") long characterId
    );

    /**
     * Updates the given crafter
     * Updates the given crafter with the new values
     *
     * @param characterId The id of the character (required)
     * @param id          The id of the crafter (required)
     * @param crafter     The crafter data to update to (required)
     * @return Completable
     */
    @Headers({
            "Content-Type:application/json"
    })
    @PUT("api/final-fantasy/character/{character_id}/crafter/{id}")
    Completable updateCrafter(
            @retrofit2.http.Path("character_id") long characterId, @retrofit2.http.Path("id") long id, @retrofit2.http.Body Crafter crafter
    );

    /**
     * Creates a new fighter for the current user
     * Creates a new fighter for the current user
     *
     * @param characterId (required)
     * @param fighter     The fighter to create (required)
     * @return Observable&lt;Fighter&gt;
     */
    @Headers({
            "Content-Type:application/json"
    })
    @POST("api/final-fantasy/character/{character_id}/fighter")
    Observable<Fighter> createFighter(
            @retrofit2.http.Path("character_id") long characterId, @retrofit2.http.Body Fighter fighter
    );

    /**
     * Delete the given fighter
     * Deletes the given fighter
     *
     * @param characterId The id of the character (required)
     * @param id          The id of the fighter (required)
     * @return Completable
     */
    @DELETE("api/final-fantasy/character/{character_id}/fighter/{id}")
    Completable deleteFighter(
            @retrofit2.http.Path("character_id") long characterId, @retrofit2.http.Path("id") long id
    );

    /**
     * Get a fighter by job
     * Gets the fighter with the given job
     *
     * @param characterId The id of the character (required)
     * @param id          The id of the fighter (required)
     * @return Observable&lt;Fighter&gt;
     */
    @GET("api/final-fantasy/character/{character_id}/fighter/{id}")
    Observable<Fighter> getFighter(
            @retrofit2.http.Path("character_id") long characterId, @retrofit2.http.Path("id") long id
    );

    /**
     * Get list of fighter
     * Gets a list of all fighters the current user has configured
     *
     * @param characterId (required)
     * @return Observable&lt;List&lt;Fighter&gt;&gt;
     */
    @GET("api/final-fantasy/character/{character_id}/fighter")
    Observable<List<Fighter>> getFighters(
            @retrofit2.http.Path("character_id") long characterId
    );

    /**
     * Updates the given fighter
     * Updates the given fighter with the new values
     *
     * @param characterId The id of the character (required)
     * @param id          The id of the fighter (required)
     * @param fighter     The fighter data to update to (required)
     * @return Completable
     */
    @Headers({
            "Content-Type:application/json"
    })
    @PUT("api/final-fantasy/character/{character_id}/fighter/{id}")
    Completable updateFighter(
            @retrofit2.http.Path("character_id") long characterId, @retrofit2.http.Path("id") long id, @retrofit2.http.Body Fighter fighter
    );

    /**
     * Changes the current users password
     * Changes the current users password, while checking if the old password is valid
     *
     * @param changeMyPassword The current and new password (required)
     * @return Completable
     */
    @Headers({
            "Content-Type:application/json"
    })
    @PUT("api/my/profile/password")
    Completable changeMyPassword(
            @retrofit2.http.Body ChangeMyPassword changeMyPassword
    );

    /**
     * Gets the current user
     * Gets the current users profile
     *
     * @return Observable&lt;User&gt;
     */
    @GET("api/my/profile")
    Observable<User> getMyProfile();

    /**
     * Updates the current users profile
     * Updates the current users profile
     *
     * @param updateMyProfile The current users profile (required)
     * @return Completable
     */
    @Headers({
            "Content-Type:application/json"
    })
    @PUT("api/my/profile")
    Completable updateMyProfile(
            @retrofit2.http.Body UpdateMyProfile updateMyProfile
    );

    /**
     * Changes the password for the given user
     * Changes the password for the given user. Cannot be used to changes the current users password
     *
     * @param id The id of the user (required)
     * @return Completable
     */
    @Headers({
            "Content-Type:application/json"
    })
    @PUT("api/user/{id}/password")
    Completable changePassword(
            @retrofit2.http.Path("id") long id
    );

    /**
     * Creates a new user
     * Creates a new user with the given values
     *
     * @param user The data for the new user (required)
     * @return Observable&lt;User&gt;
     */
    @Headers({
            "Content-Type:application/json"
    })
    @POST("api/user")
    Observable<User> createUser(
            @retrofit2.http.Body User user
    );

    /**
     * Deletes the given user
     * Deletes the user with the given username
     *
     * @param id The id of the user (required)
     * @return Completable
     */
    @DELETE("api/user/{id}")
    Completable deleteUser(
            @retrofit2.http.Path("id") long id
    );

    /**
     * Gets the given user
     * Gets the user by the given username
     *
     * @param id The id of the user (required)
     * @return Observable&lt;User&gt;
     */
    @GET("api/user/{id}")
    Observable<User> getUser(
            @retrofit2.http.Path("id") long id
    );

    /**
     * Get a list of all users
     * Gets a list with all users
     *
     * @return Observable&lt;List&lt;User&gt;&gt;
     */
    @GET("api/user")
    Observable<List<User>> getUsers();

    /**
     * Gives the given user mod status
     * Gives the given user the mod status
     *
     * @param id The id of the user (required)
     * @return Completable
     */
    @PUT("api/user/{id}/mod")
    Completable makeUserMod(
            @retrofit2.http.Path("id") long id
    );

    /**
     * Revokes the mod rights for the given user
     * Revokes the mod rights for the given user
     *
     * @param id The id of the user (required)
     * @return Completable
     */
    @DELETE("api/user/{id}/mod")
    Completable revokeUserModRights(
            @retrofit2.http.Path("id") long id
    );

    /**
     * Reset two factor code of the user
     * Reset two factor code of the user
     *
     * @param id The id of the user (required)
     * @return Completable
     */
    @DELETE("api/user/{id}/totp")
    Completable resetUserTotp(
            @retrofit2.http.Path("id") long id
    );

    /**
     * Update the profile
     * Updates the profile of the given user
     *
     * @param id              The id of the user (required)
     * @param updateProfile The data to update the user with (required)
     * @return Completable
     */
    @Headers({
            "Content-Type:application/json"
    })
    @PUT("api/user/{id}/profile")
    Completable updateUserProfile(
            @retrofit2.http.Path("id") long id, @retrofit2.http.Body UpdateUserProfile updateProfile
    );
}
