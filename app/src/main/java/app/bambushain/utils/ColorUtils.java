package app.bambushain.utils;

import android.content.Context;
import android.graphics.Color;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import app.bambushain.R;
import lombok.val;

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

    private static String getColor(@ColorRes int color, Context context) {
        val c = context.getColor(color);

        return String.format("#%06x", c).replace("#ff", "#");
    }

    public static List<String> getColors(Context context) {
        return Arrays.asList(getColor(R.color.color_dialog_E57373, context), getColor(R.color.color_dialog_7986CB, context), getColor(R.color.color_dialog_4FC3F7, context), getColor(R.color.color_dialog_81C784, context), getColor(R.color.color_dialog_BA68C8, context), getColor(R.color.color_dialog_FF8A65, context), getColor(R.color.color_dialog_DCE775, context), getColor(R.color.color_dialog_FFD54F, context), getColor(R.color.color_dialog_4DB6AC, context), getColor(R.color.color_dialog_FFB74D, context));
    }

    public static String getRandomColor(Context context) {
        val random = new Random();
        val colors = getColors(context);

        return colors.get(random.nextInt(colors.size()));
    }
}
