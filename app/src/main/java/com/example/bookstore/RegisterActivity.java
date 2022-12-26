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

import com.example.bookstore.Classes.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText nameInput,surnameInput, emailInput,passwordInput,confirmPasswordInput;
    private Button registerBtn,loginActivityBtn;
    private DatabaseReference userTable;

    private String emailPattern = "^(.+)@(\\S+)$";

    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); // Removes top bar
        setContentView(R.layout.activity_register);

        progressBar = findViewById(R.id.progressBar);
        toggleProgressBar(false);

        userTable = FirebaseDatabase.getInstance().getReference("users");

        nameInput = findViewById(R.id.nameInput);
        surnameInput = findViewById(R.id.surnameInput);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput);
        registerBtn = findViewById(R.id.registerBtn);
        loginActivityBtn = findViewById(R.id.LoginAccountBtn);

        mAuth = FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authRegisterInput();
            }
        });

        loginActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                RegisterActivity.this.finish();
            }
        });



    }


    private void authRegisterInput()
    {
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();
        String confirmPassword = confirmPasswordInput.getText().toString();

        if(!email.matches(emailPattern))
        {
            emailInput.setError(getString(R.string.invalidEmail));
        }
        else if(password.isEmpty())
        {
            passwordInput.setError(getString(R.string.emptyPasswordError));
        }
        else if(password.length() < 6)
        {
            passwordInput.setError(getString(R.string.passwordLenghtError));
        }
        else if(!password.equals(confirmPassword))
        {
            confirmPasswordInput.setError(getString(R.string.passwordMatchError));
        }
        else
        {
            toggleProgressBar(true);

            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                   if(task.isSuccessful())
                   {
                       toggleProgressBar(false);
                       String userId = mAuth.getCurrentUser().getUid();
                       String name = nameInput.getText().toString();
                       String surname = surnameInput.getText().toString();
                       AddUserToDatabase(new User(userId,name,surname));
                       SendToMainActivity();
                       Toast.makeText(RegisterActivity.this,getString(R.string.registerComplited),Toast.LENGTH_SHORT).show();
                   }
                   else
                   {
                       toggleProgressBar(false);
                       Toast.makeText(RegisterActivity.this,task.getException().toString(),Toast.LENGTH_SHORT).show();
                   }

                }
            });
        }
    }

    private void SendToMainActivity()
    {
        startActivity(new Intent(RegisterActivity.this,MainActivity.class));
        RegisterActivity.this.finish();
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


    private void AddUserToDatabase(User user)
    {
        userTable.push().setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity.this,"Failed!",Toast.LENGTH_SHORT).show();
            }
        });
    }


}