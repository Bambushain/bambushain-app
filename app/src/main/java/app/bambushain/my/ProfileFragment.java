package app.bambushain.my;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import app.bambushain.R;
import app.bambushain.base.BindingFragment;
import app.bambushain.databinding.FragmentProfileBinding;
import com.google.android.material.snackbar.Snackbar;
import dagger.hilt.android.AndroidEntryPoint;

import javax.inject.Inject;

@AndroidEntryPoint
public class ProfileFragment extends BindingFragment<FragmentProfileBinding> {

    ProfileViewModel viewModel;
    Snackbar snackbar;

    @Inject
    public ProfileFragment() {
    }

    @Override
    protected FragmentProfileBinding getViewBinding() {
        return FragmentProfileBinding.inflate(getLayoutInflater());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        viewModel.errorMessage.observe(getViewLifecycleOwner(), value -> {
            if (value != 0) {
                if (snackbar == null) {
                    snackbar = Snackbar.make(binding.layout, value, Snackbar.LENGTH_INDEFINITE);
                }
                snackbar
                        .setText(value)
                        .setBackgroundTint(getColor(R.color.md_theme_error))
                        .setTextColor(getColor(R.color.md_theme_onError))
                        .show();
                viewModel.errorMessage.setValue(0);
            }
        });
        viewModel.successMessage.observe(getViewLifecycleOwner(), value -> {
            if (value != 0) {
                Toast
                        .makeText(getContext(), value, Toast.LENGTH_LONG)
                        .show();
                activity.updateHeader();
                viewModel.successMessage.setValue(0);
                if (snackbar != null && snackbar.isShown()) {
                    snackbar.dismiss();
                }
            }
        });
        viewModel.email.observe(getViewLifecycleOwner(), value -> {
            if (snackbar != null && snackbar.isShown()) {
                snackbar.dismiss();
            }
            if (value == null || value.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
                binding.profileEmail.setError(getString(R.string.error_profile_email_invalid));
                viewModel.isEmailValid.setValue(false);
            } else {
                binding.profileEmail.setError(null);
                viewModel.isEmailValid.setValue(true);
            }
        });
        viewModel.discordName.observe(getViewLifecycleOwner(), value -> {
            if (snackbar != null && snackbar.isShown()) {
                snackbar.dismiss();
            }
            if (value != null && !value.isEmpty() && value.length() < 3) {
                binding.profileDiscordName.setError(getString(R.string.error_profile_discord_too_short));
                viewModel.isDiscordNameValid.setValue(false);
            } else if (value != null && !value.isEmpty() && value.length() > 32) {
                binding.profileDiscordName.setError(getString(R.string.error_profile_discord_too_long));
                viewModel.isDiscordNameValid.setValue(false);
            } else {
                binding.profileDiscordName.setError(null);
                viewModel.isDiscordNameValid.setValue(true);
            }
        });
        viewModel.displayName.observe(getViewLifecycleOwner(), value -> {
            if (snackbar != null && snackbar.isShown()) {
                snackbar.dismiss();
            }
            if (value == null || value.isEmpty()) {
                binding.profileDisplayName.setError(getString(R.string.error_profile_name_required));
                viewModel.isDisplayNameValid.setValue(false);
            } else {
                binding.profileDisplayName.setError(null);
                viewModel.isDisplayNameValid.setValue(true);
            }
        });
        viewModel.loadProfile();
    }
}