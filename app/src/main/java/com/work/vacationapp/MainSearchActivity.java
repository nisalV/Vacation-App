package com.work.vacationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MainSearchActivity extends AppCompatActivity {

    TextView sId,hName,hAddress,hDescription,hCall,hEmail;
    CardView card;
    FloatingActionButton locationButton,callButton;
    ImageButton toEmail,toCall;
    ImageView image1,image2,image3;
    ProgressBar progress3;
    String id;


    GoogleMap map;
    ArrayList<Double> cordList;
    ArrayList<String> place;
    FirebaseFirestore database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_search);

        hName = findViewById(R.id.hName);
        hAddress = findViewById(R.id.hAddress);
        hCall = findViewById(R.id.hCall);
        hEmail = findViewById(R.id.hEmail);
        image1 = findViewById(R.id.image1);
        image2 = findViewById(R.id.image2);
        image3 = findViewById(R.id.image3);
        card = findViewById(R.id.card);
        locationButton = findViewById(R.id.locationButton);
        callButton = findViewById(R.id.callButton);
        toEmail = findViewById(R.id.toEmail);
        toCall = findViewById(R.id.toCall);
        hDescription = findViewById(R.id.hDescription);
        progress3 = findViewById(R.id.progress3);

        progress3.setVisibility(View.VISIBLE);
        card.setVisibility(View.INVISIBLE);

        Bundle extras1 = getIntent().getExtras();
        id = extras1.getString("addId");
        //sId.setText(id);

        cordList = new ArrayList<>();
        place = new ArrayList<>();
        database = FirebaseFirestore.getInstance();
        Task<DocumentSnapshot> query = database.collection("Advertisements").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    Advertisement advertisement = documentSnapshot.toObject(Advertisement.class);
                    assert advertisement != null;
                    if(advertisement.getPhoto1() != null){
                        Picasso.get().load(advertisement.getPhoto1() ).into(image1);
                    }
                    if(advertisement.getPhoto2() != null){
                        Picasso.get().load(advertisement.getPhoto2()).into(image2);
                    }
                    if(advertisement.getPhoto3() != null){
                        Picasso.get().load(advertisement.getPhoto3()).into(image3);
                    }

                    hName.setText(advertisement.getName());
                    hAddress.setText(advertisement.getAddress());
                    hDescription.setText(advertisement.getDescription());
                    cordList.add(advertisement.getLat());
                    cordList.add(advertisement.getLon());
                    place.add(advertisement.getName());
                    place.add(advertisement.getPhone());
                    if(advertisement.getPhone() != null) {
                        hCall.setText(advertisement.getPhone());
                    }else{
                        hCall.setText("no phone number provided");
                    }
                    if(advertisement.getEmail() != null) {
                        hEmail.setText(advertisement.getEmail());
                    }else{
                        hEmail.setText("no email address provided");
                    }

                }
            }
        }).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                progress3.setVisibility(View.INVISIBLE);
                card.setVisibility(View.VISIBLE);
                toCall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (hCall.getText() == "no phone number provided"){
                            Toast.makeText(MainSearchActivity.this, "no phone number provided", Toast.LENGTH_SHORT).show();
                        }else{
                            ClipboardManager clipboard1 = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData clip1 = ClipData.newPlainText("phone",hCall.getText().toString());
                            clipboard1.setPrimaryClip(clip1);
                            Toast.makeText(MainSearchActivity.this, "Phone number copied to clipboard", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                toEmail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (hEmail.getText() == "no email address provided"){
                            Toast.makeText(MainSearchActivity.this, "no email address provided", Toast.LENGTH_SHORT).show();
                        }else {
                            ClipboardManager clipboard2 = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData clip2 = ClipData.newPlainText("email", hEmail.getText().toString());
                            clipboard2.setPrimaryClip(clip2);
                            Toast.makeText(MainSearchActivity.this, "Email copied to clipboard", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                String geo = "http://maps.google.com/maps?q=loc:"+cordList.get(0).toString()+","+cordList.get(1).toString();
                locationButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geo));
                        Intent locate = Intent.createChooser(intent,place.get(0));
                        try {
                            startActivity(locate);
                        } catch (ActivityNotFoundException e) {
                            e.printStackTrace();
                            Toast.makeText(MainSearchActivity.this, "a maps application needed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                String tel = "tel:"+place.get(1);
                callButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(place.get(1) == null){
                            Toast.makeText(MainSearchActivity.this, "Phone number not provided", Toast.LENGTH_SHORT).show();
                        }else {
                            Intent intent2 = new Intent(Intent.ACTION_DIAL);
                            intent2.setData(Uri.parse(tel));
                            startActivity(intent2);
                        }
                    }
                });
            }

        });
    }
}