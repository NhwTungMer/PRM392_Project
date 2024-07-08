package com.example.prm_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import model.Users;

public class UserProfileActivity extends AppCompatActivity {
    private TextInputEditText editUserName;
    private TextView textEmail;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private Button btnSave;
    private Button btnGoal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        bindingView();
        bindingAction();
        loadUserData();
    }

    private void loadUserData() {
        String userId = firebaseAuth.getCurrentUser().getUid();
        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Users user = dataSnapshot.getValue(Users.class);
                    if (user != null) {
                        // Display current user data
                        editUserName.setText(user.getUser_name());
                        textEmail.setText(user.getEmail());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(UserProfileActivity.this, "Failed to load user data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void bindingAction() {
        btnSave.setOnClickListener(this::saveUserName);
        btnGoal.setOnClickListener(this::redirectGoalScreen);
    }

    private void redirectGoalScreen(View view) {
        startActivity(new Intent(UserProfileActivity.this, GoalActivity.class));
        finish();
    }

    private void saveUserName(View view) {
        String newUserName = editUserName.getText().toString().trim();
        if (newUserName.isEmpty()) {
            Toast.makeText(UserProfileActivity.this, "Username cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = firebaseAuth.getCurrentUser().getUid();
        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Users user = dataSnapshot.getValue(Users.class);
                    if (user != null) {
                        // Update username
                        user.setUser_name(newUserName);
                        databaseReference.child(userId).setValue(user).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(UserProfileActivity.this, "Username updated successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(UserProfileActivity.this, "Failed to update username", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(UserProfileActivity.this, "Failed to update username", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void bindingView() {
        editUserName = findViewById(R.id.editUserName);
        textEmail = findViewById(R.id.textEmail);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        btnSave = findViewById(R.id.btnSaveUserName);
        btnGoal = findViewById(R.id.btnMyGoal);
    }
}