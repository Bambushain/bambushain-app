package app.bambushain.api;

import android.content.Context;
import androidx.preference.PreferenceManager;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import lombok.val;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import javax.inject.Singleton;
import java.util.concurrent.TimeUnit;

@Module
@InstallIn(SingletonComponent.class)
public class ApiModule {

    @Provides
    @Singleton
    OkHttpClient provideOkHttpBuilder(@ApplicationContext Context context) {
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);

        return new OkHttpClient.Builder()
                .addNetworkInterceptor(chain -> {
                    val authenticationToken = sharedPrefs.getString(context.getString(R.string.bambooAuthenticationToken), "");

                    return chain
                            .proceed(chain
                                    .request()
                                    .newBuilder()
                                    .header("Authorization", "Panda " + authenticationToken)
                                    .build());
                })
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .callTimeout(60, TimeUnit.SECONDS)
                .retryOnConnectionFailure(false)
                .build();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(@ApplicationContext Context context, OkHttpClient client, BambooCallAdapterFactory callAdapterFactory) {
        val instance = context.getString(R.string.bambooInstance);
        val baseUrl = "https://" + instance + ".bambushain.app/";

        return new Retrofit.Builder()
                .addCallAdapterFactory(callAdapterFactory)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl)
                .client(client)
                .build();
    }

    @Provides
    @Singleton
    public BambooApi provideBambooApi(Retrofit retrofit) {
        return retrofit.create(BambooApi.class);
    }
}
