package com.work.vacationapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener, FirestoreAdapter.OnListItemClick {

    EditText editText;
    RecyclerView realtimeList;

    ProgressBar progress2;

    FirebaseFirestore database;
    FirestoreAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.edit_text);
        realtimeList = findViewById(R.id.realtimeList);
        progress2 = findViewById(R.id.progress2);
        progress2.setVisibility(View.INVISIBLE);

        Places.initialize(getApplicationContext(), "AIzaSyD237Uwi3Y8FBUrc11Y1rRbWU7p3HDRb3A");

        editText.setFocusable(false);
        editText.setOnClickListener(v -> {
            List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME);
            Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList).build(MainActivity.this);
            startActivityForResult(intent, 100);
        });

        ImageButton btn = findViewById(R.id.button);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            btn.setOnClickListener(v -> {
                PopupMenu popup = new PopupMenu(MainActivity.this, v);
                popup.setOnMenuItemClickListener(MainActivity.this);
                popup.inflate(R.menu.menu_layout);
                popup.show();
            });
        } else if (auth.getCurrentUser() != null) {
            btn.setOnClickListener(v -> {
                PopupMenu popup2 = new PopupMenu(MainActivity.this, v);
                popup2.setOnMenuItemClickListener(MainActivity.this);
                popup2.inflate(R.menu.menu2_layout);
                popup2.show();
            });
        }

        database = FirebaseFirestore.getInstance();
        PagedList.Config config = new  PagedList.Config.Builder().setInitialLoadSizeHint(5).setPageSize(3).build();
        Query query = database.collection("Advertisements");

        FirestorePagingOptions<Advertisement> options = new FirestorePagingOptions.Builder<Advertisement>().setLifecycleOwner(this).setQuery(query, config, snapshot -> {
            Advertisement advertisement = snapshot.toObject(Advertisement.class);
            String placeId = snapshot.getId();
            assert advertisement != null;
            advertisement.setId(placeId);
            return advertisement;
        }).build();

        adapter = new FirestoreAdapter(options, this);
        realtimeList.setHasFixedSize(true);
        realtimeList.setLayoutManager(new LinearLayoutManager(this));
        realtimeList.setAdapter(adapter);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK) {
            assert data != null;
            Place place = Autocomplete.getPlaceFromIntent(data);
            Intent intent = new Intent(MainActivity.this,SearchActivity.class);
            intent.putExtra("latitude",Objects.requireNonNull(place.getLatLng()).latitude);
            intent.putExtra("longitude",Objects.requireNonNull(place.getLatLng()).longitude);
            intent.putExtra("address",place.getAddress());
            startActivity(intent);
        } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
            assert data != null;
            Status status = Autocomplete.getStatusFromIntent(data);
            Toast.makeText(getApplicationContext(), status.getStatusMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.signIn:
                Intent settingsActivity = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(settingsActivity);
                return true;
            case R.id.settings:
                Toast.makeText(this, "settings", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.add:
                Intent goToAdActivity = new Intent(MainActivity.this, AdActivity.class);
                startActivity(goToAdActivity);
                return true;
            case R.id.account:
                Intent goToAccountActivity = new Intent(MainActivity.this, AccountActivity.class);
                startActivity(goToAccountActivity);
                return true;
            case R.id.signOut:
                FirebaseAuth.getInstance().signOut();
                Intent mainActivity = new Intent(MainActivity.this, MainActivity.class);
                startActivity(mainActivity);
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onItemClick(DocumentSnapshot snapshot, int position) {
        Log.d("item click",+position+"  id: "+snapshot.getId());
        Intent intent2 = new Intent(MainActivity.this,MainSearchActivity.class);
        intent2.putExtra("addId",snapshot.getId());
        startActivity(intent2);
    }
}