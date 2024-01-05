package app.bambushain.my;

import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import app.bambushain.R;
import app.bambushain.api.BambooApi;
import app.bambushain.models.UpdateMyProfile;
import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

import javax.inject.Inject;

@HiltViewModel
public class ProfileViewModel extends ViewModel {
    private static final String TAG = ProfileViewModel.class.getName();
    public MutableLiveData<UpdateMyProfile> profile = new MutableLiveData<>(new UpdateMyProfile());
    public MutableLiveData<Boolean> isLoading = new MutableLiveData<>(true);
    public MutableLiveData<Integer> toastMessage = new MutableLiveData<>(0);

    @Inject
    BambooApi bambooApi;

    @Inject
    ProfileViewModel() {
    }

    public void loadProfile() {
        Log.d(TAG, "loadProfile: Load current users profile");
        bambooApi
                .getMyProfile()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(profile -> {
                    Log.d(TAG, "loadProfile: Loaded profile successfully");
                    this.profile.setValue(new UpdateMyProfile(profile.getEmail(), profile.getDisplayName(), profile.getDiscordName()));
                    isLoading.setValue(false);
                }, ex -> {
                    Log.e(TAG, "loadProfile: Failed to load profile", ex);
                    toastMessage.setValue(R.string.error_profile_update_loading_failed);
                    isLoading.setValue(false);
                });

    }

    public void onUpdateMyProfile() {
        Log.d(TAG, "updateMyProfile: Perform profile update " + profile.getValue().toString());
        isLoading.setValue(true);
        bambooApi
                .updateMyProfile(profile.getValue())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    Log.d(TAG, "updateMyProfile: Update successful");
                    toastMessage.setValue(R.string.success_profile_update);
                    isLoading.setValue(false);
                }, ex -> {
                    Log.e(TAG, "updateMyProfile: Update failed", ex);
                    toastMessage.setValue(R.string.error_profile_update_failed);
                    isLoading.setValue(false);
                });
    }
}
