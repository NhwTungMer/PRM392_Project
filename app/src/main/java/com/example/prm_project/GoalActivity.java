package com.example.prm_project;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import model.Goal;

public class GoalActivity extends AppCompatActivity {
    private EditText edtCurrentWeight, edtDesiredWeight, edtDuration;
    private Button btnSave;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_goal);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });
        bindingView();
        bindingAction();
    }

    private void bindingAction() {
        btnSave.setOnClickListener(this::saveGoal);
    }

    private void saveGoal(View view) {
        String currentWeight = edtCurrentWeight.getText().toString();
        String desiredWeight = edtDesiredWeight.getText().toString();
        String duration = edtDuration.getText().toString();

        if (currentWeight.isEmpty()) {
            edtCurrentWeight.setError("Current weight is required");
            edtCurrentWeight.requestFocus();
            return;
        }

        if (desiredWeight.isEmpty()) {
            edtDesiredWeight.setError("Desired weight is required");
            edtDesiredWeight.requestFocus();
            return;
        }

        if (duration.isEmpty()) {
            edtDuration.setError("Duration is required");
            edtDuration.requestFocus();
            return;
        }

        String userId = firebaseAuth.getCurrentUser().getUid();

        Goal goal = new Goal(currentWeight, desiredWeight, duration);

        // Lưu thông tin mục tiêu vào Firebase
        databaseReference.child(userId).setValue(goal).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(GoalActivity.this, "Goal saved successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(GoalActivity.this, "Failed to save goal: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void bindingView() {
        edtCurrentWeight = findViewById(R.id.current_weight_value);
        edtDesiredWeight = findViewById(R.id.desired_weight_value);
        edtDuration = findViewById(R.id.duration_value);
        btnSave = findViewById(R.id.save_button);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("goals");
    }
}