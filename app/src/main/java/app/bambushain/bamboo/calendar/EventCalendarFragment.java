package app.bambushain.bamboo.calendar;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
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
        val today = LocalDate.now();
        val firstDayOfMonth = LocalDate.of(today.getYear(), today.getMonth(), 1);
        val lastDayOfMonth = LocalDate.of(today.getYear(), today.getMonth(), today.getMonth().length(today.isLeapYear()));
        binding.getViewModel().isLoading.setValue(true);
        bambooApi
                .getEvents(firstDayOfMonth, lastDayOfMonth)
                .subscribe(events -> {
                    binding.getViewModel().isLoading.setValue(false);
                    binding.eventList.setAdapter(new CalendarViewAdapter(new ViewModelProvider(this), getViewLifecycleOwner(), events, today.getMonth(), today.getYear()));
                }, throwable -> {
                    Log.e(TAG, "onViewCreated: Failed to load the events", throwable);
                    Snackbar
                            .make(view, R.string.error_calendar_loading_failed, Snackbar.LENGTH_LONG)
                            .setBackgroundTint(getColor(R.color.md_theme_error))
                            .setTextColor(getColor(R.color.md_theme_onError))
                            .show();
                });
    }
}