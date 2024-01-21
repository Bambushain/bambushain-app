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
        this.race.setValue(context.getString(switch (race) {
            case HYUR -> R.string.character_race_hyur;
            case ELEZEN -> R.string.character_race_elezen;
            case LALAFELL -> R.string.character_race_lalafell;
            case MIQOTE -> R.string.character_race_minqote;
            case ROEGADYN -> R.string.character_race_roegadyn;
            case AURA -> R.string.character_race_aura;
            case HROTHGAR -> R.string.character_race_hrothgar;
            case VIERA -> R.string.character_race_viera;
        }));
    }
}
