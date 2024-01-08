package app.bambushain.my;

import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import app.bambushain.R;
import app.bambushain.base.BindingFragment;
import app.bambushain.databinding.FragmentProfileBinding;
import dagger.hilt.android.AndroidEntryPoint;
import lombok.val;

import javax.inject.Inject;

@AndroidEntryPoint
public class ProfileFragment extends BindingFragment<FragmentProfileBinding> {

    ProfileViewModel viewModel;

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

        val headerViewModel = activity.headerViewModel;
        viewModel.email.setValue(headerViewModel.email.getValue());
        viewModel.displayName.setValue(headerViewModel.displayName.getValue());
        viewModel.discordName.setValue(headerViewModel.discordName.getValue());

        binding.editProfile.setOnClickListener(v -> {
            val bundle = new Bundle();
            bundle.putString("email", viewModel.email.getValue());
            bundle.putString("displayName", viewModel.displayName.getValue());
            bundle.putString("discordName", viewModel.discordName.getValue());
            navigator.navigate(R.id.action_fragment_profile_to_edit_profile_fragment, bundle);
        });
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_change_my_password) {
                navigator.navigate(R.id.action_fragment_profile_to_change_my_password_fragment);
            }

            return true;
        });

        val stateHandle = navigator
                .getCurrentBackStackEntry()
                .getSavedStateHandle();
        stateHandle
                .getLiveData("email", viewModel.email.getValue())
                .observe(getViewLifecycleOwner(), email -> {
                    viewModel.email.setValue(email);
                    headerViewModel.email.setValue(email);
                });
        stateHandle
                .getLiveData("displayName", viewModel.displayName.getValue())
                .observe(getViewLifecycleOwner(), displayName -> {
                    viewModel.displayName.setValue(displayName);
                    headerViewModel.displayName.setValue(displayName);
                });
        stateHandle
                .getLiveData("discordName", viewModel.discordName.getValue())
                .observe(getViewLifecycleOwner(), discordName -> {
                    viewModel.discordName.setValue(discordName);
                    headerViewModel.discordName.setValue(discordName);
                });
    }
}