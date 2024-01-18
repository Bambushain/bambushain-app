package app.bambushain;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewTreeObserver;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;
import app.bambushain.api.BambooApi;
import app.bambushain.databinding.ActivityMainBinding;
import app.bambushain.databinding.HeaderNavigationDrawerBinding;
import app.bambushain.navigation.NavigationViewModel;
import app.bambushain.notification.calendar.EventNotificationService;
import app.bambushain.notification.calendar.database.EventDao;
import dagger.hilt.android.AndroidEntryPoint;
import lombok.val;

import javax.inject.Inject;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();

    public ActivityMainBinding binding;
    public NavigationViewModel headerViewModel;
    public NavController navigator;
    @Inject
    BambooApi bambooApi;
    @Inject
    EventDao eventDao;

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    Log.d(TAG, "requestPermissionLauncher: Permission for notifications was granted");
                } else {
                    Log.d(TAG, "requestPermissionLauncher: Permission for notifications was rejected");
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        binding.setLifecycleOwner(this);

        val view = binding.getRoot();
        setContentView(view);
        val navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(binding.navHostFragment.getId());
        navigator = navHostFragment.getNavController();

        val notificationManager = getSystemService(NotificationManager.class);
        val channel = new NotificationChannel(
                getString(R.string.service_notification_channel_id),
                getString(R.string.service_notification_channel_name),
                NotificationManager.IMPORTANCE_DEFAULT
        );
        channel.setDescription(getString(R.string.service_notification_channel_description));
        channel.enableLights(false);
        channel.enableVibration(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            channel.setBlockable(false);
        }
        notificationManager.createNotificationChannel(channel);

        if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Log.d(TAG, "onCreate: Request permission for notifications");
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }

        val startServiceIntent = new Intent(this, EventNotificationService.class);
        startServiceIntent.setAction(getString(R.string.service_intent_startup));

        startForegroundService(startServiceIntent);

        val preDrawListener = new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                return false;
            }
        };
        view.getViewTreeObserver().addOnPreDrawListener(preDrawListener);

        val headerBinding = HeaderNavigationDrawerBinding.bind(binding.navView.getHeaderView(0));
        headerViewModel = new ViewModelProvider(this).get(NavigationViewModel.class);
        headerBinding.setViewModel(headerViewModel);
        headerBinding.setLifecycleOwner(this);

        navigator.addOnDestinationChangedListener((controller, destination, arguments) -> {
            binding.navView.setCheckedItem(destination.getId());
        });
        headerBinding.actionLogout.setOnClickListener(v -> {
            bambooApi.logout();

            val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
            sharedPrefs
                    .edit()
                    .remove(getString(app.bambushain.api.R.string.bambooAuthenticationToken))
                    .apply();

            binding.drawerLayout.closeDrawers();

            eventDao.cleanDatabase();
            val logoutServiceIntent = new Intent(this, EventNotificationService.class);
            logoutServiceIntent.setAction(getString(R.string.service_intent_stop_listening));

            startForegroundService(logoutServiceIntent);
            navigator.navigate(R.id.action_global_fragment_login);
        });

        bambooApi
                .getMyProfile()
                .subscribe(profile -> {
                    headerViewModel.email.setValue(profile.getEmail());
                    headerViewModel.displayName.setValue(profile.getDisplayName());
                    headerViewModel.discordName.setValue(profile.getDiscordName());
                    headerViewModel.id.setValue(profile.getId());
                    headerViewModel.isMod.setValue(profile.getIsMod());
                    view.getViewTreeObserver().removeOnPreDrawListener(preDrawListener);

                    val startListeningIntent = new Intent(this, EventNotificationService.class);
                    startListeningIntent.setAction(getString(R.string.service_intent_start_listening));
                    startForegroundService(startListeningIntent);
                }, throwable -> {
                    Log.e(TAG, "loadProfile: Failed to load profile", throwable);
                    navigator.navigate(R.id.action_global_fragment_login);

                    view.getViewTreeObserver().removeOnPreDrawListener(preDrawListener);
                });

        NavigationUI.setupWithNavController(binding.navView, navigator);
    }
}