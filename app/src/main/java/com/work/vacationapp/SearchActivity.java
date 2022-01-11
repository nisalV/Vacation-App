package com.work.vacationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    ListView listView;
    TextView place;
    double latitude, longitude;
    String address;
    ArrayList<String> list2;

    List<Add> lstData;

    FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        place = findViewById(R.id.place);
        listView = findViewById(R.id.list2);

        lstData = new ArrayList<>();

        Bundle extras = getIntent().getExtras();
        latitude = extras.getDouble("latitude");
        longitude = extras.getDouble("longitude");
        address = extras.getString("address");

        place.setText(address);

        database = FirebaseFirestore.getInstance();
        Task query = database.collection("Advertisements").whereGreaterThan("Lat", latitude-0.001).whereLessThan("Lat", latitude+0.001).orderBy("Lat").get();
        Task<List<QuerySnapshot>> allTask = Tasks.whenAllSuccess(query);
        allTask.addOnSuccessListener(querySnapshots -> {
            list2 = new ArrayList<>();
            for (QuerySnapshot queryDocumentSnapshots : querySnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Add advertisement = documentSnapshot.toObject(Add.class);
                    if (advertisement.getLon() >= (longitude-0.01) && advertisement.getLon() < (longitude+0.01)) {
                        advertisement.setId(documentSnapshot.getId());
                        lstData.add(advertisement);
                        list2.add(advertisement.getId());

                        listView.setOnItemClickListener((parent, view, position, id) -> {
                            Intent intent3 = new Intent(SearchActivity.this,OneAddActivity.class);
                            intent3.putExtra("searchId",list2.get(position));
                            startActivity(intent3);
                        });
                    }
                }
                ListAdapter adapter = new ListAdapter(SearchActivity.this,R.layout.advertisement, (ArrayList<Add>) lstData);
                listView.setAdapter(adapter);
            }

        }).addOnFailureListener(e -> {
        });

    }
}