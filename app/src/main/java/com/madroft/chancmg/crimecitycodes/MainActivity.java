package com.madroft.chancmg.crimecitycodes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity {

    CardView addmafia, viewallmafia, getvip, showvip;
    AdView mAdView;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_bookmark:
                Intent i = new Intent(MainActivity.this, donate.class);
                startActivity(i);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-6808258907495342~5467311314");
        mAdView = (AdView) findViewById(R.id.adView);
        addmafia = (CardView) findViewById(R.id.card_view1);
        viewallmafia = (CardView) findViewById(R.id.card_view2);
        getvip = (CardView) findViewById(R.id.card_view3);
        showvip = (CardView) findViewById(R.id.card_view4);

        try {
            AdRequest adRequest = new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .addTestDevice("71450B2D1EA13B9C5269906E993492BC")
                    .build();

            mAdView.loadAd(adRequest);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        addmafia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), addmafia.class);
                startActivity(i);

            }
        });


        viewallmafia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), allmafia.class);
                startActivity(i);
            }
        });

        getvip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), addvip.class);
                startActivity(i);
            }
        });

        showvip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), allvip.class);
                startActivity(i);
            }
        });


    }
}
