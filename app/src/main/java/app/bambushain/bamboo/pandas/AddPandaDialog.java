package app.bambushain.bamboo.pandas;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

import javax.inject.Inject;

import app.bambushain.R;
import app.bambushain.api.BambooApi;
import app.bambushain.base.BindingDialogFragment;
import app.bambushain.databinding.FragmentAddPandaDialogBinding;
import app.bambushain.models.bamboo.User;
import app.bambushain.models.exception.BambooException;
import app.bambushain.models.exception.ErrorType;
import dagger.hilt.android.AndroidEntryPoint;
import lombok.val;

@AndroidEntryPoint
public class AddPandaDialog extends BindingDialogFragment<FragmentAddPandaDialogBinding> {

    private static final String TAG = EditPandaDialog.class.getName();
    @Inject
    BambooApi bambooApi;

    PandaViewModel viewModel;
    Snackbar snackbar;

    @Inject
    public AddPandaDialog() {
    }

    @Override
    protected FragmentAddPandaDialogBinding getViewBinding() {
        return FragmentAddPandaDialogBinding.inflate(getLayoutInflater());
    }

    void saveUser() {
        val user = new User(0, viewModel.displayName.getValue(), viewModel.email.getValue(),
                viewModel.isMod.getValue(), viewModel.discordName.getValue(), viewModel.appTotpEnabled.getValue());
        Log.d(TAG, "createUser: create user " + user);
        viewModel.isLoading.setValue(true);
        //noinspection ResultOfMethodCallIgnored
        bambooApi
                .createUser(user)
                .subscribe(
                        panda -> {
                            Log.d(TAG, "createUser: create user successful");
                            Toast
                                    .makeText(requireContext(), R.string.success_create_panda, Toast.LENGTH_LONG)
                                    .show();
                            val stateHandle = Objects.requireNonNull(navigator.getPreviousBackStackEntry()).getSavedStateHandle();
                            stateHandle.set("createdUser", user);
                            navigator.popBackStack();
                        }, ex -> {
                            Log.e(TAG, "createUser: create user failed", ex);
                            val bambooEx = (BambooException) ex;
                            var message = R.string.error_panda_create_failed;
                            if (bambooEx.getErrorType() == ErrorType.ExistsAlready) {
                                message = R.string.error_panda_create_exists;
                            }
                            if (snackbar == null) {
                                snackbar = Snackbar.make(binding.layout, message, Snackbar.LENGTH_INDEFINITE);
                            }
                            snackbar
                                    .setText(message)
                                    .setBackgroundTint(getColor(R.color.md_theme_error))
                                    .setTextColor(getColor(R.color.md_theme_onError))
                                    .show();
                            viewModel.isLoading.setValue(false);
                        }
                );
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(PandaViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.actionAddPanda.setOnClickListener(v -> {
            if (Boolean.TRUE.equals(viewModel.isDiscordNameValid.getValue()) && Boolean.TRUE.equals(viewModel.isDisplayNameValid.getValue()) && Boolean.TRUE.equals(viewModel.isEmailValid.getValue())) {
                saveUser();
            }
        });

        setObservers();
    }

    private void setObservers() {
        viewModel.email.observe(getViewLifecycleOwner(), value -> {
            if (snackbar != null && snackbar.isShown()) {
                snackbar.dismiss();
            }
            if (value == null || value.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
                binding.profileEmail.setError(getString(R.string.error_panda_email_invalid));
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
                binding.profileDiscordName.setError(getString(R.string.error_panda_discord_too_short));
                viewModel.isDiscordNameValid.setValue(false);
            } else if (value != null && !value.isEmpty() && value.length() > 32) {
                binding.profileDiscordName.setError(getString(R.string.error_panda_discord_too_long));
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
                binding.profileDisplayName.setError(getString(R.string.error_panda_name_required));
                viewModel.isDisplayNameValid.setValue(false);
            } else {
                binding.profileDisplayName.setError(null);
                viewModel.isDisplayNameValid.setValue(true);
            }
        });
    }
}