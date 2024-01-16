package app.bambushain;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import app.bambushain.api.BambooApi;
import app.bambushain.api.BambooCalendarEventSource;
import com.launchdarkly.eventsource.StreamException;
import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.schedulers.Schedulers;
import lombok.val;

import javax.inject.Inject;

@AndroidEntryPoint
public class EventNotificationService extends Service {
    private final static String TAG = EventNotificationService.class.getName();
    @Inject
    BambooApi bambooApi;
    @Inject
    BambooCalendarEventSource bambooCalendarEventSource;
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
                    updateNotification(getString(R.string.service_no_events));
                    Schedulers.io().scheduleDirect(() -> {
                        try {
                            Log.d(TAG, "startListening: Start the sse connection");
                            bambooCalendarEventSource.start().subscribe(calendarEventAction -> {
                                Log.d(TAG, "startListening: New event notification received " + calendarEventAction.getAction().name());
                            }, throwable -> {
                                Log.d(TAG, "startListening: Failure in receiving", throwable);
                            });
                        } catch (StreamException e) {
                            Log.e(TAG, "startListening: Failure in listening start", e);
                            stopSelf();
                        }
                    });
                }, throwable -> {
                    Log.i(TAG, "startListening: Login is not valid, wait for login action to trigger start", throwable);
                    updateNotification(getString(R.string.service_not_logged_in));
                });
    }

    private void updateNotification(String message) {
        val manager = getSystemService(NotificationManager.class);
        manager.notify(100, createNotification(message));
    }

    private void startService() {
        try {
            val powerManager = getSystemService(PowerManager.class);
            powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "EventNotificationService::lock").acquire(10 * 60 * 1000L /*10 minutes*/);

            startForeground(100, createNotification(getString(R.string.service_no_events)), ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC);
        } catch (Exception e) {
            Log.e(TAG, "startService: Failed to start service, no persistent notification is going to be displayed", e);
        }
    }

    private Notification createNotification(String message) {
        Log.d(TAG, "createNotification: Create the persistent notification");
        return new NotificationCompat
                .Builder(this, getString(R.string.service_notification_channel_id))
                .setContentTitle(getString(R.string.service_title))
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setChannelId(getString(R.string.service_notification_channel_id))
                .setOngoing(true)
                .setSilent(true)
                .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
                .build();
    }

    @Override
    public boolean stopService(Intent name) {
        Log.d(TAG, "stopService: Closing the sse connection");
        bambooCalendarEventSource.close();

        return super.stopService(name);
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

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}