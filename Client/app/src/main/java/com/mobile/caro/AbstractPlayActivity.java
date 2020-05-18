package com.mobile.caro;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.mobile.caro.Board.Board;
import com.mobile.caro.Board.BoardViewer;

public abstract class AbstractPlayActivity extends FragmentActivity {

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
        boardViewer.setConfirmMove(-1, -1);
        boardViewer.setLastMove(-1, -1);
        boardViewer.draw();
    }

    public abstract void onTileSelected(int x, int y);
}
