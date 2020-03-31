package com.mobile.caro;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

public abstract class AbstractPlayActivity extends Activity {

    protected BoardViewer boardViewer;
    protected Board board;

    protected void setupBoardViewer() {
        boardViewer = findViewById(R.id.boardViewer);
        boardViewer.setBoard(board);
        boardViewer.setActivity(this);
    }

    protected abstract void onTileSelected(int x, int y);
}
