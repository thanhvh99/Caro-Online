package com.mobile.caro.OnePlayerActivity;

import android.content.SharedPreferences;
import android.graphics.Point;
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

import com.mobile.caro.AI.Computer;
import com.mobile.caro.AbstractPlayActivity;
import com.mobile.caro.Board.Board;
import com.mobile.caro.R;


public class OnePlayerActivity extends AbstractPlayActivity {

    private DrawerLayout drawerLayout;
    private CheckBox confirmMove;
    private LinearLayout player1;
    private LinearLayout player2;
    private ImageView undoImage;
    private TextView mapSize;
    private TextView difficulty;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_player);

        mapping();
        initialize();
        setupListener();
    }

    private void setupListener() {
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

        findViewById(R.id.undo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (board.isOngoing()) {
                    board.undo();
                    board.undo();
                    boardViewer.setLastMove(-1, -1);
                    boardViewer.draw();
                } else {
                    undoImage.setImageResource(R.drawable.ic_undo);
                    board = new Board(Integer.parseInt(mapSize.getText().toString()));
                    setupBoardViewer();
                }
                updatePlayerBackground();
            }
        });

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.easy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                undoImage.setImageResource(R.drawable.ic_undo);
                board = new Board(Integer.parseInt(mapSize.getText().toString()));
                setupBoardViewer();
                updatePlayerBackground();
            }
        });

        findViewById(R.id.mapSize).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapSize.setText(mapSize.getText().equals("15") ? "19" : "15");
                Toast.makeText(OnePlayerActivity.this, R.string.settings_will_be_applied_in_new_game, Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.normal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                undoImage.setImageResource(R.drawable.ic_undo);
                board = new Board(Integer.parseInt(mapSize.getText().toString()));
                setupBoardViewer();
                updatePlayerBackground();
            }
        });

        findViewById(R.id.hard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                undoImage.setImageResource(R.drawable.ic_undo);
                board = new Board(Integer.parseInt(mapSize.getText().toString()));
                setupBoardViewer();
                updatePlayerBackground();
            }
        });
    }

    private void mapping() {
        drawerLayout = findViewById(R.id.drawerlayout);
        drawerLayout = findViewById(R.id.drawerlayout);
        mapSize = findViewById(R.id.mapSizeText);
        confirmMove = findViewById(R.id.confirmMoveCheckBox);
        player1 = findViewById(R.id.player1);
        player2 = findViewById(R.id.player2);
        undoImage = findViewById(R.id.undo);
        difficulty = findViewById(R.id.difficulty);
    }

    private void initialize() {
        SharedPreferences sharedPreferences = getSharedPreferences("singleplayer", MODE_PRIVATE);
        int width = sharedPreferences.getInt("width", 15);
        mapSize.setText(width + "");
        board = new Board(width);
        confirmMove.setChecked(sharedPreferences.getBoolean("confirm", false));
        difficulty.setText(sharedPreferences.getString("difficulty", getString(R.string.easy)));
        setupBoardViewer();
        updatePlayerBackground();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences sharedPreferences = getSharedPreferences("singleplayer", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("width", Integer.parseInt(mapSize.getText().toString()));
        editor.putBoolean("confirm", confirmMove.isChecked());
        editor.putString("difficulty", difficulty.getText().toString());
        editor.apply();
    }

    @Override
    public void onTileSelected(int x, int y) {
        if (!board.isOngoing() || !board.isFirstPlayerTurn()) {
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
            boardViewer.setLastMove(x, y);
            boardViewer.removeConfirmMove();
            updatePlayerBackground();
            boardViewer.draw();
            if (!board.isOngoing()) {
                switch (board.getStatus()) {
                    case P1_WIN:
                        Toast.makeText(this, R.string.first_player_win, Toast.LENGTH_SHORT).show();
                        break;
                    case P2_WIN:
                        Toast.makeText(this, R.string.second_player_win, Toast.LENGTH_SHORT).show();
                        break;
                    case EVEN:
                        Toast.makeText(this, R.string.even, Toast.LENGTH_SHORT).show();
                        break;
                }
                undoImage.setImageResource(R.drawable.ic_restart);
            } else {
                computerTurn();
            }
        }
    }

    private void computerTurn() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Point point = new Computer(board).AI();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (board.select(point.x, point.y)) {
                            boardViewer.setLastMove(point.x, point.y);
                            updatePlayerBackground();
                            boardViewer.draw();
                        }
                        if (!board.isOngoing()) {
                            switch (board.getStatus()) {
                                case P1_WIN:
                                    Toast.makeText(OnePlayerActivity.this, R.string.first_player_win, Toast.LENGTH_SHORT).show();
                                    break;
                                case P2_WIN:
                                    Toast.makeText(OnePlayerActivity.this, R.string.second_player_win, Toast.LENGTH_SHORT).show();
                                    break;
                                case EVEN:
                                    Toast.makeText(OnePlayerActivity.this, R.string.even, Toast.LENGTH_SHORT).show();
                                    break;
                            }
                            undoImage.setImageResource(R.drawable.ic_restart);
                        }
                    }
                });
            }
        }).start();
    }

    private void updatePlayerBackground() {
        player1.setEnabled(board.isFirstPlayerTurn());
        player2.setEnabled(!board.isFirstPlayerTurn());
    }
}
