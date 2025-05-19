package com.example.setp.encyclopedia;

import android.os.Bundle;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.setp.R;

public class EncyclopediaActivity extends AppCompatActivity {

    private EditText etSearchQuery;
    private RecyclerView rvSearchResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_common);
        setTitle(getString(R.string.title_encyclopedia));

        etSearchQuery = findViewById(R.id.etSearchQuery);
        rvSearchResults = findViewById(R.id.rvSearchResults);

        rvSearchResults.setLayoutManager(new LinearLayoutManager(this));
    }
}