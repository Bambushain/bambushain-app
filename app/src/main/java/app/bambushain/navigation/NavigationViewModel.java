package app.bambushain.navigation;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import app.bambushain.api.BambooApi;
import app.bambushain.models.User;
import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

import javax.inject.Inject;

@HiltViewModel
public class NavigationViewModel extends ViewModel {
    public MutableLiveData<User> profile = new MutableLiveData<>(new User());

    @Inject
    BambooApi bambooApi;

    @Inject
    public NavigationViewModel() {
    }

    public void onLogout() {
        bambooApi
                .logout()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
        profile.setValue(null);

    }
}
