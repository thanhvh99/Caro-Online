package com.mobile.caro.TwoPlayersOnlineActivity.Entity;

import java.io.Serializable;

public class Player implements Serializable {

    private String username;
    private String imageUrl;
    private String elo;

    public Player(String username, String imageUrl, String elo) {
        this.username = username;
        this.imageUrl = imageUrl;
        this.elo = elo;
    }

    public String getUsername() {
        return username;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getElo() {
        return elo;
    }

}
