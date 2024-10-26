package app.bambushain.bamboo.pandas;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

import javax.inject.Inject;

import app.bambushain.R;
import app.bambushain.api.BambooApi;
import app.bambushain.api.ProfilePictureLoader;
import app.bambushain.base.BindingFragment;
import app.bambushain.databinding.FragmentPandasBinding;
import app.bambushain.models.bamboo.User;
import app.bambushain.models.exception.BambooException;
import app.bambushain.models.exception.ErrorType;
import dagger.hilt.android.AndroidEntryPoint;
import lombok.val;

@AndroidEntryPoint
public class PandasFragment extends BindingFragment<FragmentPandasBinding> {
    private static final String TAG = PandasFragment.class.getName();
    @Inject
    BambooApi bambooApi;

    @Inject
    ProfilePictureLoader profilePictureLoader;

    @Inject
    public PandasFragment() {
    }

    @Override
    protected FragmentPandasBinding getViewBinding() {
        return FragmentPandasBinding.inflate(getLayoutInflater());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        val view = super.onCreateView(inflater, container, savedInstanceState);
        val adapter = getPandasAdapter();

        binding.itemList.setAdapter(adapter);
        binding.itemList.setLayoutManager(getLayoutManager());
        binding.itemList.addItemDecoration(getGridDivider(GridLayoutManager.VERTICAL));
        binding.itemList.addItemDecoration(getGridDivider(GridLayoutManager.HORIZONTAL));

        val stateHandle = Objects.requireNonNull(navigator.getCurrentBackStackEntry()).getSavedStateHandle();
        stateHandle
                .getLiveData("updatedUser", (User) null)
                .observe(getViewLifecycleOwner(), user -> {
                    if (user != null) {
                        adapter.updatePanda(user.getId(), user.getEmail(), user.getDisplayName(), user.getDiscordName());
                    }
                });
        stateHandle
                .getLiveData("createdUser", (User) null)
                .observe(getViewLifecycleOwner(), user -> {
                    if (user != null) {
                        adapter.addPanda(user);
                    }
                });

        return view;
    }

    @NotNull
    private PandasAdapter getPandasAdapter() {
        val isMod = activity.headerViewModel.isMod.getValue();
        val myId = activity.headerViewModel.id.getValue();
        //noinspection DataFlowIssue
        val adapter = new PandasAdapter(new ViewModelProvider(this), getViewLifecycleOwner(), Boolean.TRUE.equals(isMod), myId, profilePictureLoader, new ArrayList<>());
        adapter.setOnEditUserListener((position, user) -> {
            val bundle = new Bundle();
            bundle.putSerializable("user", user);
            navigator.navigate(R.id.action_fragment_pandas_to_edit_panda_dialog, bundle);
        });
        adapter.setOnMakeModListener(this::makeMod);
        adapter.setOnRevokeModListener(this::revokeMod);
        adapter.setOnResetTotpListener(this::resetTotp);
        adapter.setOnResetPasswordListener((position, user) -> resetPassword(user));
        adapter.setOnDeleteUserListener(this::delete);

        return adapter;
    }

