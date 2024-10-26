package app.bambushain.my;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ChangeMyPasswordViewModel extends ViewModel {
    public final MutableLiveData<String> oldPassword = new MutableLiveData<>("");
    public final MutableLiveData<String> newPassword = new MutableLiveData<>("");

    @Inject
    public ChangeMyPasswordViewModel() {
    }
}
