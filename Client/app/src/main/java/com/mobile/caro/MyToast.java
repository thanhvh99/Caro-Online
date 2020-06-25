package com.mobile.caro;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.StringRes;

public final class MyToast {

    private static Toast toast = null;

    public static void show(Context context, String string)
    {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(context, string, Toast.LENGTH_SHORT);
        toast.show();
    }

    public static void show(Context context, @StringRes int stringId) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(context, stringId, Toast.LENGTH_SHORT);
        toast.show();
    }

    public static void show(Context context, String string, int length)
    {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(context, string, length);
        toast.show();
    }

    public static void show(Context context, @StringRes int stringId, int length)
    {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(context, stringId, length);
        toast.show();
    }
}
