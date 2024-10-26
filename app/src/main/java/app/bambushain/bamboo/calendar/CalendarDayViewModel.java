package app.bambushain.bamboo.calendar;

import static java.time.temporal.ChronoField.DAY_OF_MONTH;
import static java.time.temporal.ChronoField.DAY_OF_WEEK;
import static java.time.temporal.ChronoField.MONTH_OF_YEAR;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import java.time.LocalDate;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.SignStyle;
import java.time.format.TextStyle;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class CalendarDayViewModel extends ViewModel {
    public final MutableLiveData<LocalDate> date = new MutableLiveData<>();
    public LiveData<String> formattedDate = Transformations.map(date, localDate -> localDate.format(new DateTimeFormatterBuilder()
            .appendText(DAY_OF_WEEK, TextStyle.SHORT)
            .appendLiteral(", ")
            .appendValue(DAY_OF_MONTH, 1, 2, SignStyle.NOT_NEGATIVE)
            .appendLiteral(". ")
            .appendText(MONTH_OF_YEAR, TextStyle.FULL)
            .toFormatter()));

    @Inject
    public CalendarDayViewModel() {
    }
}
