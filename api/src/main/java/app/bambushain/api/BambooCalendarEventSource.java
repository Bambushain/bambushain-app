package app.bambushain.api;

import android.content.Context;
import android.util.Log;
import app.bambushain.models.bamboo.Event;
import com.google.gson.Gson;
import com.launchdarkly.eventsource.ErrorStrategy;
import com.launchdarkly.eventsource.EventSource;
import com.launchdarkly.eventsource.HttpConnectStrategy;
import com.launchdarkly.eventsource.StreamException;
import dagger.hilt.android.qualifiers.ApplicationContext;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.observables.ConnectableObservable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.val;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class BambooCalendarEventSource {
    private static final String TAG = BambooCalendarEventSource.class.getName();
    @Inject
    OkHttpClient client;
    @Inject
    Gson gson;
    @ApplicationContext
    @Inject
    Context context;
    private boolean isConnected = false;
    private ConnectableObservable<CalendarEventAction> observable;
    private EventSource source;

    @Inject
    public BambooCalendarEventSource() {
    }

    public Observable<CalendarEventAction> start() throws StreamException {
        if (!isConnected) {
            val instance = context.getString(R.string.bambooInstance);
            val calendarEventSourceUrl = "https://" + instance + ".bambushain.app/sse/event";
            val connectStrategy = HttpConnectStrategy
                    .http(HttpUrl.get(calendarEventSourceUrl))
                    .httpClient(client);
            source = new EventSource
                    .Builder(connectStrategy)
                    .errorStrategy(ErrorStrategy.alwaysContinue())
                    .build();
            observable = Observable
                    .fromIterable(source.messages())
                    .map(messageEvent -> {
                        val name = messageEvent.getEventName();
                        Log.d(TAG, "onMessage: Got a new message " + name);
                        switch (name) {
                            case "created":
                                return new CalendarEventAction(Action.Created, gson.fromJson(messageEvent.getDataReader(), Event.class));
                            case "updated":
                                return new CalendarEventAction(Action.Updated, gson.fromJson(messageEvent.getDataReader(), Event.class));
                            case "deleted":
                                return new CalendarEventAction(Action.Deleted, gson.fromJson(messageEvent.getDataReader(), Event.class));
                        }

                        throw new Exception();
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.newThread())
                    .publish();
            source.start();
            observable.connect();
            isConnected = true;
        }

        return observable;
    }

    public void stop() {
        if (isConnected) {
            observable.reset();
            source.stop();
            source = null;
            observable = null;
            isConnected = false;
        }
    }

    public enum Action {
        Created,
        Updated,
        Deleted
    }

    @AllArgsConstructor
    @Data
    public static class CalendarEventAction {
        private Action action;
        private Event event;
    }
}
