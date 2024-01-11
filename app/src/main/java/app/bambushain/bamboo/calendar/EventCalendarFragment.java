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
import com.google.android.material.snackbar.Snackbar;
import dagger.hilt.android.AndroidEntryPoint;
import lombok.val;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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
        binding.eventList.setAdapter(new CalendarViewAdapter(new ViewModelProvider(this), getViewLifecycleOwner(), new ArrayList<>(), LocalDate.now().getMonth(), LocalDate.now().getYear()));
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
                                .getValue().minusMonths(1)
                );
            } else if (menuItem.getItemId() == R.id.actionNextMonth) {
                loadData(
                        binding
                                .getViewModel()
                                .currentMonth
                                .getValue().plusMonths(1)
                );
            }

            return true;
        });
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