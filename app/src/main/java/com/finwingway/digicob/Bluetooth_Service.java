package com.finwingway.digicob;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

/**
 * Created by AnVin on 5/22/2017.
 */

public class Bluetooth_Service extends Service {
    Context context;
    static final public String COPA_RESULT = "com.finwingway.digicob.Bluetooth_Service.REQUEST_PROCESSED";
    static final public String COPA_MESSAGE = "com.finwingway.digicob.Bluetooth_Service.COPA_MSG";
    LocalBroadcastManager broadcaster;
    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context=this;
        //Toast.makeText(context, "SERVICE STARTED", Toast.LENGTH_SHORT).show();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        this.registerReceiver(bluetoothReceiver, intentFilter);
        broadcaster = LocalBroadcastManager.getInstance(this);
    }

    @Override
    public void onDestroy() {
        if (bluetoothReceiver != null) {
            this.unregisterReceiver(bluetoothReceiver);
        }
        super.onDestroy();
    }

    public BroadcastReceiver bluetoothReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context contextt, Intent intent) {
            if (intent.getAction().equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                updateUIWithNewState(state);
            }
            if(intent.getAction().equals(BluetoothDevice.ACTION_ACL_DISCONNECTED)){
                Toast.makeText(contextt, "Device Disconnected!", Toast.LENGTH_SHORT).show();
                sendResult("disconnected");
            }
            if(intent.getAction().equals(BluetoothDevice.ACTION_ACL_CONNECTED)){
                Toast.makeText(contextt, "Device Connected!", Toast.LENGTH_SHORT).show();
                sendResult("connected");
            }
        }
    };

    protected void updateUIWithNewState(int state) {
        //Toast.makeText(context, "ON RECEIVE", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent("serviceUpdateReceivedAction");
        intent.putExtra("state", state);
        sendBroadcast(intent);
    }
    public void sendResult(String message) {
        Intent intent = new Intent(COPA_RESULT);
        if(message != null)
            intent.putExtra(COPA_MESSAGE, message);
        broadcaster.sendBroadcast(intent);
    }
}