package com.example.prm_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import model.Users;

public class RegisterActivity extends AppCompatActivity {

    private EditText edtEmail;
    private EditText register_username;
    private EditText register_password;
    private EditText register_re_password;
    private Button btnRegister;
    private TextView textViewLogin;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initView();
        initAction();
    }

    private void initAction() {
        btnRegister.setOnClickListener(this::onClickRegister);
        textViewLogin.setOnClickListener(this::onClickRedirectLogin);
    }

    private void onClickRedirectLogin(View view) {
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        finish();
    }

    private void onClickRegister(View view) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");
        String email = "";
        if (edtEmail != null && !edtEmail.getText().toString().isEmpty()) {
            email = edtEmail.getText().toString().trim();
        }
        String username = register_username.getText().toString().trim();
        String password = register_password.getText().toString().trim();
        if (password.length() < 6) {
            register_password.setError("Password must be at least 6 characters");
            register_password.requestFocus();
            return;
        }
        String re_password = register_re_password.getText().toString().trim();

        if (!re_password.equals(password)) {
            register_re_password.setError("Password not match");
            register_re_password.requestFocus();
            return;
        } else {
            // Check if the email is already registered
            String finalEmail = email;
            myRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        edtEmail.setError("This email is already registered");
                        edtEmail.requestFocus();
                    } else {
                        firebaseAuth.createUserWithEmailAndPassword(finalEmail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(RegisterActivity.this, "Registered successfully!", Toast.LENGTH_SHORT).show();

                                    FirebaseUser u = firebaseAuth.getCurrentUser();
                                    assert u != null;
                                    u.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(RegisterActivity.this, "Verification email sent to " + u.getEmail(), Toast.LENGTH_SHORT).show();

                                            Users user = new Users(username, finalEmail, password);
                                            myRef.child(u.getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Log.d("TAG", "User record saved successfully");
                                                    } else {
                                                        Log.d("TAG", "Failed to save user record: " + task.getException().getMessage());
                                                    }
                                                }
                                            });

                                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                            finish();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d("TAG", "onFailure: Email not sent: " + e.getMessage());
                                        }
                                    });
                                } else {
                                    Toast.makeText(RegisterActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle possible errors.
                    Log.d("TAG", "onCancelled: " + databaseError.getMessage());
                }
            });
        }
    }


    private void initView() {
        edtEmail = findViewById(R.id.edtEmail);
        register_username = findViewById(R.id.register_username);
        register_password = findViewById(R.id.register_password);
        register_re_password = findViewById(R.id.register_re_password);
        btnRegister = findViewById(R.id.btnRegister);
        textViewLogin = findViewById(R.id.textViewLogin);
        firebaseAuth = FirebaseAuth.getInstance();
    }
}