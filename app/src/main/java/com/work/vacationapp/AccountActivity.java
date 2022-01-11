package com.work.vacationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AccountActivity extends AppCompatActivity {

    DatabaseReference userData;

    EditText firstName,lastName,telephone;
    TextView save,nic,email;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        telephone = findViewById(R.id.telephone);
        nic = findViewById(R.id.nic);
        save = findViewById(R.id.save);
        email = findViewById(R.id.email);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        String userEmail = Objects.requireNonNull(auth.getCurrentUser()).getEmail();
        email.setText("e-mail: " + userEmail);

        userData = FirebaseDatabase.getInstance().getReference().child("users");
        userData.orderByChild("email").equalTo(userEmail).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot data: dataSnapshot.getChildren()){
                        String fName = Objects.requireNonNull(data.child("firstName").getValue()).toString();
                        String lName = Objects.requireNonNull(data.child("lastName").getValue()).toString();
                        String tel = Objects.requireNonNull(data.child("telephone").getValue()).toString();
                        String ni = Objects.requireNonNull(data.child("nic").getValue()).toString();
                        firstName.setHint(fName);
                        lastName.setHint(lName);
                        telephone.setHint(tel);
                        nic.setText(ni);
                        break;
                    }
                }else{
                    System.out.println("data not exist");
                }
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(AccountActivity.this, Objects.requireNonNull(error.toException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        save.setOnClickListener(v -> {
            String txt_firstName = firstName.getText().toString();
            String txt_lastName = lastName.getText().toString();
            String txt_telephone = telephone.getText().toString();
            String txt_nic = nic.getText().toString();
            if(txt_firstName.isEmpty() ||txt_lastName.isEmpty() ||txt_telephone.isEmpty()){
                Toast.makeText(AccountActivity.this, "Fill all!", Toast.LENGTH_SHORT).show();
            }else {
                Map<String,Object> map = new HashMap<>();
                map.put("firstName",txt_firstName);
                map.put("lastName",txt_lastName);
                map.put("telephone",txt_telephone);
                userData.child(txt_nic).updateChildren(map).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        startActivity(new Intent(AccountActivity.this,AccountActivity.class));
                        finish();
                    }else if(!task.isSuccessful()){
                        Toast.makeText(AccountActivity.this, Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
}