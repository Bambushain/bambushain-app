package app.bambushain.my;

import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import app.bambushain.R;
import app.bambushain.api.BambooApi;
import app.bambushain.base.BindingDialogFragment;
import app.bambushain.databinding.FragmentChangeMyPasswordBinding;
import app.bambushain.models.exception.BambooException;
import app.bambushain.models.exception.ErrorType;
import app.bambushain.models.my.ChangeMyPassword;
import com.google.android.material.snackbar.Snackbar;
import dagger.hilt.android.AndroidEntryPoint;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

@AndroidEntryPoint
public class ChangePasswordDialog extends BindingDialogFragment<FragmentChangeMyPasswordBinding> {
    @Inject
    BambooApi bambooApi;

    ChangeMyPasswordViewModel viewModel;

    Snackbar snackbar;

    @Override
    protected FragmentChangeMyPasswordBinding getViewBinding() {
        return FragmentChangeMyPasswordBinding.inflate(getLayoutInflater());
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(ChangeMyPasswordViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.actionChangeMyPassword.setOnClickListener(v -> changePassword());
        viewModel.newPassword.observe(getViewLifecycleOwner(), s -> {
            if (snackbar != null) {
                snackbar.dismiss();
            }
            binding.changePasswordNewPassword.setError(null);
        });
        viewModel.oldPassword.observe(getViewLifecycleOwner(), s -> {
            if (snackbar != null) {
                snackbar.dismiss();
            }
            binding.changePasswordOldPassword.setError(null);
        });
    }

    private void changePassword() {
        val changeMyPassword = new ChangeMyPassword(viewModel.oldPassword.getValue(), viewModel.newPassword.getValue());
        if (!changeMyPassword.getNewPassword().isEmpty() && !changeMyPassword.getOldPassword().isEmpty()) {
            bambooApi
                    .changeMyPassword(changeMyPassword)
                    .subscribe(() -> {
                        navigator.popBackStack();
                    }, throwable -> {
                        var message = 0;
                        val bambooException = (BambooException) throwable;
                        if (bambooException.getErrorType() == ErrorType.InsufficientRights) {
                            message = R.string.error_change_my_password_insufficient_rights;
                        } else {
                            message = R.string.error_change_my_password_failed;
                        }
                        if (snackbar == null) {
                            snackbar = Snackbar.make(binding.layout, message, Snackbar.LENGTH_INDEFINITE);
                        }
                        snackbar
                                .setText(message)
                                .setBackgroundTint(getColor(R.color.md_theme_error))
                                .setTextColor(getColor(R.color.md_theme_onError))
                                .show();
                    });
        }
        if (changeMyPassword.getOldPassword().isEmpty()) {
            binding.changePasswordOldPassword.setError(getString(R.string.error_change_my_password_old_password_empty));
        }
        if (changeMyPassword.getNewPassword().isEmpty()) {
            binding.changePasswordNewPassword.setError(getString(R.string.error_change_my_password_new_password_empty));
        }
    }
}