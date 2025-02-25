package app.bambushain.my;

import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

import javax.inject.Inject;

import app.bambushain.R;
import app.bambushain.api.BambooApi;
import app.bambushain.base.BindingDialogFragment;
import app.bambushain.databinding.FragmentEditMyProfileBinding;
import app.bambushain.models.exception.BambooException;
import app.bambushain.models.exception.ErrorType;
import app.bambushain.models.my.UpdateMyProfile;
import app.bambushain.utils.BundleUtils;
import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.core.Completable;
import lombok.val;
import okhttp3.MediaType;
import okhttp3.RequestBody;

@AndroidEntryPoint
public class EditProfileDialog extends BindingDialogFragment<FragmentEditMyProfileBinding> {
    private static final String TAG = EditProfileDialog.class.getName();

    @Inject
    BambooApi bambooApi;

    Uri profilePicture = null;

    ProfileViewModel viewModel;
    Snackbar snackbar;
    ActivityResultLauncher<PickVisualMediaRequest> launcher = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), o -> profilePicture = o);

    @Inject
    public EditProfileDialog() {
    }

    @Override
    protected FragmentEditMyProfileBinding getViewBinding() {
        return FragmentEditMyProfileBinding.inflate(getLayoutInflater());
    }

    void saveProfile() throws IOException {
        val profile = new UpdateMyProfile(viewModel.email.getValue(), viewModel.displayName.getValue(), viewModel.discordName.getValue());
        Log.d(TAG, "saveProfile: Perform profile update " + profile);
        viewModel.isLoading.setValue(true);

        //noinspection ResultOfMethodCallIgnored
        bambooApi
                .updateMyProfile(profile)
                .andThen(Completable.defer(() -> {
                    if (profilePicture != null) {
                        val source = ImageDecoder.createSource(requireContext().getContentResolver(), profilePicture);
                        val bitmap = ImageDecoder.decodeBitmap(source);
                        val buffer = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.WEBP_LOSSY, 80, buffer);
                        val requestBody = RequestBody
                                .create(MediaType.parse("application/octet-stream"), buffer.toByteArray());

                        return bambooApi.updateMyProfilePicture(requestBody);
                    } else {
                        return Completable.complete();
                    }
                }))
                .subscribe(() -> {
                    Log.d(TAG, "saveProfile: Update successful");
                    Toast
                            .makeText(requireContext(), R.string.success_profile_update, Toast.LENGTH_LONG)
                            .show();
                    val stateHandle = Objects.requireNonNull(navigator.getPreviousBackStackEntry()).getSavedStateHandle();
                    stateHandle.set("currentUser", viewModel);
                    navigator.popBackStack();
                }, ex -> {
                    Log.e(TAG, "saveProfile: Update failed", ex);
                    val bambooEx = (BambooException) ex;
                    var message = 0;
                    if (bambooEx.getErrorType() == ErrorType.ExistsAlready) {
                        message = R.string.error_profile_update_exists;
                    } else {
                        message = R.string.error_profile_update_failed;
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
    public void onViewCreated(@NonNull @NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        val args = getArguments();
        val profile = BundleUtils.getSerializable(args, "currentUser", ProfileViewModel.class);
        viewModel.email.setValue(profile.email.getValue());
        viewModel.displayName.setValue(profile.displayName.getValue());
        viewModel.discordName.setValue(profile.discordName.getValue());
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.actionSaveMyProfile.setOnClickListener(v -> {
            try {
                saveProfile();
            } catch (IOException e) {
                if (snackbar == null) {
                    snackbar = Snackbar.make(binding.layout, R.string.error_profile_picture_too_large, Snackbar.LENGTH_INDEFINITE);
                }
                snackbar
                        .setText(R.string.error_profile_picture_too_large)
                        .setBackgroundTint(getColor(R.color.md_theme_error))
                        .setTextColor(getColor(R.color.md_theme_onError))
                        .show();
            }
        });
        binding.chooseProfilePicture.setOnClickListener(v -> launcher.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build()));

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