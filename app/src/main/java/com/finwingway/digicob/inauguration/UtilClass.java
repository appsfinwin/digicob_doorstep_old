package com.finwingway.digicob.inauguration;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.RawRes;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class UtilClass {

    public static String readJsonFromDisk(@NonNull Context context, String rawRes) throws FileNotFoundException {
//        Log.e("calling", "readJsonAsStringFromDisk: " );
        InputStream inputStream = new FileInputStream(rawRes);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        int ctr;
        try {
            ctr = inputStream.read();
            while (ctr != -1) {
                byteArrayOutputStream.write(ctr);
                ctr = inputStream.read();
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArrayOutputStream.toString();
    }


    public static String readJsonAsStringFromDisk(@NonNull Context context, @RawRes int rawRes) {
        InputStream inputStream = context.getResources().openRawResource(rawRes);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        int ctr;
        try {
            ctr = inputStream.read();
            while (ctr != -1) {
                byteArrayOutputStream.write(ctr);
                ctr = inputStream.read();
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArrayOutputStream.toString();
    }

    /**
     * @param times
     * @param task
     * @return times in millis how much time requered to do work
     */
    public static long doNTimes(int times, Runnable task) {
        long before = System.currentTimeMillis();
        for (int i = 0; i < times; i++) {
            task.run();
        }
        return System.currentTimeMillis() - before;
    }
}
