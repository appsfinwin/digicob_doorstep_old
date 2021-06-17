package com.finwingway.digicob;

import android.graphics.Bitmap;
import android.net.Uri;

/**
 * Created by AnVin on 4/8/2017.
 */
public class imageAdapter {

    public static Uri uri;
    public static Bitmap fingerBitmap;
    public static String photograph_image_string="null",signature_image_string="null",id_image_string="null",
            address_image_string="null",biometric_image_string="null",biometric_image_string2="null",
            biometric_image_string3="null",biometric_data_string="null";
    public static void setintroducersSign(Uri signatureuri){
        uri=signatureuri;
    }
    public static Uri getSignatureuri(){
        return uri;
    }
    public static void setFingerBitmap(Bitmap bitmap){
        fingerBitmap=bitmap;
    }
    public static Bitmap getFingerBItmap(){
        return fingerBitmap;
    }
    public static void setPhotograph_image_string(String image){
        photograph_image_string=image;
    }
    public static String getPhotograph_image_string(){
        return photograph_image_string;
    }
    public static void setId_image_string(String image){
        id_image_string=image;
    }
    public static String getId_image_string(){
        return id_image_string;
    }
    public static void setAddress_image_string(String image){
        address_image_string=image;
    }
    public static String getAddress_image_string(){
        return address_image_string;
    }
    public static void setSignature_image_string(String image){
        signature_image_string=image;
    }
    public static String getSignature_image_string(){
        return signature_image_string;
    }
    public static void setBiometric_image_string(String image){
        biometric_image_string=image;
    }
    public static String getBiometric_image_string(){
        return biometric_image_string;
    }

    public static void setBiometric_data_string(String fingerString){
        biometric_data_string=fingerString;
    }
    public static String getBiometric_data_string(){
        return biometric_data_string;
    }

    public static void setBiometric_image_string2(String image){
        biometric_image_string2=image;
    }
    public static String getBiometric_image_string2(){
        return biometric_image_string2;
    }

    public static void setBiometric_image_string3(String image){
        biometric_image_string3=image;
    }
    public static String getBiometric_image_string3(){
        return biometric_image_string3;
    }
}
