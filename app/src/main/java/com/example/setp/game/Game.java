package com.example.setp.game;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data // @Getter, @Setter, @RequiredArgsConstructor, @ToString, @EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Game {
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("company")
    private String company;

    @SerializedName("link")
    private String link;

    @SerializedName("age_rating")
    private int ageRating;

    @SerializedName("price")
    private int price;

    @SerializedName("release_date")
    private String releaseDate;

    @SerializedName("description")
    private String description;

    private boolean expanded = false;
}