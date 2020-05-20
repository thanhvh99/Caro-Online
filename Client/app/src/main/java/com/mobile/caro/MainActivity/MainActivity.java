package com.mobile.caro.MainActivity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.mobile.caro.OnePlayerActivity.OnePlayerActivity;
import com.mobile.caro.R;
import com.mobile.caro.TwoPlayersOfflineActivity.TwoPlayersActivity;
import com.mobile.caro.TwoPlayersOnlineActivity.Activity.MainOnlineActivity;


public class MainActivity extends FragmentActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupListener();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    private void setupListener() {
        findViewById(R.id.computer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, OnePlayerActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.multiplayer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TwoPlayersActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.online).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainOnlineActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.quit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
