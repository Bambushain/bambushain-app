package app.bambushain.notification.calendar.network;

import android.util.Log;
import app.bambushain.api.BambooApi;
import app.bambushain.notification.calendar.database.Event;
import app.bambushain.notification.calendar.database.EventDao;
import io.reactivex.rxjava3.schedulers.Schedulers;
import lombok.val;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.LocalDate;
import java.util.stream.Collectors;

@Singleton
public class EventLoader {
    private final static String TAG = EventLoader.class.getName();
    @Inject
    EventDao eventDao;
    @Inject
    BambooApi bambooApi;

    @Inject
    public EventLoader() {
    }

    public void fetchEvents() {
        bambooApi
                .getEvents(LocalDate.now(), LocalDate.now().plusYears(1))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(events -> {
                    Log.d(TAG, "fetchEvents: Loaded all events from today to the next year");
                    val evts = events
                            .stream()
                            .map(Event::fromEvent)
                            .collect(Collectors.toList());
                    eventDao.cleanDatabase().subscribe(() -> eventDao
                            .createOrUpdateEvents(evts)
                            .subscribe(() -> Log.d(TAG, "fetchEvents: Successfully fetched events"), throwable -> Log.e(TAG, "fetchEvents: Error fetching events", throwable)), throwable -> Log.e(TAG, "fetchEvents: Error cleaning database", throwable));
                }, throwable -> {
                    Log.e(TAG, "fetchEvents: Failed to load events for the next year", throwable);
                });
    }
}
