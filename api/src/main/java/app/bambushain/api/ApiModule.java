package app.bambushain.api;

import android.content.Context;
import app.bambushain.api.authentication.PandaAuthenticator;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import lombok.val;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import javax.inject.Singleton;

@Module
@InstallIn(SingletonComponent.class)
public class ApiModule {

    @Provides
    @Singleton
    OkHttpClient provideOkhttpClient(PandaAuthenticator authenticator) {
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.authenticator(authenticator);

        return client.build();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(@ApplicationContext Context context, OkHttpClient okHttpClient) {
        val instance = context.getString(R.string.bambooInstance);
        val baseUrl = "https://" + instance + ".bambushain.app/";

        return new Retrofit.Builder()
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .build();
    }

    @Provides
    @Singleton
    public BambooApi provideBambooApi(Retrofit retrofit) {
        return retrofit.create(BambooApi.class);
    }
}
