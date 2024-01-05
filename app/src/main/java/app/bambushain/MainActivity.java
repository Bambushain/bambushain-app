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

    public ActivityMainBinding binding;
    NavController navigator;

    public NavigationViewModel headerViewModel;

    boolean preDrawTriggered = false;

    @Inject
    BambooApi bambooApi;

    public void updateHeader() {
        bambooApi
                .getMyProfile()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(profile -> {
                    headerViewModel.profile.setValue(profile);
                }, ex -> {
                    Log.e(TAG, "loadProfile: Failed to load profile", ex);
                    navigator.navigate(R.id.action_global_fragment_login);
                });
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

        val preDrawListener = new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (!preDrawTriggered) {
                    bambooApi
                            .getMyProfile()
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(profile -> {
                                headerViewModel.profile.setValue(profile);
                                view.getViewTreeObserver().removeOnPreDrawListener(this);
                            }, ex -> {
                                Log.e(TAG, "loadProfile: Failed to load profile", ex);
                                navigator.navigate(R.id.action_global_fragment_login);
                            });
                    preDrawTriggered = true;
                }

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
            if (destination.getId() == R.id.fragment_login) {
                view.getViewTreeObserver().removeOnPreDrawListener(preDrawListener);
            }
        });
        headerViewModel.profile.observe(this, profile -> {
            if (profile == null) {
                updateHeader();
                binding.drawerLayout.closeDrawers();
            }
        });

        NavigationUI.setupWithNavController(binding.navView, navigator);
    }
}