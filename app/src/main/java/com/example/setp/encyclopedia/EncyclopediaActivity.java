package com.example.setp.encyclopedia;

import android.os.Bundle;
import android.text.TextUtils;
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

public class EncyclopediaActivity extends AppCompatActivity {

    private static final String TAG = "EncyclopediaActivity";

    // UI components
    private EditText etSearchQuery;
    private ImageButton searchEncycBtn;
    private RecyclerView rvSearchResults;
    private ProgressBar progressBarSearch;

    // Adapter and data list
    private EncyclopediaAdapter encyclopediaAdapter;
    private ArrayList<Encyclopedia> encyclopediaResultsList;

    // API service
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_encyclopedia);
        setTitle(getString(R.string.title_encyclopedia));

        // Initialize API service
        apiService = RetrofitClient.getApiService();

        // Bind UI elements
        etSearchQuery = findViewById(R.id.etSearchQuery);
        searchEncycBtn = findViewById(R.id.searchEncycBtn);
        rvSearchResults = findViewById(R.id.rvSearchResults);
        progressBarSearch = findViewById(R.id.progressBarSearch);

        // Set up RecyclerView
        encyclopediaResultsList = new ArrayList<>();
        encyclopediaAdapter = new EncyclopediaAdapter(this, encyclopediaResultsList);
        rvSearchResults.setLayoutManager(new LinearLayoutManager(this));
        rvSearchResults.setAdapter(encyclopediaAdapter);

        // Set click listener for search button
        searchEncycBtn.setOnClickListener(v -> performEncyclopediaSearch());

        // Search when user presses enter key
        etSearchQuery.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performEncyclopediaSearch();
                return true;
            }
            return false;
        });
    }

    // Perform search using entered keyword
    private void performEncyclopediaSearch() {
        String keyword = etSearchQuery.getText().toString().trim();

        // Show warning if keyword is empty
        if (TextUtils.isEmpty(keyword)) {
            Toast.makeText(this, "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show loading indicator
        progressBarSearch.setVisibility(View.VISIBLE);
        rvSearchResults.setVisibility(View.GONE);

        Log.d(TAG, "Searching encyclopedia with keyword: " + keyword);

        // Call API to search encyclopedia
        apiService.searchEncyclopedia(keyword)
                .enqueue(new Callback<List<Encyclopedia>>() {
                    @Override
                    public void onResponse(Call<List<Encyclopedia>> call, Response<List<Encyclopedia>> response) {
                        // Hide loading indicator
                        progressBarSearch.setVisibility(View.GONE);
                        rvSearchResults.setVisibility(View.VISIBLE);

                        if (response.isSuccessful() && response.body() != null) {
                            List<Encyclopedia> encyclopedias = response.body();

                            // Show message if result is empty
                            if (encyclopedias.isEmpty()) {
                                Toast.makeText(EncyclopediaActivity.this, "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show();
                            }

                            // Update RecyclerView with new data
                            encyclopediaAdapter.updateData(encyclopedias);
                            Log.d(TAG, "Search successful. Items found: " + encyclopedias.size());
                        } else {
                            // Show error message
                            Toast.makeText(EncyclopediaActivity.this, "검색에 실패했습니다 (코드: " + response.code() + ")", Toast.LENGTH_LONG).show();
                            Log.e(TAG, "Search failed. Code: " + response.code() + ", Message: " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Encyclopedia>> call, Throwable t) {
                        // Handle network error
                        progressBarSearch.setVisibility(View.GONE);
                        rvSearchResults.setVisibility(View.VISIBLE);
                        Toast.makeText(EncyclopediaActivity.this, "네트워크 오류: " + t.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e(TAG, "Network request failed", t);
                    }
                });
    }
}