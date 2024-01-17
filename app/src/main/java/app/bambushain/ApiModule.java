package app.bambushain;

import app.bambushain.api.BambooApi;
import app.bambushain.base.api.UnauthorizedCallAdapterFactory;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Retrofit;

import javax.inject.Singleton;

@Module
@InstallIn(SingletonComponent.class)
public class ApiModule {
    @Provides
    @Singleton
    public BambooApi providesBambooApi(Retrofit.Builder retrofit, UnauthorizedCallAdapterFactory callAdapterFactory) {
        return retrofit
                .addCallAdapterFactory(callAdapterFactory)
                .build()
                .create(BambooApi.class);
    }
}
