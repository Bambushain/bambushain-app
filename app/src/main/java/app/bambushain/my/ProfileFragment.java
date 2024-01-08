package app.bambushain.my;

import android.os.Bundle;
import android.view.View;
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
        viewModel.isEditMode.observe(getViewLifecycleOwner(), value -> {
            if (value) {
                navigator.navigate(R.id.action_fragment_profile_to_edit_profile_fragment);
                viewModel.isEditMode.setValue(false);
            }
        });
        viewModel.loadProfile();
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_change_my_password) {
                navigator.navigate(R.id.action_fragment_profile_to_change_my_password_fragment);
            }

            return true;
        });
    }
}