package app.bambushain.finalfantasy.housing;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import app.bambushain.base.listener.OnDeleteListener;
import app.bambushain.base.listener.OnEditListener;
import app.bambushain.databinding.HousingCardBinding;
import app.bambushain.models.finalfantasy.CharacterHousing;
import lombok.Getter;
import lombok.Setter;
import lombok.val;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class HousingAdapter extends RecyclerView.Adapter<HousingAdapter.ViewHolder> {
    private final ViewModelProvider viewModelProvider;
    private final LifecycleOwner lifecycleOwner;
    @Setter
    private List<CharacterHousing> housings = new ArrayList<>();
    @Setter
    private OnEditListener<CharacterHousing> onEditHousingListener;
    @Setter
    private OnDeleteListener<CharacterHousing> onDeleteHousingListener;

    public HousingAdapter(ViewModelProvider viewModelProvider, LifecycleOwner lifecycleOwner) {
        this.viewModelProvider = viewModelProvider;
        this.lifecycleOwner = lifecycleOwner;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        val binding = HousingCardBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false);
        binding.setLifecycleOwner(lifecycleOwner);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        val housing = housings.get(position);
        val viewModel = viewModelProvider.get(housing.getId().toString(), HousingViewModel.class);
        viewHolder.binding.setViewModel(viewModel);
        viewHolder.binding.editHousing.setOnClickListener(v -> {
            if (onEditHousingListener != null) {
                onEditHousingListener.onEdit(position, housing);
            }
        });
        viewHolder.binding.deleteHousing.setOnClickListener(v -> {
            if (onDeleteHousingListener != null) {
                onDeleteHousingListener.onDelete(position, housing);
            }
        });

        viewHolder.setHousing(housing);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addHousing(CharacterHousing housing) {
        housings.add(housing);
        housings
                .sort(
                        Comparator.comparing(CharacterHousing::getDistrict)
                                .thenComparing(CharacterHousing::getWard)
                                .thenComparing(CharacterHousing::getPlot));
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateHousing(int position, CharacterHousing housing) {
        housings.set(position, housing);
        housings
                .sort(
                        Comparator.comparing(CharacterHousing::getDistrict)
                                .thenComparing(CharacterHousing::getWard)
                                .thenComparing(CharacterHousing::getPlot));
        notifyDataSetChanged();
    }

    public void removeHousing(int position) {
        housings.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return housings.size();
    }

    @Getter
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final HousingCardBinding binding;

        public ViewHolder(HousingCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setHousing(CharacterHousing housing) {
            binding.getViewModel().ward.setValue(housing.getWard());
            binding.getViewModel().plot.setValue(housing.getPlot());
            binding.getViewModel().setDistrict(housing.getDistrict(), binding.getRoot().getContext());
            binding.getViewModel().setHousingType(housing.getHousingType(), binding.getRoot().getContext());
        }
    }
}
