package converter.android.com.bitcoinconverter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    private EditText nameEditText;
    private Button registerButton;
    private final String namePref = "com.android.converter.namePrefs";
    private final String name = "name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        nameEditText = findViewById(R.id.nameEditText);
        registerButton = findViewById(R.id.registerButton);
        onButtonClicked();
    }

    void onButtonClicked() {
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nameEditText.getText().toString().trim().isEmpty() ||
                        nameEditText.getText().toString().equals(" ")) {
                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                    nameEditText.startAnimation(animation);
                } else {
                    new CustomAsyncTask().execute(nameEditText.getText().toString().trim());
                }
            }
        });
    }

    private class CustomAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Intent intent = new Intent(RegisterActivity.this, CurrencyActivity.class);
            startActivity(intent);

            finish();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... strings) {
            SharedPreferences sharedPreferences = getSharedPreferences(namePref, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(name, nameEditText.getText().toString().trim());
            editor.commit();
            Log.e(TAG, "doInBackground: " + sharedPreferences.toString());
            return strings[0];
        }
    }
}
