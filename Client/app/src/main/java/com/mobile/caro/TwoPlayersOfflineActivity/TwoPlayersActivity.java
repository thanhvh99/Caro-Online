package com.mobile.caro.TwoPlayersOfflineActivity;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.mobile.caro.AbstractPlayActivity;
import com.mobile.caro.Board.Board;
import com.mobile.caro.R;

public class TwoPlayersActivity extends AbstractPlayActivity {

    private DrawerLayout drawerLayout;
    private TextView mapSize;
    private CheckBox confirmMove;
    private LinearLayout player1;
    private LinearLayout player2;
    private ImageView undoImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_players);

        mapping();
        initialize();
        setupListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences sharedPreferences = getSharedPreferences("multiplayer", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("width", Integer.parseInt(mapSize.getText().toString()));
        editor.putBoolean("confirm", confirmMove.isChecked());
        editor.apply();
    }

    private void mapping() {
        drawerLayout = findViewById(R.id.drawerlayout);
        mapSize = findViewById(R.id.mapSizeText);
        confirmMove = findViewById(R.id.confirmMoveCheckBox);
        player1 = findViewById(R.id.player1);
        player2 = findViewById(R.id.player2);
        undoImage = findViewById(R.id.undo);
    }

    private void initialize() {
        SharedPreferences sharedPreferences = getSharedPreferences("multiplayer", MODE_PRIVATE);
        int width = sharedPreferences.getInt("width", 15);
        mapSize.setText(width + "");
        board = new Board(width);
        confirmMove.setChecked(sharedPreferences.getBoolean("confirm", false));
        setupBoardViewer();
        updatePlayerBackground();
    }

    private void setupListener() {
        undoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (board.isOngoing()) {
                    board.undo();
                    boardViewer.draw();
                } else {
                    undoImage.setImageResource(R.drawable.ic_undo);
                    board = new Board(Integer.parseInt(mapSize.getText().toString()));
                    setupBoardViewer();
                }
            }
        });

        findViewById(R.id.newGame).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(GravityCompat.START);
                undoImage.setImageResource(R.drawable.ic_undo);
                board = new Board(Integer.parseInt(mapSize.getText().toString()));
                setupBoardViewer();
            }
        });
        
        findViewById(R.id.mapSize).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapSize.setText(mapSize.getText().equals("15") ? "19" : "15");
                Toast.makeText(TwoPlayersActivity.this, R.string.settings_will_be_applied_in_new_game, Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.confirmMove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmMove.setChecked(!confirmMove.isChecked());
                if (!confirmMove.isChecked() && !boardViewer.getConfirmMove().equals(-1, -1)) {
                    boardViewer.setConfirmMove(-1, -1);
                    boardViewer.draw();
                }
            }
        });

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });
    }

    private void updatePlayerBackground() {
        player1.setEnabled(board.isFirstPlayerTurn());
        player2.setEnabled(!board.isFirstPlayerTurn());
    }

    @Override
    public void onTileSelected(int x, int y) {
        if (!board.isOngoing()) {
            return;
        }
        if (confirmMove.isChecked()) {
            if (!board.isEmptyAt(x, y)) {
                boardViewer.removeConfirmMove();
                boardViewer.draw();
                return;
            }
            if (!boardViewer.getConfirmMove().equals(x, y)) {
                boardViewer.setConfirmMove(x, y);
                boardViewer.draw();
                return;
            }
        }

        if (board.select(x, y)) {
            updatePlayerBackground();
            boardViewer.draw();
            if (!board.isOngoing()) {
                switch (board.getStatus()) {
                    case P1_WIN: Toast.makeText(this, R.string.first_player_win, Toast.LENGTH_SHORT).show(); break;
                    case P2_WIN: Toast.makeText(this, R.string.second_player_win, Toast.LENGTH_SHORT).show(); break;
                    case EVEN: Toast.makeText(this, R.string.even, Toast.LENGTH_SHORT).show(); break;
                }
                undoImage.setImageResource(R.drawable.ic_restart);
            }
        }
    }
}
