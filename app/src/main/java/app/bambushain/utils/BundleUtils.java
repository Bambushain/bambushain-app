package app.bambushain.utils;

import android.os.Bundle;

import java.io.Serializable;

public class BundleUtils {
    public static <T extends Serializable> T getSerializable(Bundle bundle, String key, Class<T> clazz) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            return bundle.getSerializable(key, clazz);
        } else {
            //noinspection deprecation
            return (T) bundle.getSerializable(key);
        }
    }
}
