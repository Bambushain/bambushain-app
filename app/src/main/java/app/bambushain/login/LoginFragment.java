package app.bambushain.login;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import app.bambushain.R;
import app.bambushain.base.BindingFragment;
import app.bambushain.databinding.FragmentLoginBinding;
import com.google.android.material.snackbar.Snackbar;
import dagger.hilt.android.AndroidEntryPoint;
import dagger.hilt.android.qualifiers.ActivityContext;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

@AndroidEntryPoint
public class LoginFragment extends BindingFragment<FragmentLoginBinding> {
    LoginViewModel viewModel;

    @ActivityContext
    @Inject
    Context context;

    @Inject
    public LoginFragment() {
    }

    @Override
    protected FragmentLoginBinding getViewBinding() {
        return FragmentLoginBinding.inflate(getLayoutInflater());
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        viewModel.errorMessage.observe(getViewLifecycleOwner(), value -> {
            if (value != 0) {
                Snackbar
                        .make(binding.layout, value, Snackbar.LENGTH_LONG)
                        .setBackgroundTint(getColor(R.color.md_theme_error))
                        .setTextColor(getColor(R.color.md_theme_onError))
                        .show();
                viewModel.errorMessage.setValue(0);
            }
        });
        viewModel.apiToken.observe(getViewLifecycleOwner(), value -> {
            if (value != null && !value.isEmpty()) {
                val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                sharedPrefs
                        .edit()
                        .putString(getString(app.bambushain.api.R.string.bambooAuthenticationToken), value)
                        .apply();
                navigator.navigate(R.id.action_fragment_login_to_fragment_event_calendar);
            }
        });
    }
}