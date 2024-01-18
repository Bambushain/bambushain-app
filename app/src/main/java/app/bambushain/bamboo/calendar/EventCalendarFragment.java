package app.bambushain.bamboo.calendar;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import app.bambushain.R;
import app.bambushain.api.BambooApi;
import app.bambushain.api.BambooCalendarEventSource;
import app.bambushain.base.BindingFragment;
import app.bambushain.databinding.FragmentEventCalendarBinding;
import app.bambushain.models.bamboo.Event;
import app.bambushain.utils.SwipeDetector;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.launchdarkly.eventsource.StreamException;
import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.ArrayList;

@AndroidEntryPoint
public class EventCalendarFragment extends BindingFragment<FragmentEventCalendarBinding> {

    private static final String TAG = EventCalendarFragment.class.getName();
    public static final String EVENT_CALENDAR_CURRENT_MONTH = "event_calendar_current_month";

    @Inject
    BambooApi bambooApi;

    @Inject
    BambooCalendarEventSource bambooCalendarEventSource;

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
        adapter.setOnEventDeleteListener(this::openDelete);
        adapter.setOnEventUpdateListener(event -> {
            val args = new Bundle();
            args.putSerializable("event", event);
            navigator.navigate(R.id.action_fragment_event_calendar_to_edit_event_dialog, args);
        });
        binding.eventList.setAdapter(adapter);
        binding.eventList.setLayoutManager(new LinearLayoutManager(getContext()));
        val dividerItemDecoration = new DividerItemDecoration(activity, LinearLayoutManager.VERTICAL);
        binding.eventList.addItemDecoration(dividerItemDecoration);

        return view;
    }

    private void openDelete(Event event) {
        new MaterialAlertDialogBuilder(activity)
                .setPositiveButton(R.string.action_delete_calendar_event_confirm, (dialog, which) -> {
                    bambooApi.deleteEvent(event.getId()).subscribe(() -> {
                        Log.d(TAG, "openDelete: Successfully deleted event " + event.getTitle());
                    }, throwable -> {
                        Log.e(TAG, "openDelete: Failed to delete event " + event.getTitle(), throwable);
                        Snackbar
                                .make(getView(), R.string.error_calendar_delete_failed, Snackbar.LENGTH_LONG)
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
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(activity);
        var dateString = sharedPrefs.getLong(EVENT_CALENDAR_CURRENT_MONTH, LocalDate.now().toEpochDay());
        val date = LocalDate.ofEpochDay(dateString);
        val viewModel = new ViewModelProvider(this).get(CalendarViewModel.class);
        viewModel.currentMonth.setValue(date);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.toolbar.setOnMenuItemClickListener(menuItem -> {
            if (menuItem.getItemId() == R.id.actionToday) {
                loadData(LocalDate.now());
            } else if (menuItem.getItemId() == R.id.actionPreviousMonth) {
                navigateToPreviousMonth();
            } else if (menuItem.getItemId() == R.id.actionNextMonth) {
                navigateToNextMonth();
            }

            return true;
        });
        binding.eventList.setOnTouchListener(getSwipeDetector());
        binding.addEvent.setOnClickListener(v -> {
            navigator.navigate(R.id.action_fragment_event_calendar_to_add_event_dialog);
        });
        Schedulers.io().scheduleDirect(() -> {
            try {
                Log.d(TAG, "onViewCreated: Start observer on calendar sse");
                bambooCalendarEventSource
                        .start()
                        .subscribe(calendarEventAction -> {
                            val event = calendarEventAction.getEvent();
                            val since = viewModel.currentMonth.getValue().withDayOfMonth(1).minusDays(1);
                            val until = viewModel.currentMonth.getValue().withDayOfMonth(since.lengthOfMonth()).plusDays(1);
                            if ((event.getStartDate().isAfter(since) && event.getStartDate().isBefore(until)) || (event.getEndDate().isAfter(since) && event.getEndDate().isBefore(until))) {
                                val adapter = (CalendarViewAdapter) binding.eventList.getAdapter();
                                switch (calendarEventAction.getAction()) {
                                    case Created:
                                        adapter.addEvent(event);
                                        break;
                                    case Updated:
                                        adapter.updateEvent(event);
                                        break;
                                    case Deleted:
                                        adapter.removeEvent(event);
                                        break;
                                }
                            }
                        }, throwable -> {
                            Log.e(TAG, "onViewCreated: Failed to load data", throwable);
                        });
            } catch (StreamException exception) {
                Log.e(TAG, "onViewCreated: Failed to start observer on calendar sse, setup reload on change", exception);
                AndroidSchedulers.mainThread().scheduleDirect(() -> navigator.addOnDestinationChangedListener(this::destinationChanged));
            }
        });
        loadData(date);
    }

    private void navigateToPreviousMonth() {
        loadData(
                binding
                        .getViewModel()
                        .currentMonth
                        .getValue()
                        .minusMonths(1)
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        navigator.removeOnDestinationChangedListener(this::destinationChanged);
    }

    private void destinationChanged(NavController controller, NavDestination destination, Bundle arguments) {
        if (destination.getId() == R.id.fragment_event_calendar) {
            loadData(binding.getViewModel().currentMonth.getValue());
        }
    }

    @NotNull
    private SwipeDetector getSwipeDetector() {
        val swipeListener = new SwipeDetector();
        swipeListener.setOnSwipeLeftListener(this::navigateToNextMonth);
        swipeListener.setOnSwipeRightListener(this::navigateToPreviousMonth);

        return swipeListener;
    }

    private void navigateToNextMonth() {
        loadData(
                binding
                        .getViewModel()
                        .currentMonth
                        .getValue()
                        .plusMonths(1)
        );
    }

    private void loadData(LocalDate date) {
        binding.getViewModel().isLoading.setValue(true);
        val firstDayOfMonth = date.withDayOfMonth(1);
        val lastDayOfMonth = firstDayOfMonth.withDayOfMonth(firstDayOfMonth.lengthOfMonth());
        binding.getViewModel().currentMonth.setValue(date);
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(activity);
        sharedPrefs
                .edit()
                .putLong(EVENT_CALENDAR_CURRENT_MONTH, date.toEpochDay())
                .apply();
        bambooApi
                .getEvents(firstDayOfMonth, lastDayOfMonth)
                .subscribe(events -> {
                    binding.getViewModel().isLoading.setValue(false);
                    val adapter = (CalendarViewAdapter) binding.eventList.getAdapter();
                    adapter.setData(events, date.getMonth(), date.getYear());
                }, throwable -> {
                    Log.e(TAG, "onViewCreated: Failed to load the events", throwable);
                    if (getView() != null) {
                        Snackbar
                                .make(getView(), R.string.error_calendar_loading_failed, Snackbar.LENGTH_LONG)
                                .setBackgroundTint(getColor(R.color.md_theme_error))
                                .setTextColor(getColor(R.color.md_theme_onError))
                                .show();
                    }
                });
    }
}