package app.bambushain.login;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public final class LoginViewModel extends ViewModel {
    public final MutableLiveData<String> email = new MutableLiveData<>("");
    public final MutableLiveData<String> password = new MutableLiveData<>("");
    public final MutableLiveData<String> twoFactorCode = new MutableLiveData<>("");
    public final MutableLiveData<Boolean> twoFactorRequested = new MutableLiveData<>(false);

    @Inject
    LoginViewModel() {
    }
}
