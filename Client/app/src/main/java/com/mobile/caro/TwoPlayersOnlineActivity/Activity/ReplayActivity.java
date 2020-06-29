package com.mobile.caro.TwoPlayersOnlineActivity.Activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.mobile.caro.AbstractPlayActivity;
import com.mobile.caro.Board.Board;
import com.mobile.caro.MyToast;
import com.mobile.caro.R;
import com.mobile.caro.TwoPlayersOnlineActivity.Entity.Match;
import com.mobile.caro.TwoPlayersOnlineActivity.Network.SocketHandler;

import java.util.ArrayList;

public class ReplayActivity extends AbstractPlayActivity {

    private ConstraintLayout user1;
    private ConstraintLayout user2;
    private ImageView play;
    private SeekBar seekBar;
    private Match match;
    private CountDownTimer timer;
    private ArrayList<Integer> moves;
    private boolean autoPlay = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replay);

        board = new Board(19);
        setupBoardViewer();

        mapping();
        initialize();
        setupListener();
    }

    private void mapping() {
        user1 = findViewById(R.id.user1);
        user2 = findViewById(R.id.user2);
        play = findViewById(R.id.play);
        seekBar = findViewById(R.id.seekBar);
    }

    private void initialize() {
        match = (Match) getIntent().getSerializableExtra("match");
        if (match == null) {
            MyToast.show(this, R.string.error_loading_match);
            finish();
            return;
        }

        moves = match.getMoves();

        seekBar.setMax(moves.size());

        updatePlayerBackground();

        if (!match.isFirst()) {
            boardViewer.setSwitchMarker(true);
        }

        ((TextView) findViewById(R.id.username1)).setText(SocketHandler.getPlayer().getUsername());
        ((TextView) findViewById(R.id.username2)).setText(match.getOpponent());

        timer = new CountDownTimer(400000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                seekBar.setProgress(seekBar.getProgress() + 1);
                if (seekBar.getProgress() == seekBar.getMax()) {
                    timer.cancel();
                    autoPlay = false;
                    play.setImageResource(R.drawable.ic_play);
                }
            }

            @Override
            public void onFinish() {

            }

        }.start();

    }

    private void setupListener() {
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoPlay = !autoPlay;
                play.setImageResource(autoPlay ? R.drawable.ic_pause : R.drawable.ic_play);
                timer.cancel();
                if (autoPlay) {
                    if (seekBar.getProgress() == seekBar.getMax()) {
                        seekBar.setProgress(0);
                    }
                    timer.start();
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && autoPlay) {
                    timer.cancel();
                    autoPlay = false;
                    play.setImageResource(R.drawable.ic_play);
                }
                while (progress != board.getTotalMoves()) {
                    if (progress < board.getTotalMoves()) {
                        board.undo();
                    } else {
                        int move = moves.get(board.getTotalMoves());
                        board.select(move % 19, move / 19);
                    }
                }
                int move = board.getLastMove();
                if (move == -1) {
                    boardViewer.setLastMove(-1, -1);
                } else {
                    boardViewer.setLastMove(move % 19, move / 19);
                }
                updatePlayerBackground();
                boardViewer.draw();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.cancel();
                finish();
            }
        });
    }

    @Override
    public void onTileSelected(int x, int y) {

    }

    private void updatePlayerBackground() {
        int count = board.getTotalMoves();
        if ((count % 2 == 0 && match.isFirst()) || (count % 2 == 1 && !match.isFirst())) {
            user1.setEnabled(true);
            user2.setEnabled(false);
        } else {
            user1.setEnabled(false);
            user2.setEnabled(true);
        }
    }
}
