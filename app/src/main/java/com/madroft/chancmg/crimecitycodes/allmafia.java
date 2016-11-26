package com.madroft.chancmg.crimecitycodes;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chan CMG on 11/14/2016.
 */
public class allmafia extends Activity implements AdapterView.OnItemClickListener {

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String REGISTER_URL = "http://madroft.16mb.com/android_connect/getallmafia.php";
    static JSONObject jObj = null;
    private ListView lv;
    private List<String> contactList;

    public static String getData(String stringUrl) {
        BufferedReader reader = null;
        try {
            URL url = new URL(stringUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            StringBuilder sb = new StringBuilder();

            InputStreamReader is = new InputStreamReader(connection.getInputStream());

            reader = new BufferedReader(is);

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }

            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.allmafia);

        // Get listview
        lv = (ListView) findViewById(R.id.list);

        RegisterUser ru = new RegisterUser();
        ru.execute(REGISTER_URL);
        lv.setOnItemClickListener(this);

    }

    public void ContactAdapter(String json) {
        contactList = new JsonParser().Parse(json);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, contactList);
        lv.setAdapter(adapter);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            String item = lv.getItemAtPosition(position).toString();
            doCopy(item);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

    public void doCopy(String text) {
        try {
            if (android.os.Build.VERSION.SDK_INT < 11) {
                android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                clipboard.setText(text);
            } else {
                android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                android.content.ClipData clip = android.content.ClipData.newPlainText("WordKeeper", text);
                clipboard.setPrimaryClip(clip);
            }
            Toast.makeText(getApplicationContext(), "Mafia copied to clipboard", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error copying text to clipboard", Toast.LENGTH_SHORT).show();
        }
    }

    private class RegisterUser extends AsyncTask<String, Void, String> {
        ProgressDialog loading;
        addmafiaconnection ruc = new addmafiaconnection();


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(allmafia.this, "Please Wait", null, true, true);
            loading.setCanceledOnTouchOutside(false);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                jObj = new JSONObject(s);
                int success = jObj.getInt(TAG_SUCCESS);

                if (success == 1) {
                    ContactAdapter(s);
                } else {
                    // failed to create product
                    Toast.makeText(getApplicationContext(), "Failed to get mafia list", Toast.LENGTH_LONG).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Check network connection", Toast.LENGTH_SHORT).show();
            }
            loading.dismiss();

        }

        @Override
        protected String doInBackground(String... params) {

            String result = null;

            BufferedReader reader = null;
            try {
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                StringBuilder sb = new StringBuilder();

                InputStreamReader is = new InputStreamReader(connection.getInputStream());

                reader = new BufferedReader(is);

                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }

                result = sb.toString();
            } catch (Exception e) {
                e.printStackTrace();

            } finally {
                try {
                    reader.close();
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }


            return result;


        }
    }

    private class JsonParser {

        public List Parse(String json) {
            String s[];
            try {
                JSONObject jsonObject = new JSONObject(json);
                List contactList = new ArrayList<>();
                JSONArray jb = jsonObject.getJSONArray("codes");
                s = new String[jb.length()];
                for (int i = 0; i < jb.length(); i++) {

                    s[i] = jb.getString(i);
                }

                for (int j = s.length - 1; j >= 0; j--) {
                    contactList.add(s[j]);
                }
                return contactList;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }

        }

    }
}
