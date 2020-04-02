package com.mobile.caro.OnePlayerActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.mobile.caro.AbstractPlayActivity;
import com.mobile.caro.Board;
import com.mobile.caro.R;


public class OnePlayerActivity extends AbstractPlayActivity {
    private ImageButton ibtnMenu, ibtn_replay;
    private DrawerLayout drawerLayout;
    private NavigationView navigationOnePlayer;
    private TextView textViewLevel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_player);
        mapping();

        board = new Board(19);
        setupBoardViewer();

        //click menu
        onMenu();

        //đánh lại
        re_play();

        //set text
        textViewLevel.setText("Độ khó");
    }

    private void re_play() {
        ibtn_replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(OnePlayerActivity.this,"replay",Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void onMenu() {
        ibtnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
    }

    private void mapping() {
        ibtnMenu = (ImageButton)findViewById(R.id.ibtn_menu);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        ibtn_replay = (ImageButton) findViewById(R.id.ibtn_replay);
        navigationOnePlayer = (NavigationView)findViewById(R.id.navigation_one);
        textViewLevel = (TextView) findViewById(R.id.tv_mode);
    }

    @Override
    protected void onTileSelected(int x, int y) {

    }




//    @Override
//    public void setActionBar(@Nullable android.widget.Toolbar toolbar) {
//        super.setActionBar(toolbar);
//    }
}
