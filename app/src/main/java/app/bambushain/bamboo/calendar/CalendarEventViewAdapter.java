package app.bambushain.bamboo.calendar;


import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import app.bambushain.R;
import app.bambushain.databinding.CalendarEventBinding;
import app.bambushain.models.bamboo.Event;
import app.bambushain.utils.ColorUtils;
import lombok.Setter;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CalendarEventViewAdapter extends RecyclerView.Adapter<CalendarEventViewAdapter.ViewHolder> {

    private final ViewModelProvider viewModelProvider;
    private final LifecycleOwner lifecycleOwner;
    private final List<Event> events;
    @Setter
    private OnEventUpdateListener onEventUpdateListener;
    @Setter
    private OnEventDeleteListener onEventDeleteListener;

    public CalendarEventViewAdapter(ViewModelProvider viewModelProvider, LifecycleOwner lifecycleOwner, List<Event> events) {
        this.viewModelProvider = viewModelProvider;
        this.lifecycleOwner = lifecycleOwner;
        this.events = events;
    }

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
        viewHolder.binding.moreButton.setOnClickListener(v -> {
            val popup = new PopupMenu(v.getContext(), v);
            popup.inflate(R.menu.calendar_event_more_menu);
            popup.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.actionDeleteEvent && onEventDeleteListener != null) {
                    onEventDeleteListener.onEventDelete(event);
                } else if (item.getItemId() == R.id.actionEditEvent && onEventUpdateListener != null) {
                    onEventUpdateListener.onEventUpdate(event);
                }

                return true;
            });
            popup.show();
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setEvents(List<Event> events) {
        this.events.clear();
        this.events.addAll(events);
        this.notifyDataSetChanged();
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
            binding.getViewModel().isPrivate.setValue(event.getIsPrivate());
            binding.moreButton.setIconTintResource(ColorUtils.colorYiqRes(event.getColor()));
        }
    }
}
