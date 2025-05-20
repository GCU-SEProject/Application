package com.example.setp.video;

import com.google.gson.annotations.SerializedName;
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

    @SerializedName("videoCount")
    private int videoCount;

    @SerializedName("uploadTime")
    private String uploadTime;



    private boolean expanded = false;
}
