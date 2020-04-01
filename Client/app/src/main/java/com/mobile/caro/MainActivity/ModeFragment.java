package com.mobile.caro.MainActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mobile.caro.OnePlayerActivity.OnePlayerActivity;
import com.mobile.caro.R;
import com.mobile.caro.TwoPlayersOfflineActivity.TwoPlayersActivity;
import com.mobile.caro.TwoPlayersOnlineActivity.MainOnlineActivity;

public class ModeFragment extends Fragment {

    private MainActivity activity;

    public ModeFragment(MainActivity activity) {
        this.activity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mode, null);

        view.findViewById(R.id.btn_play_with_computer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, OnePlayerActivity.class);
                startActivity(intent);
            }
        });

        view.findViewById(R.id.btn_play_with_human).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, TwoPlayersActivity.class);
                startActivity(intent);
            }
        });

        view.findViewById(R.id.btn_play_online).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, MainOnlineActivity.class);
                startActivity(intent);
            }
        });

        view.findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.changeMenuFragment();
            }
        });
        return view;
    }
}
