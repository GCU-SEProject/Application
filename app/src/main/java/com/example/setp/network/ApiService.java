package com.example.setp.network;

import com.example.setp.game.Game; // Game import
import com.example.setp.video.Video; // Video import

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {


    @GET("/game-service/search/game")// API Gateway EndPoint
    Call<List<Game>> searchGamesByTags(@Query("tag") List<String> tags);

    @GET("/video-service/search/video")
    Call<List<Video>> searchVideosByKeyword(@Query("keyword") List<String> keyword);

}