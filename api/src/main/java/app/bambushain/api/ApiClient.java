package app.bambushain.api;

import app.bambushain.api.authentication.PandaAuthenticator;
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import lombok.*;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ApiClient {

    private ApiClient() {
    }

    private static Retrofit createAdapter(@NonNull String instance) {
        return createAdapter(instance, null);
    }

    private static Retrofit createAdapter(@NonNull String instance, String authenticationToken) {
        val baseUrl = "https://" + instance + ".bambushain.app/";
        var builder = new Retrofit
                .Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create());

        if (authenticationToken != null) {
            val pandaAuthentication = new PandaAuthenticator(authenticationToken);

            builder = builder.client(new OkHttpClient.Builder().authenticator(pandaAuthentication).build());
        }

        return builder.build();
    }

    public static LoginApi getLoginApi(String instance) {
        return createAdapter(instance).create(LoginApi.class);
    }

    public static HainApi getBambushainApi(String instance, String authToken) {
        return createAdapter(instance, authToken).create(HainApi.class);
    }
}
