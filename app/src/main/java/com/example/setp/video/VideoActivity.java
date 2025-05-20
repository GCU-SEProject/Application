package com.example.setp.video;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.setp.R;
import com.example.setp.network.ApiService;
import com.example.setp.network.RetrofitClient;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoActivity extends AppCompatActivity {

    private static final String TAG = "VideoActivity";

    private EditText etSearchQuery;
    private ImageButton searchVideoBtn;
    private RecyclerView rvSearchResults;
    private ProgressBar progressBarSearch;

    private VideoAdapter videoAdapter;
    private ArrayList<Video> videoResultsList;

    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_video);
        setTitle(getString(R.string.title_video)); // Set the activity title

        apiService = RetrofitClient.getApiService(); // Initialize API service

        // Initialize UI components
        etSearchQuery = findViewById(R.id.etSearchQuery);
        searchVideoBtn = findViewById(R.id.searchVideoBtn);
        rvSearchResults = findViewById(R.id.rvSearchResults);
        progressBarSearch = findViewById(R.id.progressBarSearch);

        // Set up RecyclerView with adapter
        videoResultsList = new ArrayList<>();
        videoAdapter = new VideoAdapter(this, videoResultsList);
        rvSearchResults.setLayoutManager(new LinearLayoutManager(this));
        rvSearchResults.setAdapter(videoAdapter);

        // Search button click listener
        searchVideoBtn.setOnClickListener(v -> performVideoSearch());

        // Handle keyboard search action
        etSearchQuery.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performVideoSearch();
                return true;
            }
            return false;
        });
    }

    // Perform video search with given keyword
    private void performVideoSearch() {
        String keyword = etSearchQuery.getText().toString().trim();
        if (keyword.isEmpty()) {
            Toast.makeText(this, "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show loading indicator
        progressBarSearch.setVisibility(View.VISIBLE);
        rvSearchResults.setVisibility(View.GONE);

        Log.d(TAG, "Searching videos with keyword: " + keyword);

        // Make API request
        apiService.searchVideosByKeyword(keyword)
                .enqueue(new Callback<List<Video>>() {
                    @Override
                    public void onResponse(Call<List<Video>> call, Response<List<Video>> response) {
                        // Hide loading
                        progressBarSearch.setVisibility(View.GONE);
                        rvSearchResults.setVisibility(View.VISIBLE);

                        if (response.isSuccessful() && response.body() != null) {
                            List<Video> videos = response.body();
                            if (videos.isEmpty()) {
                                Toast.makeText(VideoActivity.this, "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show();
                            }
                            videoAdapter.updateData(videos); // Update RecyclerView
                            Log.d(TAG, "Search successful. Videos found: " + videos.size());
                        } else {
                            // API error
                            Toast.makeText(VideoActivity.this, "검색에 실패했습니다 (코드: " + response.code() + ")", Toast.LENGTH_LONG).show();
                            Log.e(TAG, "Search failed. Code: " + response.code() + ", Message: " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Video>> call, Throwable t) {
                        // Network error
                        progressBarSearch.setVisibility(View.GONE);
                        rvSearchResults.setVisibility(View.VISIBLE);
                        Toast.makeText(VideoActivity.this, "네트워크 오류: " + t.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e(TAG, "Network request failed", t);
                    }
                });
    }
}