package com.finwingway.digicob.leopard;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.finwingway.digicob.R;
import com.finwingway.digicob.leopard.bluetooth.BluetoothComm;
import com.finwingway.digicob.leopard.bluetooth.BluetoothPair;
import com.leopard.api.BaudChange;
import com.leopard.api.Setup;

import java.util.Hashtable;

/**
 * The main interface <br />
 *   * Maintain a connection with the Bluetooth communication operations,
 * check Bluetooth status after the first entry, did not start then turn on Bluetooth,
 * then immediately into the search interface. <br/>
 *   * The need to connect the device to get built on the main interface paired with a connection,
 * Bluetooth object is stored in globalPool so that other functional modules of different
 * communication modes calls.
 */
public class Act_Main extends Activity {
    /**
     * CONST: scan device menu id
     */
    private static final String TAG = "Prow LeopardImp App";
    public static GlobalPool mGP = null;
    public static BluetoothAdapter mBT = BluetoothAdapter.getDefaultAdapter();
    public static BluetoothDevice mBDevice = null;
    private TextView mtvDeviceInfo = null;
    private TextView mtvServiceUUID = null;
    private LinearLayout mllDeviceCtrl = null;
    private Button mbtnPair = null;
    private Button mbtnComm = null;
    public static final byte REQUEST_DISCOVERY = 0x01;
    public static final byte REQUEST_ABOUT = 0x05;
    private Hashtable<String, String> mhtDeviceInfo = new Hashtable<String, String>();
    private boolean mbBonded = false;
    public final static String EXTRA_DEVICE_TYPE = "android.bluetooth.device.extra.DEVICE_TYPE";
    private boolean mbBleStatusBefore = false;
    final Context context = this;
    Dialog dlgRadioBtn;
    static BaudChange bdchange = null;
    private int iRetVal;
    public static ProgressDialog prgDialog;

