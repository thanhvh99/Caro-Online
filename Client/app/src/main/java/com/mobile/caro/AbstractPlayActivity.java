package com.mobile.caro;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

public abstract class AbstractPlayActivity extends Activity {

    protected BoardViewer customSurfaceView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void initialize() {

    }

    protected abstract void onTileSelected(int x, int y);
}
