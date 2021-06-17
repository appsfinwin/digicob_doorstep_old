package com.finwingway.digicob;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by AnVin on 4/28/2017.
 */

public class ConnectivityChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("app", "Network connectivity change");
        if (intent.getExtras() != null) {

            NetworkInfo ni = (NetworkInfo) intent.getExtras().get(ConnectivityManager.EXTRA_NETWORK_INFO);
            if (ni != null && ni.getState() == NetworkInfo.State.CONNECTED) {
                if(isInternetAvailable()){
                    Toast.makeText(context, "You Are Connected to Internet", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "There is NO INTERNET Connection!", Toast.LENGTH_LONG).show();
                }
                Log.i("app", "Network " + ni.getTypeName() + " connected");
            } else if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE)) {
                Log.d("app", "There's no network connectivity");
                Toast.makeText(context, "You Are NOT CONNECTED to Internet", Toast.LENGTH_LONG).show();
            }
        }
    }
    private boolean isInternetAvailable()
    {
        try
        {
            return (Runtime.getRuntime().exec ("ping -c 1 google.com").waitFor() == 0);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return false;
    }
}