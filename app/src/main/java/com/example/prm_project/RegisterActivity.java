package com.example.prm_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import model.Users;

public class RegisterActivity extends AppCompatActivity {

    private EditText edtEmail;
    private EditText edtPhone;
    private EditText register_username;
    private EditText register_password;
    private EditText register_re_password;
    private Button btnRegister;
    private TextView textViewLogin;

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
        String re_password = register_re_password.getText().toString().trim();

        if (!re_password.equals(password)) {
            register_re_password.setError("Password not match");
            register_re_password.requestFocus();
            return;
        } else {
            // Kiểm tra sự tồn tại của username và email
            String finalEmail = email;
            myRef.orderByChild("user_name").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        register_username.setError("Username already exists");
                        register_username.requestFocus();
                    } else {
                        myRef.orderByChild("email").equalTo(finalEmail).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    edtEmail.setError("This email is already registered");
                                    edtEmail.requestFocus();
                                } else {
                                    myRef.orderByChild("user_id").limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            int maxUserId = 0;
                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                Users user = snapshot.getValue(Users.class);
                                                if (user != null && user.getUser_id() > maxUserId) {
                                                    maxUserId = user.getUser_id();
                                                }
                                            }
                                            int newUserId = maxUserId + 1;

                                            // Tạo user mới
                                            Users u = new Users();
                                            u.setUser_id(newUserId);
                                            u.setUser_name(username);
                                            u.setPassword(password);
                                            if (!finalEmail.isEmpty()) {
                                                u.setEmail(finalEmail);
                                            }
                                            // Thêm user vào Firebase
                                            myRef.push().setValue(u);
                                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                            finish();
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            // Xử lý lỗi nếu cần
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                // Xử lý lỗi nếu cần
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Xử lý lỗi nếu cần
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
    }
}