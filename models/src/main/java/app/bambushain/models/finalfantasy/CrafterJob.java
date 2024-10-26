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
@JsonAdapter(CrafterJob.Adapter.class)
public enum CrafterJob implements Serializable {
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

    public static CrafterJob getFromTranslated(Context context, String translated) {
        if (translated.equals(context.getString(R.string.crafter_job_carpenter))) {
            return CrafterJob.CARPENTER;
        }
        if (translated.equals(context.getString(R.string.crafter_job_blacksmith))) {
            return CrafterJob.BLACKSMITH;
        }
        if (translated.equals(context.getString(R.string.crafter_job_armorer))) {
            return CrafterJob.ARMORER;
        }
        if (translated.equals(context.getString(R.string.crafter_job_goldsmith))) {
            return CrafterJob.GOLDSMITH;
        }
        if (translated.equals(context.getString(R.string.crafter_job_leatherworker))) {
            return CrafterJob.LEATHERWORKER;
        }
        if (translated.equals(context.getString(R.string.crafter_job_weaver))) {
            return CrafterJob.WEAVER;
        }
        if (translated.equals(context.getString(R.string.crafter_job_alchemist))) {
            return CrafterJob.ALCHEMIST;
        }
        if (translated.equals(context.getString(R.string.crafter_job_culinarian))) {
            return CrafterJob.CULINARIAN;
        }
        if (translated.equals(context.getString(R.string.crafter_job_miner))) {
            return CrafterJob.MINER;
        }
        if (translated.equals(context.getString(R.string.crafter_job_botanist))) {
            return CrafterJob.BOTANIST;
        }
        if (translated.equals(context.getString(R.string.crafter_job_fisher))) {
            return CrafterJob.FISHER;
        }
        if (translated.equals(context.getString(R.string.crafter_job_weaver))) {
            return CrafterJob.WEAVER;
        }

        throw new IllegalArgumentException("Unexpected value '" + translated + "'");
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public String getTranslated(Context context) {
        return context.getString(switch (this) {
            case CARPENTER -> R.string.crafter_job_carpenter;
            case BLACKSMITH -> R.string.crafter_job_blacksmith;
            case ARMORER -> R.string.crafter_job_armorer;
            case GOLDSMITH -> R.string.crafter_job_goldsmith;
            case LEATHERWORKER -> R.string.crafter_job_leatherworker;
            case WEAVER -> R.string.crafter_job_weaver;
            case ALCHEMIST -> R.string.crafter_job_alchemist;
            case CULINARIAN -> R.string.crafter_job_culinarian;
            case MINER -> R.string.crafter_job_miner;
            case BOTANIST -> R.string.crafter_job_botanist;
            case FISHER -> R.string.crafter_job_fisher;
        });
    }

    public Drawable getIcon(Context context) {
        return context.getDrawable(switch (this) {
            case CARPENTER -> R.drawable.carpenter;
            case BLACKSMITH -> R.drawable.blacksmith;
            case ARMORER -> R.drawable.armorer;
            case GOLDSMITH -> R.drawable.goldsmith;
            case LEATHERWORKER -> R.drawable.leatherworker;
            case WEAVER -> R.drawable.weaver;
            case ALCHEMIST -> R.drawable.alchemist;
            case CULINARIAN -> R.drawable.culinarian;
            case MINER -> R.drawable.miner;
            case BOTANIST -> R.drawable.botanist;
            case FISHER -> R.drawable.fisher;
        });
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
