package app.bambushain.base.api;

import android.content.Context;
import android.content.Intent;
import app.bambushain.BambooApplication;
import app.bambushain.MainActivity;
import app.bambushain.api.BambooCallAdapterFactory;
import app.bambushain.models.exception.BambooException;
import app.bambushain.models.exception.ErrorType;
import app.bambushain.notification.calendar.EventNotificationService;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import lombok.AllArgsConstructor;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.HttpException;
import retrofit2.Retrofit;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

@Module
@InstallIn(SingletonComponent.class)
public class UnauthorizedCallAdapterFactory extends CallAdapter.Factory {
    BambooCallAdapterFactory original;
    Context context;

    @Inject
    public UnauthorizedCallAdapterFactory() {
    }

    public UnauthorizedCallAdapterFactory(BambooCallAdapterFactory callAdapterFactory, Context context) {
        original = callAdapterFactory;
        this.context = context;
    }

    static UnauthorizedCallAdapterFactory create(BambooCallAdapterFactory callAdapterFactory, Context context) {
        return new UnauthorizedCallAdapterFactory(callAdapterFactory, context);
    }

    MainActivity getMainActivity() {
        val app = (BambooApplication) context.getApplicationContext();
        return (MainActivity) app.getCurrentActivity();
    }

    @Nullable
    @Override
    public CallAdapter<?, ?> get(@NotNull Type returnType, @NotNull Annotation @NotNull [] annotations, @NotNull Retrofit retrofit) {
        //noinspection rawtypes
        return new UnauthorizedCallAdapterWrapper(original.get(returnType, annotations, retrofit));
    }

    @Provides
    @Singleton
    public UnauthorizedCallAdapterFactory provideBambooErrorHandlingCallAdapterFactory(BambooCallAdapterFactory callAdapterFactory, @ApplicationContext Context context) {
        return UnauthorizedCallAdapterFactory.create(callAdapterFactory, context);
    }

    @AllArgsConstructor
    private final class UnauthorizedCallAdapterWrapper<R> implements CallAdapter<R, Object> {
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
                        .onErrorComplete(this::handleError);
            } else if (wrappedResult instanceof Observable<?>) {
                //noinspection ReactiveStreamsUnusedPublisher
                return ((Observable<?>) wrappedResult)
                        .onErrorResumeNext(throwable -> {
                            handleError(throwable);

                            return Observable.error(throwable);
                        });
            }

            return wrappedResult;
        }

        private boolean handleError(Throwable throwable) {
            if ("Login data is invalid".equals(throwable.getMessage())) {
                return false;
            }

            if ((throwable instanceof BambooException && ((BambooException) throwable).getErrorType() == ErrorType.Unauthorized) || (throwable instanceof HttpException && ((HttpException) throwable).code() == 401)) {
                getMainActivity().navigator.navigate(app.bambushain.R.id.action_global_fragment_login);
                val logoutServiceIntent = new Intent(getMainActivity(), EventNotificationService.class);
                logoutServiceIntent.setAction(getMainActivity().getString(app.bambushain.R.string.service_intent_stop_listening));

                getMainActivity().startForegroundService(logoutServiceIntent);

                return true;
            }

            return false;
        }
    }
}
