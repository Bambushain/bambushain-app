package app.bambushain.navigation;

import android.content.Context;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.preference.PreferenceManager;
import app.bambushain.api.BambooApi;
import app.bambushain.models.User;
import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import lombok.val;

import javax.inject.Inject;

@HiltViewModel
public class NavigationViewModel extends ViewModel {
    public MutableLiveData<User> profile = new MutableLiveData<>(new User());

    @Inject
    BambooApi bambooApi;

    @ApplicationContext
    @Inject
    Context context;

    @Inject
    public NavigationViewModel() {
    }

    public void onLogout() {
        bambooApi
                .logout()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());

        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPrefs
                .edit()
                .remove(context.getString(app.bambushain.api.R.string.bambooAuthenticationToken))
                .apply();
        profile.setValue(null);
    }
}
