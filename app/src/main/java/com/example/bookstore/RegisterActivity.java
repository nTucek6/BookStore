package com.example.bookstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
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

    private TextInputEditText nameInput,surnameInput,emailInput,passwordInput,confirmPasswordInput,addressInput,cityInput;
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
        addressInput = findViewById(R.id.addressInput);
        cityInput = findViewById(R.id.cityInput);
        registerBtn = findViewById(R.id.registerBtn);
        loginActivityBtn = findViewById(R.id.LoginAccountBtn);

        SetEditText();

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


    private void SetEditText()
    {
        emailInput.setImeOptions(EditorInfo.IME_ACTION_GO);
        passwordInput.setImeOptions(EditorInfo.IME_ACTION_GO);
        confirmPasswordInput.setImeOptions(EditorInfo.IME_ACTION_GO);
        nameInput.setImeOptions(EditorInfo.IME_ACTION_GO);
        surnameInput.setImeOptions(EditorInfo.IME_ACTION_GO);
        addressInput.setImeOptions(EditorInfo.IME_ACTION_GO);
        cityInput.setImeOptions(EditorInfo.IME_ACTION_DONE);

        emailInput.setSingleLine();
        passwordInput.setSingleLine();
        confirmPasswordInput.setSingleLine();
        nameInput.setSingleLine();
        surnameInput.setSingleLine();
        addressInput.setSingleLine();
        cityInput.setSingleLine();

    }



    private void authRegisterInput()
    {
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();
        String confirmPassword = confirmPasswordInput.getText().toString();
        String name = nameInput.getText().toString();
        String surname = surnameInput.getText().toString();
        String address = addressInput.getText().toString();
        String city = cityInput.getText().toString();

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
        else if(name.isEmpty())
        {
            nameInput.setError(getString(R.string.emptyName));
        }
        else if(surname.isEmpty())
        {
            surnameInput.setError(getString(R.string.emptySurname));
        }
        else if(address.isEmpty())
        {
            addressInput.setError(getString(R.string.emptyAddress));
        }
        else if(city.isEmpty())
        {
            cityInput.setError(getString(R.string.emptyCity));
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

                       AddUserToDatabase(new User(userId,name,surname,address,city));
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