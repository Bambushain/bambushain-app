package app.bambushain.api;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.inject.Singleton;

import app.bambushain.models.exception.BambooException;
import app.bambushain.models.exception.ErrorType;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import lombok.AllArgsConstructor;
import lombok.val;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.HttpException;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class BambooCallAdapterFactory extends CallAdapter.Factory {
    final RxJava3CallAdapterFactory original;

    public BambooCallAdapterFactory() {
        original = RxJava3CallAdapterFactory.create();
    }

    static BambooCallAdapterFactory create() {
        return new BambooCallAdapterFactory();
    }

    @Nullable
    @Override
    public CallAdapter<?, ?> get(@NotNull Type returnType, @NotNull Annotation[] annotations, @NotNull Retrofit retrofit) {
        //noinspection rawtypes
        return new RxCallAdapterWrapper(original.get(returnType, annotations, retrofit));
    }

    @Provides
    @Singleton
    public BambooCallAdapterFactory provideBambooErrorHandlingCallAdapterFactory() {
        return BambooCallAdapterFactory.create();
    }

    @AllArgsConstructor
    private static final class RxCallAdapterWrapper<R> implements CallAdapter<R, Object> {
        private CallAdapter<R, Object> wrapped;

        @NotNull
        @Override
        public Type responseType() {
            return wrapped.responseType();
        }

        @NotNull
        @Override
        public Object adapt(@NotNull Call<R> call) {
            val wrappedResult = wrapped.adapt(call);
            if (wrappedResult instanceof Completable) {
                //noinspection ReactiveStreamsUnusedPublisher
                return ((Completable) wrappedResult)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.newThread())
                        .onErrorResumeNext(throwable -> Completable.error(asBambooException(throwable)));
            } else if (wrappedResult instanceof Observable<?>) {
                //noinspection ReactiveStreamsUnusedPublisher
                return ((Observable<?>) wrappedResult)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.newThread())
                        .onErrorResumeNext(throwable -> Observable.error(asBambooException(throwable)));
            }

            return wrappedResult;
        }

        private BambooException asBambooException(Throwable throwable) {
            if (throwable instanceof HttpException httpException) {
                val response = httpException.response();
                assert response != null;
                assert response.errorBody() != null;
                return new Gson().fromJson(response.errorBody().charStream(), BambooException.class);
            }
            if (throwable instanceof IOException) {
                return new BambooException(ErrorType.Network, "", throwable.getMessage());
            }

            return new BambooException(throwable);
        }
    }
}
