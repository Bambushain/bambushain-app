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
import app.bambushain.databinding.FragmentEditEventBinding;
import app.bambushain.models.bamboo.Event;
import app.bambushain.utils.BundleUtils;
import dagger.hilt.android.AndroidEntryPoint;
import lombok.val;

@AndroidEntryPoint
public class EditEventDialog extends ChangeEventDialog<FragmentEditEventBinding> {
    @Inject
    BambooApi bambooApi;

    private int id;

    @Inject
    public EditEventDialog() {
    }

    @Override
    protected FragmentEditEventBinding getViewBinding() {
        return FragmentEditEventBinding.inflate(getLayoutInflater());
    }

    @Override
    protected CalendarEventViewModel getViewModel() {
        return binding.getViewModel();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        val args = getArguments();
        val event = BundleUtils.getSerializable(args, "event", Event.class);
        val viewModel = new ViewModelProvider(this).get(CalendarEventViewModel.class);
        viewModel.fromEvent(event);
        id = event.getId();

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
                        .updateEvent(id, viewModel.toEvent())
                        .subscribe(() -> navigator.popBackStack(), throwable -> Toast.makeText(requireContext(), R.string.error_edit_event_unknown, Toast.LENGTH_LONG).show());
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
}
