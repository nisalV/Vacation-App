package com.work.vacationapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AdActivity extends AppCompatActivity {

    TextView getLocation;
    EditText name, address, description, phone, email;
    Button send;
    ImageView searchButton1,searchButton2,searchButton3,searchButton4;
    ProgressBar progressBar;
    int value1,value2,value3,value4;
    double latitude,longitude;
    String lat,lon;
    private Uri imageUri1,imageUri2,imageUri3,imageUri4;
    FusedLocationProviderClient fusedLocationProviderClient;

    CollectionReference userData;
    DocumentReference photoData;
    private final StorageReference reference = FirebaseStorage.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad);

        getLocation = findViewById(R.id.location);
        send = findViewById(R.id.send);
        progressBar = findViewById(R.id.progress);
        searchButton1 = findViewById(R.id.searchButton1);
        searchButton2 = findViewById(R.id.searchButton2);
        searchButton3 = findViewById(R.id.searchButton3);
        searchButton4 = findViewById(R.id.searchButton4);
        name = findViewById(R.id.PlaceName);
        address = findViewById(R.id.PlaceAddress);
        description = findViewById(R.id.descriptionDetail);
        phone = findViewById(R.id.phoneNo);
        email = findViewById(R.id.email);

        progressBar.setVisibility(View.INVISIBLE);

        userData = FirebaseFirestore.getInstance().collection("Advertisements");

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(AdActivity.this);

        searchButton1.setOnClickListener(v -> {
            value1 = 1;
            value2 = 0;
            value3 = 0;
            value4 = 0;
            ImagePicker.Companion.with(AdActivity.this).cropSquare().start();
        });
        searchButton2.setOnClickListener(v -> {
            value1 = 0;
            value2 = 1;
            value3 = 0;
            value4 = 0;
            ImagePicker.Companion.with(AdActivity.this).cropSquare().start();
        });
        searchButton3.setOnClickListener(v -> {
            value1 = 0;
            value2 = 0;
            value3 = 1;
            value4 = 0;
            ImagePicker.Companion.with(AdActivity.this).cropSquare().start();
        });
        searchButton4.setOnClickListener(v -> {
            value1 = 0;
            value2 = 0;
            value3 = 0;
            value4 = 1;
            ImagePicker.Companion.with(AdActivity.this).crop(9f,16f).start();
        });

        getLocation.setOnClickListener(v -> {
            if(ActivityCompat.checkSelfPermission(AdActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(AdActivity.this,Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                getCurrentLocation();
            }else {
                ActivityCompat.requestPermissions(AdActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},100);
            }
        });

        send.setOnClickListener(v -> {

            String txt_name = name.getText().toString();
            String txt_address = address.getText().toString();
            String txt_description = description.getText().toString();
            String txt_phone = phone.getText().toString();
            String txt_email = email.getText().toString();

            if (txt_name.isEmpty() || txt_description.isEmpty() || txt_address.isEmpty() || txt_phone.isEmpty() || txt_email.isEmpty()) {
                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show();
            } else if (lat == null || lon == null) {
                Toast.makeText(this, "Obtain the location", Toast.LENGTH_SHORT).show();
            } else if (imageUri4 == null) {
                Toast.makeText(this, "Upload unsuccessful, please add a photo of licence document", Toast.LENGTH_SHORT).show();
            } else {
                progressBar.setVisibility(View.VISIBLE);
                photoData = FirebaseFirestore.getInstance().collection("Advertisements").document(lat+"_"+lon);

                Map<String, Object> map = new HashMap<>();
                map.put("Name", txt_name);
                map.put("Address", txt_address);
                map.put("Description", txt_description);
                map.put("Phone", txt_phone);
                map.put("Email", txt_email);
                map.put("Lat", latitude);
                map.put("Lon", longitude);
                userData.document(lat+"_"+lon).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        if (imageUri1 != null) {
                            StorageReference fileRef = reference.child(lat+"_"+lon).child(String.valueOf(System.currentTimeMillis()));
                            fileRef.putFile(imageUri1).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri1) {
                                            String photo1 = uri1.toString();
                                            Map<String, Object> map1 = new HashMap<>();
                                            map1.put("Photo1",photo1);
                                            photoData.update(map1);
                                        }
                                    });
                                }
                            });
                        }
                        if (imageUri2 != null) {
                            StorageReference fileRef = reference.child(lat+"_"+lon).child(String.valueOf(System.currentTimeMillis()));
                            fileRef.putFile(imageUri2).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri2) {
                                            String photo2 = uri2.toString();
                                            Map<String, Object> map2 = new HashMap<>();
                                            map2.put("Photo2",photo2);
                                            photoData.update(map2);
                                        }
                                    });
                                }
                            });
                        }
                        if (imageUri3 != null) {
                            StorageReference fileRef = reference.child(lat+"_"+lon).child(String.valueOf(System.currentTimeMillis()));
                            fileRef.putFile(imageUri3).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri3) {
                                            String photo3 = uri3.toString();
                                            Map<String, Object> map3 = new HashMap<>();
                                            map3.put("Photo3",photo3);
                                            photoData.update(map3);
                                        }
                                    });
                                }
                            });
                        }
                        if (imageUri4 != null) {
                            StorageReference fileRef = reference.child(lat+"_"+lon).child(String.valueOf(System.currentTimeMillis()));
                            fileRef.putFile(imageUri4).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri4) {
                                            String photo4 = uri4.toString();
                                            Map<String, Object> map4 = new HashMap<>();
                                            map4.put("Photo4",photo4);
                                            photoData.update(map4);
                                        }
                                    });
                                }
                            });
                        }
                    }
                }).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(AdActivity.this, "Completed", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AdActivity.this, MainActivity.class));
                    }
                });
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults.length > 0 && (grantResults[0] + grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
            getCurrentLocation();
        } else {
            Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint({"MissingPermission", "SetTextI18n"})
    private void getCurrentLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(task -> {
                Location location = task.getResult();
                if (location != null){
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    lat = String.valueOf(latitude).replace(".",",");
                    lon = String.valueOf(longitude).replace(".",",");
                    getLocation.setText("Your location is obtained!");
                    Toast.makeText(AdActivity.this, "Your current location is obtained", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(this, "Your current location cannot be obtained!", Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        assert data != null;
        LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(500, 500);

        if(value1 == 1){
            imageUri1 = data.getData();
            if(imageUri1 != null){
                searchButton1.setImageURI(imageUri1);
                searchButton1.setLayoutParams(param2);
            }
        }
        if(value2 == 1){
            imageUri2 = data.getData();
            if(imageUri2 != null){
                searchButton2.setImageURI(imageUri2);
                searchButton2.setLayoutParams(param2);
            }
        }
        if(value3 == 1){
            imageUri3 = data.getData();
            if(imageUri3 != null){
                searchButton3.setImageURI(imageUri3);
                searchButton3.setLayoutParams(param2);
            }
        }
        if(value4 == 1){
            imageUri4 = data.getData();
            if(imageUri4 != null){
                searchButton4.setImageURI(imageUri4);
                searchButton4.setBackground(null);
                searchButton4.setLayoutParams(param2);
            }
        }
    }
}