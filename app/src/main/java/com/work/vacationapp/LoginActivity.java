package com.work.vacationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private EditText email;
    private TextInputEditText password;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        TextView register = findViewById(R.id.register);
        TextView forgot = findViewById(R.id.forgot);
        Button login = findViewById(R.id.login);

        auth = FirebaseAuth.getInstance();

        login.setOnClickListener(v -> {
            String txt_email = email.getText().toString();
            String txt_password = Objects.requireNonNull(password.getText()).toString();

            if(TextUtils.isEmpty(txt_email) ||TextUtils.isEmpty(txt_password)){
                Toast.makeText(LoginActivity.this, "Empty credentials", Toast.LENGTH_SHORT).show();
            }else {
                loginUser(txt_email,txt_password);
            }
        });

        forgot.setOnClickListener(v -> {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            String txt_email = email.getText().toString();

            if(txt_email.isEmpty()){
                Toast.makeText(this, "Enter an email!", Toast.LENGTH_SHORT).show();
            }else{
                auth.sendPasswordResetEmail(txt_email).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "The password reset email was sent to your email", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(this, Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        register.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            finish();
        });
    }

    private void loginUser(String email, String password) {
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, task -> {
            if(task.isSuccessful()) {
                Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                finish();
            } else {
                Toast.makeText(LoginActivity.this, Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}