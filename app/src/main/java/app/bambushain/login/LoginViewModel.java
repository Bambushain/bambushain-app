package app.bambushain.login;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import dagger.hilt.android.lifecycle.HiltViewModel;

import javax.inject.Inject;

@HiltViewModel
public final class LoginViewModel extends ViewModel {
    public MutableLiveData<String> email = new MutableLiveData<>("");
    public MutableLiveData<String> password = new MutableLiveData<>("");
    public MutableLiveData<String> twoFactorCode = new MutableLiveData<>("");
    public MutableLiveData<Boolean> twoFactorRequested = new MutableLiveData<>(false);

    @Inject
    LoginViewModel() {
    }
}
