package com.mobile.caro.OnePlayerActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.mobile.caro.AbstractPlayActivity;
import com.mobile.caro.Board;
import com.mobile.caro.R;

public class OnePlayerActivity extends AbstractPlayActivity {
    private ImageButton ibtnMenu;
    private DrawerLayout drawerLayout;
    private int dem = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_player);
        mapping();
        ibtnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dem ++;
                onOpenDrawer(dem);
            }
        });
        board = new Board(19);
        setupBoardViewer();
    }

    private void onOpenDrawer(int x){
        if(x == 1){
            drawerLayout.openDrawer(Gravity.LEFT);
        }
        else {
            drawerLayout.closeDrawers();
            dem = 0;
        }
    }

    private void mapping() {
        ibtnMenu = (ImageButton)findViewById(R.id.ibtn_menu);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
    }

    @Override
    protected void onTileSelected(int x, int y) {

    }




//    @Override
//    public void setActionBar(@Nullable android.widget.Toolbar toolbar) {
//        super.setActionBar(toolbar);
//    }
}
