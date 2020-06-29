package com.mobile.caro.TwoPlayersOnlineActivity.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mobile.caro.R;
import com.mobile.caro.TwoPlayersOnlineActivity.Activity.ReplayActivity;
import com.mobile.caro.TwoPlayersOnlineActivity.Entity.Match;
import com.mobile.caro.TwoPlayersOnlineActivity.Network.SocketHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.socket.emitter.Emitter;

public class StatisticFragment extends Fragment {

    private List<Match> matches = new ArrayList<>();

    private View view;
    private TextView win;
    private TextView lose;
    private TextView rank;
    private TextView rate;
    private ListView listView;

    private Adapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_statistic, null);

            mapping();
            initialize();
            setupListener();
        }

        return view;
    }

    private void mapping() {
        win = view.findViewById(R.id.win);
        lose = view.findViewById(R.id.lose);
        rank = view.findViewById(R.id.rank);
        rate = view.findViewById(R.id.rate);
        listView = view.findViewById(R.id.listView);
    }

    private void initialize() {
        adapter = new Adapter(this, matches);
        listView.setAdapter(adapter);
        ((TextView) view.findViewById(R.id.username)).setText(SocketHandler.getPlayer().getUsername());
        ((TextView) view.findViewById(R.id.elo)).setText(SocketHandler.getPlayer().getElo());
    }

    private void setupListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ReplayActivity.class);
                intent.putExtra("match", matches.get(position));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        SocketHandler.on("statistic", onStatistic);
        SocketHandler.emit("statistic");
    }

    @Override
    public void onStop() {
        super.onStop();
        SocketHandler.off("statistic", onStatistic);
    }

    private Emitter.Listener onStatistic = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            try {
                final JSONObject object = (JSONObject) args[0];
                JSONArray array = object.getJSONArray("history");
                matches.clear();
                for (int i = 0; i < array.length(); i++) {
                    matches.add(Match.parse(array.getJSONObject(i)));
                }
                float total = object.getInt("win") + object.getInt("lose");
                final String rateString = total == 0 ? "0%" : ((Math.round(object.getInt("win") / total * 10000) / 100f) + "%");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            adapter.notifyDataSetChanged();
                            win.setText(object.getString("win"));
                            lose.setText(object.getString("lose"));
                            rank.setText(object.getString("rank"));
                            rate.setText(rateString);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private static class Adapter extends BaseAdapter {

        private List<Match> matches;
        private Fragment fragment;

        public Adapter(Fragment fragment, List<Match> matches) {
            this.fragment = fragment;
            this.matches = matches;
        }

        @Override
        public int getCount() {
            return matches.size();
        }

        @Override
        public Object getItem(int position) {
            return matches.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            Holder holder;
            if (view == null) {
                view = fragment.getLayoutInflater().inflate(R.layout.item_match, null);
                holder = new Holder();
                holder.username = view.findViewById(R.id.username);
                holder.elo = view.findViewById(R.id.elo);
                view.setTag(holder);
            } else {
                holder = (Holder) view.getTag();
            }
            Match match = matches.get(position);
            holder.username.setText(match.getOpponent());
            holder.elo.setText(match.getElo());
            holder.elo.setTextColor(fragment.getResources().getColor(match.isWin() ? R.color.color_emerald : R.color.color_alizarin));
            return view;
        }

        private static class Holder {
            TextView username;
            TextView elo;
        }

    }
}
