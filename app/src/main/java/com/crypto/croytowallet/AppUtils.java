package com.crypto.croytowallet;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;

public class AppUtils {

    public static void showMessageOKCancel(String message, Activity activity, DialogInterface.OnClickListener okListener) {
        new android.app.AlertDialog.Builder(activity)
                .setMessage(message)
                .setIcon(R.drawable.ic_baseline_check_circle_24)
                .setPositiveButton("OKAY", okListener)
                .create()
                .show();
    }
}
