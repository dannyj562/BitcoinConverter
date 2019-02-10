package converter.android.com.bitcoinconverter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class CryptoCalculationActivity extends AppCompatActivity {

    private TextView cryptoMarketValueTextView;
    private TextView calculationInfoTextView;
    private TextView calculationTextView;
    private String cryptoName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crypto_calculation);
        Intent intent = getIntent();
        cryptoName = intent.getStringExtra("CRYPTO_NAME");

        cryptoMarketValueTextView = findViewById(R.id.cryptoMarketValueTextView);

        calculationInfoTextView = findViewById(R.id.calculationInfoTextView);
        calculationInfoTextView.setText("1 BTC --> " + cryptoName + "\nMarket Cap Value");

        calculationTextView = findViewById(R.id.calculationTextView);

        new CalculationAsyncTask().execute();
    }

    void showMarketCapValue(double total) {
        cryptoMarketValueTextView.setText(cryptoName + " Market Cap Value" + String.valueOf(total));
    }

    void showCalculation(double total) {
        calculationTextView.setText(String.valueOf(total));
    }

    private class CalculationAsyncTask extends AsyncTask<Void, Void, Void> {

        HttpClient httpClient;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(CryptoCalculationActivity.this);
            progressDialog.setTitle("Calculating . . .");
            progressDialog.setMessage("Please wait");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            httpClient = new HttpClient();
            httpClient.calculateBitcoinToOtherCurrency(cryptoName);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            showMarketCapValue(httpClient.getMarketCapValue());
            showCalculation(httpClient.getCalculatedValue());
        }
    }
}
