package app.bambushain;

import android.os.Bundle;
import android.util.Log;
import android.view.ViewTreeObserver;
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
import dagger.hilt.android.AndroidEntryPoint;
import lombok.val;

import javax.inject.Inject;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();

    public ActivityMainBinding binding;
    public NavigationViewModel headerViewModel;
    NavController navigator;
    @Inject
    BambooApi bambooApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        binding.setLifecycleOwner(this);

        val view = binding.getRoot();
        setContentView(view);
        val navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(binding.navHostFragment.getId());
        navigator = navHostFragment.getNavController();

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
                }, throwable -> {
                    Log.e(TAG, "loadProfile: Failed to load profile", throwable);
                    navigator.navigate(R.id.action_global_fragment_login);

                    view.getViewTreeObserver().removeOnPreDrawListener(preDrawListener);
                });

        NavigationUI.setupWithNavController(binding.navView, navigator);
    }
}