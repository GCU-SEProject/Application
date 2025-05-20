package com.example.setp.encyclopedia;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // @Getter, @Setter, @RequiredArgsConstructor, @ToString, @EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Encyclopedia {
    @SerializedName("lastBuildDate")
    private String lastBuildDate;

    @SerializedName("total")
    private int total;

    @SerializedName("start")
    private int start;

    @SerializedName("display")
    private String display;

    @SerializedName("link")
    private String link;

    @SerializedName("ageRating")
    private int ageRating;

    @SerializedName("price")
    private int price;

    @SerializedName("releaseDate")
    private String releaseDate;

    @SerializedName("description")
    private String description;

    private boolean expanded = false;
}
