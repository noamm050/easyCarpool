package com.example.myapplication;


import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static com.example.myapplication.TripDetails.isPassenger;
import static com.example.myapplication.TripExecuter.originLat;
import static com.example.myapplication.TripExecuter.originLon;
import static com.example.myapplication.TripExecuter2.destinationLat;
import static com.example.myapplication.TripExecuter2.destinationLon;
import static com.example.myapplication.TripExecuter2.destinationString;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.model.DirectionsResult;

import java.util.ArrayList;
import java.util.List;

public class mapActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener, RoutingListener {

    private GoogleMap mMap;

    protected LatLng start = null;
    protected LatLng end = null;
    protected LatLng currentLocation ;

    private final static int LOCATION_REQUEST_CODE = 23;
    boolean locationPermission = false;

    private List<Polyline> polylines = null;
    private GeoApiContext geoApiContext;
    private FusedLocationProviderClient fusedLocationProviderClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLocationPermission();
    }

    /**
     * Get permission to the map to get the user's location
     */
    private void getLocationPermission() {

        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            initMap();
        } else {
            ActivityCompat.requestPermissions(this, permissions, 1234);
        }
    }

    /**
     * Initialize the map components on the Fragment in the xml through Google's API
     */
    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(mapActivity.this);
        if (geoApiContext == null) {
            geoApiContext = new GeoApiContext.Builder()
                    .apiKey("AIzaSyCmLVkevQHlO9EBoBE9TOeMx0r2cx-l8dc")
                    .build();
        }
    }


    /**
     * Get permission results to the map to get the user's locations
     * @param requestCode The request code passed in
     * @param permissions The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *     which is either {@link android.content.pm.PackageManager#PERMISSION_GRANTED}
     *     or {@link android.content.pm.PackageManager#PERMISSION_DENIED}. Never null.
     *
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case LOCATION_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //if permission granted.
                    locationPermission = true;


                }
            }
        }
    }

    /**
     * Get user's live current location (latitude and longlitude)
     * get the origin location and the destination location (latitude and longlitude)
     */
    private void getMyLocation() {

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
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(
                new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            Location location = task.getResult();
                            currentLocation = new LatLng(location.getAltitude(),
                                    location.getLongitude());
                        }
                    }
                }
        );
        start = new LatLng(originLat,originLon);
        end = new LatLng(destinationLat,destinationLon);
        Findroutes(start,end);

    }


    /**
     * Initialize Gopogl\e's map
     * @param googleMap from Google's API get map
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        getMyLocation();

    }

    /**
     *    A function that find Routes and shoes the route on the map
     *    including success failure and showing the route in the next functions
      */
    public void Findroutes(LatLng Start, LatLng End)
    {
        if(Start==null || End==null) {
            Toast.makeText(mapActivity.this,"Unable to get location", Toast.LENGTH_LONG).show();
        }
        else
        {

            Routing routing = new Routing.Builder()
                    .travelMode(AbstractRouting.TravelMode.DRIVING)
                    .withListener(this)
                    .alternativeRoutes(true)
                    .waypoints(Start, End)
                    .key("AIzaSyCmLVkevQHlO9EBoBE9TOeMx0r2cx-l8dc")
                    .build();
            routing.execute();
        }
    }

    //Routing call back functions.
    @Override
    public void onRoutingFailure(RouteException e) {
        View parentLayout = findViewById(android.R.id.content);
        Snackbar snackbar= Snackbar.make(parentLayout, e.toString(), Snackbar.LENGTH_LONG);
        snackbar.show();
//        Findroutes(start,end);
    }

    @Override
    public void onRoutingStart() {
        Toast.makeText(mapActivity.this,"Finding Route...",Toast.LENGTH_LONG).show();
    }

    /**
     * On Route success show the markers of origin and destination on the map,
     * Show the most short route on the map
     * @param route the route
     * @param shortestRouteIndex the shorthest route
     */
    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {

        CameraUpdate center = CameraUpdateFactory.newLatLng(start);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);
        if(polylines!=null) {
            polylines.clear();
        }
        PolylineOptions polyOptions = new PolylineOptions();
        LatLng polylineStartLatLng=null;
        LatLng polylineEndLatLng=null;


        polylines = new ArrayList<>();

        for (int i = 0; i <route.size(); i++) {

            if(i==shortestRouteIndex)
            {
                polyOptions.color(R.color.colorPrimary);
                polyOptions.width(7);
                polyOptions.addAll(route.get(shortestRouteIndex).getPoints());
                Polyline polyline = mMap.addPolyline(polyOptions);
                polylineStartLatLng=polyline.getPoints().get(0);
                int k=polyline.getPoints().size();
                polylineEndLatLng=polyline.getPoints().get(k-1);
                polylines.add(polyline);

            }
        }

        //Add Marker on route starting position
        MarkerOptions startMarker = new MarkerOptions();
        startMarker.position(polylineStartLatLng);
        startMarker.title("Origin");
        mMap.addMarker(startMarker);
        Toast.makeText(this,
                "\t\tPress Marker\nthen Press little Arrow to start navigation" ,
                Toast.LENGTH_LONG).show();

        /**
         * Add Marker on route ending position Just for the driver
         * Passengers can route to Driver's start trip location
         */
        if(!isPassenger) {
            MarkerOptions endMarker = new MarkerOptions();
            endMarker.position(polylineEndLatLng);
            endMarker.title("Destination");
            mMap.addMarker(endMarker);
        }

        LatLng latLng = start ;
        float z = 9f ;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, z));

        // if User is passenger navigate to the Driver's Position
        // if User is driver navigate to destination
        if (isPassenger)
            calculateDirections(startMarker);

    }

    @Override
    public void onRoutingCancelled() {
        Findroutes(start,end);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Findroutes(start,end);

    }

    /**
     * * Navigation : Open the opportunity of navigation through Google Maps API
     * @param marker the markers of Origin and Destination provided above
     */
    private void calculateDirections(MarkerOptions marker){
        Log.d(TAG, "calculateDirections: calculating directions.");

        com.google.maps.model.LatLng destination = new com.google.maps.model.LatLng(
                marker.getPosition().latitude,
                marker.getPosition().longitude
        );
        DirectionsApiRequest directions = new DirectionsApiRequest(geoApiContext);

        directions.alternatives(false);

        if (isPassenger) {

            directions.origin(
                    new com.google.maps.model.LatLng( /**
                     Live view will put myLiveLocation
                     */
                            currentLocation.latitude,
                            currentLocation.longitude

            ));
        }
        else {
            directions.origin(
                    new com.google.maps.model.LatLng( /**
                     Live view will put myLiveLocation
                     */
                            originLat,
                            originLon
                    )
            );
        }
        directions.destination(destination).setCallback(new PendingResult.Callback<DirectionsResult>() {
            @Override
            public void onResult(DirectionsResult result) {

            }

            @Override
            public void onFailure(Throwable e) {
                Log.e(TAG, "calculateDirections: Failed to get directions: " + e.getMessage() );

            }
        });
    }

    /**
     * Finish the trip and move out of the map
     * @param v for xml
     */
    public void finishPress(View v) {
        if(isPassenger) {
            Intent intent = new Intent(this, Finish.class);
            startActivity(intent);
        }
        else {
            Toast.makeText(this, "TRIP FINISHED", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, mainPage.class);
            startActivity(intent);
        }
    }
}



