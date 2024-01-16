package app.bambushain.notification.calendar;

import android.app.Service;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import androidx.work.Constraints;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import app.bambushain.R;
import app.bambushain.api.BambooApi;
import app.bambushain.notification.calendar.database.CleanupWorker;
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

    @Inject
    public EventNotificationService() {
    }

    private void startListening() {
        if (isListening) {
            Log.d(TAG, "startListening: Service is already listening just ignore this request");
            return;
        }

        Log.d(TAG, "startListening: Check if the auth token is valid");
        bambooApi
                .validateToken()
                .subscribe(() -> {
                    isListening = true;
                    Log.d(TAG, "startListening: Auth token is valid");
                    eventDao
                            .getEventsForDay()
                            .subscribe(events -> notifier.showNotificationForToday(events), throwable -> Log.e(TAG, "startListening: Failed to listen to database changes", throwable));
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

    private void startService() {
        try {
            val powerManager = getSystemService(PowerManager.class);
            powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "EventNotificationService::lock").acquire(10 * 60 * 1000L /*10 minutes*/);

            startForeground(notifier.notificationId, notifier.createNotification(getString(R.string.service_no_events)), ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC);
        } catch (Exception e) {
            Log.e(TAG, "startService: Failed to start service, no persistent notification is going to be displayed", e);
        }
    }

    private void enqueueCleanupWorker() {
        val workerConstraints = new Constraints.Builder()
                .setRequiresDeviceIdle(true)
                .build();
        val workRequest = new PeriodicWorkRequest
                .Builder(CleanupWorker.class, 7, TimeUnit.DAYS)
                .setConstraints(workerConstraints)
                .build();
        WorkManager
                .getInstance(this)
                .enqueue(workRequest);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: Received new intent");
        if (intent != null && intent.getAction().equals(getString(R.string.service_intent_startup))) {
            Log.d(TAG, "onStartCommand: Start the service");
            startService();
            Log.d(TAG, "onStartCommand: Start listening to the sse");
            startListening();
        } else if (intent != null && intent.getAction().equals(getString(R.string.service_intent_login_successful))) {
            Log.d(TAG, "onStartCommand: Start listening to the sse");
            startListening();
        }

        enqueueCleanupWorker();

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}