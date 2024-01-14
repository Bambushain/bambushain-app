package app.bambushain.bamboo.calendar;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.lifecycle.ViewModelProvider;
import app.bambushain.R;
import app.bambushain.api.BambooApi;
import app.bambushain.base.BindingDialogFragment;
import app.bambushain.databinding.FragmentEditEventBinding;
import app.bambushain.models.bamboo.Event;
import app.bambushain.ui.color.ColorPickerDialog;
import com.google.android.material.datepicker.MaterialDatePicker;
import dagger.hilt.android.AndroidEntryPoint;
import lombok.val;

import javax.inject.Inject;
import java.time.LocalDate;

@AndroidEntryPoint
public class EditEventDialog extends BindingDialogFragment<FragmentEditEventBinding> {
    @Inject
    BambooApi bambooApi;

    @Inject
    public EditEventDialog() {
    }

    @Override
    protected FragmentEditEventBinding getViewBinding() {
        return FragmentEditEventBinding.inflate(getLayoutInflater());
    }

    void chooseRange() {
        val viewModel = binding.getViewModel();
        val secondsInDay = 86400000L;
        val dialog = MaterialDatePicker
                .Builder
                .dateRangePicker()
                .setSelection(
                        Pair.create(
                                viewModel.startDate.getValue().toEpochDay() * secondsInDay,
                                viewModel.endDate.getValue().toEpochDay() * secondsInDay
                        ))
                .build();
        dialog.addOnPositiveButtonClickListener(range -> {
            val startDate = LocalDate.ofEpochDay(range.first / secondsInDay);
            val endDate = LocalDate.ofEpochDay(range.second / secondsInDay);
            binding.getViewModel().startDate.setValue(startDate);
            binding.getViewModel().endDate.setValue(endDate);
        });

        dialog.show(getChildFragmentManager(), null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        val args = getArguments();
        val viewModel = new ViewModelProvider(this).get(CalendarEventViewModel.class);
        viewModel.color.setValue(args.getString("color"));
        viewModel.title.setValue(args.getString("title"));
        viewModel.description.setValue(args.getString("description"));
        viewModel.id.setValue(args.getInt("id"));
        viewModel.startDate.setValue(LocalDate.parse(args.getString("startDate")));
        viewModel.endDate.setValue(LocalDate.parse(args.getString("endDate")));
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.eventColorPicker.setOnClickListener(v -> ColorPickerDialog
                .builder()
                .context(requireContext())
                .title(getString(R.string.choose_event_color))
                .color(viewModel.color.getValue())
                .onColorPickedListener(col -> viewModel.color.setValue(col))
                .build()
                .show());
        binding.eventStartDate.setEndIconOnClickListener(v -> chooseRange());
        binding.eventEndDate.setEndIconOnClickListener(v -> chooseRange());
        binding.actionAddEvent.setOnClickListener(v -> {
            if (viewModel.title.getValue().isBlank()) {
                binding.eventTitle.setError(getString(R.string.error_add_event_title_empty));
            } else {
                binding.eventTitle.setError(null);
                val event = new Event();
                event.setColor(viewModel.color.getValue());
                event.setTitle(viewModel.title.getValue());
                event.setDescription(viewModel.description.getValue());
                event.setEndDate(viewModel.endDate.getValue());
                event.setStartDate(viewModel.startDate.getValue());
                event.setIsPrivate(viewModel.isPrivate.getValue());
                bambooApi
                        .updateEvent(viewModel.id.getValue(), event)
                        .subscribe(() -> {
                            navigator.popBackStack();
                        }, throwable -> {
                            Toast.makeText(requireContext(), R.string.error_edit_event_unknown, Toast.LENGTH_LONG).show();
                        });
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
