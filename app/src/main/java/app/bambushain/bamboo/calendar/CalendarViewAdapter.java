package app.bambushain.bamboo.calendar;


import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.List;
import java.util.stream.Collectors;

import app.bambushain.R;
import app.bambushain.base.listener.OnDeleteListener;
import app.bambushain.base.listener.OnEditListener;
import app.bambushain.databinding.CalendarDayBinding;
import app.bambushain.databinding.CalendarEventBinding;
import app.bambushain.models.bamboo.Event;
import app.bambushain.utils.ColorUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.val;

public class CalendarViewAdapter extends RecyclerView.Adapter<CalendarViewAdapter.ViewHolder> {

    private final ViewModelProvider viewModelProvider;
    private final LifecycleOwner lifecycleOwner;
    @Getter
    private final List<Event> events;
    private Month month;
    private int year;
    @Setter
    private OnEditListener<Event> onEventUpdateListener;
    @Setter
    private OnDeleteListener<Event> onEventDeleteListener;

    public CalendarViewAdapter(ViewModelProvider viewModelProvider, LifecycleOwner lifecycleOwner, List<Event> events, Month month, int year) {
        this.viewModelProvider = viewModelProvider;
        this.lifecycleOwner = lifecycleOwner;
        this.events = events;
        this.month = month;
        this.year = year;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup viewGroup, int viewType) {
        val binding = CalendarDayBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false);
        binding.setLifecycleOwner(lifecycleOwner);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        val day = LocalDate.of(year, month, position + 1);
        val viewModel = viewModelProvider.get(day.toString(), CalendarDayViewModel.class);
        viewHolder.binding.setViewModel(viewModel);
        viewHolder.setData(day, events
                .stream()
                .filter(event -> (event.getStartDate().isBefore(day) || event.getStartDate().isEqual(day)) && (event.getEndDate().isAfter(day) || event.getEndDate().isEqual(day)))
                .collect(Collectors.toList()));
    }

    @Override
    public int getItemCount() {
        return month.length(Year.isLeap(year));
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<Event> events, Month month, int year) {
        this.year = year;
        this.month = month;
        this.events.clear();
        this.events.addAll(events);
        notifyDataSetChanged();
    }

    public void addEvent(Event event) {
        events.add(event);

        val count = event.getEndDate().getDayOfMonth() - event.getStartDate().getDayOfMonth() + 1;
        val firstItem = event.getStartDate().getDayOfMonth() - 1;
        notifyItemRangeChanged(firstItem, count);
    }

    public void updateEvent(Event event) {
        val oldEvent = events.stream().filter(event1 -> event1.getId().equals(event.getId())).findFirst();
        events.removeIf(event1 -> event1.getId().equals(event.getId()));
        events.add(event);

        val countNew = event.getEndDate().getDayOfMonth() - event.getStartDate().getDayOfMonth() + 1;
        val firstItemNew = event.getStartDate().getDayOfMonth() - 1;

        val countOld = oldEvent.orElse(event).getEndDate().getDayOfMonth() - oldEvent.orElse(event).getStartDate().getDayOfMonth() + 1;
        val firstItemOld = oldEvent.orElse(event).getStartDate().getDayOfMonth() - 1;

        val firstItem = Math.min(firstItemNew, firstItemOld);
        val count = Math.max(countNew, countOld);

        notifyItemRangeChanged(firstItem, count);
    }

    public void removeEvent(Event event) {
        events.removeIf(event1 -> event1.getId().equals(event.getId()));

        val count = event.getEndDate().getDayOfMonth() - event.getStartDate().getDayOfMonth() + 1;
        val firstItem = event.getStartDate().getDayOfMonth() - 1;
        notifyItemRangeChanged(firstItem, count);
    }

    private void moreButtonClicked(Event event, View v) {
        val popup = new PopupMenu(v.getContext(), v);
        popup.inflate(R.menu.calendar_event_more_menu);
        popup.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.actionDeleteEvent && onEventDeleteListener != null) {
                onEventDeleteListener.onDelete(0, event);
            } else if (item.getItemId() == R.id.actionEditEvent && onEventUpdateListener != null) {
                onEventUpdateListener.onEdit(0, event);
            }

            return true;
        });
        popup.show();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final CalendarDayBinding binding;

        public ViewHolder(CalendarDayBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setData(LocalDate day, List<Event> events) {
            binding.getViewModel().date.setValue(day);
            renderEvents(events);
        }

        private void renderEvents(List<Event> events) {
            binding.events.removeAllViews();
            val margin = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            margin.bottomMargin = (int) itemView.getResources().getDimension(R.dimen.margin);
            margin.topMargin = (int) itemView.getResources().getDimension(R.dimen.margin);
            for (val event : events) {
                renderEvent(event, margin);
            }
        }

        private void renderEvent(Event event, ViewGroup.MarginLayoutParams margin) {
            val layoutInflater = LayoutInflater.from(itemView.getContext());
            val card = CalendarEventBinding.inflate(layoutInflater);
            val viewModel = viewModelProvider.get(event.getId().toString(), CalendarEventViewModel.class);
            viewModel.fromEvent(event);
            card.setViewModel(viewModel);
            card.moreButton.setOnClickListener(v -> moreButtonClicked(event, v));
            val textColorRes = ColorUtils.colorYiqRes(event.getColor());
            val textColor = itemView.getContext().getColor(textColorRes);
            val backgroundColor = ColorUtils.parseColor(event.getColor());
            card.eventCard.setCardBackgroundColor(backgroundColor);
            card.moreButton.setIconTintResource(textColorRes);
            card.eventTitle.setTextColor(textColor);
            card.eventDescription.setTextColor(textColor);

            binding.events.addView(card.getRoot(), margin);
        }
    }
}
