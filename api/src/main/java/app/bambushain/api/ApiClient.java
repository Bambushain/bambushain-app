package app.bambushain.api;

import android.content.Context;
import androidx.preference.PreferenceManager;
import app.bambushain.api.authentication.PandaAuthenticator;
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import lombok.NonNull;
import lombok.val;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ApiClient {

    private ApiClient() {
    }

    private static Retrofit createAdapter(@NonNull Context ctx) {
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        val instance = sharedPrefs.getString(ctx.getString(R.string.bambooInstance), "pandas");
        val baseUrl = "https://" + instance + ".bambushain.app/";
        val pandaAuthentication = new PandaAuthenticator(ctx);

        val builder = new Retrofit
                .Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder().authenticator(pandaAuthentication).build());

        return builder.build();
    }

    public static LoginApi getLoginApi(@NonNull Context ctx) {
        return createAdapter(ctx).create(LoginApi.class);
    }

    public static HainApi getBambushainApi(@NonNull Context ctx) {
        return createAdapter(ctx).create(HainApi.class);
    }
}
