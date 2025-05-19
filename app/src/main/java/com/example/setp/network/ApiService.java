package com.example.setp.network;

import com.example.setp.game.Game; // Game import
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("search/game")// API Gateway EndPoint
    Call<List<Game>> searchGamesByTags(@Query("tag") List<String> tags);

    // ToDo: Other API EndPoint Definition
}