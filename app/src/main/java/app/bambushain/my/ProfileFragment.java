package app.bambushain.my;

import app.bambushain.base.BindingFragment;
import app.bambushain.databinding.FragmentProfileBinding;
import dagger.hilt.android.AndroidEntryPoint;

import javax.inject.Inject;

@AndroidEntryPoint
public class ProfileFragment extends BindingFragment<FragmentProfileBinding> {

    @Inject
    public ProfileFragment() {
    }

    @Override
    protected FragmentProfileBinding getViewBinding() {
        return FragmentProfileBinding.inflate(getLayoutInflater());
    }
}