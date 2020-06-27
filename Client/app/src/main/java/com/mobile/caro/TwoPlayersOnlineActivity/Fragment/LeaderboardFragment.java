package com.mobile.caro.TwoPlayersOnlineActivity.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.mobile.caro.R;
import com.mobile.caro.TwoPlayersOnlineActivity.Entity.Player;
import com.mobile.caro.TwoPlayersOnlineActivity.Network.SocketHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.socket.emitter.Emitter;

public class LeaderboardFragment extends Fragment {

    private List<Player> players = new ArrayList<>();

    private Adapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leaderboard, null);
        adapter = new Adapter(this, players);
        ((ListView) view.findViewById(R.id.listView)).setAdapter(adapter);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        SocketHandler.on("leaderboard", onLeaderboard);
        SocketHandler.emit("leaderboard");
    }

    @Override
    public void onStop() {
        super.onStop();
        SocketHandler.off("leaderboard", onLeaderboard);
    }

    private Emitter.Listener onLeaderboard = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            players.clear();
            try {
                JSONArray array = (JSONArray) args[0];
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    players.add(new Player(object.getString("username"), object.getString("imageUrl"), object.getString("elo")));
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                        getView().findViewById(R.id.progressBar).setVisibility(View.GONE);
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
            SocketHandler.emit("leaderboard");
        }
    };

    private static class Adapter extends BaseAdapter {

        private List<Player> players;
        private Fragment fragment;

        Adapter(Fragment fragment, final List<Player> players) {
            this.fragment = fragment;
            this.players = players;
        }

        @Override
        public int getCount() {
            return players.size();
        }

        @Override
        public Object getItem(int position) {
            return players.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            Holder holder;
            if (view == null) {
                view = fragment.getLayoutInflater().inflate(R.layout.item_player, null);
                holder = new Holder();
                holder.userImage = view.findViewById(R.id.userImage);
                holder.username = view.findViewById(R.id.username);
                holder.index = view.findViewById(R.id.index);
                holder.elo = view.findViewById(R.id.elo);
                view.setTag(holder);
            } else {
                holder = (Holder) view.getTag();
            }
            Player player = players.get(position);
            int id = fragment.getResources().getIdentifier(player.getImageUrl(), "drawable", fragment.getActivity().getPackageName());
            Glide.with(fragment).load(id).into(holder.userImage);
            holder.username.setText(player.getUsername());
            holder.index.setText(Integer.toString(position + 1));
            holder.elo.setText(player.getElo());
            return view;
        }

        private static class Holder {
            ImageView userImage;
            TextView username;
            TextView index;
            TextView elo;
        }
    }
}
