package app.bambushain.utils;

import android.content.Context;
import android.graphics.Color;
import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import app.bambushain.R;
import lombok.val;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public final class ColorUtils {
    private ColorUtils() {
    }

    @ColorInt
    public static int parseColor(String color) {
        val c = color.replaceAll("^#([0-9a-fA-F])([0-9a-fA-F])([0-9a-fA-F])$", "#$1$1$2$2$3$3");

        return Color.parseColor(c);
    }

    @ColorRes
    public static int colorYiqRes(String data) {
        val color = Color.valueOf(parseColor(data));
        val yiq = ((color.red() * 255 * 299) + (color.green() * 255 * 587) + (color.blue() * 255 * 114)) / 1000;
        if (yiq >= 128) {
            return R.color.color_yiq_dark;
        } else {
            return R.color.color_yiq_light;
        }
    }

    public static List<String> getColors(Context context) {
        return Arrays.asList("#E57373", "#7986CB", "#4FC3F7", "#81C784", "#BA68C8", "#FF8A65", "#DCE775", "#FFD54F", "#4DB6AC", "#FFB74D");
    }

    public static String getRandomColor(Context context) {
        val random = new Random();
        val colors = getColors(context);

        return colors.get(random.nextInt(colors.size()));
    }
}
