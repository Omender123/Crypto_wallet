package com.crypto.croytowallet;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AppUtils {

    public static void showMessageOKCancel(String message, Activity activity, DialogInterface.OnClickListener okListener) {
        new android.app.AlertDialog.Builder(activity)
                .setMessage(message)
                .setIcon(R.drawable.ic_baseline_check_circle_24)
                .setPositiveButton("OKAY", okListener)
                .create()
                .show();
    }

    public static String getTime(String dateTime){
        String times ="";
        try {
            DateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            Date d = f.parse(dateTime);

            DateFormat date = new SimpleDateFormat("MM/dd/yyyy");
            DateFormat time = new SimpleDateFormat("hh:mm:ss a");
            System.out.println("Date: " + date.format(d));
            System.out.println("Time: " + time.format(d));//09:00:00 am

            String []word= time.format(d).split(":");
            times=times+word[0]+":"+word[1]+" "+word[2].split(" ")[1];

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return times;
    }

    public static String getDate(String dateTime){
        String dateStr="";
        try {
            DateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            Date d = f.parse(dateTime);

            DateFormat date = new SimpleDateFormat("EEE, d MMM yyyy");
//            System.out.println("Date: " + date.format(d));
            dateStr = date.format(d);


        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateStr;
    }

}
