package app.bambushain.finalfantasy.fighter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
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
        this.job.setValue(job.getTranslated(context));
        this.jobIconSrc.setValue(job.getIcon(context));
    }
}
