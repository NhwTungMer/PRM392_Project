package com.example.prm_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.tabs.TabLayout;


public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Set the text of the welcome message
        TextView welcomeMessage = findViewById(R.id.welcome_message);
        welcomeMessage.setText("Welcome, User");

        // Set the date of the calendar
        TextView calendarDate = findViewById(R.id.calendar_date);
        calendarDate.setText("Monday, 18 June 2022");

        // Set the calorie count
        TextView calorieCount = findViewById(R.id.calorie_count);
        calorieCount.setText("619 cal");

        // Set the images for each meal


        // Set the click listeners for each button
        Button calculateButton = findViewById(R.id.calculateBmi_button);
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this,BmiActivity.class);
                startActivity(intent);
            }
        });

        Button historyButton = findViewById(R.id.history_button);
        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this,HistoryActivity.class);
                startActivity(intent);
            }
        });

        Button addButton = findViewById(R.id.addFood_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this,CaloriesAcitivity.class);
                startActivity(intent);
            }
        });
    }
}