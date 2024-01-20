package app.bambushain.bamboo.pandas;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import dagger.hilt.android.lifecycle.HiltViewModel;

import javax.inject.Inject;

@HiltViewModel
public class PandaViewModel extends ViewModel {
    public final MutableLiveData<Integer> id = new MutableLiveData<>(0);
    public final MutableLiveData<String> email = new MutableLiveData<>("");
    public final MutableLiveData<String> displayName = new MutableLiveData<>("");
    public final MutableLiveData<String> discordName = new MutableLiveData<>("");
    public final MutableLiveData<Boolean> isMod = new MutableLiveData<>(false);
    public final MutableLiveData<Boolean> appTotpEnabled = new MutableLiveData<>(false);
    public final MutableLiveData<Boolean> canEdit = new MutableLiveData<>(false);
    public final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    public final MutableLiveData<Boolean> isEmailValid = new MutableLiveData<>(true);
    public final MutableLiveData<Boolean> isDiscordNameValid = new MutableLiveData<>(true);
    public final MutableLiveData<Boolean> isDisplayNameValid = new MutableLiveData<>(true);

    @Inject
    PandaViewModel() {
    }
}
