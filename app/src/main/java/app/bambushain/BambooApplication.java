package app.bambushain;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import javax.inject.Inject;

import app.bambushain.notification.calendar.network.EventLoader;
import dagger.hilt.android.HiltAndroidApp;
import lombok.Getter;

@HiltAndroidApp
public class BambooApplication extends Application implements Application.ActivityLifecycleCallbacks {
    @Inject
    EventLoader eventLoader;

    @Getter
    private Activity currentActivity;

    @Override
    public void onCreate() {
        registerActivityLifecycleCallbacks(this);
        super.onCreate();
        eventLoader.fetchEvents();
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        currentActivity = activity;
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        currentActivity = activity;
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        currentActivity = activity;
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        currentActivity = null;
    }
}
