package com.madroft.chancmg.crimecitycodes;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Chan CMG on 11/14/2016.
 */
public class addmafia extends Activity {
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String REGISTER_URL = "http://madroft.16mb.com/android_connect/add_mafia.php";
    static JSONObject jObj = null;
    Button add;
    EditText mafia;
    AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addmafia);
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-6808258907495342~5467311314");
        mAdView = (AdView) findViewById(R.id.adView1);
        add = (Button) findViewById(R.id.btnCreateProduct1);
        mafia = (EditText) findViewById(R.id.inputmafia);
        try {
            AdRequest adRequest = new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .addTestDevice("71450B2D1EA13B9C5269906E993492BC")
                    .build();

            mAdView.loadAd(adRequest);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addmafia();
            }
        });

    }

    private void addmafia() {
        String m = mafia.getText().toString();

        if (!m.isEmpty() && m.length() == 9) {
            RegisterUser ru = new RegisterUser();
            ru.execute(m);

        } else {
            Toast.makeText(getApplicationContext(), "Enter valid mafia", Toast.LENGTH_LONG).show();
        }


    }


    private class RegisterUser extends AsyncTask<String, Void, String> {
        ProgressDialog loading;
        addmafiaconnection ruc = new addmafiaconnection();


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(addmafia.this, "Please Wait", null, true, true);
            loading.setCanceledOnTouchOutside(false);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            loading.dismiss();
            try {
                jObj = new JSONObject(result);
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());
            }

            try {
                int success = jObj.getInt(TAG_SUCCESS);

                if (success == 1) {

                    Toast.makeText(getApplicationContext(), "Code Added succesfully", Toast.LENGTH_LONG).show();
                    mafia.getText().clear();


                } else if (success == 2) {
                    Toast.makeText(getApplicationContext(), "Already in top 100!!", Toast.LENGTH_LONG).show();


                } else {
                    // failed to create product
                    Toast.makeText(getApplicationContext(), "Failed to add mafia", Toast.LENGTH_LONG).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Check network connection", Toast.LENGTH_SHORT).show();
            }


        }

        @Override
        protected String doInBackground(String... params) {

            HashMap<String, String> data = new HashMap<String, String>();
            data.put("code", params[0]);
            String result = ruc.sendPostRequest(REGISTER_URL, data);

            return result;


        }
    }
}
