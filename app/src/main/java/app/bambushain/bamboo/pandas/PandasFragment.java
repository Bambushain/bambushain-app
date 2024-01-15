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
                adapter.updatePanda(position, user);
                Toast.makeText(requireContext(), R.string.success_panda_make_mod, Toast.LENGTH_LONG).show();
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
                adapter.updatePanda(position, user);
                Toast.makeText(requireContext(), R.string.success_panda_mod_revoked, Toast.LENGTH_LONG).show();
            }, throwable -> {
                Log.e(TAG, "makeUserMod: revoke user modstatus failed", throwable);
                val bambooException = (BambooException) throwable;
                var errorMessage = R.string.error_panda_remove_mod_failed;
                if (bambooException.getErrorType() == ErrorType.InsufficientRights) {
                    errorMessage = R.string.error_panda_remove_mod_insufficient_rights;
                } else if (bambooException.getErrorType() == ErrorType.Validation) {
                    errorMessage = R.string.error_panda_remove_mod_validation;
                }
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show();
            });
        });
        adapter.setOnResetTotpListener((position, user) -> {
            bambooApi.resetUserTotp(user.getId()).subscribe(() -> {
                user.setAppTotpEnabled(false);
                adapter.updatePanda(position, user);
                Toast.makeText(requireContext(), R.string.success_panda_disable_totp, Toast.LENGTH_LONG).show();
            }, throwable -> {
                Log.e(TAG, "resetUserTotp: resetting two factor code for user failed", throwable);
                val bambooException = (BambooException) throwable;
                var errorMessage = R.string.error_panda_disable_totp_failed;
                if (bambooException.getErrorType() == ErrorType.InsufficientRights) {
                    errorMessage = R.string.error_panda_disable_totp_insufficient_rights;
                } else if (bambooException.getErrorType() == ErrorType.Validation) {
                    errorMessage = R.string.error_panda_disable_totp_validation;
                }
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show();
            });
        });
        adapter.setOnResetPasswordListener((position, user) -> {
            bambooApi.changePassword(user.getId()).subscribe(() -> {
                adapter.notifyItemChanged(position);
                Toast.makeText(requireContext(), R.string.success_panda_change_password, Toast.LENGTH_LONG).show();
            }, throwable -> {
                Log.e(TAG, "changePassword: reset password failed", throwable);
                val bambooException = (BambooException) throwable;
                var errorMessage = R.string.error_panda_change_password_failed;
                if (bambooException.getErrorType() == ErrorType.InsufficientRights) {
                    errorMessage = R.string.error_panda_change_password_insufficient_rights;
                } else if (bambooException.getErrorType() == ErrorType.Validation) {
                    errorMessage = R.string.error_panda_change_password_validation;
                }
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show();
            });
        });
        adapter.setOnDeleteUserListener((position, user) -> {
            bambooApi.deleteUser(user.getId()).subscribe(() -> {
                adapter.removePanda(position);
                Toast.makeText(requireContext(), R.string.success_panda_delete, Toast.LENGTH_LONG).show();
            }, throwable -> {
                Log.e(TAG, "deleteUser: deleting user failed", throwable);
                val bambooException = (BambooException) throwable;
                var errorMessage = R.string.error_panda_delete_failed;
                if (bambooException.getErrorType() == ErrorType.InsufficientRights) {
                    errorMessage = R.string.error_panda_delete_insufficient_rights;
                } else if (bambooException.getErrorType() == ErrorType.Validation) {
                    errorMessage = R.string.error_panda_delete_validation;
                }
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show();
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
        binding.addPanda.setOnClickListener(v -> {
            val bundle = new Bundle();
            bundle.putString("email", "");
            bundle.putString("displayName", "");
            bundle.putString("discordName", "");
            navigator.navigate(R.id.action_fragment_pandas_to_add_panda_dialog, bundle);
        });
        loadData();

    }
}