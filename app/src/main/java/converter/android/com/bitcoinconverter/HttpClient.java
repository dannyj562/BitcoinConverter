package converter.android.com.bitcoinconverter;

/**
 * Created by Danny on 2/7/19.
 */

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;

public class HttpClient {

    private static final String TAG = "HttpClient";
    private static final String BASE_URL = "https://apiv2.bitcoinaverage.com/metadata";
    private static SyncHttpClient client;
    private List<String> cryptoNames;
    private double currencyValue;


    public HttpClient() {
        client = new SyncHttpClient();
    }

    public void getAllCryptoDetails() {
        client.get(BASE_URL, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //super.onSuccess(statusCode, headers, response);

                cryptoNames = new ArrayList<>(response.length());
                JSONArray jsonCryptoArray = response.names();
                System.out.println(jsonCryptoArray);
                for (int i=0; i<response.length(); i++) {
                    try {
                        cryptoNames.add(jsonCryptoArray.get(i).toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    public void calculateBitcoinToOtherCurrency(final String cryptoName) {
        client.get(BASE_URL, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //super.onSuccess(statusCode, headers, response);
                try {
                    System.out.println(response.getString(cryptoName));
                    String valueString = convertRegExpIntoMarketCapValue(response.getString(cryptoName).toString());
                    currencyValue = Double.valueOf(valueString);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    public String convertRegExpIntoMarketCapValue(String regExp) {
        StringBuilder sb = new StringBuilder();
        /* when data is in this format
            {"market_cap":######.####E##,"volume":###.########}
         */
        if (regExp.substring(2,12).equalsIgnoreCase("market_cap")) {
            String patternExp = "(\\d+[.]\\d+[E?]\\d*[,])";
            Pattern pattern = Pattern.compile(patternExp);
            Matcher matcher = pattern.matcher(regExp);
            while (matcher.find()) {
                System.out.println(matcher.group(0));
                sb.append(matcher.group(0));
            }
            System.out.println("String length: " + sb.length());
            sb.delete(sb.length()-1, sb.length());
            System.out.println(sb.toString());
        }
        /* when data is in this format
           {"volume":###.########, "market_cap":#######.######E###}
         */
        if (regExp.substring(2,8).equalsIgnoreCase("volume")) {
            String patternExp = "(\\d+[.]\\d+[E?]\\d*[}])";
            Pattern pattern = Pattern.compile(patternExp);
            Matcher matcher = pattern.matcher(regExp);
            while (matcher.find()) {
                System.out.println(matcher.group(0));
                sb.append(matcher.group(0));
            }
            System.out.println("String length: " + sb.length());
            sb.delete(sb.length()-1, sb.length());
            System.out.println(sb.toString());
        }
        return sb.toString();
    }

    public List<String> getCryptoNames() {
        return cryptoNames;
    }

    public double getMarketCapValue() {
        return currencyValue;
    }

    public double getCalculatedValue() {
        return 1.0/currencyValue;
    }
}
