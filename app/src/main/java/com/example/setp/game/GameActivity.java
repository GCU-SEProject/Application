package com.example.setp.game;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
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
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GameActivity extends AppCompatActivity {

    private static final String TAG = "GameActivity";

    private Button addTagBtnGenre, addTagBtnPricing, addTagBtnGameMode;
    private Button addTagBtnLanguage, addTagBtnPlatform, addTagBtnStyle;
    private ImageButton searchTagBtn;
    private ChipGroup chipGroupSelectedTags;
    private RecyclerView rvGameSearchResults;
    private ProgressBar progressBarSearch;

    private GameAdapter gameAdapter;
    private ArrayList<Game> gameResultsList;
    private ArrayList<String> selectedTagsList;

    private LinkedHashMap<String, String[]> allTagsMap;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_game);
        setTitle(getString(R.string.title_game_search));

        apiService = RetrofitClient.getApiService();

        addTagBtnGenre = findViewById(R.id.addTagBtnGenre);
        addTagBtnPricing = findViewById(R.id.addTagBtnPricing);
        addTagBtnGameMode = findViewById(R.id.addTagBtnGameMode);
        addTagBtnLanguage = findViewById(R.id.addTagBtnLanguage);
        addTagBtnPlatform = findViewById(R.id.addTagBtnPlatform);
        addTagBtnStyle = findViewById(R.id.addTagBtnStyle);
        searchTagBtn = findViewById(R.id.searchTagBtn);
        chipGroupSelectedTags = findViewById(R.id.chipGroupSelectedTags);
        rvGameSearchResults = findViewById(R.id.rvGameSearchResults);
        progressBarSearch = findViewById(R.id.progressBarSearch);

        selectedTagsList = new ArrayList<>();
        initializeTags();
        setupRecyclerView();
        setupTagButtonListeners();

        searchTagBtn.setOnClickListener(v -> performTagSearch());
    }

    private void initializeTags() {
        allTagsMap = new LinkedHashMap<>();
        allTagsMap.put("장르", getResources().getStringArray(R.array.game_tags_genre));
        allTagsMap.put("가격/BM", getResources().getStringArray(R.array.game_tags_pricing));
        allTagsMap.put("플레이방식", getResources().getStringArray(R.array.game_tags_playstyle));
        allTagsMap.put("지원 언어", getResources().getStringArray(R.array.game_tags_language));
        allTagsMap.put("플랫폼", getResources().getStringArray(R.array.game_tags_platform));
        allTagsMap.put("스타일", getResources().getStringArray(R.array.game_tags_style));
    }

    private void setupTagButtonListeners() {
        addTagBtnGenre.setOnClickListener(v -> showTagSelectionDialog("장르", allTagsMap.get("장르")));
        addTagBtnPricing.setOnClickListener(v -> showTagSelectionDialog("가격/BM", allTagsMap.get("가격/BM")));
        addTagBtnGameMode.setOnClickListener(v -> showTagSelectionDialog("플레이방식", allTagsMap.get("플레이방식")));
        addTagBtnLanguage.setOnClickListener(v -> showTagSelectionDialog("지원 언어", allTagsMap.get("지원 언어")));
        addTagBtnPlatform.setOnClickListener(v -> showTagSelectionDialog("플랫폼", allTagsMap.get("플랫폼")));
        addTagBtnStyle.setOnClickListener(v -> showTagSelectionDialog("스타일", allTagsMap.get("스타일")));
    }

    private void showTagSelectionDialog(String categoryTitle, final String[] categoryTags) {
        if (categoryTags == null || categoryTags.length == 0) {
            Toast.makeText(this, categoryTitle + " 카테고리에 등록된 태그가 없습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean[] checkedItems = new boolean[categoryTags.length];
        for (int i = 0; i < categoryTags.length; i++) {
            checkedItems[i] = selectedTagsList.contains(categoryTags[i]);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
        builder.setTitle(categoryTitle + " 태그 선택");
        builder.setMultiChoiceItems(categoryTags, checkedItems,
                (dialog, which, isChecked) -> checkedItems[which] = isChecked);

        builder.setPositiveButton("확인", (dialog, which) -> {
            for (int i = 0; i < categoryTags.length; i++) {
                String currentTag = categoryTags[i];
                if (checkedItems[i]) {
                    if (!selectedTagsList.contains(currentTag)) {
                        selectedTagsList.add(currentTag);
                    }
                } else {
                    selectedTagsList.remove(currentTag);
                }
            }
            updateSelectedTagsUI();
        });
        builder.setNegativeButton("취소", null);
        builder.setNeutralButton("모두 해제 (이 카테고리)", (dialog, which) -> {
            for (int i = 0; i < categoryTags.length; i++) {
                checkedItems[i] = false;
                ((AlertDialog) dialog).getListView().setItemChecked(i, false);
                selectedTagsList.remove(categoryTags[i]);
            }
            updateSelectedTagsUI();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void updateSelectedTagsUI() {
        chipGroupSelectedTags.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);
        for (String tag : selectedTagsList) {
            Chip chip = (Chip) inflater.inflate(R.layout.chip_item_tag, chipGroupSelectedTags, false);
            chip.setText(tag);
            chip.setCloseIconVisible(true);
            chip.setOnCloseIconClickListener(v -> {
                selectedTagsList.remove(tag);
                updateSelectedTagsUI();
            });
            chipGroupSelectedTags.addView(chip);
        }
    }

    private void setupRecyclerView() {
        gameResultsList = new ArrayList<>();
        gameAdapter = new GameAdapter(this, gameResultsList);
        rvGameSearchResults.setLayoutManager(new LinearLayoutManager(this));
        rvGameSearchResults.setAdapter(gameAdapter);
    }

    private void performTagSearch() {
        if (selectedTagsList.isEmpty()) {
            Toast.makeText(this, "검색할 태그를 하나 이상 선택해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBarSearch.setVisibility(View.VISIBLE);
        rvGameSearchResults.setVisibility(View.GONE);

        Log.d(TAG, "Searching games by tags: " + selectedTagsList.toString());

        apiService.searchGamesByTags(selectedTagsList)
                .enqueue(new Callback<List<Game>>() {
                    @Override
                    public void onResponse(Call<List<Game>> call, Response<List<Game>> response) {
                        progressBarSearch.setVisibility(View.GONE);
                        rvGameSearchResults.setVisibility(View.VISIBLE);

                        if (response.isSuccessful() && response.body() != null) {
                            List<Game> games = response.body();
                            if (games.isEmpty()) {
                                Toast.makeText(GameActivity.this, "선택한 태그에 해당하는 게임이 없습니다.", Toast.LENGTH_SHORT).show();
                            }
                            gameAdapter.updateData(games);
                            Log.d(TAG, "Search successful. Games found: " + games.size());
                        } else {
                            Toast.makeText(GameActivity.this, "검색에 실패했습니다 (코드: " + response.code() + ")", Toast.LENGTH_LONG).show();
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

                        Toast.makeText(GameActivity.this, "네트워크 오류: " + t.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e(TAG, "Network request failed", t);
                    }
                });
    }
}