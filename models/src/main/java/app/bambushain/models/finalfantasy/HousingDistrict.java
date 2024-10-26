package app.bambushain.models.finalfantasy;

import android.content.Context;

import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.Serializable;

import app.bambushain.models.R;
import lombok.Getter;

@Getter
@JsonAdapter(HousingDistrict.Adapter.class)
public enum HousingDistrict implements Serializable {
    THELAVENDERBEDS("TheLavenderBeds"),
    MIST("Mist"),
    THEGOBLET("TheGoblet"),
    SHIROGANE("Shirogane"),
    EMPYREUM("Empyreum");

    private final String value;

    HousingDistrict(String value) {
        this.value = value;
    }

    public static HousingDistrict fromValue(String value) {
        for (HousingDistrict b : HousingDistrict.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }

        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }

    public static HousingDistrict getFromTranslated(Context context, String translated) {
        if (translated.equals(context.getString(R.string.district_empyreum))) {
            return HousingDistrict.EMPYREUM;
        }
        if (translated.equals(context.getString(R.string.district_mist))) {
            return HousingDistrict.MIST;
        }
        if (translated.equals(context.getString(R.string.district_shirogane))) {
            return HousingDistrict.SHIROGANE;
        }
        if (translated.equals(context.getString(R.string.district_the_goblet))) {
            return HousingDistrict.THEGOBLET;
        }
        if (translated.equals(context.getString(R.string.district_the_lavender_beds))) {
            return HousingDistrict.THELAVENDERBEDS;
        }

        throw new IllegalArgumentException("Unexpected value '" + translated + "'");
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public String getTranslated(Context context) {
        return context.getString(switch (this) {
            case THELAVENDERBEDS -> R.string.district_the_lavender_beds;
            case MIST -> R.string.district_mist;
            case THEGOBLET -> R.string.district_the_goblet;
            case SHIROGANE -> R.string.district_shirogane;
            case EMPYREUM -> R.string.district_empyreum;
        });
    }

    public static class Adapter extends TypeAdapter<HousingDistrict> {
        @Override
        public void write(final JsonWriter jsonWriter, final HousingDistrict enumeration) throws IOException {
            jsonWriter.value(enumeration.getValue());
        }

        @Override
        public HousingDistrict read(final JsonReader jsonReader) throws IOException {
            String value = jsonReader.nextString();
            return HousingDistrict.fromValue(value);
        }
    }
}
