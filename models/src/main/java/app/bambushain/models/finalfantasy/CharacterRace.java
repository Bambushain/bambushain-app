package app.bambushain.models.finalfantasy;

import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import lombok.Getter;
import lombok.val;

import java.io.IOException;

@JsonAdapter(CharacterRace.Adapter.class)
@Getter
public enum CharacterRace {
    HYUR("Hyur"),

    ELEZEN("Elezen"),

    LALAFELL("Lalafell"),

    MIQOTE("Miqote"),

    ROEGADYN("Roegadyn"),

    AURA("AuRa"),

    HROTHGAR("Hrothgar"),

    VIERA("Viera");

    private final String value;

    CharacterRace(String value) {
        this.value = value;
    }

    public static CharacterRace fromValue(String value) {
        for (CharacterRace b : CharacterRace.values()) {
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

    public static class Adapter extends TypeAdapter<CharacterRace> {
        @Override
        public void write(final JsonWriter jsonWriter, final CharacterRace enumeration) throws IOException {
            jsonWriter.value(enumeration.getValue());
        }

        @Override
        public CharacterRace read(final JsonReader jsonReader) throws IOException {
            val value = jsonReader.nextString();

            return CharacterRace.fromValue(value);
        }
    }
}
