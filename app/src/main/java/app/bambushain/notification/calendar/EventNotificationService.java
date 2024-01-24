package app.bambushain.notification.calendar;

import android.app.Service;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import app.bambushain.R;
import app.bambushain.api.BambooApi;
import app.bambushain.notification.calendar.database.EventDao;
import app.bambushain.notification.calendar.network.EventSubscriber;
import com.launchdarkly.eventsource.StreamException;
import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.schedulers.Schedulers;
import lombok.val;

import javax.inject.Inject;
import java.util.concurrent.TimeUnit;

@AndroidEntryPoint
public class EventNotificationService extends Service {
    private final static String TAG = EventNotificationService.class.getName();
    @Inject
    BambooApi bambooApi;
    @Inject
    EventSubscriber eventListener;
    @Inject
    Notifier notifier;
    @Inject
    EventDao eventDao;

    private boolean isListening = false;
    private PowerManager.WakeLock wakeLock;

    @Inject
    public EventNotificationService() {
    }

    private void startListening() {
        if (isListening) {
            Log.d(TAG, "startListening: Service is already listening just ignore this request");
            return;
        }

        Log.d(TAG, "startListening: Check if the auth token is valid");
        //noinspection ResultOfMethodCallIgnored
        bambooApi
                .validateToken()
                .subscribe(() -> {
                    isListening = true;
                    Log.d(TAG, "startListening: Auth token is valid");
                    //noinspection ResultOfMethodCallIgnored
                    eventDao
                            .getEventsForDay()
                            .subscribe(notifier::showNotificationForToday, throwable -> Log.e(TAG, "startListening: Failed to listen to database changes", throwable));
                    Schedulers.io().scheduleDirect(() -> {
                        try {
                            eventListener.subscribeToEventChanges();
                        } catch (StreamException e) {
                            Log.e(TAG, "startListening: Failure in listening start", e);
                            stopSelf();
                        }
                    });
                }, throwable -> {
                    Log.i(TAG, "startListening: Login is not valid, wait for login action to trigger start", throwable);
                    notifier.updateNotification(getString(R.string.service_not_logged_in));
                });
    }

    private void stopListening() {
        Log.d(TAG, "stopListening: Stop the listening and close connection");
        isListening = false;
        eventListener.unsubscribeFromEventChanges();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        wakeLock.release();
    }

    private void startService() {
        try {
            val powerManager = getSystemService(PowerManager.class);
            wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "EventNotificationService::lock");
            wakeLock.acquire();

            startForeground(notifier.notificationId, notifier.createNotification(getString(R.string.service_no_events)), ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC);

            Log.d(TAG, "startService: Start listening to the sse");
            startListening();
        } catch (Exception e) {
            Log.e(TAG, "startService: Failed to start service, no persistent notification is going to be displayed", e);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: Received new intent");
        if (intent != null && intent.getAction().equals(getString(R.string.service_intent_startup))) {
            Log.d(TAG, "service_intent_startup: Start the service");
            startService();
        } else if (intent != null && intent.getAction().equals(getString(R.string.service_intent_start_listening))) {
            Log.d(TAG, "service_intent_login_successful: Start listening to the sse");
            startListening();
        } else if (intent != null && intent.getAction().equals(getString(R.string.service_intent_stop_listening))) {
            Log.d(TAG, "service_intent_stop_listening: Stop listening");
            notifier.updateNotification(getString(R.string.service_not_logged_in));
            stopListening();
        } else if (intent != null && intent.getAction().equals(getString(R.string.service_intent_cleanup))) {
            Log.d(TAG, "service_intent_cleanup: Enqueue delete for events older than today");
            cleanupDatabase();
        } else if (intent != null && intent.getAction().equals(getString(R.string.service_intent_update_today))) {
            Log.d(TAG, "service_intent_update_today: Force update of the notification for the current day");
            updateToday();
        }

        return START_STICKY;
    }

    private void cleanupDatabase() {
        //noinspection ResultOfMethodCallIgnored
        eventDao
                .deleteEventsBeforeToday()
                .delay(1, TimeUnit.HOURS)
                .subscribe(() -> Log.d(TAG, "service_intent_cleanup: Successful cleanup done"), throwable -> Log.e(TAG, "service_intent_cleanup: Failed to run cleanup", throwable));
    }

    private void updateToday() {
        //noinspection ResultOfMethodCallIgnored
        bambooApi
                .validateToken()
                .subscribe(() -> {
                    Log.d(TAG, "updateToday: Auth token is valid");
                    //noinspection ResultOfMethodCallIgnored
                    eventDao
                            .getEventsForDay()
                            .subscribe(notifier::showNotificationForToday, throwable -> Log.e(TAG, "updateToday: Failed to listen to database changes", throwable));
                }, throwable -> Log.e(TAG, "updateToday: Failed to update today", throwable));
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
