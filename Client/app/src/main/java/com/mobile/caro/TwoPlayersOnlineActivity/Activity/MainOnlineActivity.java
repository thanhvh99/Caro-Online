package com.mobile.caro.TwoPlayersOnlineActivity.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;

import com.mobile.caro.R;
import com.mobile.caro.TwoPlayersOnlineActivity.Dialog.ErrorDialog;
import com.mobile.caro.TwoPlayersOnlineActivity.Dialog.LoginDialog;
import com.mobile.caro.TwoPlayersOnlineActivity.Entity.Player;
import com.mobile.caro.TwoPlayersOnlineActivity.Fragment.ChallengeFragment;
import com.mobile.caro.TwoPlayersOnlineActivity.Fragment.RoomsFragment;
import com.mobile.caro.TwoPlayersOnlineActivity.Fragment.LeaderboardFragment;
import com.mobile.caro.TwoPlayersOnlineActivity.Fragment.StatisticFragment;
import com.mobile.caro.TwoPlayersOnlineActivity.Network.SocketHandler;

import org.json.JSONObject;

import io.socket.emitter.Emitter;

public class MainOnlineActivity extends FragmentActivity {

    private boolean active;

    private DrawerLayout drawerLayout;
    private ImageView userImage;
    private TextView username;
    private TextView elo;
    private TextView title;

    private RoomsFragment roomsFragment;
    private ChallengeFragment challengeFragment;
    private StatisticFragment statisticFragment;
    private LeaderboardFragment leaderboardFragment;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_main);

        mapping();
        initialize();
        setupSocket();
        setupListener();

        new LoginDialog().show(getSupportFragmentManager(), null);
    }

    @Override
    protected void onStart() {
        super.onStart();
        active = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        active = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SocketHandler.close();
    }

    private void mapping() {
        drawerLayout = findViewById(R.id.drawerlayout);
        userImage = findViewById(R.id.userImage);
        username = findViewById(R.id.username);
        elo = findViewById(R.id.elo);
        title = findViewById(R.id.title);
    }

    private void initialize() {
        roomsFragment = new RoomsFragment();
        challengeFragment = new ChallengeFragment();
        statisticFragment = new StatisticFragment();
        leaderboardFragment = new LeaderboardFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, roomsFragment).commit();
    }

    private void setupListener() {
        findViewById(R.id.menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        findViewById(R.id.duel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, roomsFragment).commit();
                title.setText(R.string.duel);
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        findViewById(R.id.challenge).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, challengeFragment).commit();
                title.setText(R.string.challenge);
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        findViewById(R.id.leaderboard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, leaderboardFragment).commit();
                title.setText(R.string.leaderboard);
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        findViewById(R.id.statistic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, statisticFragment).commit();
                title.setText(R.string.statistic);
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSharedPreferences("Login", Context.MODE_PRIVATE).edit().putString("Password", "").apply();
                SocketHandler.close();
                finish();
            }
        });

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SocketHandler.close();
                finish();
            }
        });
    }

    private void setupSocket() {
        SocketHandler.once("authenticated", onAuthenticated);
        SocketHandler.on("information", onInformation);
        SocketHandler.on("disconnect", onDisconnect);
    }

    private Emitter.Listener onInformation = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            try {
                JSONObject object = (JSONObject) args[0];
                SocketHandler.setPlayer(new Player(object.getString("username"), object.getString("imageUrl"), object.getString("elo")));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MainOnlineActivity.this.username.setText(SocketHandler.getPlayer().getUsername());
                        MainOnlineActivity.this.elo.setText(SocketHandler.getPlayer().getElo());
                        userImage.setImageResource(getResources().getIdentifier(SocketHandler.getPlayer().getImageUrl(), "drawable", getPackageName()));
                        findViewById(R.id.loadingLayout).setVisibility(View.GONE);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private Emitter.Listener onAuthenticated = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            SocketHandler.emit("information");
        }
    };

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if (active) {
                new ErrorDialog(getString(R.string.lost_connection_to_server), new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        finish();
                    }
                }).show(getSupportFragmentManager(), null);
            } else {
                finish();
            }
        }
    };
}
