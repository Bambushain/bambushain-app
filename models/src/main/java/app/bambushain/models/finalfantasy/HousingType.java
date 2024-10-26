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
@JsonAdapter(HousingType.Adapter.class)
public enum HousingType implements Serializable {
    PRIVATE("Private"),
    FREECOMPANY("FreeCompany"),
    SHAREDAPARTMENT("SharedApartment");

    private final String value;

    HousingType(String value) {
        this.value = value;
    }

    public static HousingType fromValue(String value) {
        for (HousingType b : HousingType.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }

        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }

    public static HousingType getFromTranslated(Context context, String translated) {
        if (translated.equals(context.getString(R.string.housing_type_private))) {
            return HousingType.PRIVATE;
        }
        if (translated.equals(context.getString(R.string.housing_type_freecompany))) {
            return HousingType.FREECOMPANY;
        }
        if (translated.equals(context.getString(R.string.housing_type_sharedapartment))) {
            return HousingType.SHAREDAPARTMENT;
        }

        throw new IllegalArgumentException("Unexpected value '" + translated + "'");
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public String getTranslated(Context context) {
        return context.getString(switch (this) {
            case PRIVATE -> R.string.housing_type_private;
            case FREECOMPANY -> R.string.housing_type_freecompany;
            case SHAREDAPARTMENT -> R.string.housing_type_sharedapartment;
        });
    }

    public static class Adapter extends TypeAdapter<HousingType> {
        @Override
        public void write(final JsonWriter jsonWriter, final HousingType enumeration) throws IOException {
            jsonWriter.value(enumeration.getValue());
        }

        @Override
        public HousingType read(final JsonReader jsonReader) throws IOException {
            String value = jsonReader.nextString();
            return HousingType.fromValue(value);
        }
    }
}
