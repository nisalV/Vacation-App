package com.work.vacationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private EditText firstName,lastName,telephone,nic,email;
    private TextInputEditText password;
    private FirebaseAuth auth;

    DatabaseReference userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        telephone = findViewById(R.id.telephone);
        nic = findViewById(R.id.nic);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        Button login = findViewById(R.id.login);

        auth = FirebaseAuth.getInstance();

        login.setOnClickListener(v -> {
            String txt_firstName = firstName.getText().toString();
            String txt_lastName = lastName.getText().toString();
            String txt_telephone = telephone.getText().toString();
            String txt_nic = nic.getText().toString();
            assert email != null;
            String txt_email = email.getText().toString();
            String txt_password = Objects.requireNonNull(password.getText()).toString();

            if(TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password) || txt_firstName.isEmpty() ||txt_lastName.isEmpty() ||txt_telephone.isEmpty() || txt_nic.isEmpty()){
                Toast.makeText(RegisterActivity.this, "Fill all!", Toast.LENGTH_SHORT).show();
            }else if(txt_password.length() < 6){
                Toast.makeText(RegisterActivity.this, "Password is too short!", Toast.LENGTH_SHORT).show();
            }else {
                registerUser(txt_email,txt_password);
            }
        });
    }

    public void insertUserData() {
        String txt_firstName = firstName.getText().toString();
        String txt_lastName = lastName.getText().toString();
        String txt_telephone = telephone.getText().toString();
        String txt_nic = nic.getText().toString();
        String txt_email = email.getText().toString();
        String txt_password = Objects.requireNonNull(password.getText()).toString();

        Users users = new Users(txt_firstName,txt_lastName,txt_telephone,txt_nic,txt_email,txt_password);

        userData.child(txt_nic).setValue(users);
    }

    private void registerUser(String email, String password) {
        String txt_nic = nic.getText().toString();
        userData = FirebaseDatabase.getInstance().getReference().child("users");
        userData.orderByChild("nic").equalTo(txt_nic).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Toast.makeText(RegisterActivity.this, "User from this NIC "+txt_nic+"V already exist!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                    finish();
                }else if(!(dataSnapshot.exists())){
                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, task -> {
                        if(task.isSuccessful()){
                            insertUserData();
                            Toast.makeText(RegisterActivity.this,"Registration completed",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                            finish();
                        }else{
                            Toast.makeText(RegisterActivity.this, Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });
    }
}