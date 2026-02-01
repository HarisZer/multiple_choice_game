package com.uniwa.multiplechoice;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ResultListActivity extends AppCompatActivity {

    RecyclerView rvResults;
    List<Result> results;
    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_list);




        rvResults = findViewById(R.id.recyclerViewResults);
        rvResults.setLayoutManager(new LinearLayoutManager(this));

        results = loadResults(); // χρησιμοποιούμε το πεδίο της κλάσης
        ResultAdapter adapter = new ResultAdapter(results);
        rvResults.setAdapter(adapter);


        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(ResultListActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        Button btnClearHistory = findViewById(R.id.btnClearHistory);

        btnClearHistory.setOnClickListener(v -> {
            // Καθαρίζει το SharedPreferences
            getSharedPreferences("results", Context.MODE_PRIVATE)
                    .edit()
                    .remove("results_list")
                    .apply();

            // Καθαρίζει τη λίστα και ενημερώνει τον Adapter
            results.clear();
            rvResults.getAdapter().notifyDataSetChanged();
        });


    }

    private List<Result> loadResults() {
        SharedPreferences prefs = getSharedPreferences("results", Context.MODE_PRIVATE);
        String json = prefs.getString("results_list", "[]");

        Gson gson = new Gson();
        Type type = new TypeToken<List<Result>>() {}.getType();
        List<Result> res = gson.fromJson(json, type);
        if (res == null) res = new ArrayList<>();
        return res;
    }
}

