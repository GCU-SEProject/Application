package com.example.setp.news;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsResponse {

    @SerializedName("lastBuildDate")
    private String lastBuildDate;

    @SerializedName("total")
    private int total;

    @SerializedName("start")
    private int start;

    @SerializedName("display")
    private int display;

    @SerializedName("items")
    private List<News> items;
}
