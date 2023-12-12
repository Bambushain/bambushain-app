package app.bambushain.api.authentication;

import android.content.Context;
import androidx.preference.PreferenceManager;
import app.bambushain.api.R;
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
    private Context ctx;

    public PandaAuthenticator(@NonNull Context ctx) {
        this.ctx = ctx;
    }

    @Override
    public Request authenticate(Route route, Response response) {
        val request = response.request();
        if (request.header("Authorization") != null) {
            return null;
        }

        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        val authenticationToken = sharedPrefs.getString(ctx.getString(R.string.bambooAuthenticationToken), "");

        return request.newBuilder().header("Authorization", "Panda " + authenticationToken).build();
    }
}
