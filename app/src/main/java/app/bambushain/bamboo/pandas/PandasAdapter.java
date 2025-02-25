package app.bambushain.bamboo.pandas;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;

import app.bambushain.R;
import app.bambushain.api.ProfilePictureLoader;
import app.bambushain.base.listener.OnDeleteListener;
import app.bambushain.base.listener.OnEditListener;
import app.bambushain.databinding.PandaCardBinding;
import app.bambushain.models.bamboo.User;
import lombok.Getter;
import lombok.Setter;
import lombok.val;

public class PandasAdapter extends RecyclerView.Adapter<PandasAdapter.ViewHolder> {
    private static ProfilePictureLoader profilePictureLoader;
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
    private OnDeleteListener<User> onDeleteUserListener;
    @Setter
    private OnEditListener<User> onEditUserListener;

    public PandasAdapter(ViewModelProvider viewModelProvider, LifecycleOwner lifecycleOwner, boolean iAmMod, int myId, ProfilePictureLoader profilePictureLoader, List<User> pandas) {
        this.viewModelProvider = viewModelProvider;
        this.lifecycleOwner = lifecycleOwner;
        this.iAmMod = iAmMod;
        this.myId = myId;
        PandasAdapter.profilePictureLoader = profilePictureLoader;
        this.pandas = pandas;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup viewGroup, int viewType) {
        val binding = PandaCardBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false);
        binding.setLifecycleOwner(lifecycleOwner);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        val panda = pandas.get(position);
        val viewModel = viewModelProvider.get(panda.getId().toString(), PandaViewModel.class);
        viewModel.canEdit.setValue(iAmMod && panda.getId() != myId);
        viewHolder.binding.setViewModel(viewModel);
        viewHolder.binding.editPanda.setOnClickListener(v -> {
            if (onEditUserListener != null) {
                onEditUserListener.onEdit(position, panda);
            }
        });
        viewHolder.binding.deletePanda.setOnClickListener(v -> {
            if (onDeleteUserListener != null) {
                onDeleteUserListener.onDelete(position, panda);
            }
        });
        viewHolder.setPanda(panda);
        viewHolder.binding.actionMore.setOnClickListener(v -> {
                    val popupMenu = new PopupMenu(v.getContext(), v);
                    val menu = popupMenu.getMenu();
                    if (panda.getIsMod()) {
                        menu
                                .add(R.string.action_revoke_mod_status)
                                .setOnMenuItemClickListener(item -> {
                                    if (onRevokeModListener != null) {
                                        onRevokeModListener.onRevokeMod(position, panda);
                                    }

                                    return true;
                                });
                    } else {
                        menu
                                .add(R.string.action_give_mod_status)
                                .setOnMenuItemClickListener(item -> {
                                    if (onMakeModListener != null) {
                                        onMakeModListener.onMakeMod(position, panda);
                                    }

                                    return true;
                                });
                    }
                    if (panda.getAppTotpEnabled()) {
                        menu
                                .add(R.string.action_reset_two_factor)
                                .setOnMenuItemClickListener(item -> {
                                    if (onResetTotpListener != null) {
                                        onResetTotpListener.onResetTotp(position, panda);
                                    }

                                    return true;
                                });
                    }
                    menu
                            .add(R.string.action_reset_password)
                            .setOnMenuItemClickListener(item -> {
                                if (onResetPasswordListener != null) {
                                    onResetPasswordListener.onResetPassword(position, panda);
                                }

                                return true;
                            });

                    popupMenu.show();
                }
        );
        if (panda.getId() == myId) {
            viewHolder.binding.actionMore.setVisibility(View.GONE);
        } else {
            viewHolder.binding.actionMore.setVisibility(View.VISIBLE);
        }
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

    @SuppressLint("NotifyDataSetChanged")
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
            pandas.sort(Comparator.comparing(User::getEmail));
            notifyDataSetChanged();
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

    @Getter
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final PandaCardBinding binding;

        public ViewHolder(PandaCardBinding binding) {
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
            profilePictureLoader.loadProfilePicture(panda.getId(), binding.profilePicture);
        }
    }
}
