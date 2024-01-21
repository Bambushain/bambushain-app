package app.bambushain.finalfantasy.crafter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import app.bambushain.R;
import app.bambushain.models.finalfantasy.CrafterJob;
import dagger.hilt.android.lifecycle.HiltViewModel;

import javax.inject.Inject;

@HiltViewModel
public class CrafterViewModel extends ViewModel {
    public final MutableLiveData<String> job = new MutableLiveData<>("");
    public final MutableLiveData<String> level = new MutableLiveData<>("");
    public final MutableLiveData<Drawable> jobIconSrc = new MutableLiveData<>();

    @Inject
    public CrafterViewModel() {
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void setJob(CrafterJob job, Context context) {
        this.job.setValue(context.getString(switch (job) {
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
        }));
        this.jobIconSrc.setValue(context.getDrawable(switch (job) {
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
        }));
    }
}
