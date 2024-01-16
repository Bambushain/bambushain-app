package app.bambushain.finalfantasy.characters;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import app.bambushain.models.finalfantasy.CharacterRace;
import app.bambushain.models.finalfantasy.FreeCompany;
import dagger.hilt.android.lifecycle.HiltViewModel;

import javax.inject.Inject;
import java.util.List;

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
}
