package com.example.setp.network;

import com.example.setp.apifeedback.ApiFeedbackActivity;
import com.example.setp.encyclopedia.Encyclopedia; //Encyclopedia import
import com.example.setp.game.Game; // Game import
import com.example.setp.news.NewsResponse; // News import
import com.example.setp.video.Video; // Video import

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {


    @GET("/game-service/search/game")// Game Query
    Call<List<Game>> searchGames(@Query("tag") List<String> tags);

    @GET("/video-service/search/video") // Video Query
    Call<List<Video>> searchVideos(@Query("keyword") String keyword);

    @GET("/encyclopedia-service/search/encyclopedia") // Encyclopedia Query
    Call<List<Encyclopedia>> searchEncycloPedia(@Query("keyword") String keyword);

    @GET("/news-service/api/v1/news/search") // News Query
    Call<NewsResponse> searchNews(
            @Query("keyword") String keyword,
            @Query("tags") List<String> tags,
            @Query("start") Integer start,
            @Query("display") Integer display);

    @GET("/monitor-service/usage")
    Call<List<ApiFeedbackActivity>> searchUsage(@Query("") List<String> usage);

}