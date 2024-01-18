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
import app.bambushain.utils.BundleUtils;
import com.google.android.material.datepicker.MaterialDatePicker;
import dagger.hilt.android.AndroidEntryPoint;
import lombok.val;

import javax.inject.Inject;
import java.time.LocalDate;

@AndroidEntryPoint
public class EditEventDialog extends ChangeEventDialog<FragmentEditEventBinding> {
    @Inject
    BambooApi bambooApi;

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
        val viewModel = new ViewModelProvider(this).get(CalendarEventViewModel.class);
        viewModel.fromEvent(BundleUtils.getSerializable(args, "event", Event.class));

        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.eventColorPicker.setOnClickListener(v -> openColorPicker());
        binding.eventStartDate.setEndIconOnClickListener(v -> chooseRange());
        binding.eventEndDate.setEndIconOnClickListener(v -> chooseRange());
        binding.actionAddEvent.setOnClickListener(v -> {
            if (viewModel.title.getValue().isBlank()) {
                binding.eventTitle.setError(getString(R.string.error_add_event_title_empty));
            } else {
                binding.eventTitle.setError(null);
                bambooApi
                        .updateEvent(viewModel.id.getValue(), viewModel.toEvent())
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
