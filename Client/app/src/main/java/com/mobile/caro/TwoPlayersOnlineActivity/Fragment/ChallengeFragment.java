package com.mobile.caro.TwoPlayersOnlineActivity.Fragment;

import android.os.Bundle;
import android.os.CountDownTimer;
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

import com.mobile.caro.R;
import com.mobile.caro.TwoPlayersOnlineActivity.Entity.Player;
import com.mobile.caro.TwoPlayersOnlineActivity.Network.SocketHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.socket.emitter.Emitter;

public class ChallengeFragment extends Fragment {

    private List<Player> players = new ArrayList<>();

    private Adapter adapter;
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_challenge, null);
            adapter = new Adapter(this, players);
            ((ListView) view.findViewById(R.id.listView)).setAdapter(adapter);
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        SocketHandler.on("player", onPlayer);
        SocketHandler.on("players", onPlayers);
        SocketHandler.emit("players");
    }

    @Override
    public void onStop() {
        super.onStop();
        SocketHandler.off("player", onPlayer);
        SocketHandler.off("players", onPlayers);
    }

    private Emitter.Listener onPlayer = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            try {
                JSONObject object = (JSONObject) args[0];
                String username = object.getString("username");
                if (object.getBoolean("busy")) {
                    for (int i = 0; i < players.size(); i++) {
                        if (players.get(i).getUsername().equals(username)) {
                            players.remove(i);
                            break;
                        }
                    }
                } else {
                    boolean contain = false;
                    for (int i = 0; i < players.size(); i++) {
                        if (players.get(i).getUsername().equals(username)) {
                            contain = true;
                            break;
                        }
                    }
                    if (!contain) {
                        players.add(new Player(object.getString("username"), object.getString("imageUrl"), object.getString("elo")));
                    }
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private Emitter.Listener onPlayers = new Emitter.Listener() {
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
            SocketHandler.emit("players");
        }
    };

    private static class Adapter extends BaseAdapter {

        private List<Player> players;
        private Fragment fragment;
        private View.OnClickListener onClickListener;

        Adapter(Fragment fragment, final List<Player> players) {
            this.fragment = fragment;
            this.players = players;
            onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    try {
                        View item = (View) v.getParent().getParent().getParent();
                        ListView listView = (ListView) item.getParent();
                        int index = listView.getPositionForView(item);
                        JSONObject object = new JSONObject();
                        object.put("username", players.get(index).getUsername());
                        SocketHandler.emit("invite", object);
                        v.setEnabled(false);
                        new CountDownTimer(5000, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {

                            }

                            @Override
                            public void onFinish() {
                                v.setEnabled(true);
                            }
                        }.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
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
                view = fragment.getLayoutInflater().inflate(R.layout.item_challenge, null);
                holder = new Holder();
                holder.userImage = view.findViewById(R.id.userImage);
                holder.username = view.findViewById(R.id.username);
                holder.elo = view.findViewById(R.id.elo);
                view.findViewById(R.id.challenge).setOnClickListener(onClickListener);
                view.setTag(holder);
            } else {
                holder = (Holder) view.getTag();
            }
            Player player = players.get(position);
            holder.userImage.setImageResource(fragment.getResources().getIdentifier(player.getImageUrl(), "drawable", fragment.getActivity().getPackageName()));
            holder.username.setText(player.getUsername());
            holder.elo.setText(player.getElo());
            return view;
        }

        private static class Holder {
            ImageView userImage;
            TextView username;
            TextView elo;
        }
    }
}
