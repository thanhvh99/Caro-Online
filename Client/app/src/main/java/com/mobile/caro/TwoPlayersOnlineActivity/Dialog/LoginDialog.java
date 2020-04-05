package com.mobile.caro.TwoPlayersOnlineActivity.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.mobile.caro.R;
import com.mobile.caro.TwoPlayersOnlineActivity.Activity.MainOnlineActivity;
import com.mobile.caro.TwoPlayersOnlineActivity.Network.Network;
import com.mobile.caro.TwoPlayersOnlineActivity.Network.UserAPI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginDialog extends DialogFragment {

    private EditText username;
    private EditText password;
    private View view;
    private MainOnlineActivity activity;

    public LoginDialog(MainOnlineActivity activity) {
        this.activity = activity;
        view = activity.getLayoutInflater().inflate(R.layout.dialog_login, null);
        mapping();
        setupListener();
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        setCancelable(false);
        return new AlertDialog.Builder(activity)
                .setView(view)
                .create();
    }

    private void mapping() {
        username = view.findViewById(R.id.edt_username);
        password = view.findViewById(R.id.edt_password);
    }

    private void setupListener() {
        view.findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendLoginRequest();
            }
        });

        view.findViewById(R.id.btn_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.showRegisterDialog();
                dismiss();
            }
        });

        view.findViewById(R.id.img_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, R.string.you_must_login_to_continue, Toast.LENGTH_SHORT).show();
                dismiss();
                activity.finish();
            }
        });
    }

    private void sendLoginRequest() {
        UserAPI.UserDTO userDTO = new UserAPI.UserDTO();
        userDTO.username = username.getText().toString();
        userDTO.password = password.getText().toString();
        Network.getRetrofit(UserAPI.class).login(userDTO).enqueue(new Callback<UserAPI.ResultDTO>() {

            @Override
            public void onResponse(Call<UserAPI.ResultDTO> call, Response<UserAPI.ResultDTO> response) {
                if (response.body().success) {
                    Toast.makeText(getContext(), response.body().token, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), response.body().message, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UserAPI.ResultDTO> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
