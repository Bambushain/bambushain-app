package app.bambushain.models.finalfantasy;

import android.content.Context;
import app.bambushain.models.R;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import lombok.Getter;
import lombok.val;

import java.io.IOException;
import java.io.Serializable;

@JsonAdapter(CharacterRace.Adapter.class)
@Getter
public enum CharacterRace implements Serializable {
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

    public static CharacterRace getFromTranslated(Context context, String translated) {
        if (translated.equals(context.getString(R.string.character_race_hyur))) {
            return CharacterRace.HYUR;
        } else if (translated.equals(context.getString(R.string.character_race_elezen))) {
            return CharacterRace.ELEZEN;
        } else if (translated.equals(context.getString(R.string.character_race_viera))) {
            return CharacterRace.VIERA;
        } else if (translated.equals(context.getString(R.string.character_race_minqote))) {
            return CharacterRace.MIQOTE;
        } else if (translated.equals(context.getString(R.string.character_race_roegadyn))) {
            return CharacterRace.ROEGADYN;
        } else if (translated.equals(context.getString(R.string.character_race_aura))) {
            return CharacterRace.AURA;
        } else if (translated.equals(context.getString(R.string.character_race_hrothgar))) {
            return CharacterRace.HROTHGAR;
        } else if (translated.equals(context.getString(R.string.character_race_lalafell))) {
            return CharacterRace.LALAFELL;
        }


        throw new IllegalArgumentException("Unexpected value '" + translated + "'");
    }

    public String getTranslated(Context context) {
        return context.getString(switch (this) {
            case HYUR -> R.string.character_race_hyur;
            case ELEZEN -> R.string.character_race_elezen;
            case LALAFELL -> R.string.character_race_lalafell;
            case MIQOTE -> R.string.character_race_minqote;
            case ROEGADYN -> R.string.character_race_roegadyn;
            case AURA -> R.string.character_race_aura;
            case HROTHGAR -> R.string.character_race_hrothgar;
            case VIERA -> R.string.character_race_viera;
        });
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
