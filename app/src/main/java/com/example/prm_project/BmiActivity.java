package com.example.prm_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import model.Bmi;

public class BmiActivity extends AppCompatActivity {

    private EditText etWeight, etHeight;
    private Button btnCalculate, btnHistory,btnViewCalories;
    private TextView tvLocation, tvTemp, tvWelcome, tvResult, tvBmiCategory;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi);

        // Initialize Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("BMIHistory");

        // Initialize views
        tvLocation = findViewById(R.id.tvLocation);
        tvTemp = findViewById(R.id.tvTemp);
        tvWelcome = findViewById(R.id.tvWelcome);
        etWeight = findViewById(R.id.etWeight);
        etHeight = findViewById(R.id.etHeight);
        btnCalculate = findViewById(R.id.btnCalculate);

        tvResult = findViewById(R.id.tvResult);
        tvBmiCategory = findViewById(R.id.tvBmiCategory);

        // Set welcome text
        tvWelcome.setText("Welcome to the BMI Calculator!");

        // Set click listener for the Calculate button
        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndCalculateBmi();
            }
        });



    }

    private void checkAndCalculateBmi() {
        databaseReference.orderByChild("timestamp").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int count = 0;
                long currentTime = System.currentTimeMillis();
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(currentTime);
                int currentMonth = calendar.get(Calendar.MONTH);
                int currentYear = calendar.get(Calendar.YEAR);

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String timestampStr = dataSnapshot.child("timestamp").getValue(String.class);  // Đọc timestamp dưới dạng chuỗi
                    if (timestampStr != null) {
                        long timestamp = Long.parseLong(timestampStr);  // Chuyển đổi timestamp từ chuỗi thành số nguyên (long)
                        calendar.setTimeInMillis(timestamp);
                        int month = calendar.get(Calendar.MONTH);
                        int year = calendar.get(Calendar.YEAR);
                        if (month == currentMonth && year == currentYear) {
                            count++;
                        }
                    }
                }

                if (count < 10) {
                    calculateBmi();
                } else {
                    Toast.makeText(BmiActivity.this, "You can only calculate BMI 10 times a month.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(BmiActivity.this, "Failed to check BMI calculation limit", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void calculateBmi() {
        String weightStr = etWeight.getText().toString();
        String heightStr = etHeight.getText().toString();

        if (!weightStr.isEmpty() && !heightStr.isEmpty()) {
            int weight = Integer.parseInt(weightStr);
            int height = Integer.parseInt(heightStr);

            if (height > 0) {
                double heightInMeters = height / 100.0;
                double bmi = weight / (heightInMeters * heightInMeters);
                String bmiResult = String.format(Locale.getDefault(), "Your BMI: %.2f", bmi);
                String bmiCategory = getBmiCategory(bmi);
                tvResult.setText(bmiResult);
                tvBmiCategory.setText(bmiCategory);

                // Save BMI to Firebase
                saveBmiToFirebase(weight, height, bmi, bmiCategory);
            } else {
                Toast.makeText(BmiActivity.this, "Height must be greater than 0", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(BmiActivity.this, "Please enter both weight and height", Toast.LENGTH_LONG).show();
        }
    }

    private String getBmiCategory(double bmi) {

        if (bmi < 16) {
            return "Gầy độ III";
        } else if (bmi >= 16 && bmi < 17) {
            return "Gầy độ II";
        } else if (bmi >= 17 && bmi < 18.5) {
            return "Gầy độ I";
        } else if (bmi >= 18.5 && bmi < 25) {
            return "Bình thường";
        } else if (bmi >= 25 && bmi < 30) {
            return "Thừa cân";
        } else if (bmi >= 30 && bmi < 35) {
            return "Béo phì độ I";
        } else if (bmi >= 35 && bmi < 40) {
            return "Béo phì độ II";
        } else {
            return "Béo phì độ III";
        }
    }

    private void saveBmiToFirebase(int weight, int height, double bmi, String bmiCategory) {
        String id = databaseReference.push().getKey();
        long timestamp = System.currentTimeMillis();  // Lưu timestamp dưới dạng số nguyên (long)
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String date = dateFormat.format(calendar.getTime());

        Bmi bmiRecord = new Bmi(bmi, height, weight, String.valueOf(timestamp), bmiCategory, date);

        if (id != null) {
            databaseReference.child(id).setValue(bmiRecord).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(BmiActivity.this, "BMI recorded successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(BmiActivity.this, "Failed to record BMI", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }




}
