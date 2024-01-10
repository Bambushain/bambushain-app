package app.bambushain.bamboo.calendar;


import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import app.bambushain.databinding.CalendarDayBinding;
import app.bambushain.models.bamboo.Event;
import lombok.AllArgsConstructor;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class CalendarViewAdapter extends RecyclerView.Adapter<CalendarViewAdapter.ViewHolder> {

    private final ViewModelProvider viewModelProvider;
    private final LifecycleOwner lifecycleOwner;
    private List<Event> events;
    private Month month;
    private int year;

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        val binding = CalendarDayBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false);
        binding.setLifecycleOwner(lifecycleOwner);
        binding.events.setAdapter(new CalendarEventViewAdapter(viewModelProvider, lifecycleOwner, new ArrayList<>()));
        binding.events.setLayoutManager(new LinearLayoutManager(viewGroup.getContext()));

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        val day = LocalDate.of(year, month, position + 1);
        val viewModel = viewModelProvider.get(day.toString(), CalendarDayViewModel.class);
        viewHolder.binding.setViewModel(viewModel);
        viewHolder.setData(day, events.stream().filter(event -> event.getStartDate().isBefore(day) && event.getEndDate().isAfter(day)).collect(Collectors.toList()));
    }

    @Override
    public int getItemCount() {
        return month.length(Year.isLeap(year));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final CalendarDayBinding binding;

        public ViewHolder(CalendarDayBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setData(LocalDate day, List<Event> events) {
            binding.getViewModel().date.setValue(day);
            binding.events.setAdapter(new CalendarEventViewAdapter(viewModelProvider, binding.getLifecycleOwner(), events));
        }
    }
}
