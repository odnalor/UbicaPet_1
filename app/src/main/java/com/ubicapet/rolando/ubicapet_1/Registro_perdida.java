package com.ubicapet.rolando.ubicapet_1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.location.*;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;


public class Registro_perdida extends Activity implements LocationListener {
    public static final String TAG = RegisterPetActivity.class.getSimpleName();
    protected ParseUser mCurrentUser;
    protected String mName;
    protected EditText mAds, mTitulo;
    private LocationManager locationManager;
    protected Button mPerdida;
    ParseGeoPoint mLocation;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_perdida);

        mCurrentUser =ParseUser.getCurrentUser();
        Intent intent = getIntent();
        mName= intent.getStringExtra("Pet");

        mPerdida = (Button) findViewById(R.id.button);
        mPerdida.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                mTitulo = (EditText)findViewById(R.id.editText_titulo);
                mAds = (EditText)findViewById(R.id.AdsText);

                String titulo = mTitulo.getText().toString();
                String Name = mName;
                String Ads = mAds.getText().toString();

                titulo = titulo.trim();
                Name = Name.trim();
                Ads = Ads.trim();

                if (titulo.isEmpty() || Ads.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Registro_perdida.this);
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
                    advertise.put("type", "Pérdida");
                    advertise.put("advertice", Ads);
                    advertise.put("location",mLocation );
                    advertise.put("parent", mCurrentUser);
                    advertise.saveInBackground();




                    final ParseQuery<ParseObject> query = ParseQuery.getQuery("Pets");
                    query.whereEqualTo("parent", mCurrentUser);
                    query.whereEqualTo("name", mName);
                    query.getFirstInBackground(new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject parseObject, ParseException e) {

                            if (e == null ){

                                parseObject.put("Estado","Perdido" );
                                parseObject.saveInBackground();



                            }

                            else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(Registro_perdida.this);
                                builder.setMessage(getString(R.string.ocurrio_error))
                                        .setTitle(R.string.login_error_title)
                                        .setPositiveButton(android.R.string.ok, null);
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }

                        }
                    });
                    ParseObject pet= new ParseObject("Pets");
                    pet.put("Estado","Perdido" );
                    advertise.saveInBackground();





                    Intent intent2 = new Intent(Registro_perdida.this, MyActivity.class);
                    intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent2);

                    finish();

                }


            }
        });

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
            android.location.Location location = locationManager.getLastKnownLocation(provider);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_registro_perdida, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
