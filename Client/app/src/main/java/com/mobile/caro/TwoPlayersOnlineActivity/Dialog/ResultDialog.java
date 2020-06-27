package com.mobile.caro.TwoPlayersOnlineActivity.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.mobile.caro.MyToast;
import com.mobile.caro.R;
import com.mobile.caro.TwoPlayersOnlineActivity.Network.SocketHandler;

import io.socket.emitter.Emitter;

public class ResultDialog extends DialogFragment {

    private String title;
    private String message;
    private boolean rematchable;

    private View view;
    private ToggleButton rematch;

    public ResultDialog(String title, String message, boolean rematchable) {
        this.title = title;
        this.message = message;
        this.rematchable = rematchable;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        setCancelable(false);

        view = getActivity().getLayoutInflater().inflate(R.layout.dialog_result, null);

        mapping();
        initialize();
        setupListener();

        return new AlertDialog.Builder(getContext())
                .setView(view)
                .create();
    }

    @Override
    public void onStart() {
        super.onStart();
        SocketHandler.on("leave", onLeave);
        SocketHandler.on("rematch", onRematch);
    }

    @Override
    public void onStop() {
        super.onStop();
        SocketHandler.off("leave", onLeave);
        SocketHandler.off("rematch", onRematch);
    }

    private void mapping() {
        rematch = view.findViewById(R.id.rematch);
    }

    private void initialize() {
        ((TextView) view.findViewById(R.id.winner)).setText(title);
        ((TextView) view.findViewById(R.id.message)).setText(message);
        if (!rematchable) {
            rematch.setVisibility(View.GONE);
        }
    }


    private void setupListener() {
        rematch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SocketHandler.emit("rematch");
                rematch.setEnabled(false);
            }
        });

        view.findViewById(R.id.leave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
                dismiss();
            }
        });
    }

    private Emitter.Listener onLeave = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    rematch.setVisibility(View.GONE);
                    MyToast.show(getContext(), R.string.your_opponent_has_left);
                }
            });
        }
    };

    private Emitter.Listener onRematch = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            dismiss();
        }
    };
}
