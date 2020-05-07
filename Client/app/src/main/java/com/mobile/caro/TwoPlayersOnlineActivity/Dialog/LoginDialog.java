package com.mobile.caro.TwoPlayersOnlineActivity.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import com.mobile.caro.R;
import com.mobile.caro.TwoPlayersOnlineActivity.Network.Network;
import com.mobile.caro.TwoPlayersOnlineActivity.Network.SocketHandler;
import com.mobile.caro.TwoPlayersOnlineActivity.Network.UserAPI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginDialog extends DialogFragment {

    private View view;
    private EditText username;
    private EditText password;
    private Button login;
    private Button register;
    private ImageView close;
    private ConstraintLayout dialogContent;
    private ProgressBar progressBar;
    private CheckBox remember;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        setCancelable(false);

        view = getActivity().getLayoutInflater().inflate(R.layout.dialog_login, null);

        mapping();
        initialize();
        setupListener();

        if (remember.isChecked() && username.getText().length() != 0 && password.getText().length() != 0) {
            login.performClick();
        }

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();
    }

    private void mapping() {
        username = view.findViewById(R.id.edt_username);
        password = view.findViewById(R.id.edt_password);
        close = view.findViewById(R.id.img_close);
        login = view.findViewById(R.id.btn_login);
        register = view.findViewById(R.id.btn_register);
        dialogContent = view.findViewById(R.id.dialogContent);
        progressBar = view.findViewById(R.id.progressBar);
        remember = view.findViewById(R.id.cb_remember);
    }

    private void initialize() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
        remember.setChecked(sharedPreferences.getBoolean("Remember", false));
        username.setText(sharedPreferences.getString("Username", ""));
        password.setText(sharedPreferences.getString("Password", ""));
    }

    private void saveLoginData() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Username", username.getText().toString());
        editor.putString(remember.isChecked() ? "Password" : "", password.getText().toString());
        editor.putBoolean("Remember", remember.isChecked());
        editor.apply();
    }

    private void setupListener() {
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendLoginRequest();
                setLoading(true);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                new RegisterDialog().show(getParentFragmentManager(), null);
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), R.string.you_must_login_to_continue, Toast.LENGTH_SHORT).show();
                dismiss();
                getActivity().finish();
            }
        });
    }

    private void setLoading(boolean loading) {
        username.setEnabled(!loading);
        password.setEnabled(!loading);
        login.setClickable(!loading);
        register.setClickable(!loading);
        close.setClickable(!loading);
        remember.setClickable(!loading);
        dialogContent.setAlpha(loading ? 0.5f : 1f);
        progressBar.setVisibility(loading ? View.VISIBLE : View.INVISIBLE);
    }

    private void sendLoginRequest() {
        UserAPI.UserDTO userDTO = new UserAPI.UserDTO();
        userDTO.username = username.getText().toString();
        userDTO.password = password.getText().toString();
        Network.getRetrofit(UserAPI.class).login(userDTO).enqueue(new Callback<UserAPI.ResultDTO>() {
            @Override
            public void onResponse(Call<UserAPI.ResultDTO> call, Response<UserAPI.ResultDTO> response) {
                setLoading(false);
                if (response.body() == null) {
                    Toast.makeText(getActivity(), R.string.error_please_try_again, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (response.body().success) {
                    SocketHandler.connect(response.body().message);
                    saveLoginData();
                    dismiss();
                } else {
                    Toast.makeText(getActivity(), response.body().message, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UserAPI.ResultDTO> call, Throwable t) {
                setLoading(false);
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
