package com.mobile.caro.TwoPlayersOnlineActivity.Network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserAPI {

    @POST("api/login")
    Call<ResultDTO> login(@Body UserDTO userDTO);

    @POST("api/register")
    Call<ResultDTO> register(@Body UserDTO userDTO);

    class ResultDTO {
        @SerializedName("success")
        public boolean success;

        @SerializedName("message")
        public String message;
    }

    class UserDTO {
        @SerializedName("username")
        public String username;

        @SerializedName("password")
        public String password;
    }

}
