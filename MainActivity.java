package com.example.radhikasriram.cs125final;

import android.content.Intent;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {
    final String apple = "AAPL";
    final String ms = "MSFT";
    final String amazon = "AMZN";
    final String alibaba = "BABA";
    final String facebook = "FB";
    final String gm = "GM";
    final String nasdaq = "NASDAQ";
    final String sandp = "SandP";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button AAPL = (Button)findViewById(R.id.AAPL);
        AAPL.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                startActvity(apple);
            }
        });
        Button AMZN = (Button)findViewById(R.id.AMZN);
        AMZN.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0) {
                startActvity(amazon);
            }
        });
        Button BABA = (Button)findViewById(R.id.BABA);
        BABA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                startActvity(alibaba);
            }
        });
        Button MSFT = (Button)findViewById(R.id.MSFT);
        MSFT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                startActvity(ms);
            }
        });
        Button FB = (Button)findViewById(R.id.FB);
        FB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActvity(facebook);
            }
        });
        Button GM = (Button)findViewById(R.id.GM);
        GM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActvity(gm);
            }
        });
        Button NASDAQ = (Button)findViewById(R.id.NASDAQ);
        NASDAQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActvity(nasdaq);
            }
        });
        Button SandP = (Button)findViewById(R.id.SandP);
        SandP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActvity(sandp);
            }
        });
        final Button search = (Button)findViewById(R.id.Search);
        final EditText text = (EditText)findViewById(R.id.editText2);
        search.setOnClickListener(new View.OnClickListener() {
            String value = text.getText().toString();
            @Override
            public void onClick(View v) {
                startActvity(value);
            }
        });

    }

    private void startActvity(String symbol) {
        Intent myIntent = new Intent(MainActivity.this,
                StockView.class);
        Bundle b = new Bundle();
        b.putString("key", symbol);
        myIntent.putExtras(b);
        startActivity(myIntent);
    }
    private static String convert(InputStream a) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(a));
        StringBuilder s = new StringBuilder();

        String input = null;
        try {
            while ((input = reader.readLine()) != null) {
                s.append(input + "\n");

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                a.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return s.toString();
    }

    public static String[] connect(String url) {
        HttpClient httpClient = new DefaultHttpClient();
        String[] newString = new String[5];

        Httpget httpget = new HttpGet(url);
        HttpResponse response;
        try {
            response = httpClient.execute(httpget);
            Log.d(Tag, response.getStatusLine().toString());
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                InputStream instream = entity.getContent();
                String result = convertStreamToString(instream);
                result = result.replace("parseExchangeRate(", "").replace("); ", "");
                Log.d(Tag, result);

                JSONObject json = new JSONObject(result);
                Log.d(Tag,"<jsonobject>\n" + json.toString() + "\n</jsonobject>");
                JSONObject query = json.getJSONObject("query");
                Log.d(Tag, query.toString());
                JSONObject results = query.getJSONObject("results");
                Log.d(Tag, results.toString());
                JSONObject quote = results.getJSONObject("row");
                Log.d(Tag, quote.toString());
                for (int i = 0; i < quote.length(); i++) {
                    String symbol = quote.getString("rate");
                    newString[0] = symbol;
                    String dayslow = quote.getString("DaysLow");
                    newString[1]=dayslow;
                    // tv1.setText(quote.getString("DaysLow"));
                    newString[2]= quote.getString("DaysHigh");
                    newString[3]= quote.getString("Open");
                    newString[4]= quote.getString("Change");
                }
                Log.d(TAG, "<jsonobject>\n" + json.toString() + "\n</jsonobject>");
                instream.close();
            }
        } catch (ClientProtocolException e) {
            Log.d(Tag, "ClientProtocolException");
            e.printStackTrace();
        } catch (IOException e) {
            Log.d(Tag, "IOException " + e.getMessage());
            e.printStackTrace();
        } catch (JSONException e) {
            Log.d(Tag, "JSONException " + e.getMessage());
            e.getMessage();
        }
        return newString;
    }

}
