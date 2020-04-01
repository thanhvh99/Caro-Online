package com.mobile.caro;


import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public abstract class AbstractPlayActivity extends AppCompatActivity {

    protected BoardViewer boardViewer;
    protected Board board;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BitmapManager.initialize(this);
    }

    protected void setupBoardViewer() {
        boardViewer = findViewById(R.id.boardViewer);
        boardViewer.setBoard(board);
        boardViewer.setActivity(this);
    }

    protected abstract void onTileSelected(int x, int y);
}
