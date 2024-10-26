package app.bambushain.finalfantasy.housing;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import app.bambushain.models.finalfantasy.HousingDistrict;
import app.bambushain.models.finalfantasy.HousingType;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class HousingViewModel extends ViewModel {
    public final MutableLiveData<String> district = new MutableLiveData<>("");
    public final MutableLiveData<Integer> ward = new MutableLiveData<>(0);
    public final MutableLiveData<Integer> plot = new MutableLiveData<>(0);
    public final MutableLiveData<String> housingType = new MutableLiveData<>("");

    @Inject
    public HousingViewModel() {
    }

    public void setHousingType(HousingType type, Context context) {
        this.housingType.setValue(type.getTranslated(context));
    }

    public void setDistrict(HousingDistrict district, Context context) {
        this.district.setValue(district.getTranslated(context));
    }
}
