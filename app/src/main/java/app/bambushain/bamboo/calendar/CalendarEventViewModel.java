package app.bambushain.bamboo.calendar;

import static java.time.temporal.ChronoField.DAY_OF_MONTH;
import static java.time.temporal.ChronoField.MONTH_OF_YEAR;
import static java.time.temporal.ChronoField.YEAR;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import java.time.LocalDate;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.TextStyle;

import javax.inject.Inject;

import app.bambushain.models.bamboo.Event;
import app.bambushain.utils.ColorUtils;
import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;
import lombok.val;

@HiltViewModel
public class CalendarEventViewModel extends ViewModel {
    public final MutableLiveData<Integer> id = new MutableLiveData<>(0);
    public final MutableLiveData<String> title = new MutableLiveData<>("");
    public final MutableLiveData<String> description = new MutableLiveData<>("");
    public final MutableLiveData<String> color = new MutableLiveData<>("#598c79");
    public final MutableLiveData<LocalDate> startDate = new MutableLiveData<>(LocalDate.now());
    public final MutableLiveData<LocalDate> endDate = new MutableLiveData<>(LocalDate.now());
    public final MutableLiveData<Boolean> isPrivate = new MutableLiveData<>(false);
    public LiveData<String> startDateString = Transformations.map(startDate, this::mapDate);
    public LiveData<String> endDateString = Transformations.map(endDate, this::mapDate);
    public LiveData<Integer> backgroundColor = Transformations.map(color, ColorUtils::parseColor);
    @ApplicationContext
    @Inject
    Context context;
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
        if (context != null) {
            return context.getColor(ColorUtils.colorYiqRes(data));
        }

        return ColorUtils.colorYiqRes(data);
    }

    public Event toEvent() {
        val event = new Event();
        event.setColor(color.getValue());
        event.setTitle(title.getValue());
        event.setDescription(description.getValue());
        event.setEndDate(endDate.getValue());
        event.setStartDate(startDate.getValue());
        event.setIsPrivate(isPrivate.getValue());

        return event;
    }

    public void fromEvent(Event event) {
        color.setValue(event.getColor());
        title.setValue(event.getTitle());
        description.setValue(event.getDescription());
        id.setValue(event.getId());
        startDate.setValue(event.getStartDate());
        endDate.setValue(event.getEndDate());
    }
}
