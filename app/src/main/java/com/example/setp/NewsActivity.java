package com.example.setp;

import android.os.Bundle;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class NewsActivity extends AppCompatActivity {

    private EditText etSearchQuery;
    private RecyclerView rvSearchResults;
    private SearchResultAdapter adapter;
    private ArrayList<DataModel> searchResultsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_common);
        setTitle(getString(R.string.title_news));

        etSearchQuery = findViewById(R.id.etSearchQuery);
        rvSearchResults = findViewById(R.id.rvSearchResults);

        searchResultsList = new ArrayList<>();
        adapter = new SearchResultAdapter(this, searchResultsList);
        rvSearchResults.setLayoutManager(new LinearLayoutManager(this));
        rvSearchResults.setAdapter(adapter);
    }
}
