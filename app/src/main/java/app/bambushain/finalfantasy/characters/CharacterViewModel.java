package app.bambushain.finalfantasy.characters;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import app.bambushain.models.finalfantasy.CharacterRace;
import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;

@HiltViewModel
public class CharacterViewModel extends ViewModel {
    public final MutableLiveData<Integer> id = new MutableLiveData<>(0);
    public final MutableLiveData<String> name = new MutableLiveData<>("");
    public final MutableLiveData<String> race = new MutableLiveData<>("");
    public final MutableLiveData<String> world = new MutableLiveData<>("");
    public final MutableLiveData<String> freeCompany = new MutableLiveData<>("");

    public final MutableLiveData<Boolean> nameIsValid = new MutableLiveData<>(false);
    public final MutableLiveData<Boolean> worldIsValid = new MutableLiveData<>(false);
    @Inject
    @ApplicationContext
    Context context;

    @Inject
    public CharacterViewModel() {
    }

    public void setRace(CharacterRace race) {
        this.race.setValue(race.getTranslated(context));
    }
}
