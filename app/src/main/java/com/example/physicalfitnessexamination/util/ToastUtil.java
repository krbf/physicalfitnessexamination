package com.example.physicalfitnessexamination.util;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;


public class ToastUtil {

    public static void showToast(final Context context, final String text) {

        new Handler(Looper.getMainLooper()).post(() -> {
            Toast myToast = null;
            if (myToast == null) {
                myToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
            } else {
                myToast.setText(text);
            }
            myToast.show();
        });

    }

    public static void showToast(Context context, int rid) {
        String text = context.getString(rid);
        showToast(context, text);
    }

}
