package app.bambushain.my;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import dagger.hilt.android.lifecycle.HiltViewModel;

import javax.inject.Inject;

@HiltViewModel
public class ProfileViewModel extends ViewModel {
    private static final String TAG = ProfileViewModel.class.getName();
    public MutableLiveData<String> email = new MutableLiveData<>("");
    public MutableLiveData<String> displayName = new MutableLiveData<>("");
    public MutableLiveData<String> discordName = new MutableLiveData<>("");
    public MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    public MutableLiveData<Boolean> isEmailValid = new MutableLiveData<>(true);
    public MutableLiveData<Boolean> isDiscordNameValid = new MutableLiveData<>(true);
    public MutableLiveData<Boolean> isDisplayNameValid = new MutableLiveData<>(true);

    @Inject
    ProfileViewModel() {
    }
}
