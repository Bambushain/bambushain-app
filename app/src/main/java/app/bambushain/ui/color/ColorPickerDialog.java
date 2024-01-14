package app.bambushain.ui.color;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import app.bambushain.R;
import app.bambushain.databinding.ColorPickerDialogBinding;
import app.bambushain.utils.ColorUtils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import lombok.val;

import java.util.Arrays;
import java.util.List;

public class ColorPickerDialog {
    private final Context context;
    private final String title;
    private final OnColorPickedListener onColorPickedListener;
    private String color;
    private List<String> colors;
    private List<MaterialButton> buttons;

    ColorPickerDialog(Context context, String title, OnColorPickedListener onColorPickedListener, String color) {
        this.context = context;
        this.title = title;
        this.onColorPickedListener = onColorPickedListener;
        this.color = color;
    }

    public static ColorPickerDialogBuilder builder() {
        return new ColorPickerDialogBuilder();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void show() {
        val availColors = Arrays.asList("#E57373", "#7986CB", "#4FC3F7", "#81C784", "#BA68C8", "#FF8A65", "#DCE775", "#FFD54F", "#4DB6AC", "#FFB74D");
        if (color != null && !availColors.contains(color)) {
            availColors.set(0, color);
        } else if (color == null) {
            color = availColors.get(0);
        }
        colors = availColors;
        val binding = ColorPickerDialogBinding.inflate(LayoutInflater.from(context));
        buttons = Arrays.asList(binding.colorDialogColor0, binding.colorDialogColor1, binding.colorDialogColor2, binding.colorDialogColor3, binding.colorDialogColor4, binding.colorDialogColor5, binding.colorDialogColor6, binding.colorDialogColor7, binding.colorDialogColor8, binding.colorDialogColor9);
        for (var i = 0; i < buttons.size(); i++) {
            val button = buttons.get(i);
            val color = colors.get(i);
            button.setBackgroundColor(ColorUtils.parseColor(color));
            button.setOnClickListener(this::colorSwitched);
            button.setIconTintResource(ColorUtils.colorYiqRes(color));
            if (color.equals(this.color)) {
                button.setIcon(context.getDrawable(R.drawable.ic_check));
            }
        }

        new MaterialAlertDialogBuilder(context)
                .setTitle(title)
                .setPositiveButton(R.string.choose_color, (dialog1, which) -> {
                    if (onColorPickedListener != null) {
                        onColorPickedListener.colorPicked(color);
                    }
                })
                .setNegativeButton(context.getString(R.string.close_dialog), (dialog1, which) -> dialog1.cancel())
                .setView(binding.getRoot())
                .create()
                .show();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void colorSwitched(View view) {
        val button = (MaterialButton) view;
        for (val btn : buttons) {
            btn.setIcon(null);
        }

        val idx = Integer.parseInt(button.getTag().toString());
        button.setIcon(context.getDrawable(R.drawable.ic_check));
        color = colors.get(idx);
    }

    public static class ColorPickerDialogBuilder {
        private Context context;
        private String title;
        private OnColorPickedListener onColorPickedListener;
        private String color;

        ColorPickerDialogBuilder() {
        }

        public ColorPickerDialogBuilder context(Context context) {
            this.context = context;
            return this;
        }

        public ColorPickerDialogBuilder title(String title) {
            this.title = title;
            return this;
        }

        public ColorPickerDialogBuilder onColorPickedListener(OnColorPickedListener onColorPickedListener) {
            this.onColorPickedListener = onColorPickedListener;
            return this;
        }

        public ColorPickerDialogBuilder color(String color) {
            this.color = color;
            return this;
        }

        public ColorPickerDialog build() {
            return new ColorPickerDialog(this.context, this.title, this.onColorPickedListener, this.color);
        }
    }
}