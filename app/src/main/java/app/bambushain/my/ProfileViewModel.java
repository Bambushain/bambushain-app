package app.bambushain.my;

import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import app.bambushain.R;
import app.bambushain.api.BambooApi;
import app.bambushain.models.exception.BambooException;
import app.bambushain.models.exception.ErrorType;
import app.bambushain.models.my.UpdateMyProfile;
import dagger.hilt.android.lifecycle.HiltViewModel;
import lombok.val;

import javax.inject.Inject;

@HiltViewModel
public class ProfileViewModel extends ViewModel {
    private static final String TAG = ProfileViewModel.class.getName();
    public MutableLiveData<String> email = new MutableLiveData<>("");
    public MutableLiveData<String> displayName = new MutableLiveData<>("");
    public MutableLiveData<String> discordName = new MutableLiveData<>("");
    public MutableLiveData<Boolean> isLoading = new MutableLiveData<>(true);
    public MutableLiveData<Integer> errorMessage = new MutableLiveData<>(0);
    public MutableLiveData<Integer> successMessage = new MutableLiveData<>(0);
    public MutableLiveData<Boolean> isEmailValid = new MutableLiveData<>(true);
    public MutableLiveData<Boolean> isDiscordNameValid = new MutableLiveData<>(true);
    public MutableLiveData<Boolean> isDisplayNameValid = new MutableLiveData<>(true);

    @Inject
    BambooApi bambooApi;

    @Inject
    ProfileViewModel() {
    }

    public void loadProfile() {
        Log.d(TAG, "loadProfile: Load current users profile");
        bambooApi
                .getMyProfile()
                .subscribe(profile -> {
                    Log.d(TAG, "loadProfile: Loaded profile successfully");
                    email.setValue(profile.getEmail());
                    displayName.setValue(profile.getDisplayName());
                    discordName.setValue(profile.getDiscordName());
                    isLoading.setValue(false);
                }, ex -> {
                    Log.e(TAG, "loadProfile: Failed to load profile", ex);
                    errorMessage.setValue(R.string.error_profile_update_loading_failed);
                    isLoading.setValue(false);
                });

    }

    public void onUpdateMyProfile() {
        val profile = new UpdateMyProfile(email.getValue(), displayName.getValue(), discordName.getValue());
        Log.d(TAG, "updateMyProfile: Perform profile update " + profile);
        isLoading.setValue(true);
        bambooApi
                .updateMyProfile(profile)
                .subscribe(() -> {
                    Log.d(TAG, "updateMyProfile: Update successful");
                    successMessage.setValue(R.string.success_profile_update);
                    isLoading.setValue(false);
                }, ex -> {
                    Log.e(TAG, "updateMyProfile: Update failed", ex);
                    val bambooEx = (BambooException) ex;
                    if (bambooEx.getErrorType() == ErrorType.ExistsAlready) {
                        errorMessage.setValue(R.string.error_profile_update_exists);
                    } else {
                        errorMessage.setValue(R.string.error_profile_update_failed);
                    }
                    isLoading.setValue(false);
                });
    }
}
