package app.bambushain.bamboo.pandas;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import app.bambushain.api.BambooApi;
import app.bambushain.base.BindingFragment;
import app.bambushain.databinding.FragmentPandasBinding;
import dagger.hilt.android.AndroidEntryPoint;
import lombok.val;

import javax.inject.Inject;
import java.util.ArrayList;

@AndroidEntryPoint
public class PandasFragment extends BindingFragment<FragmentPandasBinding> {
    private static final String TAG = PandasFragment.class.getName();
    @Inject
    public PandasFragment() {
    }

    @Inject
    BambooApi bambooApi;

    @Override
    protected FragmentPandasBinding getViewBinding() {
        return FragmentPandasBinding.inflate(getLayoutInflater());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        val view = super.onCreateView(inflater, container, savedInstanceState);
        val adapter = new PandasAdapter(new ViewModelProvider(this), getViewLifecycleOwner(), false, 0, new ArrayList<>());
        binding.pandaList.setAdapter(adapter);
        binding.pandaList.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        val viewModel = new ViewModelProvider(this).get(PandaListViewModel.class);
        viewModel.canCreate.setValue(activity.headerViewModel.isMod.getValue());
        viewModel.isLoading.setValue(true);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        bambooApi
                .getUsers()
                .subscribe(users -> {
                    val isMod = activity.headerViewModel.isMod.getValue();
                    val myId = activity.headerViewModel.id.getValue();
                    val adapter = new PandasAdapter(new ViewModelProvider(this), getViewLifecycleOwner(), isMod, myId, users);
                    binding.pandaList.setAdapter(adapter);
                    viewModel.isLoading.setValue(false);
                }, throwable -> {
                    Log.e(TAG, "onViewCreated: Failed to load users", throwable);
                });
    }
}