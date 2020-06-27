package com.mobile.caro.TwoPlayersOnlineActivity.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.mobile.caro.R;
import com.mobile.caro.TwoPlayersOnlineActivity.Dialog.ErrorDialog;
import com.mobile.caro.TwoPlayersOnlineActivity.Dialog.WarningDialog;
import com.mobile.caro.TwoPlayersOnlineActivity.Entity.Room;
import com.mobile.caro.TwoPlayersOnlineActivity.Network.SocketHandler;

import org.json.JSONObject;

import io.socket.emitter.Emitter;

public class JoinRoomOnlineActivity extends FragmentActivity {

    private Room room;

    private Spinner spinner;
    private Switch _switch;
    private ToggleButton button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_room_join);

        mapping();
        initialize();
        setupListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
        SocketHandler.on("disconnect", onDisconnect);
        SocketHandler.on("kick", onKick);
        SocketHandler.on("leave", onLeave);
        SocketHandler.on("room", onRoom);
        SocketHandler.on("ready", onReady);
        SocketHandler.on("start", onStart);
    }

    @Override
    protected void onStop() {
        super.onStop();
        SocketHandler.off("disconnect", onDisconnect);
        SocketHandler.off("kick", onKick);
        SocketHandler.off("leave", onLeave);
        SocketHandler.off("room", onRoom);
        SocketHandler.off("ready", onReady);
        SocketHandler.off("start", onStart);
    }

    private void mapping() {
        spinner = findViewById(R.id.spinner);
        _switch = findViewById(R.id._switch);
        button = findViewById(R.id.ready);
    }

    private void initialize() {
        spinner.setAdapter(ArrayAdapter.createFromResource(this, R.array.timelapse_choices, android.R.layout.simple_spinner_dropdown_item));

        room = (Room) getIntent().getSerializableExtra("room");

        ((TextView) findViewById(R.id.username2)).setText(SocketHandler.getPlayer().getUsername());
        ((ImageView) findViewById(R.id.userImage2)).setImageResource(getResources().getIdentifier(SocketHandler.getPlayer().getImageUrl(), "drawable", getPackageName()));
        ((TextView) findViewById(R.id.elo2)).setText(SocketHandler.getPlayer().getElo());

        ((TextView) findViewById(R.id.username1)).setText(room.getPlayer().getUsername());
        ((ImageView) findViewById(R.id.userImage1)).setImageResource(getResources().getIdentifier(room.getPlayer().getImageUrl(), "drawable", getPackageName()));
        ((TextView) findViewById(R.id.elo1)).setText(room.getPlayer().getElo());

        spinner.setSelection((room.getTimelapse() - 15) / 5);
        spinner.setEnabled(false);

        _switch.setChecked(room.isRank());
        _switch.setEnabled(false);
    }


    private void setupListener() {
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                SocketHandler.emit("leave");
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    JSONObject object = new JSONObject();
                    object.put("ready", button.isChecked());
                    SocketHandler.emit("ready", object);
                    button.setEnabled(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


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

    private Emitter.Listener onLeave = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            new WarningDialog(getString(R.string.host_has_left_the_room), new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    SocketHandler.emit("leave");
                    finish();
                }
            }).show(getSupportFragmentManager(), null);
        }
    };


    private Emitter.Listener onKick = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            new WarningDialog(getString(R.string.you_have_been_kicked), new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    finish();
                }
            }).show(getSupportFragmentManager(), null);
        }
    };


    private Emitter.Listener onRoom = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            try {
                JSONObject object = (JSONObject) args[0];
                room.setPassword(object.getBoolean("havePassword"));
                room.setTimelapse(object.getInt("timelapse"));
                room.setRank(object.getBoolean("rank"));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        button.setChecked(false);
                        spinner.setSelection((room.getTimelapse() - 15) / 5);
                        _switch.setChecked(room.isRank());
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    private Emitter.Listener onReady = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            try {
                JSONObject object = (JSONObject) args[0];
                final boolean ready = object.getBoolean("ready");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        button.setEnabled(true);
                        button.setChecked(ready);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private Emitter.Listener onStart = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            try {
                JSONObject object = (JSONObject) args[0];
                if (object.getBoolean("success")) {
                    Intent intent = new Intent(JoinRoomOnlineActivity.this, PlayOnlineActivity.class);
                    intent.putExtra("room", room);
                    startActivity(intent);
                    finish();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
}
