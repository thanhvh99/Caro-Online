package com.mobile.caro.MainActivity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.mobile.caro.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {

    private MenuFragment menuFragment;
    private ModeFragment modeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();
    }

    private void initialize() {
        menuFragment = new MenuFragment();
        modeFragment = new ModeFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_content, menuFragment).commit();
    }

    public void changeMenuFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_content, menuFragment).commit();
    }

    public void changeModeFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_content, modeFragment).commit();
    }

}
