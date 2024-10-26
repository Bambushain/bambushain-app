package app.bambushain.navigation;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class NavigationViewModel extends ViewModel {
    public final MutableLiveData<String> email = new MutableLiveData<>("");
    public final MutableLiveData<String> displayName = new MutableLiveData<>("");
    public final MutableLiveData<String> discordName = new MutableLiveData<>("");
    public final MutableLiveData<Boolean> isMod = new MutableLiveData<>(false);
    public final MutableLiveData<Integer> id = new MutableLiveData<>(0);

    @Inject
    public NavigationViewModel() {
    }
}
