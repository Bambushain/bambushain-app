package app.bambushain.my;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.Serializable;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ProfileViewModel extends ViewModel implements Serializable {
    public final MutableLiveData<String> email = new MutableLiveData<>("");
    public final MutableLiveData<String> displayName = new MutableLiveData<>("");
    public final MutableLiveData<String> discordName = new MutableLiveData<>("");
    public final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    public final MutableLiveData<Boolean> isEmailValid = new MutableLiveData<>(true);
    public final MutableLiveData<Boolean> isDiscordNameValid = new MutableLiveData<>(true);
    public final MutableLiveData<Boolean> isDisplayNameValid = new MutableLiveData<>(true);

    @Inject
    ProfileViewModel() {
    }
}
