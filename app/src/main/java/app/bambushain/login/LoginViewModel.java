package app.bambushain.login;

import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import app.bambushain.R;
import app.bambushain.api.BambooApi;
import app.bambushain.models.ForgotPasswordRequest;
import app.bambushain.models.LoginRequest;
import app.bambushain.models.TwoFactorRequest;
import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

import javax.inject.Inject;

@HiltViewModel
public final class LoginViewModel extends ViewModel {
    private static final String TAG = LoginViewModel.class.getName();
    public MutableLiveData<String> email = new MutableLiveData<>("");
    public MutableLiveData<String> password = new MutableLiveData<>("");
    public MutableLiveData<String> twoFactorCode = new MutableLiveData<>("");
    public MutableLiveData<Boolean> twoFactorRequested = new MutableLiveData<>(false);
    public MutableLiveData<Integer> toastMessage = new MutableLiveData<>(0);
    public MutableLiveData<String> apiToken = new MutableLiveData<>(null);
    @Inject
    BambooApi bambooApi;

    @Inject
    LoginViewModel() {
    }

    public void onLogin() {
        Log.d(TAG, "onLogin: Perform login with email " + email.getValue());
        if (Boolean.FALSE.equals(twoFactorRequested.getValue())) {
            bambooApi
                    .requestTwoFactorCode(new TwoFactorRequest(email.getValue(), password.getValue()))
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {
                        Log.d(TAG, "onLogin: Email and password are valid enable 2fa");
                        twoFactorRequested.setValue(true);
                    }, ex -> {
                        Log.e(TAG, "onLogin: Two factor request failed", ex);
                        toastMessage.setValue(R.string.error_login_failed_two_factor);
                    });
        } else {
            bambooApi
                    .login(new LoginRequest(email.getValue(), password.getValue(), twoFactorCode.getValue()))
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(res -> {
                        Log.d(TAG, "onLogin: Login successful, redirect to main view");
                        apiToken.setValue(res.getToken());
                    }, ex -> {
                        Log.e(TAG, "onLogin: Login failed", ex);
                        toastMessage.setValue(R.string.error_login_failed_login);
                    });
        }
    }

    public void onForgotPassword() {
        bambooApi
                .forgotPassword(new ForgotPasswordRequest(email.getValue()))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    Log.d(TAG, "onForgotPassword: Password reset email sent");
                    toastMessage.setValue(R.string.success_forgot_password);
                }, ex -> {
                    Log.e(TAG, "onForgotPassword: Failed to send password reset email", ex);
                    toastMessage.setValue(R.string.error_forgot_password_failed);
                });
    }
}
