package com.mobile.caro.TwoPlayersOnlineActivity.Entity;

import com.mobile.caro.TwoPlayersOnlineActivity.Network.SocketHandler;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Match implements Serializable {

    private String opponent;
    private String elo;
    private String moves;
    private boolean win;
    private boolean first;

    public String getOpponent() {
        return opponent;
    }

    public String getElo() {
        return elo;
    }

    public ArrayList<Integer> getMoves() {
        ArrayList<Integer> result = new ArrayList<>();
        String[] strings = moves.split(";");
        for (String string : strings) {
            result.add(Integer.parseInt(string));
        }
        return result;
    }

    public boolean isFirst() {
        return first;
    }

    public boolean isWin() {
        return win;
    }

    public static Match parse(JSONObject object) {
        Match match = new Match();
        try {
            match.win = object.getString("winner").equals(SocketHandler.getPlayer().getUsername());
            match.opponent = object.getString(match.win ? "loser" : "winner");
            match.elo = match.win ? "+" + object.getString("winPoint") : "-" + object.getString("losePoint");
            match.first = object.getString("first").equals(SocketHandler.getPlayer().getUsername());
            match.moves = object.getString("history");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return match;
    }
}
