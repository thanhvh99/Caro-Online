package com.mobile.caro.TwoPlayersOnlineActivity.Activity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.mobile.caro.AbstractPlayActivity;
import com.mobile.caro.Board.Board;
import com.mobile.caro.MyToast;
import com.mobile.caro.R;
import com.mobile.caro.TwoPlayersOnlineActivity.BottomSheet.GoFirstBottomSheet;
import com.mobile.caro.TwoPlayersOnlineActivity.Dialog.ErrorDialog;
import com.mobile.caro.TwoPlayersOnlineActivity.Dialog.ResultDialog;
import com.mobile.caro.TwoPlayersOnlineActivity.Entity.Room;
import com.mobile.caro.TwoPlayersOnlineActivity.Network.SocketHandler;

import org.json.JSONObject;

import io.socket.emitter.Emitter;

public class PlayOnlineActivity extends AbstractPlayActivity {

    private boolean isPlayerTurn = false;

    private ConstraintLayout user1;
    private ConstraintLayout user2;

    private TextView time1;
    private TextView time2;

    private boolean confirm;
    private int goFirstOption;
    private boolean switchMark;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_play);

        mapping();
        initialize();

        resetGame();
    }

    public void resetGame() {
        board = new Board(19);
        setupBoardViewer();

        if (goFirstOption == 0) {
            new GoFirstBottomSheet().show(getSupportFragmentManager(), null);
        } else {
            try {
                JSONObject object = new JSONObject();
                object.put("first", goFirstOption == 1);
                SocketHandler.emit("first", object);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        user1.setEnabled(false);
        user2.setEnabled(false);

        time1.setVisibility(View.GONE);
        time2.setVisibility(View.GONE);
    }

    private void mapping() {
        user1 = findViewById(R.id.user1);
        user2 = findViewById(R.id.user2);
        time1 = findViewById(R.id.time1);
        time2 = findViewById(R.id.time2);
    }

    private void initialize() {
        Room room = (Room) getIntent().getSerializableExtra("room");

        user1.setEnabled(false);
        user2.setEnabled(false);

        SharedPreferences sharedPreferences = getSharedPreferences("OnlineSettings", MODE_PRIVATE);
        confirm = sharedPreferences.getInt("confirm", 1) == 0;
        goFirstOption = sharedPreferences.getInt("first", 0);
        switchMark = sharedPreferences.getInt("mark", 0) == 1;

        ((TextView) findViewById(R.id.username1)).setText(SocketHandler.getPlayer().getUsername());
        ((ImageView) findViewById(R.id.userImage1)).setImageResource(getResources().getIdentifier(SocketHandler.getPlayer().getImageUrl(), "drawable", getPackageName()));

        ((TextView) findViewById(R.id.username2)).setText(room.getPlayer().getUsername());
        ((ImageView) findViewById(R.id.userImage2)).setImageResource(getResources().getIdentifier(room.getPlayer().getImageUrl(), "drawable", getPackageName()));
    }

    @Override
    protected void onStart() {
        super.onStart();
        SocketHandler.on("disconnect", onDisconnect);
        SocketHandler.on("turn", onTurn);
        SocketHandler.on("put", onPut);
        SocketHandler.on("win", onWin);
        SocketHandler.on("even", onEven);
        SocketHandler.on("rematch", onRematch);
        SocketHandler.on("first", onFirst);
    }

    @Override
    protected void onStop() {
        super.onStop();
        SocketHandler.off("disconnect", onDisconnect);
        SocketHandler.off("turn", onTurn);
        SocketHandler.off("put", onPut);
        SocketHandler.off("win", onWin);
        SocketHandler.off("even", onEven);
        SocketHandler.off("rematch", onRematch);
        SocketHandler.off("first", onFirst);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SocketHandler.emit("leave");
    }

    @Override
    public void onTileSelected(int x, int y) {
        if (!isPlayerTurn) {
            return;
        }
        if (!board.isEmptyAt(x, y)) {
            boardViewer.removeConfirmMove();
            boardViewer.draw();
            return;
        }
        if (confirm) {
            Point currentConfirmMove = boardViewer.getConfirmMove();
            if (!currentConfirmMove.equals(x, y)) {
                boardViewer.setConfirmMove(x, y);
                boardViewer.draw();
                return;
            }
        }
        try {
            JSONObject object = new JSONObject();
            object.put("x", x);
            object.put("y", y);
            SocketHandler.emit("put", object);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Emitter.Listener onTurn = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            try {
                JSONObject object = (JSONObject) args[0];
                isPlayerTurn = object.getString("username").equals(SocketHandler.getPlayer().getUsername());
                final String time = object.getString("remaining");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        user1.setEnabled(isPlayerTurn);
                        user2.setEnabled(!isPlayerTurn);

                        time1.setText(time);
                        time1.setVisibility(isPlayerTurn ? View.VISIBLE : View.GONE);

                        time2.setText(time);
                        time2.setVisibility(isPlayerTurn ? View.GONE : View.VISIBLE);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private Emitter.Listener onPut = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            try {
                JSONObject object = (JSONObject) args[0];
                int x = object.getInt("x");
                int y = object.getInt("y");
                board.select(x, y);
                boardViewer.removeConfirmMove();
                boardViewer.setLastMove(x, y);
                boardViewer.draw();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            new ErrorDialog(getString(R.string.lost_connection_to_server), new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    finish();
                }
            }).show(getSupportFragmentManager(), null);
        }
    };

    private Emitter.Listener onWin = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            try {
                JSONObject object = (JSONObject) args[0];
                boolean win = object.getString("username").equals(SocketHandler.getPlayer().getUsername());
                String message = getString(win ? R.string.congratulation : R.string.try_harder_next_time);
                if (object.has("message")) {
                    message = object.getString("message");
                }
                new ResultDialog(getString(R.string.the_winner_is) + " " + object.getString("username"), message, !object.has("message")).show(getSupportFragmentManager(), null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private Emitter.Listener onEven = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            new ResultDialog(getString(R.string.even), "", true).show(getSupportFragmentManager(), null);
        }
    };

    private Emitter.Listener onRematch = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    resetGame();
                }
            });
            SocketHandler.emit("start");
        }
    };

    private Emitter.Listener onFirst = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject object = (JSONObject) args[0];
                        String username = object.getString("username");
                        boardViewer.setSwitchMarker(!username.equals(SocketHandler.getPlayer().getUsername()) ^ switchMark);
                        MyToast.show(PlayOnlineActivity.this, username + " " + getString(R.string.will_go_first));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };
}
