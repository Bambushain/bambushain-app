package app.bambushain.api;

import android.content.Context;
import androidx.preference.PreferenceManager;
import coil.ComponentRegistry;
import coil.ImageLoader;
import coil.decode.SvgDecoder;
import coil.request.CachePolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

@Module
@InstallIn(SingletonComponent.class)
public class ApiModule {

    @Provides
    @Singleton
    public OkHttpClient provideOkHttpBuilder(@ApplicationContext Context context) {
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
                .cache(null)
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .callTimeout(60, TimeUnit.SECONDS)
                .retryOnConnectionFailure(false)
                .build();
    }

    @Provides
    @Singleton
    Retrofit.Builder provideRetrofit(@ApplicationContext Context context, Gson gson, OkHttpClient client) {
        val instance = context.getString(R.string.bambooInstance);
        val baseUrl = "https://" + instance + ".bambushain.app/";

        return new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(baseUrl)
                .client(client);
    }

    @Provides
    @Singleton
    public Gson provideGson(LocalDateAdapter adapter) {
        return new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, adapter)
                .create();
    }

    @Provides
    @Singleton
    ImageLoader provideImageLoader(OkHttpClient client, @ApplicationContext Context context) {
        return new ImageLoader
                .Builder(context)
                .okHttpClient(client)
                .components(new ComponentRegistry
                        .Builder()
                        .add(new SvgDecoder.Factory())
                        .build())
                .memoryCachePolicy(CachePolicy.DISABLED)
                .diskCachePolicy(CachePolicy.DISABLED)
                .build();
    }

    @Provides
    @Singleton
    public ProfilePictureLoader provideProfilePictureLoader(ImageLoader imageLoader, @ApplicationContext Context context) {
        return new ProfilePictureLoader(imageLoader, context);
    }
}
