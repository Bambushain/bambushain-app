package app.bambushain.bamboo.calendar;

import android.content.Context;
import android.graphics.Color;
import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import app.bambushain.R;
import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;
import lombok.val;

import javax.inject.Inject;

@HiltViewModel
public class CalendarEventViewModel extends ViewModel {
    private final static String TAG = CalendarEventViewModel.class.getName();

    public MutableLiveData<Integer> id = new MutableLiveData<>();
    public MutableLiveData<String> title = new MutableLiveData<>();
    public MutableLiveData<String> description = new MutableLiveData<>();
    public MutableLiveData<String> color = new MutableLiveData<>();
    public MutableLiveData<Boolean> isPrivate = new MutableLiveData<>();
    public LiveData<Integer> backgroundColor = Transformations.map(color, this::parseColor);
    @ApplicationContext
    @Inject
    Context context;
    public LiveData<Integer> textColor = Transformations.map(color, this::colorYiq);

    @Inject
    public CalendarEventViewModel() {
    }

    @ColorInt
    int parseColor(String color) {
        val c = color.replaceAll("^#([0-9a-fA-F])([0-9a-fA-F])([0-9a-fA-F])$", "#$1$1$2$2$3$3");

        return Color.parseColor(c);
    }

    public int colorYiq(String data) {
        return context.getColor(colorYiqRes(data));
    }

    @ColorRes
    public int colorYiqRes(String data) {
        val color = Color.valueOf(parseColor(data));
        val yiq = ((color.red() * 255 * 299) + (color.green() * 255 * 587) + (color.blue() * 255 * 114)) / 1000;
        if (yiq >= 128) {
            return R.color.color_yiq_dark;
        } else {
            return R.color.color_yiq_light;
        }
    }
}
