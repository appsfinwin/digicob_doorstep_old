package com.finwingway.digicob;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by AnVin on 5/8/2017.
 */

public class Change_kyc_image extends Activity{
    Intent intent;
    String data;
    ImageView imageView,imageView2,imageView3,close_img_btn;
    Button button;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_kyc_image_layout);
        setFinishOnTouchOutside(false);
        imageView=(ImageView)findViewById(R.id.change_image_view);
        imageView2=(ImageView)findViewById(R.id.change_image_view2);
        imageView3=(ImageView)findViewById(R.id.change_image_view3);
        button=(Button)findViewById(R.id.change_image_button);
        close_img_btn=(ImageView)findViewById(R.id.change_image_close_image_button);
        intent = getIntent();
        data=intent.getStringExtra("data");
        if(data.equals("photograph")){
            byte[] decodedString = Base64.decode(imageAdapter.getPhotograph_image_string(), Base64.DEFAULT);
            Bitmap decode_image_bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imageView.setImageBitmap(decode_image_bitmap);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setResult(RESULT_OK);
                    Log.e("1001","Called");
                    finish();
                }
            });
        }
        if(data.equals("signature")){
            byte[] decodedString = Base64.decode(imageAdapter.getSignature_image_string(), Base64.DEFAULT);
            Bitmap decode_image_bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imageView.setImageBitmap(decode_image_bitmap);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setResult(RESULT_OK);
                    Log.e("1001","Called");
                    finish();
                }
            });
        }

        if(data.equals("biometric")){
            try {
                byte[] decodedString = Base64.decode(imageAdapter.getBiometric_image_string(), Base64.DEFAULT);
                Bitmap decode_image_bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageView.setImageBitmap(decode_image_bitmap);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "No thumb finger captured!  ", Toast.LENGTH_SHORT).show();
            }
            try {
                byte[] decodedString2 = Base64.decode(imageAdapter.getBiometric_image_string2(), Base64.DEFAULT);
                Bitmap decode_image_bitmap2 = BitmapFactory.decodeByteArray(decodedString2, 0, decodedString2.length);
                imageView2.setImageBitmap(decode_image_bitmap2);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "No first finger captured!  ", Toast.LENGTH_SHORT).show();
            }
            try {
                byte[] decodedString3 = Base64.decode(imageAdapter.getBiometric_image_string3(), Base64.DEFAULT);
                Bitmap decode_image_bitmap3 = BitmapFactory.decodeByteArray(decodedString3, 0, decodedString3.length);
                imageView3.setImageBitmap(decode_image_bitmap3);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "No middle finger captured!  ", Toast.LENGTH_SHORT).show();
            }
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setResult(RESULT_OK);
                    Log.e("1001","Called");
                    finish();
                }
            });
        }

        if(data.equals("id_proof")){
            byte[] decodedString = Base64.decode(imageAdapter.getId_image_string(), Base64.DEFAULT);
            Bitmap decode_image_bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imageView.setImageBitmap(decode_image_bitmap);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setResult(RESULT_OK);
                    Log.e("1001","Called");
                    finish();
                }
            });
        }

        if(data.equals("address_proof")){
            byte[] decodedString = Base64.decode(imageAdapter.getAddress_image_string(), Base64.DEFAULT);
            Bitmap decode_image_bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imageView.setImageBitmap(decode_image_bitmap);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setResult(RESULT_OK);
                    Log.e("1001","Called");
                    finish();
                }
            });
        }

        close_img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                Log.e("0","Called");
                finish();
            }
        });
    }
}