    private void makeMod(int position, User user) {
        //noinspection ResultOfMethodCallIgnored
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.action_give_mod_status)
                .setMessage(getString(R.string.action_give_mod_status_message, user.getDisplayName()))
                .setPositiveButton(R.string.action_give_mod_status, (dialog, which) -> bambooApi.makeUserMod(user.getId()).subscribe(() -> {
                    val adapter = (PandasAdapter) binding.itemList.getAdapter();
                    user.setIsMod(true);
                    assert adapter != null;
                    adapter.updatePanda(position, user);
                    Toast.makeText(requireContext(), getString(R.string.success_panda_make_mod, user.getDisplayName()), Toast.LENGTH_LONG).show();
                }, throwable -> {
                    Log.e(TAG, "makeUserMod: make user mod failed", throwable);
                    val bambooException = (BambooException) throwable;
                    var errorMessage = getString(R.string.error_panda_make_mod_failed, user.getDisplayName());
                    if (bambooException.getErrorType() == ErrorType.InsufficientRights) {
                        errorMessage = getString(R.string.error_panda_make_mod_insufficient_rights);
                    } else if (bambooException.getErrorType() == ErrorType.Validation) {
                        errorMessage = getString(R.string.error_panda_make_mod_validation);
                    }

                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show();
                }))
                .setNegativeButton(R.string.action_cancel, (dialog, which) -> dialog.cancel())
                .create()
                .show();
    }

    private void revokeMod(int position, User user) {
        //noinspection ResultOfMethodCallIgnored
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.action_revoke_mod_status)
                .setMessage(getString(R.string.action_revoke_mod_status_message, user.getDisplayName()))
                .setPositiveButton(R.string.action_revoke_mod_status, (dialog, which) -> bambooApi.revokeUserModRights(user.getId()).subscribe(() -> {
                    val adapter = (PandasAdapter) binding.itemList.getAdapter();
                    user.setIsMod(false);
                    assert adapter != null;
                    adapter.updatePanda(position, user);
                    Toast.makeText(requireContext(), getString(R.string.success_panda_mod_revoked, user.getDisplayName()), Toast.LENGTH_LONG).show();
                }, throwable -> {
                    Log.e(TAG, "makeUserMod: make user mod failed", throwable);
                    val bambooException = (BambooException) throwable;
                    var errorMessage = getString(R.string.error_panda_remove_mod_failed, user.getDisplayName());
                    if (bambooException.getErrorType() == ErrorType.InsufficientRights) {
                        errorMessage = getString(R.string.error_panda_remove_mod_insufficient_rights);
                    } else if (bambooException.getErrorType() == ErrorType.Validation) {
                        errorMessage = getString(R.string.error_panda_remove_mod_validation);
                    }

                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show();
                }))
                .setNegativeButton(R.string.action_cancel, (dialog, which) -> dialog.cancel())
                .create()
                .show();
    }

    private void resetTotp(int position, User user) {
        //noinspection ResultOfMethodCallIgnored
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.action_reset_two_factor)
                .setMessage(getString(R.string.action_reset_two_factor_message, user.getDisplayName()))
                .setPositiveButton(R.string.action_reset_two_factor, (dialog, which) -> bambooApi.resetUserTotp(user.getId()).subscribe(() -> {
                    val adapter = (PandasAdapter) binding.itemList.getAdapter();
                    user.setAppTotpEnabled(false);
                    assert adapter != null;
                    adapter.updatePanda(position, user);
                    Toast.makeText(requireContext(), getString(R.string.success_panda_disable_totp, user.getDisplayName()), Toast.LENGTH_LONG).show();
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
                }))
                .setNegativeButton(R.string.action_cancel, (dialog, which) -> dialog.cancel())
                .create()
                .show();
    }

    private void resetPassword(User user) {
        //noinspection ResultOfMethodCallIgnored
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.action_reset_password)
                .setMessage(getString(R.string.action_reset_password_message, user.getDisplayName()))
                .setPositiveButton(R.string.action_reset_password, (dialog, which) -> bambooApi.changePassword(user.getId()).subscribe(() -> Toast.makeText(requireContext(), getString(R.string.success_panda_change_password, user.getDisplayName()), Toast.LENGTH_LONG).show(), throwable -> {
                    Log.e(TAG, "changePassword: reset password failed", throwable);
                    val bambooException = (BambooException) throwable;
                    var errorMessage = getString(R.string.error_panda_change_password_failed, user.getDisplayName());
                    if (bambooException.getErrorType() == ErrorType.InsufficientRights) {
                        errorMessage = getString(R.string.error_panda_change_password_insufficient_rights);
                    } else if (bambooException.getErrorType() == ErrorType.Validation) {
                        errorMessage = getString(R.string.error_panda_change_password_validation);
                    }
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show();
                }))
                .setNegativeButton(R.string.action_cancel, (dialog, which) -> dialog.cancel())
                .create()
                .show();
    }

    private void delete(int position, User panda) {
        //noinspection ResultOfMethodCallIgnored
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.action_delete_panda)
                .setMessage(getString(R.string.action_delete_panda_message, panda.getDisplayName()))
                .setPositiveButton(R.string.action_delete_panda, (dialog, which) -> bambooApi.deleteUser(panda.getId()).subscribe(() -> {
                    val adapter = (PandasAdapter) binding.itemList.getAdapter();
                    assert adapter != null;
                    adapter.removePanda(position);
                    Toast.makeText(requireContext(), getString(R.string.success_panda_delete, panda.getDisplayName()), Toast.LENGTH_LONG).show();
                }, throwable -> {
                    Log.e(TAG, "deleteUser: deleting user failed", throwable);
                    val bambooException = (BambooException) throwable;
                    var errorMessage = getString(R.string.error_panda_delete_failed, panda.getDisplayName());
                    if (bambooException.getErrorType() == ErrorType.InsufficientRights) {
                        errorMessage = getString(R.string.error_panda_delete_insufficient_rights);
                    } else if (bambooException.getErrorType() == ErrorType.Validation) {
                        errorMessage = getString(R.string.error_panda_delete_validation);
                    }
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show();
                }))
                .setNegativeButton(R.string.action_cancel, (dialog, which) -> dialog.cancel())
                .create()
                .show();
    }

    @SuppressLint("NotifyDataSetChanged")
    void loadData() {
        binding.pullToRefreshItemList.setRefreshing(true);
        //noinspection ResultOfMethodCallIgnored
        bambooApi
                .getUsers()
                .subscribe(users -> {
                    val adapter = (PandasAdapter) binding.itemList.getAdapter();
                    Objects.requireNonNull(adapter).setPandas(users);
                    adapter.notifyDataSetChanged();
                    binding.pullToRefreshItemList.setRefreshing(false);
                }, throwable -> {
                    Log.e(TAG, "onViewCreated: Failed to load users", throwable);
                    binding.pullToRefreshItemList.setRefreshing(false);
                });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        val viewModel = new ViewModelProvider(this).get(PandaListViewModel.class);
        viewModel.canCreate.setValue(activity.headerViewModel.isMod.getValue());
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.toolbar.setOnMenuItemClickListener(menuItem -> {
            if (menuItem.getItemId() == R.id.action_add_panda) {
                navigator.navigate(R.id.action_fragment_pandas_to_add_panda_dialog);
            }

            return true;
        });
        if (!viewModel.canCreate.getValue()) {
            binding.toolbar.getMenu().clear();
        }
        binding.pullToRefreshItemList.setOnRefreshListener(this::loadData);
        loadData();
    }
}