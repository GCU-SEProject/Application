package com.example.setp.network;

import com.example.setp.game.Game; // Game import
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

<<<<<<< Updated upstream
    // Base URL: http://221.163.123.181:8080/

    @GET("search/game") // API Gateway EndPoint
    Call<List<Game>> searchGames(
            @Query("query") String searchQuery,
            @Query("platform") String platform,
            @Query("genre") String genre,
            @Query("time") String playtimeSortOrder
    );
=======
    @GET("/game-service/search/game")// API Gateway EndPoint
    Call<List<Game>> searchGamesByTags(@Query("tag") List<String> tags);
>>>>>>> Stashed changes

    // ToDo: Other API EndPoint Definition
}