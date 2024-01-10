package app.bambushain.bamboo.pandas;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import app.bambushain.databinding.PandasCardBinding;
import app.bambushain.models.bamboo.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@AllArgsConstructor
public class PandasAdapter extends RecyclerView.Adapter<PandasAdapter.ViewHolder> {
    private final ViewModelProvider viewModelProvider;
    private final LifecycleOwner lifecycleOwner;
    private final boolean iAmMod;
    private final int myId;
    @Setter
    private List<User> pandas;

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
    }

    @Override
    public int getItemCount() {
        return pandas.size();
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
