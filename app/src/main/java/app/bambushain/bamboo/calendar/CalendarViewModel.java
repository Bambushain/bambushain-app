package app.bambushain.bamboo.calendar;

import static java.time.temporal.ChronoField.MONTH_OF_YEAR;
import static java.time.temporal.ChronoField.YEAR;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import java.time.LocalDate;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.TextStyle;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class CalendarViewModel extends ViewModel {
    public final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(true);
    public final MutableLiveData<LocalDate> currentMonth = new MutableLiveData<>(LocalDate.now());

    public LiveData<String> currentMonthText = Transformations.map(currentMonth, localDate -> localDate.format(new DateTimeFormatterBuilder()
            .appendText(MONTH_OF_YEAR, TextStyle.FULL)
            .appendLiteral(' ')
            .appendText(YEAR, TextStyle.FULL)
            .toFormatter()));

    @Inject
    public CalendarViewModel() {
    }
}
