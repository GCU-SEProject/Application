package com.example.setp.news;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.setp.R;
import com.example.setp.network.ApiService;
import com.example.setp.network.RetrofitClient;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsActivity extends AppCompatActivity {

    private static final String TAG = "NewsActivity";

    // UI elements
    private EditText etSearchKeywordNews;
    private Button btnSelectNewsTags;
    private ImageButton searchNewsBtn;
    private ChipGroup chipGroupSelectedNewsTags;
    private RecyclerView rvNewsSearchResults;
    private ProgressBar progressBarSearchNews;

    // Adapter and data
    private NewsAdapter newsAdapter;
    private ArrayList<News> newsResultsList;
    private ArrayList<String> selectedNewsTagsList;

    private String[] availableNewsTags; // Available tag options

    private ApiService apiService;
    private final int DISPLAY_COUNT = 10; // Number of results per page

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_news);
        setTitle(getString(R.string.title_news));

        // Get API service
        apiService = RetrofitClient.getApiService();

        // Initialize UI components
        etSearchKeywordNews = findViewById(R.id.etSearchKeywordNews);
        btnSelectNewsTags = findViewById(R.id.btnSelectNewsTags);
        searchNewsBtn = findViewById(R.id.searchNewsBtn);
        chipGroupSelectedNewsTags = findViewById(R.id.chipGroupSelectedNewsTags);
        rvNewsSearchResults = findViewById(R.id.rvNewsSearchResults);
        progressBarSearchNews = findViewById(R.id.progressBarSearchNews);

        // Initialize data
        selectedNewsTagsList = new ArrayList<>();
        availableNewsTags = getResources().getStringArray(R.array.news_tags);

        setupRecyclerView(); // Setup news result list

        // Set button click listeners
        btnSelectNewsTags.setOnClickListener(v -> showNewsTagSelectionDialog());
        searchNewsBtn.setOnClickListener(v -> performNewsSearch());

        // Search when enter key is pressed
        etSearchKeywordNews.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performNewsSearch();
                return true;
            }
            return false;
        });
    }

    // Show dialog for selecting tags
    private void showNewsTagSelectionDialog() {
        boolean[] checkedItems = new boolean[availableNewsTags.length];
        for (int i = 0; i < availableNewsTags.length; i++) {
            checkedItems[i] = selectedNewsTagsList.contains(availableNewsTags[i]);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(NewsActivity.this);
        builder.setTitle("뉴스 태그 선택");
        builder.setMultiChoiceItems(availableNewsTags, checkedItems,
                (dialog, which, isChecked) -> checkedItems[which] = isChecked);

        builder.setPositiveButton("확인", (dialog, which) -> {
            // Save selected tags
            selectedNewsTagsList.clear();
            for (int i = 0; i < checkedItems.length; i++) {
                if (checkedItems[i]) {
                    selectedNewsTagsList.add(availableNewsTags[i]);
                }
            }
            updateSelectedNewsTagsUI();
        });

        builder.setNegativeButton("취소", null);

        builder.setNeutralButton("모두 해제", (dialog, which) -> {
            // Uncheck all
            for (int i = 0; i < checkedItems.length; i++) {
                checkedItems[i] = false;
                ((AlertDialog) dialog).getListView().setItemChecked(i, false);
            }
            selectedNewsTagsList.clear();
            updateSelectedNewsTagsUI();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Update the chip UI for selected tags
    private void updateSelectedNewsTagsUI() {
        chipGroupSelectedNewsTags.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);
        for (String tag : selectedNewsTagsList) {
            Chip chip = (Chip) inflater.inflate(R.layout.chip_item_tag, chipGroupSelectedNewsTags, false);
            chip.setText(tag);
            chip.setCloseIconVisible(true);
            chip.setOnCloseIconClickListener(v -> {
                selectedNewsTagsList.remove(tag);
                updateSelectedNewsTagsUI();
            });
            chipGroupSelectedNewsTags.addView(chip);
        }
    }

    // Setup news RecyclerView
    private void setupRecyclerView() {
        newsResultsList = new ArrayList<>();
        newsAdapter = new NewsAdapter(this, newsResultsList);
        rvNewsSearchResults.setLayoutManager(new LinearLayoutManager(this));
        rvNewsSearchResults.setAdapter(newsAdapter);
    }

    // Search news using keyword and selected tags
    private void performNewsSearch() {
        String keyword = etSearchKeywordNews.getText().toString().trim();

        if (TextUtils.isEmpty(keyword)) {
            Toast.makeText(this, "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedNewsTagsList.isEmpty()) {
            Toast.makeText(this, "검색할 태그를 하나 이상 선택해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBarSearchNews.setVisibility(View.VISIBLE);
        rvNewsSearchResults.setVisibility(View.GONE);

        Log.d(TAG, "Searching news with keyword: " + keyword + ", tags: " + selectedNewsTagsList.toString());

        apiService.searchNews(keyword, selectedNewsTagsList, 1, DISPLAY_COUNT)
                .enqueue(new Callback<NewsResponse>() {
                    @Override
                    public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                        progressBarSearchNews.setVisibility(View.GONE);
                        rvNewsSearchResults.setVisibility(View.VISIBLE);

                        if (response.isSuccessful() && response.body() != null) {
                            NewsResponse newsResp = response.body();
                            if (newsResp.getItems() != null && !newsResp.getItems().isEmpty()) {
                                newsAdapter.updateData(newsResp.getItems());
                                Log.d(TAG, "Search successful. Items found: " + newsResp.getItems().size() + " / Total: " + newsResp.getTotal());
                            } else {
                                Toast.makeText(NewsActivity.this, "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show();
                                newsAdapter.updateData(new ArrayList<>());
                            }
                        } else {
                            Toast.makeText(NewsActivity.this, "검색에 실패했습니다 (코드: " + response.code() + ")", Toast.LENGTH_LONG).show();
                            Log.e(TAG, "Search failed. Code: " + response.code() + ", Message: " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<NewsResponse> call, Throwable t) {
                        progressBarSearchNews.setVisibility(View.GONE);
                        rvNewsSearchResults.setVisibility(View.VISIBLE);
                        Toast.makeText(NewsActivity.this, "네트워크 오류: " + t.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e(TAG, "Network request failed", t);
                    }
                });
    }
}
