package com.example.lostfoundapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.content.SharedPreferences;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        startActivity(new Intent(MapActivity.this, AutocompleteActivity.class));
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        LatLng sydney = new LatLng(-33.8688, 151.2093);
        LatLng melbourne = new LatLng(-37.8136, 144.9631);

        googleMap.addMarker(new MarkerOptions()
                .title("Sydney Marker")
                .position(sydney));

        googleMap.addMarker(new MarkerOptions()
                .title("Melbourne Marker")
                .position(melbourne));

        Polyline polyline = googleMap.addPolyline(new PolylineOptions()
                .clickable(true)
                .add(sydney, melbourne));

        polyline.setColor(Color.BLUE);

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 7));
    }
}