    private Button btn_Exit, btn_Scanbt;
    public static Setup setupInstance = null;
    private final int MESSAGE_BOX = 1;
    public static boolean blnResetBtnEnable = false;
    //BluetoothComm btcomm;
    public static SharedPreferences preferences;
    private static final String SHORTCUT = "SHORTCUT";
    TextView scanbt_tv;
    private BroadcastReceiver _mPairingRequest = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            BluetoothDevice device = null;
            if (intent.getAction().equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {
                device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getBondState() == BluetoothDevice.BOND_BONDED)
                    mbBonded = true;
                else
                    mbBonded = false;
            }
        }
    };

    /**
     * add top menu
     */
    ScrollView mainlay;

    @SuppressLint("NewApi")

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_bt);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            // Activity was brought to front and not created,
            // Thus finishing this will get us to the last viewed activity
            finish();
            return;
        }
        mainlay = (ScrollView) findViewById(R.id.mainlay);
        if (null == mBT) {
            Toast.makeText(this, "Bluetooth module not found", Toast.LENGTH_LONG).show();
            this.finish();
        } else {
            this.mtvDeviceInfo = (TextView) this.findViewById(R.id.actMain_tv_device_info);
            this.mllDeviceCtrl = (LinearLayout) this.findViewById(R.id.actMain_ll_device_ctrl);
            this.mbtnPair = (Button) this.findViewById(R.id.actMain_btn_pair);
            this.mbtnComm = (Button) this.findViewById(R.id.actMain_btn_conn);
            try {
                setupInstance = new Setup();
                boolean activate = setupInstance.blActivateLibrary(context, R.raw.licence_full);
                if (activate == true) {
                    Log.d(TAG, "Leopard Library Activated......");
                } else if (activate == false) {
                    Log.d(TAG, "Leopard Library Not Activated...");
                }
            } catch (Exception e) {
            }
        }

        btn_Exit = (Button) findViewById(R.id.btn_Exit);
        btn_Exit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //finish();
                dlgExit();
            }
        });

        this.mGP = ((GlobalPool) this.getApplicationContext());
        scanbt_tv = (Button) findViewById(R.id.scanbt_tv);
        scanbt_tv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new startBluetoothDeviceTask().execute("");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private void openDiscovery() {
        Intent intent = new Intent(this, Act_BTDiscovery.class);
        this.startActivityForResult(intent, REQUEST_DISCOVERY);
    }

    private void showDeviceInfo() {
        this.mtvDeviceInfo.setText(
                String.format(getString(R.string.actMain_device_info),
                        this.mhtDeviceInfo.get("NAME"),
                        this.mhtDeviceInfo.get("MAC"),
                        this.mhtDeviceInfo.get("COD"),
                        this.mhtDeviceInfo.get("RSSI"),
                        this.mhtDeviceInfo.get("DEVICE_TYPE"),
                        this.mhtDeviceInfo.get("BOND"))
        );
    }

    private void showServiceUUIDs() {
        if (Build.VERSION.SDK_INT >= 15) {
        } else {
            this.mtvServiceUUID.setText(getString(R.string.actMain_msg_does_not_support_uuid_service));
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mainlay.setVisibility(View.VISIBLE);
        if (requestCode == REQUEST_DISCOVERY) {
            if (Activity.RESULT_OK == resultCode) {
                this.mllDeviceCtrl.setVisibility(View.VISIBLE);
                this.mhtDeviceInfo.put("NAME", data.getStringExtra("NAME"));
                this.mhtDeviceInfo.put("MAC", data.getStringExtra("MAC"));
                this.mhtDeviceInfo.put("COD", data.getStringExtra("COD"));
                this.mhtDeviceInfo.put("RSSI", data.getStringExtra("RSSI"));
                this.mhtDeviceInfo.put("DEVICE_TYPE", data.getStringExtra("DEVICE_TYPE"));
                this.mhtDeviceInfo.put("BOND", data.getStringExtra("BOND"));
                this.showDeviceInfo();
                if (this.mhtDeviceInfo.get("BOND").equals(getString(R.string.actDiscovery_bond_nothing))) {
                    this.mbtnPair.setVisibility(View.VISIBLE);
                    this.mbtnComm.setVisibility(View.GONE);
                } else {
                    this.mBDevice = this.mBT.getRemoteDevice(this.mhtDeviceInfo.get("MAC"));
                    this.mbtnPair.setVisibility(View.GONE);
                    this.mbtnComm.setVisibility(View.VISIBLE);
                    new connSocketTask().execute(this.mBDevice.getAddress());
                }
            } else if (Activity.RESULT_CANCELED == resultCode) {
                this.finish();
            }
        } else if (requestCode == 3) {
            finish();
        }
    }

    /**
     * Pairing button click event
     *
     * @return void
     */
    public void onClickBtnPair(View v) {
        new PairTask().execute(this.mhtDeviceInfo.get("MAC"));
        this.mbtnPair.setEnabled(false);
    }

    /**
     * Connect button click event
     *
     * @return void
     */
    public void onClickBtnConn(View v) {
        new connSocketTask().execute(this.mBDevice.getAddress());
    }

    private class startBluetoothDeviceTask extends AsyncTask<String, String, Integer> {
        private static final int RET_BULETOOTH_IS_START = 0x0001;
        private static final int RET_BLUETOOTH_START_FAIL = 0x04;
        private static final int miWATI_TIME = 15;
        private static final int miSLEEP_TIME = 150;
        private ProgressDialog mpd;

        @Override
        public void onPreExecute() {
            mpd = new ProgressDialog(Act_Main.this);
            mpd.setMessage(getString(R.string.actDiscovery_msg_starting_device));
            mpd.setCancelable(false);
            mpd.setCanceledOnTouchOutside(false);
            mpd.show();
            mbBleStatusBefore = mBT.isEnabled();
        }

        @Override
        protected Integer doInBackground(String... arg0) {
            int iWait = miWATI_TIME * 1000;
            /* BT isEnable */
            if (!mBT.isEnabled()) {
                mBT.enable();
                //Wait miSLEEP_TIME seconds, start the Bluetooth device before you start scanning
                while (iWait > 0) {
                    if (!mBT.isEnabled())
                        iWait -= miSLEEP_TIME;
                    else
                        break;
                    SystemClock.sleep(miSLEEP_TIME);
                }
                if (iWait < 0)
                    return RET_BLUETOOTH_START_FAIL;
            }
            return RET_BULETOOTH_IS_START;
        }

        /**
         * After blocking cleanup task execution
         */
        @Override
        public void onPostExecute(Integer result) {
            if (mpd.isShowing())
                mpd.dismiss();
            if (RET_BLUETOOTH_START_FAIL == result) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Act_Main.this);
                builder.setTitle(getString(R.string.dialog_title_sys_err));
                builder.setMessage(getString(R.string.actDiscovery_msg_start_bluetooth_fail));
                builder.setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mBT.disable();
                        finish();
                    }
                });
                builder.create().show();
            } else if (RET_BULETOOTH_IS_START == result) {
                openDiscovery();
            }
        }
    }

    private class PairTask extends AsyncTask<String, String, Integer> {
        /**
         * Constants: the pairing is successful
         */
        static private final int RET_BOND_OK = 0x00;
        /**
         * Constants: Pairing failed
         */
        static private final int RET_BOND_FAIL = 0x01;
        /**
         * Constants: Pairing waiting time (15 seconds)
         */
        static private final int iTIMEOUT = 1000 * 15;

        /**
         * Thread start initialization
         */
        @Override
        public void onPreExecute() {
            Toast.makeText(Act_Main.this, getString(R.string.actMain_msg_bluetooth_Bonding), Toast.LENGTH_SHORT).show();
            registerReceiver(_mPairingRequest, new IntentFilter(BluetoothPair.PAIRING_REQUEST));
            registerReceiver(_mPairingRequest, new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED));
        }

        @Override
        protected Integer doInBackground(String... arg0) {
            final int iStepTime = 150;
            int iWait = iTIMEOUT;
            try {
                mBDevice = mBT.getRemoteDevice(arg0[0]);//arg0[0] is MAC address
                BluetoothPair.createBond(mBDevice);
                mbBonded = false;
            } catch (Exception e1) {
                Log.d(getString(R.string.app_name), "create Bond failed!");
                e1.printStackTrace();
                return RET_BOND_FAIL;
            }
            while (!mbBonded && iWait > 0) {
                SystemClock.sleep(iStepTime);
                iWait -= iStepTime;
            }
            if (iWait > 0) {
                //RET_BOND_OK
                Log.e("Application", "create Bond failed! RET_BOND_OK ");
            } else {
                //RET_BOND_FAIL
                Log.e("Application", "create Bond failed! RET_BOND_FAIL ");
            }
            return (int) ((iWait > 0) ? RET_BOND_OK : RET_BOND_FAIL);
        }

        @Override
        public void onPostExecute(Integer result) {
            try {
                unregisterReceiver(_mPairingRequest);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "Exception occured in unregistering reciever..." + e);
            }
            if (RET_BOND_OK == result) {
                Toast.makeText(Act_Main.this, getString(R.string.actMain_msg_bluetooth_Bond_Success), Toast.LENGTH_SHORT).show();
                mbtnPair.setVisibility(View.GONE);
                mbtnComm.setVisibility(View.VISIBLE);
                mhtDeviceInfo.put("BOND", getString(R.string.actDiscovery_bond_bonded));
                showDeviceInfo();
                showServiceUUIDs();
                //finish();
            } else {
                Toast.makeText(Act_Main.this, getString(R.string.actMain_msg_bluetooth_Bond_fail), Toast.LENGTH_LONG).show();
                try {
                    BluetoothPair.removeBond(mBDevice);
                } catch (Exception e) {
                    Log.d(getString(R.string.app_name), "removeBond failed!");
                    e.printStackTrace();
                }
                mbtnPair.setEnabled(true);
                try {
                    new connSocketTask().execute(mBDevice.getAddress());
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "Excepiton :" + e);
                }
            }
        }
    }

    private class connSocketTask extends AsyncTask<String, String, Integer> {
        /**
         * Process waits prompt box
         */
        private ProgressDialog mpd = null;
        /**
         * Constants: connection fails
         */
        private static final int CONN_FAIL = 0x01;
        /**
         * Constant: the connection is established
         */
        private static final int CONN_SUCCESS = 0x02;

        /**
         * Thread start initialization
         */
        @Override
        public void onPreExecute() {
            this.mpd = new ProgressDialog(Act_Main.this);
            this.mpd.setMessage(getString(R.string.actMain_msg_device_connecting));
            this.mpd.setCancelable(false);
            this.mpd.setCanceledOnTouchOutside(false);
            this.mpd.show();
        }

        @Override
        protected Integer doInBackground(String... arg0) {
            if (mGP.createConn(arg0[0])) {
                return CONN_SUCCESS;
            } else {
                return CONN_FAIL;
            }
        }

        /**
         * After blocking cleanup task execution
         */
        @Override
        public void onPostExecute(Integer result) {
            this.mpd.dismiss();
            if (CONN_SUCCESS == result) {
                mbtnComm.setVisibility(View.GONE);
                Toast.makeText(Act_Main.this, getString(R.string.actMain_msg_device_connect_succes), Toast.LENGTH_SHORT).show();
                //Intent all_intent = new Intent(getApplicationContext(),MainActivity.class);
                //all_intent.putExtra("connected", false);
                //startActivity(all_intent);
                finish();
            } else {
                Toast.makeText(Act_Main.this, getString(R.string.actMain_msg_device_connect_fail), Toast.LENGTH_SHORT).show();
            }
        }
    }

    // dialog box will display options to select the baud rate
