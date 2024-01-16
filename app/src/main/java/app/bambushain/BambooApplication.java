package app.bambushain;

import android.app.Application;
import app.bambushain.notification.calendar.network.EventLoader;
import dagger.hilt.android.HiltAndroidApp;

import javax.inject.Inject;

@HiltAndroidApp
public class BambooApplication extends Application {
    @Inject
    EventLoader eventLoader;

    @Override
    public void onCreate() {
        super.onCreate();
        eventLoader.fetchEvents();
    }
}
