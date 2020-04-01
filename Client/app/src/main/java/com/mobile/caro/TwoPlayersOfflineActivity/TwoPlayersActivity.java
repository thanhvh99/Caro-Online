package com.mobile.caro.TwoPlayersOfflineActivity;

import android.graphics.PointF;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.mobile.caro.AbstractPlayActivity;
import com.mobile.caro.Board;
import com.mobile.caro.R;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class TwoPlayersActivity extends AbstractPlayActivity {

    private boolean isFirstPlayerTurn = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_players);

        //loadProgress();
        board = new Board(19);
        setupBoardViewer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //saveProgress();
    }

    @Override
    protected void onTileSelected(int x, int y) {
        if (board.isEmptyAt(x, y)) {
            if (boardViewer.getConfirmMove().equals(x, y)) {
                board.setValueAt(x, y, isFirstPlayerTurn ? Board.VALUE_O : Board.VALUE_X);
                isFirstPlayerTurn = !isFirstPlayerTurn;
                boardViewer.removeConfirmMove();
                boardViewer.setLastMove(x, y);
            } else {
                boardViewer.setConfirmMove(x, y);
            }
        } else {
            boardViewer.removeConfirmMove();
        }
        boardViewer.draw();
    }

    private void saveProgress() {
        try {
            FileOutputStream fileOutputStream = openFileOutput("progress2.mob", MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(board.getMatrix());
            objectOutputStream.writeBoolean(isFirstPlayerTurn);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadProgress() {
        try {
            FileInputStream fileInputStream = openFileInput("progress2.mob");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            int[][] matrix = (int[][]) objectInputStream.readObject();
            isFirstPlayerTurn = objectInputStream.readBoolean();
            board = new Board(matrix);
            objectInputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            board = new Board(Board.DEFAULT_SIZE);
        }
    }
}
