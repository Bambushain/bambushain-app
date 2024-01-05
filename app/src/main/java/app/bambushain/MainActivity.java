package app.bambushain;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import app.bambushain.api.BambooApi;
import app.bambushain.databinding.ActivityMainBinding;
import app.bambushain.databinding.HeaderNavigationDrawerBinding;
import app.bambushain.models.User;
import app.bambushain.navigation.NavigationViewModel;
import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import lombok.val;

import javax.inject.Inject;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();

    ActivityMainBinding binding;
    NavController navigator;

    NavigationViewModel headerViewModel;

    @Inject
    BambooApi bambooApi;

    Observable<User> loadProfile() {
        val apiCall = bambooApi
                .getMyProfile()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
        apiCall
                .subscribe(profile -> {
                    headerViewModel.profile.setValue(profile);
                }, ex -> {
                    Log.e(TAG, "onCreate: Failed to load profile", ex);
                    navigator.navigate(R.id.action_global_fragment_login);
                });

        return apiCall;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        binding.setLifecycleOwner(this);

        val view = binding.getRoot();
        setContentView(view);
        val navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(binding.navHostFragment.getId());
        navigator = navHostFragment.getNavController();
        view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                loadProfile()
                        .subscribe(profile -> {
                            view.getViewTreeObserver().removeOnPreDrawListener(this);
                        }, ex -> {
                            view.getViewTreeObserver().removeOnPreDrawListener(this);
                        });

                return false;
            }
        });

        val headerBinding = HeaderNavigationDrawerBinding.bind(binding.navView.getHeaderView(0));
        headerViewModel = new ViewModelProvider(this).get(NavigationViewModel.class);
        headerBinding.setViewModel(headerViewModel);
        headerBinding.setLifecycleOwner(this);

        val appBarConfiguration = new AppBarConfiguration.Builder(R.id.fragment_login, R.id.fragment_event_calendar, R.id.fragment_pandas, R.id.fragment_profile)
                .setDrawerLayout(binding.drawerLayout)
                .build();

        navigator.addOnDestinationChangedListener((controller, destination, arguments) -> {
            var showAppBar = true;
            if (destination.getId() != R.id.fragment_login) {
                loadProfile();
            } else {
                binding.drawerLayout.closeDrawers();
            }
            if (arguments != null) {
                showAppBar = arguments.getBoolean("ShowAppBar", true);
            }
            if (showAppBar) {
                binding.toolbar.setVisibility(View.VISIBLE);
            } else {
                binding.toolbar.setVisibility(View.GONE);
            }
            binding.navView.setCheckedItem(destination.getId());
        });
        headerViewModel.profile.observe(this, user -> {
            if (user == null) {
                navigator.navigate(R.id.action_global_fragment_login);
            }
        });

        NavigationUI.setupWithNavController(binding.toolbar, navigator, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navigator);
    }
}