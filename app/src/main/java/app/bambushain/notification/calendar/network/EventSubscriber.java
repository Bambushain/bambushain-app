package app.bambushain.notification.calendar.network;

import android.util.Log;
import app.bambushain.api.BambooCalendarEventSource;
import app.bambushain.notification.calendar.Notifier;
import app.bambushain.notification.calendar.database.Event;
import app.bambushain.notification.calendar.database.EventDao;
import com.launchdarkly.eventsource.StreamException;
import io.reactivex.rxjava3.schedulers.Schedulers;
import lombok.val;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.LocalDate;

@Singleton
public class EventSubscriber {
    private final static String TAG = EventSubscriber.class.getName();

    @Inject
    BambooCalendarEventSource bambooCalendarEventSource;
    @Inject
    EventDao eventDao;
    @Inject
    Notifier notifier;

    @Inject
    public EventSubscriber() {
    }

    public void subscribeToEventChanges() throws StreamException {
        Log.d(TAG, "startListening: Start the sse connection");
        bambooCalendarEventSource
                .start()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(this::handleEvent, throwable -> Log.d(TAG, "startListening: Failure in receiving", throwable));
    }

    private void handleEvent(BambooCalendarEventSource.CalendarEventAction calendarEventAction) {
        Log.d(TAG, "startListening: New event notification received " + calendarEventAction.getAction().name());
        val event = Event.fromEvent(calendarEventAction.getEvent());
        val today = LocalDate.now().toEpochDay();
        switch (calendarEventAction.getAction()) {
            case Created:
                if (LocalDate.parse(event.startDate).toEpochDay() <= today && LocalDate.parse(event.endDate).toEpochDay() >= today) {
                    eventDao
                            .upsertEvent(event)
                            .subscribe(() -> Log.d(TAG, "handleEvent: Successfully created new event " + event.title), throwable -> Log.e(TAG, "handleEvent: Failed to create event " + event.title, throwable));
                }
                break;
            case Updated:
                if (LocalDate.parse(event.endDate).toEpochDay() < today) {
                    eventDao
                            .deleteEvent(event)
                            .subscribe(() -> Log.d(TAG, "handleEvent: Successfully deleted event that ends in the past"), throwable -> Log.e(TAG, "handleEvent: Failed to delete event " + event.title, throwable));
                } else {
                    eventDao
                            .upsertEvent(event)
                            .subscribe(() -> Log.d(TAG, "handleEvent: Successfully updated the event " + event.title), throwable -> Log.e(TAG, "handleEvent: Failed to update event " + event.title, throwable));
                }
                break;
            case Deleted:
                eventDao
                        .deleteEvent(event)
                        .subscribe(() -> Log.d(TAG, "handleEvent: Successfully deleted event " + event.title), throwable -> Log.e(TAG, "handleEvent: Failed to delete event " + event.title, throwable));
                break;
        }
    }

    public void unsubscribeFromEventChanges() {
        bambooCalendarEventSource
                .stop();
    }
}
