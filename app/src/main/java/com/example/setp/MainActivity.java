package com.example.setp;

import android.content.Intent;
import android.os.Bundle;
// import android.widget.Toast; // Toast는 현재 사용 안 함

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.setp.encyclopedia.EncyclopediaActivity;
import com.example.setp.game.GameActivity;
import com.example.setp.news.NewsActivity;
import com.example.setp.video.VideoActivity;
// import com.google.android.material.button.MaterialButton; // MaterialButton 대신 CardView 사용
import com.google.android.material.card.MaterialCardView; // MaterialCardView 임포트

public class MainActivity extends AppCompatActivity {

    // 변수 타입을 MaterialCardView로 변경
    private MaterialCardView btnGameCard, btnEncyclopediaCard, btnVideoCard, btnNewsCard;

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

        // ID를 새로운 카드 ID로 변경
        btnGameCard = findViewById(R.id.btnGameCard);
        btnEncyclopediaCard = findViewById(R.id.btnEncyclopediaCard);
        btnVideoCard = findViewById(R.id.btnVideoCard);
        btnNewsCard = findViewById(R.id.btnNewsCard);

        // 클릭 리스너 설정
        btnGameCard.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, GameActivity.class)));
        btnEncyclopediaCard.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, EncyclopediaActivity.class)));
        btnVideoCard.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, VideoActivity.class)));
        btnNewsCard.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, NewsActivity.class)));
    }
}