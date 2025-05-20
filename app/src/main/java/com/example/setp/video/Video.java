package com.example.setp.video;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data // @Getter, @Setter, @RequiredArgsConstructor, @ToString, @EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Video {
    @SerializedName("videoId")
    private String videoId;

    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String description;

    @SerializedName("thumbnailUrl")
    private String thumbnailUrl;

    @SerializedName("videoUrl")
    private String videoUrl;

    @SerializedName("viewCount")
    private int viewCount;

    @SerializedName("uploadTime")
    private String uploadTime;

    @SerializedName("tags")
    private List<String> tags;

    private boolean expanded = false;
}
