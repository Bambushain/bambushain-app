package app.bambushain.bamboo.pandas;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import app.bambushain.R;
import app.bambushain.api.BambooApi;
import app.bambushain.base.BindingDialogFragment;
import app.bambushain.databinding.FragmentEditPandaDialogBinding;
import app.bambushain.models.exception.BambooException;
import app.bambushain.models.exception.ErrorType;
import app.bambushain.models.pandas.UpdateUserProfile;
import com.google.android.material.snackbar.Snackbar;
import dagger.hilt.android.AndroidEntryPoint;
import lombok.val;

import javax.inject.Inject;

@AndroidEntryPoint
public class EditPandaDialog extends BindingDialogFragment<FragmentEditPandaDialogBinding> {
    private static final String TAG = EditPandaDialog.class.getName();
    @Inject
    BambooApi bambooApi;

    PandaViewModel viewModel;
    Snackbar snackbar;

    @Inject
    public EditPandaDialog() {
    }

    @Override
    protected FragmentEditPandaDialogBinding getViewBinding() {
        return FragmentEditPandaDialogBinding.inflate(getLayoutInflater());
    }

    void saveProfile() {
        val profile = new UpdateUserProfile(viewModel.email.getValue(), viewModel.displayName.getValue(), viewModel.discordName.getValue());
        Log.d(TAG, "saveProfile: Perform profile update " + profile);
        viewModel.isLoading.setValue(true);
        bambooApi
                .updateUserProfile(viewModel.id.getValue(), profile)
                .subscribe(() -> {
                    Log.d(TAG, "saveProfile: Update successful");
                    Toast
                            .makeText(getContext(), R.string.success_edit_panda, Toast.LENGTH_LONG)
                            .show();
                    val stateHandle = navigator.getPreviousBackStackEntry().getSavedStateHandle();
                    stateHandle.set("id", viewModel.id.getValue());
                    stateHandle.set("email", profile.getEmail());
                    stateHandle.set("displayName", profile.getDisplayName());
                    stateHandle.set("discordName", profile.getDiscordName());
                    stateHandle.set("action", "update");
                    navigator.popBackStack();
                }, ex -> {
                    Log.e(TAG, "saveProfile: Update failed", ex);
                    val bambooEx = (BambooException) ex;
                    var message = 0;
                    if (bambooEx.getErrorType() == ErrorType.ExistsAlready) {
                        message = R.string.error_panda_update_exists;
                    } else {
                        message = R.string.error_panda_update_failed;
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
                });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(PandaViewModel.class);
        viewModel.id.setValue(getArguments().getInt("id"));
        viewModel.email.setValue(getArguments().getString("email"));
        viewModel.displayName.setValue(getArguments().getString("displayName"));
        viewModel.discordName.setValue(getArguments().getString("discordName"));
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.actionSavePanda.setOnClickListener(v -> {
            if (viewModel.isDiscordNameValid.getValue() && viewModel.isDisplayNameValid.getValue() && viewModel.isEmailValid.getValue()) {
                saveProfile();
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
    }
}