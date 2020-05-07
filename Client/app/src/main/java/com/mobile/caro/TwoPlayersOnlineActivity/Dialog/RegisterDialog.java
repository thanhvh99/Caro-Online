package com.mobile.caro.TwoPlayersOnlineActivity.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class RegisterDialog extends DialogFragment {

    private View view;
    private EditText username;
    private EditText password;
    private EditText confirm;
    private Button back;
    private Button register;
    private ImageView close;
    private ProgressBar progressBar;
    private ConstraintLayout dialogContent;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        setCancelable(false);

        view = getActivity().getLayoutInflater().inflate(R.layout.dialog_register, null);

        mapping();
        setupListener();

        return new AlertDialog.Builder(getContext())
                .setView(view)
                .create();
    }

    private void mapping() {
        username = view.findViewById(R.id.edt_username);
        password = view.findViewById(R.id.edt_password);
        confirm = view.findViewById(R.id.edt_confirm);
        back = view.findViewById(R.id.btn_back);
        register = view.findViewById(R.id.btn_register);
        close = view.findViewById(R.id.img_close);
        progressBar = view.findViewById(R.id.progressBar);
        dialogContent = view.findViewById(R.id.dialogContent);
    }

    private void setupListener() {
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRegisterRequest();
                setLoading(true);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LoginDialog().show(getParentFragmentManager(), null);
                dismiss();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
                dismiss();
            }
        });
    }

    private void setLoading(boolean loading) {
        username.setEnabled(!loading);
        password.setEnabled(!loading);
        confirm.setEnabled(!loading);
        register.setClickable(!loading);
        back.setClickable(!loading);
        close.setClickable(!loading);
        dialogContent.setAlpha(loading ? 0.5f : 1f);
        progressBar.setVisibility(loading ? View.VISIBLE : View.INVISIBLE);
    }

    private void saveLoginData() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Username", username.getText().toString());
        editor.putString("Password", "");
        editor.putBoolean("Remember", false);
        editor.apply();
    }

    private void sendRegisterRequest() {
        if (!password.getText().toString().equals(confirm.getText().toString())) {
            Toast.makeText(getContext(), R.string.confirm_password_does_not_match, Toast.LENGTH_SHORT).show();
            return;
        }
        UserAPI.UserDTO userDTO = new UserAPI.UserDTO();
        userDTO.username = username.getText().toString();
        userDTO.password = password.getText().toString();
        Network.getRetrofit(UserAPI.class).register(userDTO).enqueue(new Callback<UserAPI.ResultDTO>() {

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
