package com.mobile.caro.TwoPlayersOnlineActivity;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.mobile.caro.R;

public class MainOnlineActivity extends Activity {
    TextView textView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_online);

        //ánh xạ
        mapping();

        // nhap
        openLogIn();

    }

    private void openLogIn() {
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogLogIn();
            }
        });
    }



    //diaLogdialogLogIn
    private void dialogLogIn (){
        final Dialog dialogLogIn = new Dialog(this);

        dialogLogIn.setTitle("Log In");
        dialogLogIn.setContentView(R.layout.dialog_login);
        dialogLogIn.setCanceledOnTouchOutside(false);
        Window window = dialogLogIn.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        //anh xa
        Button btnSignInLogin = (Button) dialogLogIn.findViewById(R.id.btn_sign_in_login);
        Button btnSignUpLogin = (Button) dialogLogIn.findViewById(R.id.btn_sign_up_login);
        ImageButton ibtnCloseLogIn = (ImageButton) dialogLogIn.findViewById(R.id.ibtn_close_login);

//        //xu li su kien

        btnSignInLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainOnlineActivity.this, "Log In", Toast.LENGTH_SHORT).show();
            }
        });

        btnSignUpLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainOnlineActivity.this, "Sign Up", Toast.LENGTH_SHORT).show();
                dialogLogIn.dismiss();
                dialogRegister();
            }
        });

        ibtnCloseLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogLogIn.dismiss();
            }
        });

        //show
        dialogLogIn.show();
    }

    //dialogRegister
    private void dialogRegister (){
        final Dialog dialogRegister = new Dialog(this);
        dialogRegister.setTitle("Register");
        dialogRegister.setContentView(R.layout.dialog_register);
        dialogRegister.setCanceledOnTouchOutside(false);
        Window window = dialogRegister.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        //anh xa
        Button btnSignUpRegister = (Button) dialogRegister.findViewById(R.id.btn_sign_up_register);
        ImageButton ibtnCloseRegister = (ImageButton) dialogRegister.findViewById(R.id.ibtn_close_register);

        //xu li su kien
        btnSignUpRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainOnlineActivity.this, "Sign Up", Toast.LENGTH_SHORT).show();
            }
        });
        ibtnCloseRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogRegister.dismiss();
                dialogLogIn();
            }
        });

        //show
        dialogRegister.show();
    }

//    //dialogChangePassword
    private void dialogChangePassword (){
        Dialog dialogChangePassword = new Dialog(this);
        dialogChangePassword.setTitle("Change Password");
        dialogChangePassword.setContentView(R.layout.dialog_change_password);
        dialogChangePassword.setCanceledOnTouchOutside(false);
        Window window = dialogChangePassword.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        //anh xa
        Button btnComfirm = (Button) dialogChangePassword.findViewById(R.id.btn_comfirm);

       dialogChangePassword.show();
    }


    private void mapping() {
        textView = (TextView) findViewById(R.id.textView);
    }
}
