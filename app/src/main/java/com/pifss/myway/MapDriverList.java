package com.pifss.myway;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pifss.myway.direction.Navigator;

public class MapDriverList extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_driver_list);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent i = getIntent();

        String lat = i.getExtras().getString("myCoorLat");
        String lng = i.getExtras().getString("myCoorLng");

        Log.d("bader","lat is: " + lat + " lng is: "+ lng);

        // Add a marker in Sydney and move the camera
        final LatLng driverDest = new LatLng(Float.parseFloat(lat), Float.parseFloat(lng));
        mMap.addMarker(new MarkerOptions().position(driverDest).title("Marker for Driver"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(driverDest));

        MyLocation.LocationResult locationResult = new MyLocation.LocationResult() {
            @Override
            public void gotLocation(Location location) {
                //Got the location!

                Navigator nav = new Navigator(mMap,new LatLng(location.getLatitude(),location.getLongitude()),driverDest);
                nav.findDirections(false);

            }
        };
        MyLocation myLocation = new MyLocation();
        myLocation.getLocation(MapDriverList.this, locationResult);



    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;




    }
}
