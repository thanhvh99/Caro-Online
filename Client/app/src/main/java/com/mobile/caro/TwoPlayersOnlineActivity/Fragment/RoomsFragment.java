package com.mobile.caro.TwoPlayersOnlineActivity.Fragment;

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

import com.bumptech.glide.Glide;
import com.mobile.caro.R;
import com.mobile.caro.TwoPlayersOnlineActivity.Dialog.JoinDialog;
import com.mobile.caro.TwoPlayersOnlineActivity.Dialog.CreateDialog;
import com.mobile.caro.TwoPlayersOnlineActivity.Entity.Player;
import com.mobile.caro.TwoPlayersOnlineActivity.Entity.Room;
import com.mobile.caro.TwoPlayersOnlineActivity.Network.SocketHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.socket.emitter.Emitter;


public class RoomsFragment extends Fragment {

    private List<Room> rooms;

    private View view;
    private ListView listView;
    private Adapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_rooms, null);

            mapping();
            initialize();
            setupListener();
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        SocketHandler.on("create", onCreate);
        SocketHandler.on("rooms", onRooms);
        SocketHandler.on("room", onRoom);
        SocketHandler.on("delete", onDelete);
        SocketHandler.emit("rooms");
    }

    @Override
    public void onStop() {
        super.onStop();
        SocketHandler.off("create", onCreate);
        SocketHandler.off("rooms", onRooms);
        SocketHandler.off("room", onRoom);
        SocketHandler.off("delete", onDelete);
    }

    private void initialize() {
        rooms = new ArrayList<>();
        adapter = new Adapter(this, rooms);
        listView.setAdapter(adapter);
    }

    private void setupListener() {
        view.findViewById(R.id.create).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CreateDialog().show(getParentFragmentManager(), null);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Room room = rooms.get(position);
                new JoinDialog(room).show(getParentFragmentManager(), null);
            }
        });
    }

    private void mapping() {
        listView = view.findViewById(R.id.listView);
    }

    private Emitter.Listener onCreate = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            try {
                JSONObject object = (JSONObject) args[0];
                String username = object.getString("username");
                Room room = null;
                for (int i = 0; i < rooms.size(); i++) {
                    if (rooms.get(i).getPlayer().getUsername().equals(username)) {
                        room = rooms.get(i);
                        break;
                    }
                }
                if (room == null) {
                    room = new Room();
                    rooms.add(room);
                }
                room.setPlayer(new Player(object.getString("username"), object.getString("imageUrl"), object.getString("elo")));
                room.setTimelapse(object.getInt("timelapse"));
                room.setPassword(object.getBoolean("havePassword"));
                room.setRank(object.getBoolean("rank"));
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

    private Emitter.Listener onRooms = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            rooms.clear();
            try {
                JSONArray array = (JSONArray) args[0];
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    Room room = new Room();
                    room.setPlayer(new Player(object.getString("username"), object.getString("imageUrl"), object.getString("elo")));
                    room.setTimelapse(object.getInt("timelapse"));
                    room.setPassword(object.getBoolean("havePassword"));
                    room.setRank(object.getBoolean("rank"));
                    rooms.add(room);
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

    private Emitter.Listener onDelete = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            try {
                JSONObject object = (JSONObject) args[0];
                String username = object.getString("username");
                for (int i = 0; i < rooms.size(); i++) {
                    if (rooms.get(i).getPlayer().getUsername().equals(username)) {
                        rooms.remove(i);
                        break;
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

    private Emitter.Listener onRoom = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            try {
                JSONObject object = (JSONObject) args[0];
                String username = object.getString("username");
                Room room = null;
                for (int i = 0; i < rooms.size(); i++) {
                    if (rooms.get(i).getPlayer().getUsername().equals(username)) {
                        room = rooms.get(i);
                        break;
                    }
                }
                if (room == null) {
                    return;
                }

                room.setTimelapse(object.getInt("timelapse"));
                room.setPassword(object.getBoolean("havePassword"));
                room.setRank(object.getBoolean("rank"));
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
            SocketHandler.emit("rooms");
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
            Room room = rooms.get(position);
            Holder holder;
            if (view == null) {
                holder = new Holder();
                view = fragment.getLayoutInflater().inflate(R.layout.item_room, null);
                holder.userImage = view.findViewById(R.id.userImage);
                holder.username = view.findViewById(R.id.username);
                holder.lock = view.findViewById(R.id.lockImage);
                holder.elo = view.findViewById(R.id.elo);
                holder.timelapse = view.findViewById(R.id.timelapse);
                view.setTag(holder);
            } else {
                holder = (Holder) view.getTag();
            }

            int resId = fragment.getResources().getIdentifier(room.getPlayer().getImageUrl(), "drawable", fragment.getActivity().getPackageName());
            Glide.with(fragment)
                    .load(resId)
                    .optionalCircleCrop()
                    .into(holder.userImage);
            holder.username.setText(room.getPlayer().getUsername());
            holder.lock.setVisibility(room.havePassword() ? View.VISIBLE : View.GONE);
            holder.elo.setVisibility(room.isRank() ? View.VISIBLE : View.GONE);
            holder.elo.setText(room.getPlayer().getElo());
            holder.timelapse.setText(room.getTimelapse() + "");
            return view;
        }

        private static class Holder {
            ImageView userImage;
            TextView username;
            ImageView lock;
            TextView elo;
            TextView timelapse;
        }
    }


}
