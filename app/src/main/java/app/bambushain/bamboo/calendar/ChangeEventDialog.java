package app.bambushain.bamboo.calendar;

import androidx.core.util.Pair;
import androidx.viewbinding.ViewBinding;
import app.bambushain.R;
import app.bambushain.base.BindingDialogFragment;
import app.bambushain.ui.color.ColorPickerDialog;
import com.google.android.material.datepicker.MaterialDatePicker;
import lombok.val;

import java.time.LocalDate;
import java.util.Objects;

public abstract class ChangeEventDialog<T extends ViewBinding> extends BindingDialogFragment<T> {

    protected abstract CalendarEventViewModel getViewModel();

    void chooseRange() {
        val viewModel = getViewModel();
        val secondsInDay = 86400000L;
        val dialog = MaterialDatePicker
                .Builder
                .dateRangePicker()
                .setSelection(
                        Pair.create(
                                Objects.requireNonNull(viewModel.startDate.getValue()).toEpochDay() * secondsInDay,
                                Objects.requireNonNull(viewModel.endDate.getValue()).toEpochDay() * secondsInDay
                        ))
                .build();
        dialog.addOnPositiveButtonClickListener(range -> {
            val startDate = LocalDate.ofEpochDay(range.first / secondsInDay);
            val endDate = LocalDate.ofEpochDay(range.second / secondsInDay);
            getViewModel().startDate.setValue(startDate);
            getViewModel().endDate.setValue(endDate);
        });

        dialog.show(getChildFragmentManager(), null);
    }

    void openColorPicker() {
        ColorPickerDialog
                .builder()
                .context(requireContext())
                .title(getString(R.string.choose_event_color))
                .color(getViewModel().color.getValue().toLowerCase())
                .onColorPickedListener(col -> getViewModel().color.setValue(col))
                .build()
                .show();
    }
}
