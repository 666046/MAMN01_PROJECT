package com.example.sustaintrain;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.sustaintrain.directionhelpers.FetchURL;
import com.example.sustaintrain.directionhelpers.TaskLoadedCallback;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private GoogleMap mMap;
    private MarkerOptions place1, place2, place3, currentPosition;
    private RadioButton route1, route2, route3;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;
    private Polyline currentPolyline;
    private Location currentLocation;
    private Button pickRoute;
    private LatLng testLatLng;
    private GoogleApiClient googleApiClient;
    double lat1, lng1, lat2, lng2, lat3, lng3;
    double totalLat, totalLng;
    private boolean oneTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);
        oneTime=true;

        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {

                           currentPosition = new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title("Current Location");
                        }
                    }
                });


        route1 = (RadioButton) findViewById(R.id.btnRoute1);
        route2 =(RadioButton) findViewById(R.id.btnRoute2);
        route3= (RadioButton) findViewById(R.id.btnRoute3);
        pickRoute= (Button) findViewById(R.id.pickRoute);



        currentPosition = new MarkerOptions().position(new LatLng(55.71111, 13.210267)).title("Current Location");



        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.RGroup);

        if(oneTime==true){
            route2.setChecked(true);
            oneTime=false;
        }


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.btnRoute1) {
                    new FetchURL(MapsActivity.this).execute(getUrl(currentPosition.getPosition(), place1.getPosition(), "walking"), "walking");
                  //  totalLat = Math.abs(currentPosition.getPosition().latitude - lat1);
                }
                else if(checkedId == R.id.btnRoute2){
                    new FetchURL(MapsActivity.this).execute(getUrl(currentPosition.getPosition(), place3.getPosition(), "walking"), "walking");

                }
                else if(checkedId == R.id.btnRoute3){
                    new FetchURL(MapsActivity.this).execute(getUrl(currentPosition.getPosition(), place2.getPosition(), "walking"), "walking");

                }


            }


        });

        pickRoute.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                    openRoute();
            }
        });




        //27.658143,85.3199503
        //27.667491,85.3208583

        place1 = new MarkerOptions().position(new LatLng(55.710982, 13.208511)).title("Studie Centrum");
        place2 = new MarkerOptions().position(new LatLng(55.711804, 13.210109)).title("John Ericssons väg");
        place3 = new MarkerOptions().position(new LatLng(55.709957, 13.208661)).title("Grönt o' Gott");
        /*lat1 = place1.getPosition().latitude;
        lng1 = place1.getPosition().longitude;
        lat2 = place2.getPosition().latitude;
        lng2 = place2.getPosition().longitude;
        lat3 = place3.getPosition().latitude;
        lng3 = place3.getPosition().longitude;*/



        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapNearBy);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Log.d("mylog", "Added Markers");
        LatLng latLng = new LatLng(55.710976, 13.209676);
        MarkerOptions markerOptions = new MarkerOptions().position(latLng)
                .title("HERE");
        markerOptions.visible(false);

        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
        googleMap.addMarker(markerOptions);
        mMap.addMarker(place1);
        mMap.addMarker(place2);
        mMap.addMarker(place3);
    }


    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        return url;
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }


    public void openRoute(){
        Intent intent = new Intent(this, StepCounter.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        startActivity(intent);
        finish();
    }


    @Override
    protected void onStart(){
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop(){
        if(googleApiClient.isConnected()){
            googleApiClient.disconnect();
        }
        super.onStop();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {



        if(ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            requestPermission();
        }
        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if(location != null){

                            testLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                            currentPosition = new MarkerOptions().position(testLatLng).title("Current Location");

                        }
                    }
                });
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(MapsActivity.this, new
                String[]{ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
