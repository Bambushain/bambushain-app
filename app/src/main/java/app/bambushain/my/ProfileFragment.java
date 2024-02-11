package app.bambushain.my;

import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import app.bambushain.R;
import app.bambushain.api.ProfilePictureLoader;
import app.bambushain.base.BindingFragment;
import app.bambushain.databinding.FragmentProfileBinding;
import dagger.hilt.android.AndroidEntryPoint;
import lombok.val;

import javax.inject.Inject;
import java.util.Objects;

@AndroidEntryPoint
public class ProfileFragment extends BindingFragment<FragmentProfileBinding> {

    ProfileViewModel viewModel;

    @Inject
    ProfilePictureLoader profilePictureLoader;

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
            bundle.putSerializable("currentUser", viewModel);
            navigator.navigate(R.id.action_fragment_profile_to_edit_profile_fragment, bundle);
        });
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_change_my_password) {
                navigator.navigate(R.id.action_fragment_profile_to_change_my_password_fragment);
            }

            return true;
        });

        profilePictureLoader.loadProfilePicture(headerViewModel.id.getValue(), binding.profilePicture);

        Objects.requireNonNull(navigator.getCurrentBackStackEntry())
                .getSavedStateHandle()
                .getLiveData("currentUser", viewModel)
                .observe(getViewLifecycleOwner(), user -> {
                    if (user != null) {
                        viewModel.email.setValue(user.email.getValue());
                        viewModel.discordName.setValue(user.discordName.getValue());
                        viewModel.displayName.setValue(user.displayName.getValue());
                        profilePictureLoader.loadProfilePicture(headerViewModel.id.getValue(), binding.profilePicture);
                    }
                });
    }
}