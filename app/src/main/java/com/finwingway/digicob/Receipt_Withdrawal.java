package com.finwingway.digicob;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.finwingway.digicob.print.DeviceListActivity;
import com.finwingway.digicob.print.UnicodeFormatter;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Set;
import java.util.UUID;

/**
 * Created by AnVin on 1/7/2017.
 */

public class Receipt_Withdrawal extends Activity implements Runnable {
    protected static final String TAG = "TAG";
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    BluetoothAdapter mBluetoothAdapter;
    private UUID applicationUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private ProgressDialog mBluetoothConnectProgressDialog;
    private BluetoothSocket mBluetoothSocket;
    BluetoothDevice mBluetoothDevice;
    TextView accountNO,Name,Mobile,oldBal,WithdrawalAmount,currentBal,withDrawalDate,withdrawalTime,transaction_id;

    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.receipt_withdrawal_layout);
        Intent intent=getIntent();
        final String account_number = intent.getStringExtra("account_number");
        final String name = intent.getStringExtra("introName");
        final String mobile = intent.getStringExtra("mobile");
        final String old_balance = intent.getStringExtra("old_balance");
        final String current_balance = intent.getStringExtra("current_balance");
        final String withdrawalAmount = intent.getStringExtra("withdrawalAmount");
        final String withdrawalDate = intent.getStringExtra("withdrawalDate");
        final String tran_id = intent.getStringExtra("tran_id");

        accountNO=(TextView)findViewById(R.id.accNOReceipt);
        Name=(TextView)findViewById(R.id.nameReceipt);
        Mobile=(TextView)findViewById(R.id.MobileReceipttxt);
        oldBal=(TextView)findViewById(R.id.oldBalanceReceipt);
        WithdrawalAmount=(TextView)findViewById(R.id.withdrawalAmountText);
        currentBal=(TextView)findViewById(R.id.CurrentBalanceReceipt);
        withDrawalDate=(TextView)findViewById(R.id.date_receipt);
        withdrawalTime=(TextView)findViewById(R.id.timeReceipt);
        transaction_id=(TextView)findViewById(R.id.receipt_withdrawal_transaction_no);

        accountNO.setText(account_number);
        Name.setText(name);
        Mobile.setText(mobile);
        oldBal.setText(old_balance);
        WithdrawalAmount.setText(withdrawalAmount);
        currentBal.setText(current_balance);
        withDrawalDate.setText(withdrawalDate);
        transaction_id.setText(tran_id);







        ImageButton print=(ImageButton)findViewById(R.id.printReceiptBtn);
        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (mBluetoothAdapter == null) {
                    Toast.makeText(getBaseContext(), "Device Not Support", Toast.LENGTH_SHORT).show();
                } else {
                    if (!mBluetoothAdapter.isEnabled()) {
                        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableBtIntent,REQUEST_ENABLE_BT);
                    } else {
                        ListPairedDevices();
                        Intent connectIntent = new Intent(Receipt_Withdrawal.this,DeviceListActivity.class);
                        startActivityForResult(connectIntent,REQUEST_CONNECT_DEVICE);
                    }
                }
                Thread t = new Thread() {
                    public void run() {
                        try {
                            OutputStream os = mBluetoothSocket
                                    .getOutputStream();
                            String BILL = "";
                            BILL = "\n             Bank Name               \n\n";
                            BILL = BILL + "Withdrawal Date  : " + withdrawalDate + "\n";
                            BILL = BILL + "Transaction ID   : " + tran_id + "\n";
                            BILL = BILL + "Account Number   : " + account_number + "\n";
                            BILL = BILL + "Name             : " + name +"\n";
                            BILL = BILL + "Mobile Number    : " + mobile +"\n";
                            BILL = BILL
                                        + "------------------------------";
                            BILL = BILL + "\n\n";
                            BILL = BILL + "Opening Balance  : " + old_balance+"\n";
                            BILL = BILL + "Withdrawal Amount: " + withdrawalAmount+"\n";
                            BILL = BILL + "Current Balance  : " + current_balance+"\n\n";
                            BILL = BILL + "Thank you for Banking with us...\n";
                            BILL = BILL
                                    + "------------------------------\n\n\n";
                            os.write(BILL.getBytes());
                        } catch (Exception e) {
                            Log.e("Main", "Exe ", e);
                        }
                    }
                };
                t.start();
            }


        });



        ImageButton sms=(ImageButton)findViewById(R.id.smsReceiptbtn);
        sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }



    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        try {
            if (mBluetoothSocket != null)
                mBluetoothSocket.close();
        } catch (Exception e) {
            Log.e("Tag", "Exe ", e);
        }
    }

    @Override
    public void onBackPressed() {
        try {
            if (mBluetoothSocket != null)
                mBluetoothSocket.close();
        } catch (Exception e) {
            Log.e("Tag", "Exe ", e);
        }
        setResult(RESULT_CANCELED);
        finish();
    }

    public void onActivityResult(int mRequestCode, int mResultCode,
                                 Intent mDataIntent) {
        super.onActivityResult(mRequestCode, mResultCode, mDataIntent);

        switch (mRequestCode) {
            case REQUEST_CONNECT_DEVICE:
                if (mResultCode == Activity.RESULT_OK) {
                    Bundle mExtra = mDataIntent.getExtras();
                    String mDeviceAddress = mExtra.getString("DeviceAddress");
                    Log.v(TAG, "Coming incoming address " + mDeviceAddress);
                    mBluetoothDevice = mBluetoothAdapter
                            .getRemoteDevice(mDeviceAddress);

                    mBluetoothConnectProgressDialog = ProgressDialog.show(this,
                            "Connecting...", mBluetoothDevice.getName() + " : "
                                    + mBluetoothDevice.getAddress(), true, false);
                    Thread mBlutoothConnectThread = new Thread(this);
                    mBlutoothConnectThread.start();
                    // pairToDevice(mBluetoothDevice); This method is replaced by
                    // progress dialog with thread
                }
                break;

            case REQUEST_ENABLE_BT:
                if (mResultCode == Activity.RESULT_OK) {
                    ListPairedDevices();
                    Intent connectIntent = new Intent(Receipt_Withdrawal.this,
                            DeviceListActivity.class);
                    startActivityForResult(connectIntent, REQUEST_CONNECT_DEVICE);
                } else {
                    Toast.makeText(Receipt_Withdrawal.this, "Bluetooth activate denied", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void ListPairedDevices() {
        Set<BluetoothDevice> mPairedDevices = mBluetoothAdapter
                .getBondedDevices();
        if (mPairedDevices.size() > 0) {
            for (BluetoothDevice mDevice : mPairedDevices) {
                Log.v(TAG, "PairedDevices: " + mDevice.getName() + "  "
                        + mDevice.getAddress());
            }
        }
    }

    public void run() {
        try {
            mBluetoothSocket = mBluetoothDevice
                    .createRfcommSocketToServiceRecord(applicationUUID);
            mBluetoothAdapter.cancelDiscovery();
            mBluetoothSocket.connect();
            mHandler.sendEmptyMessage(0);
        } catch (IOException eConnectException) {
            Log.d(TAG, "CouldNotConnectToSocket", eConnectException);
            closeSocket(mBluetoothSocket);
            mBluetoothConnectProgressDialog.cancel();
            this.runOnUiThread(new Runnable() {
                                   public void run() {
                                       Toast.makeText(getBaseContext(), "Connecting failed!", Toast.LENGTH_SHORT).show();
                                   }
                               }
            );
        }
    }

    private void closeSocket(BluetoothSocket nOpenSocket) {
        try {
            nOpenSocket.close();
            Log.d(TAG, "SocketClosed");
        } catch (IOException ex) {
            Log.d(TAG, "CouldNotCloseSocket");
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mBluetoothConnectProgressDialog.dismiss();
            Toast.makeText(Receipt_Withdrawal.this, "DeviceConnected", Toast.LENGTH_LONG).show();
        }
    };

    public static byte intToByteArray(int value) {
        byte[] b = ByteBuffer.allocate(4).putInt(value).array();

        for (int k = 0; k < b.length; k++) {
            System.out.println("Selva  [" + k + "] = " + "0x"
                    + UnicodeFormatter.byteToHex(b[k]));
        }

        return b[3];
    }

    public byte[] sel(int val) {
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.putInt(val);
        buffer.flip();
        return buffer.array();
    }

}
