package com.uniwa.multiplechoice;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    TextView tvName, tvAM, tvScore, tvTotal;
    Button btnFinish, btnHistory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        tvName = findViewById(R.id.tvName);
        tvAM = findViewById(R.id.tvAM);
        tvScore = findViewById(R.id.tvScore);
        tvTotal = findViewById(R.id.tvDateTime); // αυτό θα δείχνει σύνολο ή ώρα
        btnFinish = findViewById(R.id.btnFinish);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String am = intent.getStringExtra("am");
        int score = intent.getIntExtra("score", 0);
        int total = intent.getIntExtra("total", 0);

        tvName.setText("Όνομα: " + name);
        tvAM.setText("ΑΜ: " + am);
        tvScore.setText("Σωστές απαντήσεις: " + score + " / " + total);
        tvTotal.setText("Ολοκλήρωση τεστ ✔");

        btnFinish.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }



}
