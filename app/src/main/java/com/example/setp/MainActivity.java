package com.example.setp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    private MaterialButton btnGame, btnEncyclopedia, btnVideo, btnNews, btnApi, btnOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnGame = findViewById(R.id.btnGame);
        btnEncyclopedia = findViewById(R.id.btnEncyclopedia);
        btnVideo = findViewById(R.id.btnVideo);
        btnNews = findViewById(R.id.btnNews);
        btnApi = findViewById(R.id.btnApi);
        btnOption = findViewById(R.id.btnOption);

        btnGame.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, GameSearchActivity.class)));
        btnEncyclopedia.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, EncyclopediaActivity.class)));
        btnVideo.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, VideoActivity.class)));
        btnNews.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, NewsActivity.class)));
        btnApi.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ApiFeedbackActivity.class)));
        btnOption.setOnClickListener(v -> Toast.makeText(MainActivity.this, getString(R.string.option_toast_message), Toast.LENGTH_SHORT).show());
    }
}