package app.bambushain.finalfantasy.fighter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import app.bambushain.R;
import app.bambushain.models.finalfantasy.FighterJob;
import dagger.hilt.android.lifecycle.HiltViewModel;

import javax.inject.Inject;

@HiltViewModel
public class FighterViewModel extends ViewModel {
    public final MutableLiveData<String> job = new MutableLiveData<>("");
    public final MutableLiveData<String> level = new MutableLiveData<>("");
    public final MutableLiveData<String> gearScore = new MutableLiveData<>("");
    public final MutableLiveData<Drawable> jobIconSrc = new MutableLiveData<>();

    @Inject
    public FighterViewModel() {
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void setJob(FighterJob job, Context context) {
        this.job.setValue(context.getString(switch (job) {
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
        }));
        this.jobIconSrc.setValue(context.getDrawable(switch (job) {
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
        }));
    }
}
