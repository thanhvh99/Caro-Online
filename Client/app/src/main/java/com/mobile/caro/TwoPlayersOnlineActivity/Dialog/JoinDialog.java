package com.mobile.caro.TwoPlayersOnlineActivity.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.mobile.caro.MyToast;
import com.mobile.caro.R;
import com.mobile.caro.TwoPlayersOnlineActivity.Activity.HostRoomOnlineActivity;
import com.mobile.caro.TwoPlayersOnlineActivity.Activity.JoinRoomOnlineActivity;
import com.mobile.caro.TwoPlayersOnlineActivity.Entity.Room;
import com.mobile.caro.TwoPlayersOnlineActivity.Network.SocketHandler;
import org.json.JSONObject;

import io.socket.emitter.Emitter;

public class JoinDialog extends DialogFragment {

    private Room room;

    private View view;
    private EditText username;
    private EditText password;
    private Button join;
    private Button back;

    public JoinDialog(Room room) {
        this.room = room;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        view = getActivity().getLayoutInflater().inflate(R.layout.dialog_join, null);

        mapping();
        initialize();
        setupListener();

        if (!room.havePassword()) {
            join.performClick();
        }

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SocketHandler.on("join", onJoin);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SocketHandler.off("join", onJoin);
    }

    private void mapping() {
        username = view.findViewById(R.id.username);
        password = view.findViewById(R.id.password);
        back = view.findViewById(R.id.back);
        join = view.findViewById(R.id.join);
    }

    private void initialize() {
        username.setText(room.getPlayer().getUsername());
    }

    private void setupListener() {
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    JSONObject object = new JSONObject();
                    object.put("username", username.getText().toString());
                    object.put("password", password.getText().toString());
                    SocketHandler.emit("join", object);
                    join.setEnabled(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private Emitter.Listener onJoin = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            try {
                JSONObject object = (JSONObject) args[0];
                boolean success = object.getBoolean("success");
                if (success) {
                    dismiss();
                    Intent intent = new Intent(getActivity(), JoinRoomOnlineActivity.class);
                    intent.putExtra("room", room);
                    startActivity(intent);
                } else {
                    final String message = object.getString("message");
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            join.setEnabled(true);
                            MyToast.show(getActivity(), message);
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

}
