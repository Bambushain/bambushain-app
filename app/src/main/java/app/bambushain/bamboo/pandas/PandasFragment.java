package app.bambushain.bamboo.pandas;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import app.bambushain.R;
import app.bambushain.api.BambooApi;
import app.bambushain.base.BindingFragment;
import app.bambushain.databinding.FragmentPandasBinding;
import app.bambushain.models.bamboo.User;
import app.bambushain.models.exception.BambooException;
import app.bambushain.models.exception.ErrorType;
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
            navigator.navigate(R.id.action_fragment_pandas_to_edit_panda_dialog, bundle);
        });
        adapter.setOnMakeModListener((position, user) -> {
            bambooApi.makeUserMod(user.getId()).subscribe(() -> {
                user.setIsMod(true);
                adapter.notifyItemChanged(position, user);
            }, throwable -> {
                Log.e(TAG, "makeUserMod: make user mod failed", throwable);
                val bambooException = (BambooException) throwable;
                var errorMessage = R.string.error_panda_make_mod_failed;
                if (bambooException.getErrorType() == ErrorType.InsufficientRights) {
                    errorMessage = R.string.error_panda_make_mod_insufficient_rights;
                } else if (bambooException.getErrorType() == ErrorType.Validation) {
                    errorMessage = R.string.error_panda_make_mod_validation;
                }

                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show();
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

        val stateHandle = navigator
                .getCurrentBackStackEntry()
                .getSavedStateHandle();
        val action = stateHandle.getLiveData("action", "");
        action.observe(getViewLifecycleOwner(), a -> {
            if (a.equals("update")) {
                Integer id = stateHandle.get("id");
                String email = stateHandle.get("email");
                String displayName = stateHandle.get("displayName");
                String discordName = stateHandle.get("discordName");

                adapter.updatePanda(id, email, displayName, discordName);

                action.setValue("");
            } else if (a.equals("create")) {
                Integer id = stateHandle.get("id");
                String email = stateHandle.get("email");
                String displayName = stateHandle.get("displayName");
                String discordName = stateHandle.get("discordName");
                Boolean mod = stateHandle.get("isMod");

                val user = new User(id, displayName, email, mod, discordName, false);
                adapter.addPanda(user);

                action.setValue("");
            }
        });

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