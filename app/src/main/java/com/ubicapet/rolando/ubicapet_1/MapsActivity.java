package com.ubicapet.rolando.ubicapet_1;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class MapsActivity extends FragmentActivity implements LocationListener{

    public static final String TAG = RegisterPetActivity.class.getSimpleName();
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    protected ParseUser mCurrentUser;
    UiSettings mapSettings;
    private LatLng MUSEUM;
    private LatLng mLocation, adsLocation;
    private ParseGeoPoint mLocat;
    private LocationManager locationManager;
    ParseQuery <ParseObject>query;
    private static final int MAX_POST_SEARCH_DISTANCE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        mCurrentUser = ParseUser.getCurrentUser();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
        mapSettings = mMap.getUiSettings();



        try {
            if (mMap !=null){


                mMap.setMyLocationEnabled(true);
                mapSettings.setCompassEnabled(true);
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                MyLocation();
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLocation, 10));



            }
        }

        catch (Exception e){
            Log.e(TAG, getString(R.string.error_e), e);
        }



    }

    private void MyLocation() {
        try {
            // Get the LocationManager object from the System Service LOCATION_SERVICE
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

// Create a criteria object needed to retrieve the provider
            Criteria criteria = new Criteria();

// Get the name of the best available provider
            String provider = locationManager.getBestProvider(criteria, true);

// We can use the provider immediately to get the last known location
            Location location = locationManager.getLastKnownLocation(provider);

// request that the provider send this activity GPS updates every 10 seconds
            locationManager.requestLocationUpdates(provider, 200000, 0, this);


            mLocation = new LatLng(location.getLatitude(), location.getLongitude());
            mLocat = new ParseGeoPoint(location.getLatitude(), location.getLongitude());
            //Toast toast = Toast.makeText(this, mLocation+"", Toast.LENGTH_LONG);
            //toast.show();

        }
        catch (Exception e){
            Toast toast = Toast.makeText(this, getString(R.string.error_ubicacion), Toast.LENGTH_LONG);
            toast.show();
            Log.e(TAG, getString(R.string.error_e), e);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();

    }


    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();


            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        try {

            query = ParseQuery.getQuery("Advertise");



            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> avisos, ParseException e) {

                    if (e == null) {


                        for (ParseObject ads : avisos) {
                            String titulo = ads.getString("Titulo");
                            String aviso = ads.getString("advertice");
                            ParseGeoPoint position = ads.getParseGeoPoint("location");
                            adsLocation = new LatLng(position.getLatitude(), position.getLongitude());

                            Marker av = mMap.addMarker(new MarkerOptions()
                                    .position(adsLocation)
                                    .title(titulo)
                                    .snippet(aviso));
                        }


                    }


                }
            });
        }
        catch (Exception e){
            Toast toast = Toast.makeText(this, getString(R.string.error_marcadores), Toast.LENGTH_LONG);
            toast.show();
            Log.e(TAG, getString(R.string.ocurrio_error), e);
        }


    }

    @Override
    public void onLocationChanged(Location location) {


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
