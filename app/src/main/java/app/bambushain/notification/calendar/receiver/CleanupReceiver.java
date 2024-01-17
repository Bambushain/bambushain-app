package app.bambushain.notification.calendar.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import app.bambushain.R;
import app.bambushain.notification.calendar.EventNotificationService;
import dagger.hilt.android.AndroidEntryPoint;
import lombok.val;

@AndroidEntryPoint
public class CleanupReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_DATE_CHANGED)) {
            val startServiceIntent = new Intent(context, EventNotificationService.class);
            startServiceIntent.setAction(context.getString(R.string.service_intent_cleanup));
            context.startForegroundService(startServiceIntent);
        }
    }
}