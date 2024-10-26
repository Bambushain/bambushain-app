package app.bambushain.bamboo.calendar;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import java.util.Objects;

import javax.inject.Inject;

import app.bambushain.R;
import app.bambushain.api.BambooApi;
import app.bambushain.databinding.FragmentAddEventBinding;
import app.bambushain.utils.ColorUtils;
import dagger.hilt.android.AndroidEntryPoint;
import lombok.val;

@AndroidEntryPoint
public class AddEventDialog extends ChangeEventDialog<FragmentAddEventBinding> {
    @Inject
    BambooApi bambooApi;

    @Inject
    public AddEventDialog() {
    }

    @Override
    protected FragmentAddEventBinding getViewBinding() {
        return FragmentAddEventBinding.inflate(getLayoutInflater());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        val color = ColorUtils.getRandomColor(requireContext());
        val viewModel = new ViewModelProvider(this).get(CalendarEventViewModel.class);
        viewModel.color.setValue(color);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.eventColorPicker.setOnClickListener(v -> openColorPicker());
        binding.eventStartDate.setEndIconOnClickListener(v -> chooseRange());
        binding.eventEndDate.setEndIconOnClickListener(v -> chooseRange());
        binding.actionAddEvent.setOnClickListener(v -> {
            if (Objects.requireNonNull(viewModel.title.getValue()).isBlank()) {
                binding.eventTitle.setError(getString(R.string.error_add_event_title_empty));
            } else {
                binding.eventTitle.setError(null);
                //noinspection ResultOfMethodCallIgnored
                bambooApi
                        .createEvent(viewModel.toEvent())
                        .subscribe(event -> navigator.popBackStack(), throwable -> Toast.makeText(requireContext(), R.string.error_add_event_unknown, Toast.LENGTH_LONG).show());
            }
        });
        viewModel.title.observe(getViewLifecycleOwner(), s -> {
            if (s.isBlank()) {
                binding.eventTitle.setError(getString(R.string.error_add_event_title_empty));
            } else {
                binding.eventTitle.setError(null);
            }
        });
    }

    @Override
    protected CalendarEventViewModel getViewModel() {
        return binding.getViewModel();
    }
}
