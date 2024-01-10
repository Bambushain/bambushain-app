package app.bambushain.bamboo.pandas;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import dagger.hilt.android.lifecycle.HiltViewModel;

import javax.inject.Inject;

@HiltViewModel
public class PandaViewModel extends ViewModel {
    public MutableLiveData<Integer> id = new MutableLiveData<>(0);
    public MutableLiveData<String> email = new MutableLiveData<>("");
    public MutableLiveData<String> displayName = new MutableLiveData<>("");
    public MutableLiveData<String> discordName = new MutableLiveData<>("");
    public MutableLiveData<Boolean> isMod = new MutableLiveData<>(false);
    public MutableLiveData<Boolean> appTotpEnabled = new MutableLiveData<>(false);
    public MutableLiveData<Boolean> canEdit = new MutableLiveData<>(false);

    @Inject
    PandaViewModel() {
    }
}
