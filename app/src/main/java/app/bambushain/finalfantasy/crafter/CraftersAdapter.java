package app.bambushain.finalfantasy.crafter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import app.bambushain.base.listener.OnDeleteListener;
import app.bambushain.base.listener.OnEditListener;
import app.bambushain.databinding.CrafterCardBinding;
import app.bambushain.models.finalfantasy.Crafter;
import lombok.Getter;
import lombok.Setter;
import lombok.val;

public class CraftersAdapter extends RecyclerView.Adapter<CraftersAdapter.ViewHolder> {
    private final ViewModelProvider viewModelProvider;
    private final LifecycleOwner lifecycleOwner;
    @Setter
    private List<Crafter> crafters = new ArrayList<>();
    @Setter
    private OnEditListener<Crafter> onEditCrafterListener;
    @Setter
    private OnDeleteListener<Crafter> onDeleteCrafterListener;

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
        viewHolder.binding.editCrafter.setOnClickListener(v -> {
            if (onEditCrafterListener != null) {
                onEditCrafterListener.onEdit(position, crafter);
            }
        });
        viewHolder.binding.deleteCrafter.setOnClickListener(v -> {
            if (onDeleteCrafterListener != null) {
                onDeleteCrafterListener.onDelete(position, crafter);
            }
        });

        viewHolder.setCrafter(crafter);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addCrafter(Crafter crafter) {
        crafters.add(crafter);
        crafters.sort(Comparator.comparing(Crafter::getJob));
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateCrafter(int position, Crafter crafter) {
        crafters.set(position, crafter);
        crafters.sort(Comparator.comparing(Crafter::getJob));
        notifyDataSetChanged();
    }

    public void removeCrafter(int position) {
        crafters.remove(position);
        notifyItemRemoved(position);
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
