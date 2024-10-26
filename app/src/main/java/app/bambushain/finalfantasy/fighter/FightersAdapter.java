package app.bambushain.finalfantasy.fighter;

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
import app.bambushain.databinding.FighterCardBinding;
import app.bambushain.models.finalfantasy.Fighter;
import lombok.Getter;
import lombok.Setter;
import lombok.val;

public class FightersAdapter extends RecyclerView.Adapter<FightersAdapter.ViewHolder> {
    private final ViewModelProvider viewModelProvider;
    private final LifecycleOwner lifecycleOwner;
    @Setter
    private List<Fighter> fighters = new ArrayList<>();
    @Setter
    private OnEditListener<Fighter> onEditFighterListener;
    @Setter
    private OnDeleteListener<Fighter> onDeleteFighterListener;

    public FightersAdapter(ViewModelProvider viewModelProvider, LifecycleOwner lifecycleOwner) {
        this.viewModelProvider = viewModelProvider;
        this.lifecycleOwner = lifecycleOwner;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        val binding = FighterCardBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false);
        binding.setLifecycleOwner(lifecycleOwner);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        val fighter = fighters.get(position);
        val viewModel = viewModelProvider.get(fighter.getId().toString(), FighterViewModel.class);
        viewHolder.binding.setViewModel(viewModel);
        viewHolder.binding.editFighter.setOnClickListener(v -> {
            if (onEditFighterListener != null) {
                onEditFighterListener.onEdit(position, fighter);
            }
        });
        viewHolder.binding.deleteFighter.setOnClickListener(v -> {
            if (onDeleteFighterListener != null) {
                onDeleteFighterListener.onDelete(position, fighter);
            }
        });

        viewHolder.setFighter(fighter);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addFighter(Fighter fighter) {
        fighters.add(fighter);
        fighters.sort(Comparator.comparing(Fighter::getJob));
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateFighter(int position, Fighter fighter) {
        fighters.set(position, fighter);
        fighters.sort(Comparator.comparing(Fighter::getJob));
        notifyDataSetChanged();
    }

    public void removeFighter(int position) {
        fighters.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return fighters.size();
    }

    @Getter
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final FighterCardBinding binding;

        public ViewHolder(FighterCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setFighter(Fighter fighter) {
            binding.getViewModel().level.setValue(fighter.getLevel());
            binding.getViewModel().gearScore.setValue(fighter.getGearScore());
            binding.getViewModel().setJob(fighter.getJob(), binding.getRoot().getContext());
        }
    }
}
