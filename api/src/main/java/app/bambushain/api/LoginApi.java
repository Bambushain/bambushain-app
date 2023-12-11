package app.bambushain.api;

import app.bambushain.models.LoginRequest;
import app.bambushain.models.LoginResponse;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface LoginApi {
    /**
     * Performs a login or requests the two factor code
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
}
