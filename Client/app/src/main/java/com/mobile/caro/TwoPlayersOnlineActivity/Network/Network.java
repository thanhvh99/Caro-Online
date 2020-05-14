package com.mobile.caro.TwoPlayersOnlineActivity.Network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class Network {

    public static final String SERVER_URL = "http://192.168.100.15:2345";

    private static Retrofit retrofit;

    private Network() {

    }

    public static <T> T getRetrofit(Class<T> c) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(SERVER_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(c);
    }
}
