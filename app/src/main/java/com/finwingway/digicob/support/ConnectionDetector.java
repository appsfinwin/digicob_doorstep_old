package com.finwingway.digicob.support;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ConnectionDetector {
    private Context _context;
    List<String> connections;

    // flag for Internet connection status
    private Boolean isInternetPresent = false;
    private Boolean isConnectionPresent = false;
    // Connection detector class
    private ConnectionDetector cd;
    static boolean status;

    public ConnectionDetector(Context context) {
        this._context = context;
        connections = new ArrayList<String>();
    }

    ////////////////////////===============================================

    public boolean isnetAvailable() {
        boolean result;
        try {
            // creating connection detector class instance
            cd = new ConnectionDetector(_context);

            // get Internet status
            isConnectionPresent = cd.isConnectingToInternet();
            isInternetPresent = cd.hasActiveInternetConnection();
            ArrayList<String> conn = (ArrayList<String>) cd.getConnections();
            // check for Internet status
            if (isInternetPresent) {
                // Internet Connection is Present
                // make HTTP requests
                Log.e("isnetPresent if", "[www.google.com]" + conn + " Available");
                result = true;
            } else if (isConnectionPresent) {
                // Internet connection is not present
                // Ask user to connect to Internet
                Log.e("isConnection else if", "No Internet Connection .. You have " + conn + " Available");
                result = true;
            } else {
                // Internet connection is not present
                // Ask user to connect to Internet
                Log.e("nothing else ", "You don't have internet connection check for Mobile Data or WiFi is on.");
                result = false;
            }

        } catch (Exception e) {
            result = true;
        }
        return result;
    }

    ////////////////////////===============================================

    public boolean isConnectingToInternet() {
        boolean internetState = false;
        ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        connections.add(info[i].getTypeName());
                        internetState = true;

                    }

        }
        return internetState;
    }

    public List<String> getConnections() {
        return connections;
    }

    public boolean hasActiveInternetConnection() {
        final boolean[] rtn = new boolean[1];
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = Message.obtain();
                try {
                    HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
                    urlc.setRequestProperty("User-Agent", "Test");
                    urlc.setRequestProperty("Connection", "close");
                    urlc.setConnectTimeout(1500);
                    urlc.connect();
                    rtn[0] = urlc.getResponseCode() == 200;
                } catch (IOException ignored) {
                }
            }
        });
        t.start(); // spawn thread
        try {
            t.join();  // wait for thread to finish
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return rtn[0];
    }

//        new Thread() {
//            public void run() {
//                Message msg = Message.obtain();
//                try {
//                    HttpURLConnection urlc = (HttpURLConnection) (new URL(
//                            "http://www.google.com").openConnection());
//                    urlc.setRequestProperty("User-Agent", "Test");
//                    urlc.setRequestProperty("Connection", "close");
//                    urlc.setConnectTimeout(1500);
//                    urlc.connect();
//                    boolean status = urlc.getResponseCode() == 200;
//                    Bundle b = new Bundle();
//                    b.putBoolean("status", status);
//                    msg.setData(b);
//                } catch (IOException e) {
//                }
//                messageHandler.sendMessage(msg);
//            }
//        }.start();

//        return status;
//    }

//    @SuppressLint("HandlerLeak")
//    private static Handler messageHandler = new Handler() {
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            status = msg.getData().getBoolean("status");
//        }
//    };


}











