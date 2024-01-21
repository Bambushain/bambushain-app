package app.bambushain.finalfantasy.crafter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
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
        this.job.setValue(job.getTranslated(context));
        this.jobIconSrc.setValue(job.getIcon(context));
    }
}
