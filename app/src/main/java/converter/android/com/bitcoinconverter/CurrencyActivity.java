package converter.android.com.bitcoinconverter;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class CurrencyActivity extends AppCompatActivity {

    private ListView listView;
    private List<String> cryptoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency);
        listView = findViewById(R.id.listView);

        new CustomHtppRequestAsyncTask().execute();
        onListViewClicked();
    }

    void onListViewClicked() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String chosenCryptoText = listView.getItemAtPosition(position).toString();
                Intent intent = new Intent(CurrencyActivity.this, CryptoCalculationActivity.class);
                intent.putExtra("CRYPTO_NAME", chosenCryptoText);
                startActivity(intent);
            }
        });
    }


    @SuppressLint("StaticFieldLeak")
    private class CustomHtppRequestAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;

        @Override
        protected Void doInBackground(Void... voids) {
            HttpClient httpClient = new HttpClient();
            httpClient.getAllCryptoDetails();
            cryptoList = httpClient.getCryptoNames();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(CurrencyActivity.this);
            progressDialog.setMessage("Loading cryptocurrency. Please wait :)");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            ArrayAdapter arrayAdapter = new ArrayAdapter(CurrencyActivity.this,
                    R.layout.activity_listview, cryptoList);
            listView.setAdapter(arrayAdapter);

        }
    }
}
