package com.example.finelspruject;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private LibraryDatabaseHelper dbHelper;
    private List<Library> libraries;
    Button btnMyLocation, btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Initialize Google Maps
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Initialize database helper
        dbHelper = new LibraryDatabaseHelper(this);

        // Fetch libraries
        libraries = dbHelper.getAllLibraries();

        // Initialize location client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        btnMyLocation = findViewById(R.id.btnMyLocation);
        btnBack = findViewById(R.id.btnBack);
        String username = getIntent().getStringExtra("username");
        if (username == null) {
            Log.e("AdminPanelActivity", "Username is null");
            Toast.makeText(this, "Error: Username not found. Please log in again.", Toast.LENGTH_SHORT).show();
            finish(); // Exit to prevent undefined behavior
            return;
        }


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapsActivity.this, DashboardActivity.class);
                intent.putExtra("username", username); // Pass the username
                startActivity(intent);
                startActivity(intent);
            }
        });

        btnMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                        if (location != null) {
                            LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15));
                        } else {
                            Toast.makeText(MapsActivity.this, "Unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                }
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Enable My Location if permission is granted
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        mMap.setOnMarkerClickListener(marker -> {
            String uri = "google.navigation:q=" + marker.getPosition().latitude + "," + marker.getPosition().longitude;
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            intent.setPackage("com.google.android.apps.maps");
            startActivity(intent);
            return false;
        });

        // Add library markers
        addLibraryMarkers();
    }

    private void addLibraryMarkers() {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        for (Library library : libraries) {
            try {
                List<Address> addresses = geocoder.getFromLocationName(library.getLocation(), 1);
                if (addresses != null && !addresses.isEmpty()) {
                    Address address = addresses.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(latLng).title(library.getName()));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Handle permission results
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                }
            }
        }
    }
    private void sortLibrariesByDistance(Location currentLocation) {
        if (currentLocation == null) return;

        Collections.sort(libraries, new Comparator<Library>() {
            @Override
            public int compare(Library lib1, Library lib2) {
                float[] result1 = new float[1];
                float[] result2 = new float[1];

                try {
                    Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
                    List<Address> address1 = geocoder.getFromLocationName(lib1.getLocation(), 1);
                    List<Address> address2 = geocoder.getFromLocationName(lib2.getLocation(), 1);

                    if (!address1.isEmpty() && !address2.isEmpty()) {
                        Location.distanceBetween(currentLocation.getLatitude(), currentLocation.getLongitude(),
                                address1.get(0).getLatitude(), address1.get(0).getLongitude(), result1);

                        Location.distanceBetween(currentLocation.getLatitude(), currentLocation.getLongitude(),
                                address2.get(0).getLatitude(), address2.get(0).getLongitude(), result2);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return Float.compare(result1[0], result2[0]);
            }
        });

        addLibraryMarkers(); // Refresh markers after sorting
    }
}
