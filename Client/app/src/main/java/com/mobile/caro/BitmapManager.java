package com.mobile.caro;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapManager {

    private static BitmapManager manager = new BitmapManager();

    private Bitmap X;
    private Bitmap O;

    private BitmapManager() {

    }

    public static void initialize(Context context) {
        if (manager.X != null && manager.O != null) {
            return;
        }
        Resources resources = context.getResources();

        manager.X = BitmapFactory.decodeResource(resources, R.drawable.x);
        manager.O = BitmapFactory.decodeResource(resources, R.drawable.o);
    }

    public static Bitmap getX() {
        return manager.X;
    }

    public static Bitmap getO() {
        return manager.O;
    }

}
