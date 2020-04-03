package com.mobile.caro.TwoPlayersOfflineActivity;

import android.graphics.PointF;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
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
    private ImageView imgPlayerOne, imgPlayerTwo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_players);

        //loadProgress();
        board = new Board(19);
        setupBoardViewer();

        //ánh xạ
        mapping();

        //set chế độ chơi, quân cờ
        textViewmode.setText("Chế độ 2 người chơi");
        imgPlayerOne.setImageResource(R.drawable.x);
        imgPlayerTwo.setImageResource(R.drawable.o);

        //click menu
        onMenu();

        //select Item navigation
        selectedItemNavigation();

    }

    private void selectedItemNavigation() {
        NavigationView navigationView = new NavigationView(TwoPlayersActivity.this);
        navigationView = (NavigationView) findViewById(R.id.navigation_two);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.menu_newgame:
                        Toast.makeText(TwoPlayersActivity.this,"New Game",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.menu_width:
                        Toast.makeText(TwoPlayersActivity.this,"Width Game",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.menu_confirm:
                        Toast.makeText(TwoPlayersActivity.this,"Confirm Game",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.menu_quit:
                        Toast.makeText(TwoPlayersActivity.this,"Quit Game!",Toast.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }
        });
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
        imgPlayerOne = (ImageView) findViewById(R.id.img_player_one);
        imgPlayerTwo = (ImageView) findViewById(R.id.img_player_two);
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
