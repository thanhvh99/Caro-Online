package com.mobile.caro.TwoPlayersOnlineActivity.BottomSheet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.mobile.caro.MyToast;
import com.mobile.caro.R;
import com.mobile.caro.TwoPlayersOnlineActivity.Network.SocketHandler;

import org.json.JSONObject;

import io.socket.emitter.Emitter;

public class GoFirstBottomSheet extends BottomSheetDialogFragment {

    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.bottom_sheet_go_first, container, false);

        setupListener();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        SocketHandler.on("first", onFirst);
    }

    @Override
    public void onStop() {
        super.onStop();
        SocketHandler.off("first", onFirst);
    }

    private void setupListener() {
        view.findViewById(R.id.yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    JSONObject object = new JSONObject();
                    object.put("first", true);
                    SocketHandler.emit("first", object);
                    dismiss();
                } catch (Exception e) {
                    dismiss();
                }
            }
        });

        view.findViewById(R.id.no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    JSONObject object = new JSONObject();
                    object.put("first", false);
                    SocketHandler.emit("first", object);
                    dismiss();
                } catch (Exception e) {
                    dismiss();
                }
            }
        });
    }

    private Emitter.Listener onFirst = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            dismiss();
        }
    };
}
