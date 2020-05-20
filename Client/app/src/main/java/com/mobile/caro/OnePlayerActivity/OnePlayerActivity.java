package com.mobile.caro.OnePlayerActivity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.mobile.caro.AbstractPlayActivity;
import com.mobile.caro.Board.Board;
import com.mobile.caro.R;
import com.mobile.caro.TwoPlayersOnlineActivity.Entity.Move;

import java.util.ArrayList;
import java.util.List;


public class OnePlayerActivity extends AbstractPlayActivity {

    private DrawerLayout drawerLayout;
    private List<Move> moves = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_player);

        mapping();
        initialize();
        setupListener();

        board = new Board(19);
        setupBoardViewer();

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
                board.undo();
                board.undo();
                boardViewer.draw();
            }
        });

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void mapping() {
        drawerLayout = findViewById(R.id.drawerlayout);

    }

    private void initialize() {

    }

    @Override
    public void onTileSelected(int x, int y) {
        if (board.select(x, y)) {
        }
    }

}
