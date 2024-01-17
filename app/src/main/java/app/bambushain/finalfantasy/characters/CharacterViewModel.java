package app.bambushain.finalfantasy.characters;

import android.content.Context;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import app.bambushain.R;
import app.bambushain.models.finalfantasy.CharacterRace;
import app.bambushain.models.finalfantasy.FreeCompany;
import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;

import javax.inject.Inject;
import java.util.List;

import static android.provider.Settings.System.getString;

@HiltViewModel
public class CharacterViewModel extends ViewModel {
    public MutableLiveData<Integer> id = new MutableLiveData<>(0);
    public MutableLiveData<String> name = new MutableLiveData<>("");
    public MutableLiveData<CharacterRace> race = new MutableLiveData<>();
    public MutableLiveData<String> world = new MutableLiveData<>("");
    public MutableLiveData<List<Object>> customFields = new MutableLiveData<>();
    public MutableLiveData<FreeCompany> freeCompany = new MutableLiveData<>();

    @Inject
    public CharacterViewModel(){
    }

    @Inject
    @ApplicationContext
    Context context;

    public String getRaceText(CharacterRace race) {
        switch (race) {
            case HYUR:
                return context.getString(R.string.character_race_hyur);
            case ELEZEN:
                return context.getString(R.string.character_race_elezen);
            case LALAFELL:
                return context.getString(R.string.character_race_lalafell);
            case MIQOTE:
                return context.getString(R.string.character_race_minqote);
            case ROEGADYN:
                return context.getString(R.string.character_race_roegadyn);
            case AURA:
                return context.getString(R.string.character_race_aura);
            case HROTHGAR:
                return context.getString(R.string.character_race_hrothgar);
            case VIERA:
                return context.getString(R.string.character_race_viera);
            default:
                return "";
        }
    }
}
