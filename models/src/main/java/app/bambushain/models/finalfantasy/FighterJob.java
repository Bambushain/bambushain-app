package app.bambushain.models.finalfantasy;

import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import lombok.Getter;

import java.io.IOException;

@Getter
@JsonAdapter(FighterJob.Adapter.class)
public enum FighterJob {
    PALADIN("Paladin"),

    WARRIOR("Warrior"),

    DARKKNIGHT("DarkKnight"),

    GUNBREAKER("Gunbreaker"),

    WHITEMAGE("WhiteMage"),

    SCHOLAR("Scholar"),

    ASTROLOGIAN("Astrologian"),

    SAGE("Sage"),

    MONK("Monk"),

    DRAGOON("Dragoon"),

    NINJA("Ninja"),

    SAMURAI("Samurai"),

    REAPER("Reaper"),

    BARD("Bard"),

    MACHINIST("Machinist"),

    DANCER("Dancer"),

    BLACKMAGE("BlackMage"),

    SUMMONER("Summoner"),

    REDMAGE("RedMage"),

    BLUEMAGE("BlueMage");

    private final String value;

    FighterJob(String value) {
        this.value = value;
    }

    public static FighterJob fromValue(String value) {
        for (FighterJob b : FighterJob.values()) {
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

    public static class Adapter extends TypeAdapter<FighterJob> {
        @Override
        public void write(final JsonWriter jsonWriter, final FighterJob enumeration) throws IOException {
            jsonWriter.value(enumeration.getValue());
        }

        @Override
        public FighterJob read(final JsonReader jsonReader) throws IOException {
            String value = jsonReader.nextString();
            return FighterJob.fromValue(value);
        }
    }
}
