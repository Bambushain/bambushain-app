package app.bambushain.bamboo.calendar;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import app.bambushain.R;
import app.bambushain.api.BambooApi;
import app.bambushain.base.BindingFragment;
import app.bambushain.databinding.FragmentEventCalendarBinding;
import app.bambushain.utils.SwipeDetector;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import dagger.hilt.android.AndroidEntryPoint;
import lombok.val;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.ArrayList;

@AndroidEntryPoint
public class EventCalendarFragment extends BindingFragment<FragmentEventCalendarBinding> {

    private static final String TAG = EventCalendarFragment.class.getName();

    @Inject
    BambooApi bambooApi;

    @Inject
    public EventCalendarFragment() {
    }

    @Override
    protected FragmentEventCalendarBinding getViewBinding() {
        return FragmentEventCalendarBinding.inflate(getLayoutInflater());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        val view = super.onCreateView(inflater, container, savedInstanceState);
        val adapter = new CalendarViewAdapter(new ViewModelProvider(this), getViewLifecycleOwner(), new ArrayList<>(), LocalDate.now().getMonth(), LocalDate.now().getYear());
        adapter.setOnEventDeleteListener(event -> {
            new MaterialAlertDialogBuilder(activity)
                    .setPositiveButton(R.string.action_delete_calendar_event_confirm, (dialog, which) -> {
                        bambooApi.deleteEvent(event.getId()).subscribe(() -> {
                            adapter.removeEvent(event);
                        }, throwable -> {
                            Snackbar
                                    .make(view, R.string.error_calendar_delete_failed, Snackbar.LENGTH_LONG)
                                    .setTextColor(getColor(R.color.md_theme_onError))
                                    .setBackgroundTint(getColor(R.color.md_theme_error))
                                    .show();
                        });
                    })
                    .setNegativeButton(R.string.action_delete_calendar_event_decline, (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .setTitle(R.string.calendar_delete_event_title)
                    .setMessage(R.string.calendar_delete_event_message)
                    .create()
                    .show();
        });
        binding.eventList.setAdapter(adapter);
        binding.eventList.setLayoutManager(new LinearLayoutManager(getContext()));
        val dividerItemDecoration = new DividerItemDecoration(activity, LinearLayoutManager.VERTICAL);
        binding.eventList.addItemDecoration(dividerItemDecoration);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(activity);
        var dateString = sharedPrefs.getLong("event_calendar_current_month", LocalDate.now().toEpochDay());
        val date = LocalDate.ofEpochDay(dateString);
        val viewModel = new ViewModelProvider(this).get(CalendarViewModel.class);
        viewModel.currentMonth.setValue(date);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.toolbar.setOnMenuItemClickListener(menuItem -> {
            if (menuItem.getItemId() == R.id.actionToday) {
                loadData(LocalDate.now());
            } else if (menuItem.getItemId() == R.id.actionPreviousMonth) {
                loadData(
                        binding
                                .getViewModel()
                                .currentMonth
                                .getValue()
                                .minusMonths(1)
                );
            } else if (menuItem.getItemId() == R.id.actionNextMonth) {
                loadData(
                        binding
                                .getViewModel()
                                .currentMonth
                                .getValue()
                                .plusMonths(1)
                );
            }

            return true;
        });
        val swipeListener = new SwipeDetector();
        swipeListener.setOnSwipeLeftListener(() -> loadData(
                binding
                        .getViewModel()
                        .currentMonth
                        .getValue()
                        .plusMonths(1)
        ));
        swipeListener.setOnSwipeRightListener(() -> loadData(
                binding
                        .getViewModel()
                        .currentMonth
                        .getValue()
                        .minusMonths(1)
        ));
        binding.eventList.setOnTouchListener(swipeListener);
        loadData(date);
    }

    private void loadData(LocalDate date) {
        binding.getViewModel().isLoading.setValue(true);
        val firstDayOfMonth = LocalDate.of(date.getYear(), date.getMonth(), 1);
        val lastDayOfMonth = LocalDate.of(date.getYear(), date.getMonth(), date.getMonth().length(date.isLeapYear()));
        binding.getViewModel().currentMonth.setValue(date);
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(activity);
        sharedPrefs
                .edit()
                .putLong("event_calendar_current_month", date.toEpochDay())
                .apply();
        bambooApi
                .getEvents(firstDayOfMonth, lastDayOfMonth)
                .subscribe(events -> {
                    binding.getViewModel().isLoading.setValue(false);
                    val adapter = (CalendarViewAdapter) binding.eventList.getAdapter();
                    adapter.setData(events, date.getMonth(), date.getYear());
                }, throwable -> {
                    Log.e(TAG, "onViewCreated: Failed to load the events", throwable);
                    Snackbar
                            .make(getView(), R.string.error_calendar_loading_failed, Snackbar.LENGTH_LONG)
                            .setBackgroundTint(getColor(R.color.md_theme_error))
                            .setTextColor(getColor(R.color.md_theme_onError))
                            .show();
                });
    }
}