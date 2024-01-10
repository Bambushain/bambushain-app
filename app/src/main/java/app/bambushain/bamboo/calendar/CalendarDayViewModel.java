package app.bambushain.bamboo.calendar;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import dagger.hilt.android.lifecycle.HiltViewModel;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.SignStyle;
import java.time.format.TextStyle;

import static java.time.temporal.ChronoField.*;

@HiltViewModel
public class CalendarDayViewModel extends ViewModel {
    public MutableLiveData<LocalDate> date = new MutableLiveData<>();
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
