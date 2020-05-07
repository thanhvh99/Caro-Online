package com.mobile.caro.TwoPlayersOnlineActivity.Entity;

import java.io.Serializable;

public class Room implements Serializable {

    private Player player;
    private int timelapse;
    private boolean havePassword;
    private boolean rank;

    public Room() {

    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getTimelapse() {
        return timelapse;
    }

    public void setTimelapse(int timelapse) {
        this.timelapse = timelapse;
    }

    public boolean havePassword() {
        return havePassword;
    }

    public void setPassword(boolean havePassword) {
        this.havePassword = havePassword;
    }

    public boolean isRank() {
        return rank;
    }

    public void setRank(boolean rank) {
        this.rank = rank;
    }
}
