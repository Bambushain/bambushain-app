package app.bambushain;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewTreeObserver;
import androidx.appcompat.app.AppCompatActivity;
import app.bambushain.activities.login.LoginActivity;
import app.bambushain.api.BambooApi;
import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import lombok.val;

import javax.inject.Inject;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();

    @Inject
    BambooApi bambooApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        val content = findViewById(android.R.id.content);
        content.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                bambooApi
                        .validateToken()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            Log.d(TAG, "onCreate: Login token is valid, load start page");
                            content.getViewTreeObserver().removeOnPreDrawListener(this);
                        }, ex -> {
                            Log.d(TAG, "onCreate: Login token is invalid", ex);
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            content.getViewTreeObserver().removeOnPreDrawListener(this);
                        });

                return false;
            }
        });
    }

}