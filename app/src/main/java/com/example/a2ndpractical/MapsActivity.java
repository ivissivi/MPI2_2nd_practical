package com.example.a2ndpractical;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.a2ndpractical.databinding.ActivityMapsBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Objects;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    class Marker {
        public String title;
        public String desc;
        public double lat;
        public double lng;

        public Marker (String title, String desc, double lat, double lng) {
            this.title = title;
            this.desc = desc;
            this.lat = lat;
            this.lng = lng;
        }
    }

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private GoogleMap gMap;
    private final String userLocTitle = "I am here";
    private LatLng currLoc;
    private HashMap<String, Marker> markers = new HashMap<>();
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.example.a2ndpractical.databinding.ActivityMapsBinding binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        this.fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.repo) {
            Intent repoActivity = new Intent(this, RepositoryActivity.class);
            startActivity(repoActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gMap = googleMap;
        gMap.setOnMarkerClickListener(marker -> {
            if(!Objects.equals(marker.getTitle(), userLocTitle)) {
                Marker markObj = markers.get(marker.getTitle());
                assert markObj != null;
                new AlertDialog.Builder(MapsActivity.this)
                        .setTitle(markObj.title)
                        .setMessage(markObj.desc)
                        .setPositiveButton("Ok", (dialog, which) -> dialog.dismiss())
                        .show();
            }
            return false;
        });
        this.getCurrentLocation();
    }
    private void getCurrentLocation () {
        if (ActivityCompat.checkSelfPermission(this.getApplicationContext(),
                ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    ACCESS_FINE_LOCATION, true);
        }
        if (ActivityCompat.checkSelfPermission(this.getApplicationContext(),
                ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    ACCESS_COARSE_LOCATION, true);
        }
        this.fusedLocationClient
            .getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, null)
            .addOnSuccessListener(this, location -> {
                if (location != null) {
                    this.currLoc = new LatLng(location.getLatitude(), location.getLongitude());
                    this.markNearbyLocations();
                    this.markCurrentLocation();
                } else {
                    Toast.makeText(this, "Unable to find location", Toast.LENGTH_SHORT).show();
                }
        });
    }
    private void markCurrentLocation () {
        if(this.currLoc != null) {
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(this.currLoc)
                    .title(userLocTitle)
                    .icon(fromDrawableToBitmap(R.drawable.ic_baseline_user_location));
            gMap.animateCamera(CameraUpdateFactory.newLatLng(this.currLoc));
            gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(this.currLoc, 5));
            gMap.addMarker(markerOptions).setZIndex(Float.MAX_VALUE);
        }
    }

    private BitmapDescriptor fromDrawableToBitmap(int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(this, vectorResId);
        assert vectorDrawable != null;
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(
                vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(
                Bitmap.createScaledBitmap(
                        bitmap,
                        vectorDrawable.getIntrinsicWidth() + 30,
                        vectorDrawable.getIntrinsicHeight() + 30,
                        false));
    }
    private void markNearbyLocations () {
        if(this.currLoc != null) {
            this.createLocationMarker(R.string.Place1Title, R.string.Place1Descr, R.string.Place1LngLat);
            this.createLocationMarker(R.string.Place2Title, R.string.Place2Descr, R.string.Place2LngLat);
            this.createLocationMarker(R.string.Place3Title, R.string.Place3Descr, R.string.Place3LngLat);
            this.createLocationMarker(R.string.Place4Title, R.string.Place4Descr, R.string.Place4LngLat);
            this.createLocationMarker(R.string.Place5Title, R.string.Place5Descr, R.string.Place5LngLat);
            this.createLocationMarker(R.string.Place6Title, R.string.Place6Descr, R.string.Place6LngLat);
            this.createLocationMarker(R.string.Place8Title, R.string.Place8Descr, R.string.Place8LngLat);
            this.createLocationMarker(R.string.Place9Title, R.string.Place9Descr, R.string.Place9LngLat);
            this.createLocationMarker(R.string.Place10Title, R.string.Place10Descr, R.string.Place10LngLat);
            this.createLocationMarker(R.string.Place11Title, R.string.Place11Descr, R.string.Place11LngLat);
            this.createLocationMarker(R.string.Place12Title, R.string.Place12Descr, R.string.Place12LngLat);
            this.createLocationMarker(R.string.Place13Title, R.string.Place13Descr, R.string.Place13LngLat);
            this.createLocationMarker(R.string.Place14Title, R.string.Place14Descr, R.string.Place14LngLat);
            this.createLocationMarker(R.string.Place15Title, R.string.Place15Descr, R.string.Place15LngLat);
        }
    }
    private void createLocationMarker (int title, int descr, int lngLat) {
        String[] latLng = getString(lngLat).replaceAll("\\s+", "").split(",");
        double lat = Double.parseDouble(latLng[0]);
        double lng = Double.parseDouble(latLng[1]);
        double range = 0.5;
        boolean latCheck = this.currLoc.latitude - range <= lat && this.currLoc.latitude + range > lat;
        boolean lngCheck = this.currLoc.longitude - range <= lng && this.currLoc.longitude + range > lng;
        if(latCheck && lngCheck) {
            String titleConv = getString(title);
            String descrConv = getString(descr);
            gMap.addMarker(new MarkerOptions()
                    .position(new LatLng(lat, lng))
                    .title(titleConv)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            markers.put(titleConv, new Marker(titleConv, descrConv, lat, lng));
        }
    }
}