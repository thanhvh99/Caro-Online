package com.mobile.caro.TwoPlayersOnlineActivity.Activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;

import com.mobile.caro.R;
import com.mobile.caro.TwoPlayersOnlineActivity.Dialog.LoginDialog;
import com.mobile.caro.TwoPlayersOnlineActivity.Dialog.RegisterDialog;

public class MainOnlineActivity extends FragmentActivity {

    private DrawerLayout drawerLayout;
    private String token;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_online);

        drawerLayout = findViewById(R.id.drawerlayout);
        findViewById(R.id.img_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        showLoginDialog();
    }

    public void showLoginDialog() {
        LoginDialog dialog = new LoginDialog(this);
        dialog.show(getSupportFragmentManager(), null);
    }

    public void showRegisterDialog() {
        RegisterDialog dialog = new RegisterDialog(this);
        dialog.show(getSupportFragmentManager(), null);
    }

    public void connectSocket(String token) {
        this.token = token;
        Soc
    }
}
