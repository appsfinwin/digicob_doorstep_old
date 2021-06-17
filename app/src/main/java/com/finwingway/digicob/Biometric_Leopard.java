package com.finwingway.digicob;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.finwingway.digicob.leopard.Act_Main;
import com.finwingway.digicob.leopard.bluetooth.BluetoothComm;
import com.leopard.api.FPS;
import com.leopard.api.FpsConfig;
import com.leopard.api.FpsImageAPI;
import com.leopard.api.HexString;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

public class Biometric_Leopard extends Activity {
    ImageButton thumb_finger_btn,first_finger_btn,middle_finger_btn,close_bt;
    Button scan_finish_btn,bio_verify_btn,bio_verify_scan;
    public static final int DEVICE_NOTCONNECTED = -100;
    FPS fps;
    InputStream inputstream;
    OutputStream outputStream;
    private int iRetVal;
    byte[] array,array2,array3;
    Bitmap finger_image;
    SweetAlertDialog dialog;
    Boolean thumb_captured=false,first_captured=false,middle_captured=false;
    FpsConfig fpsconfig = new FpsConfig();
    byte[] first;
    byte[] fingerbytedata;
    String fingerdataString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biometric);
        try {
            outputStream = BluetoothComm.mosOut;
            inputstream = BluetoothComm.misIn;
            fps = new FPS(Act_Main.setupInstance, outputStream, inputstream);
            Log.e("SDK","INITIALZED");
        } catch (Exception e) {
            e.printStackTrace();
        }
        dialog=new SweetAlertDialog(this,SweetAlertDialog.PROGRESS_TYPE);
        scan_finish_btn=(Button)findViewById(R.id.biometric_scan_finish_btn);
        close_bt=(ImageButton) findViewById(R.id.biometric_close_btn);
        scan_finish_btn.setEnabled(true);
        scan_finish_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(first_captured || thumb_captured || middle_captured){
                    Intent intent=new Intent();
                    intent.putExtra("biometric_byte_array",array);
                    intent.putExtra("biometric_byte_array2",array2);
                    intent.putExtra("biometric_byte_array3",array3);
                    setResult(RESULT_OK,intent);
                    finish();
                }else{
                    setResult(RESULT_CANCELED);
                    finish();
                }
                if(thumb_captured && first_captured && middle_captured){

                }else{
                    Toast.makeText(Biometric_Leopard.this, "Please capture all fingers", Toast.LENGTH_SHORT).show();
                }
            }
        });
        close_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitAlert();
            }
        });

        thumb_finger_btn=(ImageButton) findViewById(R.id.biometric_thumb_scan_btn);
        first_finger_btn=(ImageButton)findViewById(R.id.biometric_first_scan_btn);
        middle_finger_btn=(ImageButton)findViewById(R.id.biometric_middle_scan_btn);

        thumb_finger_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FpsImagecompressed().execute(1);
            }
        });
        first_finger_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FpsImagecompressed().execute(2);
            }
        });
        middle_finger_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FpsImagecompressed().execute(3);
            }
        });
    }


    byte[] bCmpData;
    byte[] bUncmpData;
    byte[] bBmpData;

    public class FpsImagecompressed extends AsyncTask<Integer, Integer, Integer> {
        int btn;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setTitleText("Processing");
            dialog.setContentText("please place your finger on biometric");
            dialog.show();
            Log.e("DoinBAck","PRE EXECUTE");

        }

        @Override
        protected Integer doInBackground(Integer... params) {
            btn=params[0];
            try {
                byte[] bufvalue1 = new byte[3500];
                fingerbytedata = new byte[3500];
                Log.e("DoinBAck","BACKGROUND TASK");
                iRetVal = fps.iGetFingerImageCompressed(bufvalue1,new FpsConfig(1, (byte) 0x0f));
                fingerbytedata=fps.bGetMinutiaeData();
                fingerdataString=HexString.bufferToHex(fingerbytedata);
                Log.e("Finger Data String",fingerdataString);
                bCmpData = fps.bGetImageData();
                bUncmpData = FpsImageAPI.bGetUncompressedRawData(bCmpData);
                bBmpData = FpsImageAPI.bConvertRaw2bmp(bUncmpData);
                if(iRetVal<0){
                    Log.e("DoinBAck","iRetVal");
                    return iRetVal;
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
                Log.e("Ex","Exception!!");
                iRetVal = -100;
                return iRetVal;
            }
            return iRetVal;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
        @Override
        protected void onPostExecute(Integer result) {

            if (iRetVal == DEVICE_NOTCONNECTED) {
                dialog.dismiss();
                Toast.makeText(Biometric_Leopard.this, "DEVICE NOT CONNECTED", Toast.LENGTH_SHORT).show();
            } else if (iRetVal > 0) {
                dialog.dismiss();
                finger_image = BitmapFactory.decodeByteArray(bBmpData, 0,bBmpData.length);
                if(btn==1){
                    thumb_captured=true;
                    Bitmap bitmaparray=finger_image;
                    imageAdapter.setFingerBitmap(bitmaparray);
                    imageAdapter.setBiometric_data_string(fingerdataString);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmaparray.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    array = byteArrayOutputStream .toByteArray();
                    thumb_finger_btn.setImageBitmap(finger_image);
                }
                if(btn==2){
                    first_captured=true;
                    Bitmap bitmaparray2=finger_image;
                    imageAdapter.setFingerBitmap(bitmaparray2);
                    imageAdapter.setBiometric_data_string(fingerdataString);
                    ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
                    bitmaparray2.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream2);
                    array2 = byteArrayOutputStream2 .toByteArray();
                    first_finger_btn.setImageBitmap(finger_image);
                }
                if(btn==3){
                    middle_captured=true;
                    Bitmap bitmaparray3=finger_image;
                    imageAdapter.setFingerBitmap(bitmaparray3);
                    imageAdapter.setBiometric_data_string(fingerdataString);
                    ByteArrayOutputStream byteArrayOutputStream3 = new ByteArrayOutputStream();
                    bitmaparray3.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream3);
                    array3 = byteArrayOutputStream3 .toByteArray();
                    middle_finger_btn.setImageBitmap(finger_image);
                }
            } else if (iRetVal == FPS.FPS_INACTIVE_PERIPHERAL) {
                dialog.dismiss();
                Toast.makeText(Biometric_Leopard.this, "INACTIVE DEVICE", Toast.LENGTH_SHORT).show();
            } else if (iRetVal == FPS.TIME_OUT) {
                dialog.dismiss();
                Toast.makeText(Biometric_Leopard.this, "Connection Time Out", Toast.LENGTH_SHORT).show();
            } else if (iRetVal == FPS.FPS_ILLEGAL_LIBRARY) {
                dialog.dismiss();
                Toast.makeText(Biometric_Leopard.this, "Illegal Library", Toast.LENGTH_SHORT).show();
            } else if (iRetVal == FPS.FAILURE) {
                dialog.dismiss();
                Toast.makeText(Biometric_Leopard.this, "Device Failed", Toast.LENGTH_SHORT).show();
            } else if (iRetVal == FPS.PARAMETER_ERROR) {
                dialog.dismiss();
                Toast.makeText(Biometric_Leopard.this, "Device Error", Toast.LENGTH_SHORT).show();
            } else if (iRetVal == FPS.FPS_INVALID_DEVICE_ID) {
                dialog.dismiss();
                Toast.makeText(Biometric_Leopard.this, "Invalid Device ID", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(result);
        }
    }

    @Override
    public void onBackPressed() {
        exitAlert();
        super.onBackPressed();
    }
    private void exitAlert(){
        new SweetAlertDialog(this,SweetAlertDialog.WARNING_TYPE)
                .setTitleText("EXIT")
                .setContentText("are you sure ?")
                .setCancelText("NO")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        finish();
                    }
                }).show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK) {
            exitAlert();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
