package app.bambushain.bamboo.calendar;

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import app.bambushain.utils.ColorUtils;
import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.TextStyle;

import static java.time.temporal.ChronoField.*;

@HiltViewModel
public class CalendarEventViewModel extends ViewModel {
    @ApplicationContext
    @Inject
    Context context;
    public MutableLiveData<Integer> id = new MutableLiveData<>();
    public MutableLiveData<String> title = new MutableLiveData<>("");
    public MutableLiveData<String> description = new MutableLiveData<>("");
    public MutableLiveData<String> color = new MutableLiveData<>();
    public MutableLiveData<LocalDate> startDate = new MutableLiveData<>(LocalDate.now());
    public MutableLiveData<LocalDate> endDate = new MutableLiveData<>(LocalDate.now());
    public LiveData<String> startDateString = Transformations.map(startDate, this::mapDate);
    public LiveData<String> endDateString = Transformations.map(endDate, this::mapDate);
    public MutableLiveData<Boolean> isPrivate = new MutableLiveData<>(false);
    public LiveData<Integer> backgroundColor = Transformations.map(color, ColorUtils::parseColor);
    public LiveData<Integer> textColor = Transformations.map(color, this::colorYiq);

    @Inject
    public CalendarEventViewModel() {
    }

    String mapDate(LocalDate date) {
        return date.format(new DateTimeFormatterBuilder()
                .appendValue(DAY_OF_MONTH, 2)
                .appendLiteral(".")
                .appendValue(MONTH_OF_YEAR, 2)
                .appendLiteral(".")
                .appendText(YEAR, TextStyle.FULL)
                .toFormatter());
    }


    int colorYiq(String data) {
        return context.getColor(ColorUtils.colorYiqRes(data));
    }
}
