package com.example.setp;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.setp.network.ApiService;
import com.example.setp.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GameSearchActivity extends AppCompatActivity {

    private static final String TAG = "GameSearchActivity";

    private EditText etGameSearchQuery;
    private ImageView imgGameSearchIcon;
    private Spinner spinnerPlatform, spinnerGenre, spinnerPlaytime;
    private RecyclerView rvGameSearchResults;
    private ProgressBar progressBarSearch;

    private GameResultAdapter gameAdapter;
    private ArrayList<Game> gameResultsList;

    private String selectedPlatform = "";
    private String selectedGenre = "";
    private String selectedPlaytimeSortOrder = "";

    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_search);
        setTitle(getString(R.string.title_game_search));

        apiService = RetrofitClient.getApiService();

        etGameSearchQuery = findViewById(R.id.etGameSearchQuery);
        imgGameSearchIcon = findViewById(R.id.imgGameSearchIcon);
        spinnerPlatform = findViewById(R.id.spinnerPlatform);
        spinnerGenre = findViewById(R.id.spinnerGenre);
        spinnerPlaytime = findViewById(R.id.spinnerPlaytime);
        rvGameSearchResults = findViewById(R.id.rvGameSearchResults);
        progressBarSearch = findViewById(R.id.progressBarSearch);

        setupSpinners();
        setupRecyclerView();
        setupSearchAction();
    }

    private void setupSpinners() {
        spinnerPlatform.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    selectedPlatform = "";
                } else {
                    selectedPlatform = parent.getItemAtPosition(position).toString().toLowerCase();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedPlatform = "";
            }
        });

        spinnerGenre.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    selectedGenre = "";
                } else {
                    selectedGenre = parent.getItemAtPosition(position).toString().toLowerCase();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedGenre = "";
            }
        });

        spinnerPlaytime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    selectedPlaytimeSortOrder = "";
                } else if (position == 1) {
                    selectedPlaytimeSortOrder = "asc";
                } else if (position == 2) {
                    selectedPlaytimeSortOrder = "desc";
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedPlaytimeSortOrder = "";
            }
        });
    }

    private void setupRecyclerView() {
        gameResultsList = new ArrayList<>();
        gameAdapter = new GameResultAdapter(this, gameResultsList);
        rvGameSearchResults.setLayoutManager(new LinearLayoutManager(this));
        rvGameSearchResults.setAdapter(gameAdapter);
    }

    private void setupSearchAction() {
        etGameSearchQuery.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch();
                return true;
            }
            return false;
        });

        imgGameSearchIcon.setOnClickListener(v -> performSearch());
    }

    private void performSearch() {
        String searchQuery = etGameSearchQuery.getText().toString().trim();

        if (TextUtils.isEmpty(searchQuery) && TextUtils.isEmpty(selectedPlatform) && TextUtils.isEmpty(selectedGenre) && TextUtils.isEmpty(selectedPlaytimeSortOrder)) {
            Toast.makeText(this, "검색어나 필터를 하나 이상 입력/선택해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 로딩 UI 표시
        progressBarSearch.setVisibility(View.VISIBLE);
        rvGameSearchResults.setVisibility(View.GONE);
        gameResultsList.clear();
        gameAdapter.notifyDataSetChanged();


        Log.d(TAG, "Searching games - Query: '" + searchQuery +
                "', Platform: '" + selectedPlatform +
                "', Genre: '" + selectedGenre +
                "', Sort: '" + selectedPlaytimeSortOrder + "'");

        apiService.searchGames(searchQuery, selectedPlatform, selectedGenre, selectedPlaytimeSortOrder)
                .enqueue(new Callback<List<Game>>() {
                    @Override
                    public void onResponse(Call<List<Game>> call, Response<List<Game>> response) {
                        progressBarSearch.setVisibility(View.GONE);
                        rvGameSearchResults.setVisibility(View.VISIBLE);

                        if (response.isSuccessful() && response.body() != null) {
                            List<Game> games = response.body();
                            if (games.isEmpty()) {
                                Toast.makeText(GameSearchActivity.this, "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show();
                            }
                            gameAdapter.updateData(games);
                            Log.d(TAG, "Search successful. Games found: " + games.size());
                        } else {
                            Toast.makeText(GameSearchActivity.this, "검색에 실패했습니다 (코드: " + response.code() + ")", Toast.LENGTH_LONG).show();
                            Log.e(TAG, "Search failed. Code: " + response.code() + ", Message: " + response.message());
                            try {
                                if (response.errorBody() != null) {
                                    Log.e(TAG, "Error body: " + response.errorBody().string());
                                }
                            } catch (Exception e) {
                                Log.e(TAG, "Error parsing error body", e);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Game>> call, Throwable t) {
                        progressBarSearch.setVisibility(View.GONE);
                        rvGameSearchResults.setVisibility(View.VISIBLE);

                        Toast.makeText(GameSearchActivity.this, "네트워크 오류: " + t.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e(TAG, "Network request failed", t);
                    }
                });
    }
}