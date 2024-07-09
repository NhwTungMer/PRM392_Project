package com.example.prm_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import model.Users;

public class LoginActivity extends AppCompatActivity {

    private ImageView imgFb;
    private ImageView imgGg;
    private EditText edtEmail;
    private EditText edtPassword;
    private Button btnLogin;
    private TextView btnSignUp;
    private TextView btnForgotPassword;
    private FirebaseAuth firebaseAuth;
    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;
    private DatabaseReference databaseReference;
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
        if (requestCode == 1000) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    saveUserToDatabase(account.getEmail(), account.getDisplayName(), "google");
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                }
            } catch (ApiException e) {
                Toast.makeText(LoginActivity.this, "Login With Google Failed", Toast.LENGTH_SHORT).show();
            }
        }
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void initAction() {
        imgFb.setOnClickListener(this::onClickLoginFb);
        imgGg.setOnClickListener(this::onClickLoginGg);
        btnLogin.setOnClickListener(this::onClickLogin);
        btnSignUp.setOnClickListener(this::onClickSignUp);
        btnForgotPassword.setOnClickListener(this::onClickForgotPassword);
    }

    private void onClickLoginGg(View view) {
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent, 1000);
    }

    private void onClickForgotPassword(View view) {
        final EditText resetEmail = new EditText(view.getContext());
        final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(view.getContext());
        passwordResetDialog.setTitle("Reset Password ?");
        passwordResetDialog.setMessage("Enter your email to receive reset link");
        passwordResetDialog.setView(resetEmail);
        passwordResetDialog.setPositiveButton("Send", (dialog, which) -> {
            String mail = resetEmail.getText().toString();
            firebaseAuth.sendPasswordResetEmail(mail).addOnSuccessListener(unused -> {
                Toast.makeText(LoginActivity.this, "Reset link sent to your email", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(e -> {
                Toast.makeText(LoginActivity.this, "Error ! Reset link is not sent to your email" + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        });
        passwordResetDialog.setNegativeButton("Close", (dialog, which) -> {
        });
        passwordResetDialog.create().show();
    }

    private void onClickSignUp(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
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

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser fu = firebaseAuth.getCurrentUser();
                    assert fu != null;
                    if (fu.isEmailVerified()) {
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(LoginActivity.this, "Please verify your email", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
        btnForgotPassword = findViewById(R.id.forgot_password_link);
        firebaseAuth = FirebaseAuth.getInstance();
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        FirebaseUser fu = firebaseAuth.getCurrentUser();
                        assert fu != null;
                        AccessToken accessToken = AccessToken.getCurrentAccessToken();
                        GraphRequest request = GraphRequest.newMeRequest(
                                accessToken,
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {
                                        String email = fu.getEmail();
                                        String name = object.optString("name");

                                        assert email != null;
                                        if (!email.isEmpty()) {
                                            linkFacebookAccount(email, name);
                                        } else {
                                            Toast.makeText(LoginActivity.this, "Failed to retrieve email from Facebook", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email");
                        request.setParameters(parameters);
                        request.executeAsync();
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

    private void linkFacebookAccount(String email, String name) {
        firebaseAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                if (task.isSuccessful()) {
                    SignInMethodQueryResult result = task.getResult();
                    if (result != null && result.getSignInMethods() != null && !result.getSignInMethods().isEmpty()) {
                        // Email already exists, link the account
                        AuthCredential credential = EmailAuthProvider.getCredential(email, "user-password");
                        firebaseAuth.getCurrentUser().linkWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    saveUserToDatabase(email, name, "facebook");
                                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(LoginActivity.this, "Failed to link Facebook account", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        // Email does not exist, create a new account
                        saveUserToDatabase(email, name, "facebook");
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Failed to check email existence", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveUserToDatabase(String email, String name, String provider) {
        String userId = firebaseAuth.getCurrentUser().getUid();

        // Lấy dữ liệu hiện tại của người dùng từ cơ sở dữ liệu
        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users user;
                if (dataSnapshot.exists()) {
                    user = dataSnapshot.getValue(Users.class);
                    if (user != null) {
                        String currentLoginTypes = user.getLoginType();
                        Set<String> loginTypeSet = new HashSet<>(Arrays.asList(currentLoginTypes.split(",")));

                        if (!loginTypeSet.contains(provider)) {
                            loginTypeSet.add(provider);
                        }

                        String newLoginTypes = String.join(",", loginTypeSet);
                        user.setLoginType(newLoginTypes);
                    }
                } else {
                    // Nếu người dùng chưa tồn tại, tạo mới
                    user = new Users(name, "", email, provider);
                }

                // Cập nhật thông tin người dùng vào cơ sở dữ liệu
                databaseReference.child(userId).setValue(user).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "User data saved", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "Failed to save user data", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(LoginActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
