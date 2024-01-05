package app.bambushain.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.viewbinding.ViewBinding;
import app.bambushain.MainActivity;
import app.bambushain.R;
import app.bambushain.login.LoginFragment;
import lombok.val;

public abstract class BindingFragment<T extends ViewBinding> extends Fragment {
    protected T binding;
    protected NavController navigator;
    protected MainActivity activity;
    protected Toolbar toolbar;

    protected abstract T getViewBinding();

    AppBarConfiguration getAppBarConfiguration() {
        return new AppBarConfiguration.Builder(R.id.fragment_login, R.id.fragment_event_calendar, R.id.fragment_pandas, R.id.fragment_profile)
                .setDrawerLayout(activity.binding.drawerLayout)
                .build();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = getViewBinding();
        navigator = NavHostFragment.findNavController(this);
        activity = (MainActivity) getActivity();
        val root = binding.getRoot();
        toolbar = root.findViewById(R.id.toolbar);
        if (toolbar != null && !(this instanceof LoginFragment)) {
            NavigationUI.setupWithNavController(toolbar, navigator, getAppBarConfiguration());
        }

        return root;
    }
}
