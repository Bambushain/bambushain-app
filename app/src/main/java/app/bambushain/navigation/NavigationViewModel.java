package app.bambushain.navigation;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import dagger.hilt.android.lifecycle.HiltViewModel;

import javax.inject.Inject;

@HiltViewModel
public class NavigationViewModel extends ViewModel {
    public MutableLiveData<String> email = new MutableLiveData<>("");
    public MutableLiveData<String> displayName = new MutableLiveData<>("");
    public MutableLiveData<String> discordName = new MutableLiveData<>("");
    public MutableLiveData<Boolean> isMod = new MutableLiveData<>(false);
    public MutableLiveData<Integer> id = new MutableLiveData<>(0);

    @Inject
    public NavigationViewModel() {
    }
}
