package com.example.prm_project;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import model.Calories;

public class CaloriesAcitivity extends AppCompatActivity {
    private EditText foodNameEditText, caloriesEditText;
    private Button addButton;
    private TextView totalCaloriesTextView;
    private LinearLayout foodListContainer;
    private DatabaseReference databaseReference;
    private int totalCalories = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calories);

        foodNameEditText = findViewById(R.id.editText);
        caloriesEditText = findViewById(R.id.editText2);
        addButton = findViewById(R.id.calculateBtn);
        totalCaloriesTextView = findViewById(R.id.ViewCalories);
        foodListContainer = findViewById(R.id.foodListContainer);

        // Initialize Firebase Database
        databaseReference = FirebaseDatabase.getInstance().getReference("foods");

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFood();
            }
        });

        // Load total calories and food list from the database
        loadFoodData();
    }

    private void addFood() {
        String foodName = foodNameEditText.getText().toString().trim();
        String caloriesStr = caloriesEditText.getText().toString().trim();

        if (!foodName.isEmpty() && !caloriesStr.isEmpty()) {
            int caloriesValue = Integer.parseInt(caloriesStr);
            totalCalories += caloriesValue;
            String id = databaseReference.push().getKey();
            Calories calories = new Calories(id, foodName, caloriesValue, totalCalories);

            assert id != null;
            databaseReference.child(id).setValue(calories);
            addFoodToLayout(calories);
            updateTotalCalories();
            Toast.makeText(this, "Food added", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Please enter food name and calories", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadFoodData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                totalCalories = 0;
                foodListContainer.removeAllViews(); // Clear existing views
                for (DataSnapshot foodSnapshot : snapshot.getChildren()) {
                    Calories calories = foodSnapshot.getValue(Calories.class);
                    assert calories != null;
                    totalCalories += calories.getCalories();
                    addFoodToLayout(calories);
                }
                updateTotalCalories();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }

    private void addFoodToLayout(Calories food) {
        TextView textView = new TextView(this);
        textView.setText(food.getFoodName() + ": " + food.getCalories() + " calories");
        textView.setTextSize(18);
        textView.setTextColor(getResources().getColor(android.R.color.black));
        foodListContainer.addView(textView);
    }

    private void updateTotalCalories() {
        totalCaloriesTextView.setText("Total Calories: " + totalCalories);
    }
}