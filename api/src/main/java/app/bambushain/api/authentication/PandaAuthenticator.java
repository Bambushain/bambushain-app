package app.bambushain.api.authentication;

import lombok.*;
import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

@EqualsAndHashCode
@ToString
@Setter
@Getter
public class PandaAuthenticator implements Authenticator {
    @NonNull
    private String authenticationToken;

    public PandaAuthenticator(@NonNull String authenticationToken) {
        this.authenticationToken = authenticationToken;
    }

    @Override
    public Request authenticate(Route route, Response response) {
        val request = response.request();
        if (request.header("Authorization") != null) {
            return null;
        }

        return request.newBuilder().header("Authorization", "Panda " + authenticationToken).build();
    }
}
