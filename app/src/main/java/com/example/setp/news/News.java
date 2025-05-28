package com.example.setp.news;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class News {

    @SerializedName("title")
    private String title;

    @SerializedName("originallink")
    private String originalLink;

    @SerializedName("link")
    private String link;

    @SerializedName("description")
    private String description;

    @SerializedName("pubDate")
    private String pubDate;

    @SerializedName("tags")
    private List<String> tags;

    private boolean expanded = false;
}