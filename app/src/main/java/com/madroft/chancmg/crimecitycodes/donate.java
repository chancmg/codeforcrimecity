package com.madroft.chancmg.crimecitycodes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;

import billing.IabHelper;
import billing.IabResult;
import billing.Inventory;
import billing.Purchase;

/**
 * Created by Chan CMG on 11/19/2016.
 */
public class donate extends Activity implements View.OnClickListener {

    static final String TAG = "mafia";
    static final String SKU_TWO = "two_d", SKU_FIVE = "five_d", SKU_TEN = "ten_d", SKU_TWY = "twenty_d";
    static final int RC_REQUEST = 10001;
    CardView td, fd, tnd, twd;
    IabHelper mHelper;
    // Called when consumption is complete
    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
        public void onConsumeFinished(Purchase purchase, IabResult result) {
            Log.d(TAG, "Consumption finished. Purchase: " + purchase + ", result: " + result);

            // if we were disposed of in the meantime, quit.
            if (mHelper == null) return;

            // We know this is the "gas" sku because it's the only one we consume,
            // so we don't check which sku was consumed. If you have more than one
            // sku, you probably should check...
            if (result.isSuccess()) {
                // successfully consumed, so we apply the effects of the item in our
                // game world's logic, which in our case means filling the gas tank a bit
                Log.d(TAG, "Consumption successful. Provisioning.");


                alert("Thank you very much:-)");


            } else {
                complain("Error while consuming: " + result);
            }

            Log.d(TAG, "End consumption flow.");
        }
    };
    // Listener that's called when we finish querying the items and subscriptions we own
    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            Log.d(TAG, "Query inventory finished.");

            // Have we been disposed of in the meantime? If so, quit.
            if (mHelper == null) return;

            // Is it a failure?
            if (result.isFailure()) {
                complain("Failed to query inventory: " + result);
                return;
            }

            Log.d(TAG, "Query inventory was successful.");

            // Check for gas delivery -- if we own gas, we should fill up the tank immediately
            Purchase gasPurchase1 = inventory.getPurchase(SKU_TWO);
            if (gasPurchase1 != null && verifyDeveloperPayload(gasPurchase1)) {
                Log.d(TAG, "We have gas. Consuming it.");
                mHelper.consumeAsync(inventory.getPurchase(SKU_TWO), mConsumeFinishedListener);
                return;

            }

            Purchase gasPurchase2 = inventory.getPurchase(SKU_FIVE);
            if (gasPurchase2 != null && verifyDeveloperPayload(gasPurchase2)) {
                Log.d(TAG, "We have gas. Consuming it.");
                mHelper.consumeAsync(inventory.getPurchase(SKU_FIVE), mConsumeFinishedListener);
                return;

            }

            Purchase gasPurchase3 = inventory.getPurchase(SKU_TEN);
            if (gasPurchase3 != null && verifyDeveloperPayload(gasPurchase3)) {
                Log.d(TAG, "We have gas. Consuming it.");
                mHelper.consumeAsync(inventory.getPurchase(SKU_TEN), mConsumeFinishedListener);
                return;

            }

            Purchase gasPurchase4 = inventory.getPurchase(SKU_TWY);
            if (gasPurchase4 != null && verifyDeveloperPayload(gasPurchase4)) {
                Log.d(TAG, "We have gas. Consuming it.");
                mHelper.consumeAsync(inventory.getPurchase(SKU_TWY), mConsumeFinishedListener);
                return;

            }
        }
    };
    // Callback for when a purchase is finished
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            Log.d(TAG, "Purchase finished: " + result + ", purchase: " + purchase);

            // if we were disposed of in the meantime, quit.
            if (mHelper == null) return;

            if (result.isFailure()) {
                complain("Error purchasing: " + result);

                return;
            }
            if (!verifyDeveloperPayload(purchase)) {
                complain("Error purchasing. Authenticity verification failed.");

                return;
            }

            Log.d(TAG, "Purchase successful.");

            if (purchase.getSku().equals(SKU_TWO)) {
                // bought 1/4 tank of gas. So consume it.
                Log.d(TAG, "Purchase is gas. Starting gas consumption.");
                mHelper.consumeAsync(purchase, mConsumeFinishedListener);
            } else if (purchase.getSku().equals(SKU_FIVE)) {
                // bought 1/4 tank of gas. So consume it.
                Log.d(TAG, "Purchase is gas. Starting gas consumption.");
                mHelper.consumeAsync(purchase, mConsumeFinishedListener);
            } else if (purchase.getSku().equals(SKU_TEN)) {
                // bought 1/4 tank of gas. So consume it.
                Log.d(TAG, "Purchase is gas. Starting gas consumption.");
                mHelper.consumeAsync(purchase, mConsumeFinishedListener);
            } else if (purchase.getSku().equals(SKU_TWY)) {
                // bought 1/4 tank of gas. So consume it.
                Log.d(TAG, "Purchase is gas. Starting gas consumption.");
                mHelper.consumeAsync(purchase, mConsumeFinishedListener);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donate);

        td = (CardView) findViewById(R.id.twod);
        fd = (CardView) findViewById(R.id.fived);
        tnd = (CardView) findViewById(R.id.tend);
        twd = (CardView) findViewById(R.id.twd);


        //init purchase

        String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArb5/94rvqJ1Is0zu+3BBMz/kTF3J5ChjTJ9Jf3vLun13CWOKp/kOW6t3k1QBzf4cg95itA6RXmpi1DDgibeYltTFADaPSA9mqV7CA/Bh+CPgKqcWyQy05JcrWuFtbNTojiyHPZOoXBQl8ZTADjWXMRsX95V9kzWef3Bj7pfJYuXqmmWSH+gKwe5qfx0kWK4rJw62PZ7TXxtXqKOV4+c/e4sYp//u4wZTuTtC0nPWFLpLwcKTjK2nJJYT7ceXzj9sI10FeX4uIjCYBPRhNIeaAzAfeBQ1vQT3ODzrNpoXwD6azIXoXSmMgLaBBab8e0az1AydThh5a9PbRJiHaJP06wIDAQAB";

        Log.d(TAG, "Creating IAB helper.");
        mHelper = new IabHelper(this, base64EncodedPublicKey);
        mHelper.enableDebugLogging(true);

        Log.d(TAG, "Starting setup.");
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                Log.d(TAG, "Setup finished.");

                if (!result.isSuccess()) {
                    // Oh noes, there was a problem.
                    complain("Problem setting up in-app billing: " + result);
                    return;
                }

                // Have we been disposed of in the meantime? If so, quit.
                if (mHelper == null) return;

                // IAB is fully set up. Now, let's get an inventory of stuff we own.
                Log.d(TAG, "Setup successful. Querying inventory.");
                mHelper.queryInventoryAsync(mGotInventoryListener);
            }
        });

        //card listeners

        td.setOnClickListener(this);
        fd.setOnClickListener(this);
        tnd.setOnClickListener(this);
        twd.setOnClickListener(this);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);
        if (mHelper == null) return;

        // Pass on the activity result to the helper for handling
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            // not handled, so handle it ourselves (here's where you'd
            // perform any handling of activity results not related to in-app
            // billing...
            super.onActivityResult(requestCode, resultCode, data);
        } else {
            Log.d(TAG, "onActivityResult handled by IABUtil.");
        }
    }

    /**
     * Verifies the developer payload of a purchase.
     */
    boolean verifyDeveloperPayload(Purchase p) {
        String payload = p.getDeveloperPayload();

        /*
         * TODO: verify that the developer payload of the purchase is correct. It will be
         * the same one that you sent when initiating the purchase.
         *
         * WARNING: Locally generating a random string when starting a purchase and
         * verifying it here might seem like a good approach, but this will fail in the
         * case where the user purchases an item on one device and then uses your app on
         * a different device, because on the other device you will not have access to the
         * random string you originally generated.
         *
         * So a good developer payload has these characteristics:
         *
         * 1. If two different users purchase an item, the payload is different between them,
         *    so that one user's purchase can't be replayed to another user.
         *
         * 2. The payload must be such that you can verify it even when the app wasn't the
         *    one who initiated the purchase flow (so that items purchased by the user on
         *    one device work on other devices owned by the user).
         *
         * Using your own server to store and verify developer payloads across app
         * installations is recommended.
         */

        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // very important:
        Log.d(TAG, "Destroying helper.");
        if (mHelper != null) {
            mHelper.dispose();
            mHelper = null;
        }
    }

    void complain(String message) {
        Log.e(TAG, "**** Mafia Error: " + message);
        alert("Error: " + message);
    }

    void alert(String message) {
        AlertDialog.Builder bld = new AlertDialog.Builder(this);
        bld.setMessage(message);
        bld.setNeutralButton("OK", null);
        Log.d(TAG, "Showing alert dialog: " + message);
        bld.create().show();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.twod:
                Log.d(TAG, "Buy vip button clicked.");

                Log.d(TAG, "Launching purchase flow for gas.");

        /* TODO: for security, generate your payload here for verification. See the comments on
         *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
         *        an empty string, but on a production app you should carefully generate this. */
                String payload = "";

                mHelper.launchPurchaseFlow(this, SKU_TWO, RC_REQUEST,
                        mPurchaseFinishedListener, payload);
                break;
            case R.id.fived:
                Log.d(TAG, "Buy vip button clicked.");

                Log.d(TAG, "Launching purchase flow for gas.");

        /* TODO: for security, generate your payload here for verification. See the comments on
         *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
         *        an empty string, but on a production app you should carefully generate this. */
                String payload1 = "";

                mHelper.launchPurchaseFlow(this, SKU_FIVE, RC_REQUEST,
                        mPurchaseFinishedListener, payload1);
                break;
            case R.id.tend:
                Log.d(TAG, "Buy vip button clicked.");

                Log.d(TAG, "Launching purchase flow for gas.");

        /* TODO: for security, generate your payload here for verification. See the comments on
         *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
         *        an empty string, but on a production app you should carefully generate this. */
                String payload2 = "";

                mHelper.launchPurchaseFlow(this, SKU_TEN, RC_REQUEST,
                        mPurchaseFinishedListener, payload2);
                break;
            case R.id.twd:
                Log.d(TAG, "Buy vip button clicked.");

                Log.d(TAG, "Launching purchase flow for gas.");

        /* TODO: for security, generate your payload here for verification. See the comments on
         *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
         *        an empty string, but on a production app you should carefully generate this. */
                String payload3 = "";

                mHelper.launchPurchaseFlow(this, SKU_TWY, RC_REQUEST,
                        mPurchaseFinishedListener, payload3);
                break;


        }
    }
}