/*
	public void showBaudRateSelection() { //TODO
		dlgRadioBtn = new Dialog(context);
		dlgRadioBtn.setCancelable(false);
		dlgRadioBtn.setTitle("Leopard Demo Application");
		dlgRadioBtn.setContentView(R.layout.dlg_bardchange);
 		*/
    /* when the application is started it is presumed that device is started
     * along with it (i.e. Switched ON) hence by default the device will be in
     * 9600bps so entering directly to next activity
     *//*

		RadioButton radioBtn9600 = (RadioButton) dlgRadioBtn.findViewById(R.id.first_radio);
		radioBtn9600.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
 				*/
    /* ResetBtnEnable will disable the reset button in Exit dialog box as
     * the connection is not made in bps *//*

				blnResetBtnEnable = false;
				dlgRadioBtn.dismiss();
				Intent all_intent = new Intent(getApplicationContext(),MainActivity.class);
				all_intent.putExtra("connected", false);
				startActivity(all_intent);
			}
		});
		dlgRadioBtn.show();
	}
*/

    //Exit confirmation dialog box
    public void dlgExit() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        // set title
        alertDialogBuilder.setTitle("Cancel");
        //alertDialogBuilder.setIcon(R.drawable.icon);
        alertDialogBuilder.setMessage("Do you want to cancel device search ?");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                try {
                    BluetoothComm.mosOut = null;
                    BluetoothComm.misIn = null;
                } catch (NullPointerException e) {
                }
                System.gc();
                Act_Main.this.finish();
            }
        });
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // if this button is clicked, just close
                // the dialog box and do nothing
                dialog.cancel();

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    /*  To show response messages  */
    public boolean onKeyDown(int keyCode, KeyEvent event) { //TODO
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            dlgExit();
        }
        return super.onKeyDown(keyCode, event);
    }
}
