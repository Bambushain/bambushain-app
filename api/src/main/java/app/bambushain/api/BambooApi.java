package app.bambushain.api;

import java.time.LocalDate;
import java.util.List;

import app.bambushain.models.authentication.ForgotPasswordRequest;
import app.bambushain.models.authentication.LoginRequest;
import app.bambushain.models.authentication.LoginResponse;
import app.bambushain.models.authentication.TwoFactorRequest;
import app.bambushain.models.bamboo.Event;
import app.bambushain.models.bamboo.User;
import app.bambushain.models.finalfantasy.Character;
import app.bambushain.models.finalfantasy.CharacterHousing;
import app.bambushain.models.finalfantasy.Crafter;
import app.bambushain.models.finalfantasy.CustomCharacterField;
import app.bambushain.models.finalfantasy.Fighter;
import app.bambushain.models.finalfantasy.FreeCompany;
import app.bambushain.models.my.ChangeMyPassword;
import app.bambushain.models.my.UpdateMyProfile;
import app.bambushain.models.pandas.UpdateUserProfile;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import okhttp3.RequestBody;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HEAD;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;

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
     * Get a list of all characters for the current user
     *
     * @return Observable&lt;List&lt;Character&gt;&gt;
     */
    @GET("api/final-fantasy/character")
    Observable<List<Character>> getCharacters();

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
     * Creates a new housing for the current user
     * Creates a new housing for the current user
     *
     * @param characterId (required)
     * @param housing     The housing to create (required)
     * @return Observable&lt;Housing&gt;
     */
    @Headers({
            "Content-Type:application/json"
    })
    @POST("api/final-fantasy/character/{character_id}/housing")
    Observable<CharacterHousing> createHousing(
            @retrofit2.http.Path("character_id") long characterId, @retrofit2.http.Body CharacterHousing housing
    );

    /**
     * Delete the given housing
     * Deletes the given housing
     *
     * @param characterId The id of the character (required)
     * @param id          The id of the housing (required)
     * @return Completable
     */
    @DELETE("api/final-fantasy/character/{character_id}/housing/{id}")
    Completable deleteHousing(
            @retrofit2.http.Path("character_id") long characterId, @retrofit2.http.Path("id") long id
    );

    /**
     * Get list of housing
     * Gets a list of all housings the current user has configured
     *
     * @param characterId (required)
     * @return Observable&lt;List&lt;Housing&gt;&gt;
     */
    @GET("api/final-fantasy/character/{character_id}/housing")
    Observable<List<CharacterHousing>> getHousings(
            @retrofit2.http.Path("character_id") long characterId
    );

    /**
     * Updates the given housing
     * Updates the given housing with the new values
     *
     * @param characterId The id of the character (required)
     * @param id          The id of the housing (required)
     * @param housing     The housing data to update to (required)
     * @return Completable
     */
    @Headers({
            "Content-Type:application/json"
    })
    @PUT("api/final-fantasy/character/{character_id}/housing/{id}")
    Completable updateHousing(
            @retrofit2.http.Path("character_id") long characterId, @retrofit2.http.Path("id") long id, @retrofit2.http.Body CharacterHousing housing
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
     * Updates the current users profile picture
     * Updates the current users profile picture
     *
     * @param data The current users profile picture (required)
     * @return Completable
     */
    @PUT("api/my/picture")
    Completable updateMyProfilePicture(@retrofit2.http.Body RequestBody data);

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
     * @param id            The id of the user (required)
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
