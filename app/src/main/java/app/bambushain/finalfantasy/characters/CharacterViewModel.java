package app.bambushain.finalfantasy.characters;

import android.content.Context;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import app.bambushain.R;
import app.bambushain.models.finalfantasy.CharacterRace;
import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;

import javax.inject.Inject;

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
        if (race == CharacterRace.HYUR) {
            this.race.setValue(context.getString(R.string.character_race_hyur));
        } else if (race == CharacterRace.ELEZEN) {
            this.race.setValue(context.getString(R.string.character_race_elezen));
        } else if (race == CharacterRace.LALAFELL) {
            this.race.setValue(context.getString(R.string.character_race_lalafell));
        } else if (race == CharacterRace.MIQOTE) {
            this.race.setValue(context.getString(R.string.character_race_minqote));
        } else if (race == CharacterRace.ROEGADYN) {
            this.race.setValue(context.getString(R.string.character_race_roegadyn));
        } else if (race == CharacterRace.AURA) {
            this.race.setValue(context.getString(R.string.character_race_aura));
        } else if (race == CharacterRace.HROTHGAR) {
            this.race.setValue(context.getString(R.string.character_race_hrothgar));
        } else if (race == CharacterRace.VIERA) {
            this.race.setValue(context.getString(R.string.character_race_viera));
        }
    }
}
