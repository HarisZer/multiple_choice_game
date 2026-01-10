package com.uniwa.multiplechoice;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    TextView TvQuestion, TvTimer, TvProgress;
    ImageView IvQuestionImage;
    Button BtnAnswer1, BtnAnswer2, BtnAnswer3, BtnSubmit;

    CountDownTimer timer;
    int timeLeft = 30;

    List<Question> quizQuestions;
    int currentQuestion = 0;
    int score = 0;
    int selectedAnswerIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        TvQuestion = findViewById(R.id.tvQuestion);
        TvTimer = findViewById(R.id.tvTimer);
        TvProgress = findViewById(R.id.tvProgress);
        IvQuestionImage = findViewById(R.id.ivQuestionImage);

        BtnAnswer1 = findViewById(R.id.btnAnswer1);
        BtnAnswer2 = findViewById(R.id.btnAnswer2);
        BtnAnswer3 = findViewById(R.id.btnAnswer3);
        BtnSubmit = findViewById(R.id.btnNext);

        loadQuestionsFromJson();

        if (quizQuestions == null || quizQuestions.isEmpty()) {
            Toast.makeText(this, "Σφάλμα φόρτωσης ερωτήσεων", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        loadQuestion();
        startTimer();

        BtnAnswer1.setOnClickListener(v -> selectAnswer(0, BtnAnswer1));
        BtnAnswer2.setOnClickListener(v -> selectAnswer(1, BtnAnswer2));
        BtnAnswer3.setOnClickListener(v -> selectAnswer(2, BtnAnswer3));

        BtnSubmit.setOnClickListener(v -> submitAnswer());
    }

    private void loadQuestionsFromJson() {
        try {
            InputStream is = getAssets().open("questions.json");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();

            String json = new String(buffer, "UTF-8");
            Gson gson = new Gson();
            QuestionsPool pool = gson.fromJson(json, QuestionsPool.class);

            if (pool == null || pool.questions == null) {
                Toast.makeText(this, "Σφάλμα: JSON κενό ή λανθασμένο", Toast.LENGTH_LONG).show();
                return;
            }


            Collections.shuffle(pool.questions);
            quizQuestions = new ArrayList<>(pool.questions.subList(0, Math.min(10, pool.questions.size())));
            Collections.shuffle(quizQuestions);

        } catch (Exception e) {
            Toast.makeText(this, "Σφάλμα φόρτωσης JSON", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void loadQuestion() {
        Question q = quizQuestions.get(currentQuestion);
        TvQuestion.setText(q.question);
        TvProgress.setText((currentQuestion + 1) + " / " + quizQuestions.size());

        BtnAnswer1.setText(q.options.get(0));
        BtnAnswer2.setText(q.options.get(1));
        BtnAnswer3.setText(q.options.get(2));

        if (q.image != null && !q.image.isEmpty()) {
            int resId = getResources().getIdentifier(q.image, "drawable", getPackageName());
            if (resId != 0) {
                IvQuestionImage.setImageResource(resId);
                IvQuestionImage.setVisibility(View.VISIBLE);
            } else {
                IvQuestionImage.setVisibility(View.GONE);
            }
        } else {
            IvQuestionImage.setVisibility(View.GONE);
        }

        resetButtonColors();
        selectedAnswerIndex = -1;
    }

    private void selectAnswer(int index, Button button) {
        selectedAnswerIndex = index;
        resetButtonColors();
        button.setBackgroundColor(ContextCompat.getColor(this, R.color.answer_selected));
    }

    private void submitAnswer() {
        if (selectedAnswerIndex == -1) {
            Toast.makeText(this, "Επίλεξε πρώτα απάντηση", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedAnswerIndex == quizQuestions.get(currentQuestion).answer) {
            score++;
        }

        currentQuestion++;

        if (currentQuestion < quizQuestions.size()) {
            timeLeft = 30;
            loadQuestion();
            startTimer();
        } else {
            if (timer != null) timer.cancel();
            saveResult();
            Intent intent = new Intent(this, ResultListActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void resetButtonColors() {
        BtnAnswer1.setBackgroundColor(ContextCompat.getColor(this, R.color.answer_notselected));
        BtnAnswer2.setBackgroundColor(ContextCompat.getColor(this, R.color.answer_notselected));
        BtnAnswer3.setBackgroundColor(ContextCompat.getColor(this, R.color.answer_notselected));
    }

    private void startTimer() {
        if (timer != null) timer.cancel();
        TvTimer.setText("Χρόνος: " + timeLeft);

        timer = new CountDownTimer(timeLeft * 1000L, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeft--;
                TvTimer.setText("Χρόνος: " + timeLeft);
            }

            @Override
            public void onFinish() {
                submitAnswer();
            }
        }.start();
    }

    private void saveResult() {
        SharedPreferences prefs = getSharedPreferences("results", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString("results_list", "[]");
        Type type = new TypeToken<List<Result>>() {}.getType();
        List<Result> results = gson.fromJson(json, type);

        if (results == null) results = new ArrayList<>();

        String name = getIntent().getStringExtra("name"); // Από StudentInfo
        String am = getIntent().getStringExtra("am");
        String age = getIntent().getStringExtra("age");
        String dateTime = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());

        results.add(new Result(name, am, age, score, dateTime));

        String newJson = gson.toJson(results);
        prefs.edit().putString("results_list", newJson).apply();
    }
}
