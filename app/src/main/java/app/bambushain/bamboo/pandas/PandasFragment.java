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
import app.bambushain.R;
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
        val isMod = activity.headerViewModel.isMod.getValue();
        val myId = activity.headerViewModel.id.getValue();
        val adapter = new PandasAdapter(new ViewModelProvider(this), getViewLifecycleOwner(), isMod, myId, new ArrayList<>());
        adapter.setOnEditUserListener((position, user) -> {
            val bundle = new Bundle();
            bundle.putInt("id", user.getId());
            bundle.putString("email", user.getEmail());
            bundle.putString("displayName", user.getDisplayName());
            bundle.putString("discordName", user.getDiscordName());
            navigator.navigate(R.id.action_fragment_pandas_to_editPandaDialog, bundle);
        });
        adapter.setOnMakeModListener((position, user) -> {
            bambooApi.makeUserMod(user.getId()).subscribe(() -> {
                user.setIsMod(true);
                adapter.notifyItemChanged(position, user);
            }, throwable -> {
                Log.e(TAG, "makeUserMod: make user mod failed", throwable);
            });
        });
        adapter.setOnRevokeModListener((position, user) -> {
            bambooApi.makeUserMod(user.getId()).subscribe(() -> {
                user.setIsMod(false);
                adapter.notifyItemChanged(position, user);
            }, throwable -> {
                Log.e(TAG, "makeUserMod: revoke user modstatus failed", throwable);
            });
        });
        adapter.setOnResetTotpListener((position, user) -> {
            bambooApi.resetUserTotp(user.getId()).subscribe(() -> {
                adapter.notifyItemChanged(position, user);
            }, throwable -> {
                Log.e(TAG, "resetUserTotp: resetting two factor code for user failed", throwable);
            });
        });
        adapter.setOnDeleteUserListener((position, user) -> {
            bambooApi.deleteUser(user.getId()).subscribe(() -> {
                adapter.notifyItemRemoved(position);
            }, throwable -> {
                Log.e(TAG, "deleteUser: deleting user failed", throwable);
            });
        });
        binding.pandaList.setAdapter(adapter);
        binding.pandaList.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    void loadData() {
        bambooApi
                .getUsers()
                .subscribe(users -> {
                    val adapter = (PandasAdapter) binding.pandaList.getAdapter();
                    adapter.setPandas(users);
                    adapter.notifyDataSetChanged();
                    binding.getViewModel().isLoading.setValue(false);
                }, throwable -> {
                    Log.e(TAG, "onViewCreated: Failed to load users", throwable);
                });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        val viewModel = new ViewModelProvider(this).get(PandaListViewModel.class);
        viewModel.canCreate.setValue(activity.headerViewModel.isMod.getValue());
        viewModel.isLoading.setValue(true);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        loadData();

    }
}