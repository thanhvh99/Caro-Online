package com.mobile.caro.TwoPlayersOnlineActivity.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mobile.caro.R;

public class SettingsActivity extends AppCompatActivity {

    private RadioGroup first;
    private RadioGroup confirm;
    private RadioGroup mark;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        mapping();
        initialize();
        setupListener();
    }

    private void mapping() {
        first = findViewById(R.id.first);
        confirm = findViewById(R.id.confirmMove);
        mark = findViewById(R.id.mark);
    }

    private void initialize() {
        SharedPreferences sharedPreferences = getSharedPreferences("OnlineSettings", MODE_PRIVATE);
        ((RadioButton) first.getChildAt(sharedPreferences.getInt("first", 0))).setChecked(true);
        ((RadioButton) confirm.getChildAt(sharedPreferences.getInt("confirm", 1))).setChecked(true);
        ((RadioButton) mark.getChildAt(sharedPreferences.getInt("mark", 0))).setChecked(true);
    }

    private void setupListener() {
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getSharedPreferences("OnlineSettings", MODE_PRIVATE).edit()
                .putInt("first", getCheckedRadioButtonIndex(first))
                .putInt("confirm", getCheckedRadioButtonIndex(confirm))
                .putInt("mark", getCheckedRadioButtonIndex(mark))
                .apply();
    }

    private int getCheckedRadioButtonIndex(RadioGroup radioGroup) {
        int id = radioGroup.getCheckedRadioButtonId();
        View view = radioGroup.findViewById(id);
        return radioGroup.indexOfChild(view);
    }
}
