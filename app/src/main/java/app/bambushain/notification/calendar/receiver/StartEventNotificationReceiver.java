package app.bambushain.notification.calendar.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import app.bambushain.R;
import app.bambushain.notification.calendar.EventNotificationService;
import lombok.val;

public class StartEventNotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            val startServiceIntent = new Intent(context, EventNotificationService.class);
            startServiceIntent.setAction(context.getString(R.string.service_intent_startup));
            context.startForegroundService(startServiceIntent);
        }
    }
}
