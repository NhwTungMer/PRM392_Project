package com.example.prm_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.util.Arrays;

import model.Users;

public class LoginActivity extends AppCompatActivity {

    private ImageView imgFb;
    private ImageView imgGg;
    private EditText edtEmail;
    private EditText edtPassword;
    private Button btnLogin;
    private TextView btnSignUp;


    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        callbackManager = CallbackManager.Factory.create();
        initView();
        initAction();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void initAction() {
        imgFb.setOnClickListener(this::onClickLoginFb);
        imgGg.setOnClickListener(v -> {
            System.out.println("Google");
        });
        btnLogin.setOnClickListener(this::onClickLogin);
        btnSignUp.setOnClickListener(this::onClickSignUp);
    }

    private void onClickSignUp(View view) {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        finish();
    }

    private void onClickLogin(View view) {
        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();
        if (email.isEmpty()) {
            edtEmail.setError("Email is required");
            edtEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            edtPassword.setError("Password is required");
            edtPassword.requestFocus();
            return;
        }
//        Users u = dbContext.authorized(email, password);
//        if (u != null) {
//            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
//            finish();
//        } else {
//            Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
//        }
    }

    private void onClickLoginFb(View view) {
        LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile"));
    }

    private void initView() {
        imgFb = findViewById(R.id.logo_fb);
        imgGg = findViewById(R.id.logo_gg);
        edtEmail = findViewById(R.id.email);
        edtPassword = findViewById(R.id.password);
        btnLogin = findViewById(R.id.login_button);
        btnSignUp = findViewById(R.id.signup_link);
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        finish();
                    }

                    @Override
                    public void onCancel() {
                        // Handle cancellation
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // Handle error
                    }
                });
    }
}
