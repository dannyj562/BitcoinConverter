package converter.android.com.bitcoinconverter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {

    private static final String TAG = "SplashScreen";
    private TextView splash_screen_title;
    private final int DELAY = 4000;
    private final String namePref = "com.android.converter.namePrefs";
    private final String name = "name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        splash_screen_title = findViewById(R.id.splash_screen_title);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        splash_screen_title.startAnimation(animation);
        splash_screen_title.setVisibility(View.VISIBLE);
        checkIfCurrentUser();
    }

    void checkIfCurrentUser() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences = getSharedPreferences(namePref, MODE_PRIVATE);
                String result = sharedPreferences.getString(name, null);
                Log.e(TAG, "run: " + result);
                if (result != null)  {
                    Intent intent = new Intent(SplashScreen.this, CurrencyActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(SplashScreen.this, RegisterActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, DELAY);
    }

}
