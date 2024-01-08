package app.bambushain.models.finalfantasy;

import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import lombok.Getter;

import java.io.IOException;

@Getter
@JsonAdapter(CrafterJob.Adapter.class)
public enum CrafterJob {
    CARPENTER("Carpenter"),

    BLACKSMITH("Blacksmith"),

    ARMORER("Armorer"),

    GOLDSMITH("Goldsmith"),

    LEATHERWORKER("Leatherworker"),

    WEAVER("Weaver"),

    ALCHEMIST("Alchemist"),

    CULINARIAN("Culinarian"),

    MINER("Miner"),

    BOTANIST("Botanist"),

    FISHER("Fisher");

    private final String value;

    CrafterJob(String value) {
        this.value = value;
    }

    public static CrafterJob fromValue(String value) {
        for (CrafterJob b : CrafterJob.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }

        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static class Adapter extends TypeAdapter<CrafterJob> {
        @Override
        public void write(final JsonWriter jsonWriter, final CrafterJob enumeration) throws IOException {
            jsonWriter.value(enumeration.getValue());
        }

        @Override
        public CrafterJob read(final JsonReader jsonReader) throws IOException {
            String value = jsonReader.nextString();

            return CrafterJob.fromValue(value);
        }
    }
}
