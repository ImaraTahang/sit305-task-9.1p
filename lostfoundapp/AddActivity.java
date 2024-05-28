package com.example.lostfoundapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.Manifest;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.FormatFlagsConversionMismatchException;
import java.util.List;

public class AddActivity extends AppCompatActivity {

    private RadioGroup radioGroup;
    private RadioButton radioLost, radioFound;
    private EditText editTextName, editTextPhone, editTextDescription, editTextDate, editTextLocation;
    private Button saveButton;

    private Button buttonGetCurrentLocation;

    private List<LostFoundItem> lostFoundItemList;
    private SharedPreferences sharedPreferences;
    private Gson gson;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add);


        radioGroup = findViewById(R.id.radioGroup);
        radioLost = findViewById(R.id.radioLost);
        radioFound = findViewById(R.id.radioFound);
        editTextName = findViewById(R.id.editTextName);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextDate = findViewById(R.id.editTextDate);
        editTextLocation = findViewById(R.id.editTextLocation);
        buttonGetCurrentLocation = findViewById(R.id.buttonGetCurrentLocation);
        saveButton = findViewById(R.id.saveButton);

        sharedPreferences=getSharedPreferences("LostFoundPrefs", MODE_PRIVATE);
        gson = new Gson();


        String json = sharedPreferences.getString("lostFoundList", null);
        Type type=new TypeToken<ArrayList<LostFoundItem>>(){}.getType();
        lostFoundItemList = json != null ? gson.fromJson(json, type) : new ArrayList<>();

        editTextLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AddActivity.this, AutocompleteActivity.class);
                startActivity(intent);
            }
        });

        buttonGetCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentLocation();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String postType=radioLost.isChecked()?"Lost":"Found";
                String name = editTextName.getText().toString();
                String phone = editTextPhone.getText().toString();
                String description = editTextDescription.getText().toString();
                String date = editTextDate.getText().toString();
                String location = editTextLocation.getText().toString();

                LostFoundItem item=new LostFoundItem(postType, name, phone, description, date, location);
                lostFoundItemList.add(item);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                String updatedJson = gson.toJson(lostFoundItemList);
                editor.putString("lostFoundList", updatedJson);
                editor.apply();

                Intent intent = new Intent(AddActivity.this, ViewActivity.class);
                intent.putExtra("POST_TYPE", postType);
                intent.putExtra("NAME", name);
                intent.putExtra("PHONE", phone);
                intent.putExtra("DESCRIPTION", description);
                intent.putExtra("DATE", date);
                intent.putExtra("LOCATION", location);

                startActivity(intent);
            }
        });
    }
    private void getCurrentLocation() {
        // Check for location permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Request location permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            // Fetch current location
            FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                // Convert location to string and set in editTextLocation
                                String locationString = location.getLatitude() + ", " + location.getLongitude();
                                editTextLocation.setText(locationString);
                            }
                        }
                    });
        }
    }
}