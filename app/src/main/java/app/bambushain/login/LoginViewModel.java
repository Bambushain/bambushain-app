package app.bambushain.login;

import android.content.Context;
import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import app.bambushain.R;
import app.bambushain.api.BambooApi;
import app.bambushain.models.authentication.ForgotPasswordRequest;
import app.bambushain.models.authentication.LoginRequest;
import app.bambushain.models.authentication.TwoFactorRequest;
import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;

import javax.inject.Inject;

@HiltViewModel
public final class LoginViewModel extends ViewModel {
    private static final String TAG = LoginViewModel.class.getName();
    public MutableLiveData<String> email = new MutableLiveData<>("");
    public MutableLiveData<String> password = new MutableLiveData<>("");
    public MutableLiveData<String> twoFactorCode = new MutableLiveData<>("");
    public MutableLiveData<Boolean> twoFactorRequested = new MutableLiveData<>(false);
    public MutableLiveData<Integer> errorMessage = new MutableLiveData<>(0);
    public MutableLiveData<String> apiToken = new MutableLiveData<>(null);

    @Inject
    BambooApi bambooApi;

    @Inject
    @ApplicationContext
    Context context;

    @Inject
    LoginViewModel() {
    }

    public void onLogin() {
        Log.d(TAG, "onLogin: Perform login with email " + email.getValue());
        if (Boolean.FALSE.equals(twoFactorRequested.getValue())) {
            bambooApi
                    .requestTwoFactorCode(new TwoFactorRequest(email.getValue(), password.getValue()))
                    .subscribe(() -> {
                        Log.d(TAG, "onLogin: Email and password are valid enable 2fa");
                        twoFactorRequested.setValue(true);
                    }, ex -> {
                        Log.e(TAG, "onLogin: Two factor request failed", ex);
                        errorMessage.setValue(R.string.error_login_failed_two_factor);
                    });
        } else {
            bambooApi
                    .login(new LoginRequest(email.getValue(), password.getValue(), twoFactorCode.getValue()))
                    .subscribe(res -> {
                        Log.d(TAG, "onLogin: Login successful, redirect to main view");
                        apiToken.setValue(res.getToken());
                    }, ex -> {
                        Log.e(TAG, "onLogin: Login failed", ex);
                        errorMessage.setValue(R.string.error_login_failed_login);
                    });
        }
    }

    public void onForgotPassword() {
        bambooApi
                .forgotPassword(new ForgotPasswordRequest(email.getValue()))
                .subscribe(() -> {
                    Log.d(TAG, "onForgotPassword: Password reset email sent");
                    errorMessage.setValue(R.string.success_forgot_password);
                }, ex -> {
                    Log.e(TAG, "onForgotPassword: Failed to send password reset email", ex);
                    errorMessage.setValue(R.string.error_forgot_password_failed);
                });
    }
}
