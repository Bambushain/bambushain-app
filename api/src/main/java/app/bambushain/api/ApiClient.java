package app.bambushain.api;

import android.content.Context;
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
        val instance = ctx.getString(R.string.bambooInstance);
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

    public static BambooApi getBambushainApi(@NonNull Context ctx) {
        return createAdapter(ctx).create(BambooApi.class);
    }
}
