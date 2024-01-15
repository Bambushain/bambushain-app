package app.bambushain.bamboo.pandas;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import app.bambushain.R;
import app.bambushain.databinding.PandasCardBinding;
import app.bambushain.models.bamboo.User;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import lombok.Getter;
import lombok.Setter;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;

public class PandasAdapter extends RecyclerView.Adapter<PandasAdapter.ViewHolder> {
    private final ViewModelProvider viewModelProvider;
    private final LifecycleOwner lifecycleOwner;
    private final boolean iAmMod;
    private final int myId;
    @Setter
    private List<User> pandas;
    @Setter
    private OnMakeModListener onMakeModListener;
    @Setter
    private OnRevokeModListener onRevokeModListener;
    @Setter
    private OnResetTotpListener onResetTotpListener;
    @Setter
    private OnResetPasswordListener onResetPasswordListener;
    @Setter
    private OnDeleteUserListener onDeleteUserListener;
    @Setter
    private OnEditUserListener onEditUserListener;

    public PandasAdapter(ViewModelProvider viewModelProvider, LifecycleOwner lifecycleOwner, boolean iAmMod, int myId, List<User> pandas) {
        this.viewModelProvider = viewModelProvider;
        this.lifecycleOwner = lifecycleOwner;
        this.iAmMod = iAmMod;
        this.myId = myId;
        this.pandas = pandas;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        val binding = PandasCardBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false);
        binding.setLifecycleOwner(lifecycleOwner);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        val panda = pandas.get(position);
        val viewModel = viewModelProvider.get(panda.getId().toString(), PandaViewModel.class);
        viewModel.canEdit.setValue(iAmMod && panda.getId() != myId);
        viewHolder.binding.setViewModel(viewModel);
        viewHolder.setPanda(panda);
        viewHolder.binding.actionMore.setOnClickListener(v -> {
                    val dialogBuilder = new MaterialAlertDialogBuilder(v.getContext());
                    val popupMenu = new PopupMenu(v.getContext(), v);
                    popupMenu.inflate(R.menu.pandas_card_menu);
                    popupMenu.getMenu().getItem(1).setTitle(
                            panda.getIsMod() ? R.string.action_revoke_mod_status : R.string.action_give_mod_status);
            popupMenu.getMenu().getItem(2).setVisible(panda.getAppTotpEnabled());
                    popupMenu.setOnMenuItemClickListener(item -> {
                        if (item.getItemId() == R.id.action_edit_panda) {
                            if (onEditUserListener != null) {
                                onEditUserListener.onEditUser(position, panda);
                            }
                        } else if (item.getItemId() == R.id.action_change_mod_status) {
                            dialogBuilder
                                    .setTitle(panda.getIsMod() ? R.string.action_revoke_mod_status : R.string.action_give_mod_status)
                                    .setMessage(v.getContext().getString(panda.getIsMod() ? R.string.action_revoke_mod_status_message : R.string.action_give_mod_status_message, panda.getDisplayName()))
                                    .setPositiveButton(panda.getIsMod() ? R.string.action_revoke_mod_status : R.string.action_give_mod_status, (dialog, which) -> {
                                        if (panda.getIsMod()) {
                                            if (onRevokeModListener != null) {
                                                onRevokeModListener.onRevokeMod(position, panda);
                                            }
                                        } else {
                                            if (onMakeModListener != null) {
                                                onMakeModListener.onMakeMod(position, panda);
                                            }
                                        }
                                    })
                                    .setNegativeButton(R.string.action_cancel, (dialog, which) -> dialog.cancel());
                            val dialog = dialogBuilder.create();
                            dialog.show();
                        } else if (item.getItemId() == R.id.action_reset_two_factor) {
                            dialogBuilder
                                    .setTitle(R.string.action_reset_two_factor)
                                    .setMessage(v.getContext().getString(R.string.action_reset_two_factor_message, panda.getDisplayName()))
                                    .setPositiveButton(R.string.action_reset_two_factor, (dialog, which) -> {
                                        if (onResetTotpListener != null) {
                                            onResetTotpListener.onResetTotp(position, panda);
                                        }
                                    })
                                    .setNegativeButton(R.string.action_cancel, (dialog, which) -> dialog.cancel());
                            val dialog = dialogBuilder.create();
                            dialog.show();
                        } else if (item.getItemId() == R.id.action_reset_password) {
                            dialogBuilder
                                    .setTitle(R.string.action_reset_password)
                                    .setMessage(v.getContext().getString(R.string.action_reset_password_message, panda.getDisplayName()))
                                    .setPositiveButton(R.string.action_reset_password, (dialog, which) -> {
                                        if (onResetPasswordListener != null) {
                                            onResetPasswordListener.onResetPassword(position, panda);
                                        }
                                    })
                                    .setNegativeButton(R.string.action_cancel, (dialog, which) -> dialog.cancel());
                            val dialog = dialogBuilder.create();
                            dialog.show();
                        } else if (item.getItemId() == R.id.action_delete_panda) {
                            dialogBuilder
                                    .setTitle(R.string.action_delete_panda)
                                    .setMessage(v.getContext().getString(R.string.action_delete_panda_message, panda.getDisplayName()))
                                    .setPositiveButton(R.string.action_delete_panda, (dialog, which) -> {
                                        if (onDeleteUserListener != null) {
                                            onDeleteUserListener.onDeleteUser(position, panda);
                                        }
                                    })
                                    .setNegativeButton(R.string.action_cancel, (dialog, which) -> dialog.cancel());
                            val dialog = dialogBuilder.create();
                            dialog.show();
                        }

                        return true;
                    });
                    popupMenu.show();
                }
        );
    }

    @Override
    public int getItemCount() {
        return pandas.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addPanda(User user) {
        pandas.add(user);
        pandas.sort(Comparator.comparing(User::getEmail));
        notifyDataSetChanged();
    }

    public void updatePanda(Integer id, String email, String displayName, String discordName) {
        val panda = pandas
                .stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();
        if (panda.isPresent()) {
            val p = panda.get();
            val idx = pandas.indexOf(p);
            p.setEmail(email);
            p.setDisplayName(displayName);
            p.setDiscordName(discordName);
            pandas.set(idx, p);
            notifyItemChanged(idx);
        }
    }

    public void removePanda(int position) {
        pandas.remove(position);
        notifyItemRemoved(position);
    }

    public void updatePanda(int position, User user) {
        pandas.set(position, user);
        notifyItemChanged(position);
    }

    public interface OnMakeModListener {
        void onMakeMod(int position, User user);
    }

    public interface OnRevokeModListener {
        void onRevokeMod(int position, User user);
    }

    public interface OnResetTotpListener {
        void onResetTotp(int position, User user);
    }

    public interface OnResetPasswordListener {
        void onResetPassword(int position, User user);
    }

    public interface OnDeleteUserListener {
        void onDeleteUser(int position, User user);
    }

    public interface OnEditUserListener {
        void onEditUser(int position, User user);
    }

    @Getter
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final PandasCardBinding binding;

        public ViewHolder(PandasCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setPanda(User panda) {
            binding.getViewModel().displayName.setValue(panda.getDisplayName());
            binding.getViewModel().discordName.setValue(panda.getDiscordName());
            binding.getViewModel().email.setValue(panda.getEmail());
            binding.getViewModel().id.setValue(panda.getId());
            binding.getViewModel().isMod.setValue(panda.getIsMod());
            binding.getViewModel().appTotpEnabled.setValue(panda.getAppTotpEnabled());
        }


    }
}
