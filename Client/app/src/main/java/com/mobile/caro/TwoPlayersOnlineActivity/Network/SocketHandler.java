package com.mobile.caro.TwoPlayersOnlineActivity.Network;

import com.mobile.caro.TwoPlayersOnlineActivity.Entity.Player;

import org.json.JSONObject;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public final class SocketHandler {

    private static Socket socket;

    private static Player player;

    static {
        try {
            IO.Options options = new IO.Options();
            options.reconnection = false;
            socket = IO.socket(Network.SERVER_URL, options);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void connect(final String token) {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", token);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        socket.once("disconnect", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                player = null;
            }
        });
        socket.once("connect", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                socket.emit("authenticate", jsonObject);
            }
        });

        socket.connect();
    }

    public static void emit(String event, Object... object) {
        socket.emit(event, object);
    }

    public static void once(String event, Emitter.Listener listener) {
        socket.once(event, listener);
    }

    public static void on(String event, Emitter.Listener listener) {
        socket.on(event, listener);
    }

    public static void off(String event, Emitter.Listener listener) {
        socket.off(event, listener);
    }

    public static void close() {
        socket.off();
        socket.close();
    }

    public static void setPlayer(Player player) {
        SocketHandler.player = player;
    }

    public static Player getPlayer() {
        return player;
    }

}
