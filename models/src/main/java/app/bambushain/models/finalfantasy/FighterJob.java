package app.bambushain.models.finalfantasy;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.Serializable;

import app.bambushain.models.R;
import lombok.Getter;

@Getter
@JsonAdapter(FighterJob.Adapter.class)
public enum FighterJob implements Serializable {
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

    BLUEMAGE("BlueMage"),

    VIPER("Viper"),

    PICTOMANCER("Pictomancer");

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

    public static FighterJob getFromTranslated(Context context, String translated) {
        if (translated.equals(context.getString(R.string.fighter_job_paladin))) {
            return FighterJob.PALADIN;
        }
        if (translated.equals(context.getString(R.string.fighter_job_warrior))) {
            return FighterJob.WARRIOR;
        }
        if (translated.equals(context.getString(R.string.fighter_job_darkknight))) {
            return FighterJob.DARKKNIGHT;
        }
        if (translated.equals(context.getString(R.string.fighter_job_gunbreaker))) {
            return FighterJob.GUNBREAKER;
        }
        if (translated.equals(context.getString(R.string.fighter_job_whitemage))) {
            return FighterJob.WHITEMAGE;
        }
        if (translated.equals(context.getString(R.string.fighter_job_scholar))) {
            return FighterJob.SCHOLAR;
        }
        if (translated.equals(context.getString(R.string.fighter_job_astrologian))) {
            return FighterJob.ASTROLOGIAN;
        }
        if (translated.equals(context.getString(R.string.fighter_job_sage))) {
            return FighterJob.SAGE;
        }
        if (translated.equals(context.getString(R.string.fighter_job_monk))) {
            return FighterJob.MONK;
        }
        if (translated.equals(context.getString(R.string.fighter_job_dragoon))) {
            return FighterJob.DRAGOON;
        }
        if (translated.equals(context.getString(R.string.fighter_job_ninja))) {
            return FighterJob.NINJA;
        }
        if (translated.equals(context.getString(R.string.fighter_job_samurai))) {
            return FighterJob.SAMURAI;
        }
        if (translated.equals(context.getString(R.string.fighter_job_reaper))) {
            return FighterJob.REAPER;
        }
        if (translated.equals(context.getString(R.string.fighter_job_bard))) {
            return FighterJob.BARD;
        }
        if (translated.equals(context.getString(R.string.fighter_job_machinist))) {
            return FighterJob.MACHINIST;
        }
        if (translated.equals(context.getString(R.string.fighter_job_dancer))) {
            return FighterJob.DANCER;
        }
        if (translated.equals(context.getString(R.string.fighter_job_blackmage))) {
            return FighterJob.BLACKMAGE;
        }
        if (translated.equals(context.getString(R.string.fighter_job_summoner))) {
            return FighterJob.SUMMONER;
        }
        if (translated.equals(context.getString(R.string.fighter_job_redmage))) {
            return FighterJob.REDMAGE;
        }
        if (translated.equals(context.getString(R.string.fighter_job_blackmage))) {
            return FighterJob.BLUEMAGE;
        }
        if (translated.equals(context.getString(R.string.fighter_job_viper))) {
            return FighterJob.VIPER;
        }
        if (translated.equals(context.getString(R.string.fighter_job_pictomancer))) {
            return FighterJob.PICTOMANCER;
        }

        throw new IllegalArgumentException("Unexpected value '" + translated + "'");
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public String getTranslated(Context context) {
        return context.getString(switch (this) {
            case PALADIN -> R.string.fighter_job_paladin;
            case WARRIOR -> R.string.fighter_job_warrior;
            case DARKKNIGHT -> R.string.fighter_job_darkknight;
            case GUNBREAKER -> R.string.fighter_job_gunbreaker;
            case WHITEMAGE -> R.string.fighter_job_whitemage;
            case SCHOLAR -> R.string.fighter_job_scholar;
            case ASTROLOGIAN -> R.string.fighter_job_astrologian;
            case SAGE -> R.string.fighter_job_sage;
            case MONK -> R.string.fighter_job_monk;
            case DRAGOON -> R.string.fighter_job_dragoon;
            case NINJA -> R.string.fighter_job_ninja;
            case SAMURAI -> R.string.fighter_job_samurai;
            case REAPER -> R.string.fighter_job_reaper;
            case BARD -> R.string.fighter_job_bard;
            case MACHINIST -> R.string.fighter_job_machinist;
            case DANCER -> R.string.fighter_job_dancer;
            case BLACKMAGE -> R.string.fighter_job_blackmage;
            case SUMMONER -> R.string.fighter_job_summoner;
            case REDMAGE -> R.string.fighter_job_redmage;
            case BLUEMAGE -> R.string.fighter_job_bluemage;
            case VIPER -> R.string.fighter_job_viper;
            case PICTOMANCER -> R.string.fighter_job_pictomancer;
        });
    }

    public Drawable getIcon(Context context) {
        return context.getDrawable(switch (this) {
            case PALADIN -> R.drawable.paladin;
            case WARRIOR -> R.drawable.warrior;
            case DARKKNIGHT -> R.drawable.darkknight;
            case GUNBREAKER -> R.drawable.gunbreaker;
            case WHITEMAGE -> R.drawable.whitemage;
            case SCHOLAR -> R.drawable.scholar;
            case ASTROLOGIAN -> R.drawable.astrologian;
            case SAGE -> R.drawable.sage;
            case MONK -> R.drawable.monk;
            case DRAGOON -> R.drawable.dragoon;
            case NINJA -> R.drawable.ninja;
            case SAMURAI -> R.drawable.samurai;
            case REAPER -> R.drawable.reaper;
            case BARD -> R.drawable.bard;
            case MACHINIST -> R.drawable.machinist;
            case DANCER -> R.drawable.dancer;
            case BLACKMAGE -> R.drawable.blackmage;
            case SUMMONER -> R.drawable.summoner;
            case REDMAGE -> R.drawable.redmage;
            case BLUEMAGE -> R.drawable.bluemage;
            case VIPER -> R.drawable.viper;
            case PICTOMANCER -> R.drawable.pictomancer;
        });
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
