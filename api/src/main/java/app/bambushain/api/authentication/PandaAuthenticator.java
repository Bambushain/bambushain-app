package app.bambushain.api.authentication;

import android.content.Context;
import androidx.preference.PreferenceManager;
import app.bambushain.api.R;
import dagger.hilt.android.qualifiers.ApplicationContext;
import lombok.*;
import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

import javax.inject.Inject;
import javax.inject.Singleton;

@EqualsAndHashCode
@ToString
@Setter
@Getter
@Singleton
public class PandaAuthenticator implements Authenticator {
    @Inject
    @ApplicationContext
    Context ctx;

    @Inject
    public PandaAuthenticator() {
    }

    @Override
    public Request authenticate(Route route, Response response) {
        val request = response.request();
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        val authenticationToken = sharedPrefs.getString(ctx.getString(R.string.bambooAuthenticationToken), "");

        return request.newBuilder().header("Authorization", "Panda " + authenticationToken).build();
    }
}
