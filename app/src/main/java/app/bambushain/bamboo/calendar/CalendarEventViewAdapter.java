package app.bambushain.bamboo.calendar;


import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import app.bambushain.databinding.CalendarEventBinding;
import app.bambushain.models.bamboo.Event;
import lombok.AllArgsConstructor;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@AllArgsConstructor
public class CalendarEventViewAdapter extends RecyclerView.Adapter<CalendarEventViewAdapter.ViewHolder> {

    private final ViewModelProvider viewModelProvider;
    private final LifecycleOwner lifecycleOwner;
    private List<Event> events;

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        val binding = CalendarEventBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false);
        binding.setLifecycleOwner(lifecycleOwner);

        return new ViewHolder(binding);
    }

    @SuppressLint("UseCompatTextViewDrawableApis")
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        val event = events.get(position);
        val viewModel = viewModelProvider.get(event.getId().toString(), CalendarEventViewModel.class);
        viewHolder.binding.setViewModel(viewModel);
        viewHolder.setData(event);
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final CalendarEventBinding binding;

        public ViewHolder(CalendarEventBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setData(Event event) {
            binding.getViewModel().id.setValue(event.getId());
            binding.getViewModel().color.setValue(event.getColor());
            binding.getViewModel().description.setValue(event.getDescription());
            binding.getViewModel().title.setValue(event.getTitle());
            binding.moreButton.setIconTintResource(binding.getViewModel().colorYiqRes(event.getColor()));
        }
    }
}
