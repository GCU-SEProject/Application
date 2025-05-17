package com.example.setp;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class GameSearchActivity extends AppCompatActivity {

    private EditText etGameSearchQuery;
    private Spinner spinnerPlatform, spinnerGenre, spinnerPlaytime;
    private RecyclerView rvGameSearchResults;
    private SearchResultAdapter gameAdapter;
    private ArrayList<DataModel> gameResultsList;
    private String selectedPlaytimeSortOrder = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_search);
        setTitle(getString(R.string.title_game_search));

        etGameSearchQuery = findViewById(R.id.etGameSearchQuery);
        spinnerPlatform = findViewById(R.id.spinnerPlatform);
        spinnerGenre = findViewById(R.id.spinnerGenre);
        spinnerPlaytime = findViewById(R.id.spinnerPlaytime);
        rvGameSearchResults = findViewById(R.id.rvGameSearchResults);

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

        gameResultsList = new ArrayList<>();
        gameAdapter = new SearchResultAdapter(this, gameResultsList);
        rvGameSearchResults.setLayoutManager(new LinearLayoutManager(this));
        rvGameSearchResults.setAdapter(gameAdapter);
    }
}