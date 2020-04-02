package com.mobile.caro.TwoPlayersOfflineActivity;

import android.graphics.PointF;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;

import com.mobile.caro.AbstractPlayActivity;
import com.mobile.caro.Board;
import com.mobile.caro.R;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class TwoPlayersActivity extends AbstractPlayActivity {
    private DrawerLayout drawerLayout;
    private boolean isFirstPlayerTurn = true;
    private TextView textViewmode;
    private ImageButton ibtnMenu;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_players);

        //loadProgress();
        board = new Board(19);
        setupBoardViewer();

        //ánh xạ
        mapping();

        //set chế độ chơi
        textViewmode.setText("Chế độ 2 người chơi");

        //click menu
        onMenu();

    }

    private void onMenu() {
        ibtnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
    }

    private void mapping() {
        textViewmode = (TextView) findViewById(R.id.tv_mode);
        ibtnMenu = (ImageButton)findViewById(R.id.ibtn_menu);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
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
