package com.example.setp.network;

import com.example.setp.apifeedback.ApiFeedbackActivity;
import com.example.setp.encyclopedia.Encyclopedia;
import com.example.setp.game.Game; // Game import
import com.example.setp.video.News;
import com.example.setp.video.Video; // Video import

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {


    @GET("/game-service/search/game")// Game Query
    Call<List<Game>> searchGamesByTags(@Query("tag") List<String> tags);

    @GET("/video-service/search/video") // Video Query
    Call<List<Video>> searchVideosByKeyword(@Query("keyword") String keyword);

    @GET("/encyclopedia-service/search/encyclopedia")
    Call<List<Encyclopedia>> searchEncycloPediaByKeyword(@Query("keyword") String keyword);

    @GET("/news-service/search/news")
    Call<List<News>> searchNewsByKeyword(@Query("keyword") String keyword);

    @GET("/monitor-service/usage")
    Call<List<ApiFeedbackActivity>> searchUsage(@Query("") List<String> usage);

}