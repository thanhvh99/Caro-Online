package com.mobile.caro.TwoPlayersOnlineActivity.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mobile.caro.R;
import com.mobile.caro.TwoPlayersOnlineActivity.Entity.Room;
import com.mobile.caro.TwoPlayersOnlineActivity.Network.SocketHandler;

import java.util.List;

import io.socket.emitter.Emitter;

public class StatisticFragment extends Fragment {

    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_statistic, null);

        mapping();
        initialize();
        setupListener();

        return view;
    }

    private void mapping() {

    }

    private void initialize() {

    }

    private void setupListener() {

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

        }
    };

    private static class Adapter extends BaseAdapter {

        private List<Room> rooms;
        private Fragment fragment;

        public Adapter(Fragment fragment, List<Room> rooms) {
            this.fragment = fragment;
            this.rooms = rooms;
        }

        @Override
        public int getCount() {
            return rooms.size();
        }

        @Override
        public Object getItem(int position) {
            return rooms.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            return null;
        }

    }
}
