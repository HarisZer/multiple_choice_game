package com.uniwa.multiplechoice;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;


public class StudentInfo extends AppCompatActivity {


    EditText EtName, EtAM, NbAge;

    Button BtnNext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_info);

        EtName = findViewById(R.id.EtName);
        EtAM = findViewById(R.id.EtAM);
        NbAge = findViewById(R.id.NbAge);
        BtnNext = findViewById(R.id.BtnNext);

        BtnNext.setOnClickListener(v -> {
            String name = EtName.getText().toString().trim();
            String am = EtAM.getText().toString().trim();
            String age = NbAge.getText().toString().trim();

            if (name.isEmpty()) {
                Toast.makeText(this, "Παρακαλώ εισάγετε όνομα", Toast.LENGTH_SHORT).show();
                return;
            }

            if(age.isEmpty()){
                Toast.makeText(this, "Παρακαλώ εισάγετε την ηλικία", Toast.LENGTH_SHORT).show();
                return;
            }



            Intent intent = new Intent(StudentInfo.this, QuizActivity.class);
            intent.putExtra("name", name);
            intent.putExtra("am", am);
            intent.putExtra("age", age);
            startActivity(intent);
        });
    }
}
