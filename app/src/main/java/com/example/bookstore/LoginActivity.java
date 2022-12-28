package com.example.bookstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText emailInput, passwordInput;
    private Button loginBtn,newAccountBtn;
    private TextView forgotPasswordTv;

    private String emailPattern = "^(.+)@(\\S+)$";

    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); // Removes top bar

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();


        if (mUser != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }


        setContentView(R.layout.activity_login);

        progressBar = findViewById(R.id.progressBar);
        toggleProgressBar(false);

        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginBtn = findViewById(R.id.loginBtn);
        forgotPasswordTv = findViewById(R.id.passwordForgotTv);
        newAccountBtn = findViewById(R.id.newAccountBtn);



        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginUser();
            }
        });

        newAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                LoginActivity.this.finish();
            }
        });


    }

    private void LoginUser() {
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();

        if (!email.matches(emailPattern)) {
            emailInput.setError(getString(R.string.invalidEmail));
        } else if (password.isEmpty()) {
            passwordInput.setError(getString(R.string.emptyPasswordError));
        }
        else {
            toggleProgressBar(true);

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        toggleProgressBar(false);
                        SendToMainActivity();
                        Toast.makeText(LoginActivity.this, getString(R.string.loginComplited), Toast.LENGTH_SHORT).show();
                    } else {
                        toggleProgressBar(false);
                        Toast.makeText(LoginActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }
    private void SendToMainActivity()
    {
        startActivity(new Intent(LoginActivity.this,MainActivity.class));
        LoginActivity.this.finish();
    }

    private void toggleProgressBar(boolean toggle)
    {
        if(toggle)
        {
            progressBar.setVisibility(View.VISIBLE);
        }
        else
        {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }


}