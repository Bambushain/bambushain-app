package app.bambushain.finalfantasy.crafter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import app.bambushain.databinding.CrafterCardBinding;
import app.bambushain.models.finalfantasy.Crafter;
import lombok.Getter;
import lombok.Setter;
import lombok.val;

import java.util.ArrayList;
import java.util.List;

public class CraftersAdapter extends RecyclerView.Adapter<CraftersAdapter.ViewHolder> {
    private final ViewModelProvider viewModelProvider;
    private final LifecycleOwner lifecycleOwner;
    @Setter
    private List<Crafter> crafters = new ArrayList<>();

    public CraftersAdapter(ViewModelProvider viewModelProvider, LifecycleOwner lifecycleOwner) {
        this.viewModelProvider = viewModelProvider;
        this.lifecycleOwner = lifecycleOwner;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        val binding = CrafterCardBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false);
        binding.setLifecycleOwner(lifecycleOwner);

        return new CraftersAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        val crafter = crafters.get(position);
        val viewModel = viewModelProvider.get(crafter.getId().toString(), CrafterViewModel.class);
        viewHolder.binding.setViewModel(viewModel);

        viewHolder.setCrafter(crafter);
    }

    @Override
    public int getItemCount() {
        return crafters.size();
    }

    @Getter
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final CrafterCardBinding binding;

        public ViewHolder(CrafterCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setCrafter(Crafter crafter) {
            binding.getViewModel().level.setValue(crafter.getLevel());
            binding.getViewModel().setJob(crafter.getJob(), binding.getRoot().getContext());
        }
    }
}
