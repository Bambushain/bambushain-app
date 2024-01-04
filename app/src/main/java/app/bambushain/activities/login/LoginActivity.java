package app.bambushain.activities.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import app.bambushain.MainActivity;
import app.bambushain.R;
import app.bambushain.databinding.ActivityLoginBinding;
import app.bambushain.viewmodels.login.LoginViewModel;
import dagger.hilt.android.AndroidEntryPoint;
import lombok.val;

@AndroidEntryPoint
public class LoginActivity extends AppCompatActivity {

    LoginViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        ActivityLoginBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        viewModel.toastMessage.observe(this, value -> {
            if (value != 0) {
                Toast.makeText(this, value, Toast.LENGTH_LONG).show();
            }
        });
        viewModel.apiToken.observe(this, value -> {
            if (value != null && !value.isEmpty()) {
                val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
                sharedPrefs.edit().putString(getString(app.bambushain.api.R.string.bambooAuthenticationToken), value).apply();
                startActivity(new Intent(this, MainActivity.class));
            }
        });
    }
}