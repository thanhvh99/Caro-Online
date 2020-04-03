package com.mobile.caro.OnePlayerActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.mobile.caro.AbstractPlayActivity;
import com.mobile.caro.Board;
import com.mobile.caro.R;


public class OnePlayerActivity extends AbstractPlayActivity {
    private ImageButton ibtnMenu, ibtn_replay;
    private DrawerLayout drawerLayout;
    private TextView textViewLevel, tvPlayerOne, tvPlayerTwo;
    private ImageView imgPlayerOne, imgPlayerTwo;

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

        //set text, quân cờ
        textViewLevel.setText("Độ khó");
        imgPlayerOne.setImageResource(R.drawable.o);
        imgPlayerTwo.setImageResource(R.drawable.x);

        //select Item navigation
        selectedItemNavigation();

    }

    private void selectedItemNavigation() {
        NavigationView navigationView = new NavigationView(OnePlayerActivity.this);
        navigationView = (NavigationView) findViewById(R.id.navigation_one);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.menu_easy:
                        Toast.makeText(OnePlayerActivity.this,"Easy Level!",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.menu_medium:
                        Toast.makeText(OnePlayerActivity.this,"Medium Level!",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.menu_hard:
                        Toast.makeText(OnePlayerActivity.this,"Hard Level",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.menu_width:
                        Toast.makeText(OnePlayerActivity.this,"Width",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.menu_type_of_chess:
                        Toast.makeText(OnePlayerActivity.this,"Quân cờ",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.menu_confirm:
                        Toast.makeText(OnePlayerActivity.this,"Confirm",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.menu_quit:
                        Toast.makeText(OnePlayerActivity.this,"Quit!",Toast.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }
        });
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
        textViewLevel = (TextView) findViewById(R.id.tv_mode);
        tvPlayerOne = (TextView) findViewById(R.id.tv_player_one);
        tvPlayerTwo = (TextView) findViewById(R.id.tv_player_two);
        imgPlayerOne = (ImageView) findViewById(R.id.img_player_one);
        imgPlayerTwo = (ImageView) findViewById(R.id.img_player_two);
    }

    @Override
    protected void onTileSelected(int x, int y) {

    }




//    @Override
//    public void setActionBar(@Nullable android.widget.Toolbar toolbar) {
//        super.setActionBar(toolbar);
//    }
}
