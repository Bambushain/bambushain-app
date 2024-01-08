package app.bambushain.bamboo.pandas;

import app.bambushain.base.BindingFragment;
import app.bambushain.databinding.FragmentPandasBinding;
import dagger.hilt.android.AndroidEntryPoint;

import javax.inject.Inject;

@AndroidEntryPoint
public class PandasFragment extends BindingFragment<FragmentPandasBinding> {
    @Inject
    public PandasFragment() {
    }

    @Override
    protected FragmentPandasBinding getViewBinding() {
        return FragmentPandasBinding.inflate(getLayoutInflater());
    }
}