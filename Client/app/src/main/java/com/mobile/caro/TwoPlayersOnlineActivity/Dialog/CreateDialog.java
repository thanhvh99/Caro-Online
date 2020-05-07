package com.mobile.caro.TwoPlayersOnlineActivity.Dialog;

import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mobile.caro.R;
import com.mobile.caro.TwoPlayersOnlineActivity.Activity.HostRoomOnlineActivity;
import com.mobile.caro.TwoPlayersOnlineActivity.Entity.Room;
import com.mobile.caro.TwoPlayersOnlineActivity.Network.SocketHandler;

import org.json.JSONObject;

public class CreateDialog extends DialogFragment {

    private View view;
    private EditText password;
    private Button create;
    private Button back;
    private Switch _switch;
    private Spinner spinner;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        view = getActivity().getLayoutInflater().inflate(R.layout.dialog_room, null);

        mapping();
        initialize();
        setupListener();

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();
    }

    private void mapping() {
        spinner = view.findViewById(R.id.spinner);
        _switch = view.findViewById(R.id._switch);
        password = view.findViewById(R.id.editText);
        create = view.findViewById(R.id.create);
        back = view.findViewById(R.id.back);
    }

    private void initialize() {
        spinner.setAdapter(ArrayAdapter.createFromResource(getActivity(), R.array.timelapse_choices, android.R.layout.simple_spinner_dropdown_item));
        spinner.setSelection(1);
    }

    private void setupListener() {
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    JSONObject object = new JSONObject();
                    object.put("rank", _switch.isChecked());
                    object.put("timelapse", spinner.getSelectedItemPosition() * 5 + 15);
                    object.put("password", password.getText().toString());
                    SocketHandler.emit("create", object);
                    dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                    dismiss();
                }
                Intent intent = new Intent(getActivity(), HostRoomOnlineActivity.class);
                Room room = new Room();
                room.setRank(_switch.isChecked());
                room.setTimelapse(spinner.getSelectedItemPosition() * 5 + 15);
                room.setPassword(!password.getText().toString().isEmpty());
                intent.putExtra("room", room);
                intent.putExtra("password", password.getText().toString());
                startActivity(intent);
                dismiss();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
