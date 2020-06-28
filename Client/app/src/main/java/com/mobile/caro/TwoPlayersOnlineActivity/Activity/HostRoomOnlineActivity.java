package com.mobile.caro.TwoPlayersOnlineActivity.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.mobile.caro.MyToast;
import com.mobile.caro.R;
import com.mobile.caro.TwoPlayersOnlineActivity.Dialog.ErrorDialog;
import com.mobile.caro.TwoPlayersOnlineActivity.Entity.Player;
import com.mobile.caro.TwoPlayersOnlineActivity.Entity.Room;
import com.mobile.caro.TwoPlayersOnlineActivity.Network.SocketHandler;

import org.json.JSONObject;

import io.socket.emitter.Emitter;

public class HostRoomOnlineActivity extends AppCompatActivity {

    private Room room;

    private ImageView userImage;
    private TextView username;
    private TextView elo;
    private Spinner spinner;
    private Switch _switch;
    private EditText editText;
    private Button save;
    private Button kick;
    private ToggleButton start;

    private String password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_room_host);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

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
        SocketHandler.on("join", onJoin);
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
        SocketHandler.off("join", onJoin);
        SocketHandler.off("room", onRoom);
        SocketHandler.off("ready", onReady);
        SocketHandler.off("start", onStart);
    }

    private void mapping() {
        userImage = findViewById(R.id.userImage2);
        username = findViewById(R.id.username2);
        elo = findViewById(R.id.elo2);
        spinner = findViewById(R.id.spinner);
        _switch = findViewById(R.id._switch);
        editText = findViewById(R.id.editText);
        save = findViewById(R.id.save);
        kick = findViewById(R.id.kick);
        start = findViewById(R.id.start);
    }

    private void initialize() {
        room = (Room) getIntent().getSerializableExtra("room");
        password = getIntent().getStringExtra("password");

        spinner.setAdapter(ArrayAdapter.createFromResource(this, R.array.timelapse_choices, android.R.layout.simple_spinner_dropdown_item));
        spinner.setSelection((room.getTimelapse() - 15) / 5);
        _switch.setChecked(room.isRank());
        editText.setText(password);

        if (room.getPlayer() != null) {
            username.setText(room.getPlayer().getUsername());
            userImage.setImageResource(getResources().getIdentifier(room.getPlayer().getImageUrl(), "drawable", getPackageName()));
            elo.setText(room.getPlayer().getElo());
        } else {
            userImage.setVisibility(View.GONE);
            elo.setVisibility(View.GONE);
            kick.setVisibility(View.GONE);
        }
        save.setEnabled(false);

        ((TextView) findViewById(R.id.username1)).setText(SocketHandler.getPlayer().getUsername());
        ((ImageView) findViewById(R.id.userImage1)).setImageResource(getResources().getIdentifier(SocketHandler.getPlayer().getImageUrl(), "drawable", getPackageName()));
        ((TextView) findViewById(R.id.elo1)).setText(SocketHandler.getPlayer().getElo());
    }


    private void setupListener() {
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                SocketHandler.emit("leave");
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                room.setRank(_switch.isChecked());
                password = editText.getText().toString();
                room.setTimelapse(spinner.getSelectedItemPosition() * 5 + 15);
                try {
                    JSONObject object = new JSONObject();
                    object.put("rank", _switch.isChecked());
                    object.put("timelapse", spinner.getSelectedItemPosition() * 5 + 15);
                    object.put("password", editText.getText().toString());
                    SocketHandler.emit("room", object);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                save.setEnabled(isRoomSettingsChanged());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        _switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                save.setEnabled(isRoomSettingsChanged());
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                save.setEnabled(isRoomSettingsChanged());
            }
        });

        kick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SocketHandler.emit("kick");
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start.setChecked(!start.isChecked());
                SocketHandler.emit("start");
            }
        });

    }

    private boolean isRoomSettingsChanged() {
        return !(password.equals(editText.getText().toString()) &&
                spinner.getSelectedItemPosition() * 5 + 15 == room.getTimelapse() &&
                _switch.isChecked() == room.isRank());
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


    private Emitter.Listener onJoin = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            try {
                JSONObject object = (JSONObject) args[0];
                room.setPlayer(new Player(object.getString("username"), object.getString("imageUrl"), object.getString("elo")));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        username.setText(room.getPlayer().getUsername());

                        userImage.setImageResource(getResources().getIdentifier(room.getPlayer().getImageUrl(), "drawable", getPackageName()));
                        userImage.setVisibility(View.VISIBLE);

                        elo.setVisibility(View.VISIBLE);
                        elo.setText(room.getPlayer().getElo());

                        kick.setVisibility(View.VISIBLE);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    private Emitter.Listener onLeave = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    start.setChecked(false);
                    userImage.setVisibility(View.GONE);
                    elo.setVisibility(View.GONE);
                    kick.setVisibility(View.GONE);
                    username.setText(R.string.waiting_for_second_user);
                }
            });
        }
    };


    private Emitter.Listener onKick = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    start.setChecked(false);
                    userImage.setVisibility(View.GONE);
                    elo.setVisibility(View.GONE);
                    kick.setVisibility(View.GONE);
                    username.setText(R.string.waiting_for_second_user);
                }
            });
        }
    };


    private Emitter.Listener onRoom = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            try {
                final JSONObject object = (JSONObject) args[0];
                room.setPassword(object.getBoolean("havePassword"));
                room.setTimelapse(object.getInt("timelapse"));
                room.setRank(object.getBoolean("rank"));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        save.setEnabled(false);
                        spinner.setSelection((room.getTimelapse() - 15) / 5);
                        _switch.setChecked(room.isRank());
                        start.setChecked(false);
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
                final boolean result = object.getBoolean("ready");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        start.setChecked(result);
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
                    Intent intent = new Intent(HostRoomOnlineActivity.this, PlayOnlineActivity.class);
                    intent.putExtra("room", room);
                    startActivity(intent);
                    finish();
                    return;
                }

                final String message = object.getString("message");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            MyToast.show(HostRoomOnlineActivity.this, message);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
}
