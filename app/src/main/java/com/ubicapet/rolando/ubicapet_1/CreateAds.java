package com.ubicapet.rolando.ubicapet_1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class CreateAds extends Activity implements LocationListener{
    public static final String TAG = RegisterPetActivity.class.getSimpleName();
    protected ParseUser mCurrentUser;
    ParseGeoPoint mLocation;
    private LocationManager locationManager;
    protected EditText mTitulo;
    protected EditText mName;
    protected String Type;
    protected EditText mAds;
    protected Button mCreateButton;
    protected Spinner spinner, spinner2;
    protected ArrayList<String> nombres;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ads);

        mCurrentUser = ParseUser.getCurrentUser();


        mTitulo = (EditText)findViewById(R.id.editText_titulo);
        mName = (EditText)findViewById(R.id.name_Text);
        spinner = (Spinner) findViewById(R.id.spinner_tipo);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.Tipo_avisos, android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Type=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mAds = (EditText)findViewById(R.id.AdsText);

        mCreateButton = (Button)findViewById(R.id.button);
        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titulo = mTitulo.getText().toString();
                String Name = mName.getText().toString();
                String Ads = mAds.getText().toString();


                titulo = titulo.trim();
                Name = Name.trim();
                Type = Type.trim();
                Ads = Ads.trim();

                if (titulo.isEmpty() || Ads.isEmpty()|| Name.isEmpty() ) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CreateAds.this);
                    builder.setMessage(R.string.signup_error_message)
                            .setTitle(R.string.signup_error_title)
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {

                        MyLocation();

                        // crear aviso
                        ParseObject advertise= new ParseObject("Advertise");
                    advertise.put("Titulo", titulo);
                        advertise.put("PetName", Name);
                        advertise.put("type", Type);
                        advertise.put("advertice", Ads);
                        advertise.put("location",mLocation );
                        advertise.put("parent", mCurrentUser);

                        advertise.saveInBackground();



                    Intent intent = new Intent(CreateAds.this, MyActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                }


            }
        });


    }

    public void onResume () {
        super.onResume();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.create_ads, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
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

// request that the provider send this activity GPS updates every 20 seconds
            locationManager.requestLocationUpdates(provider, 20000, 0, this);


            mLocation = new ParseGeoPoint(location.getLatitude(), location.getLongitude());
        }
        catch (Exception e){
            Toast toast = Toast.makeText(this, getString(R.string.error_ubicacion), Toast.LENGTH_LONG);
            toast.show();
            Log.e(TAG, getString(R.string.error_e), e);
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
