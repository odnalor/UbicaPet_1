package com.ubicapet.rolando.ubicapet_1;

import android.content.Context;
import android.location.Criteria;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by rolando on 30-10-2014.
 */
public class Location extends FragmentActivity implements LocationListener {
    private static final String LocationListener = null;
    LocationClient mLocationClient;
    LocationManager handle;
    String provider;
    LatLng Miubicacion;
    android.location.Location loc;


    public android.location.Location getMiUbicacion()
    {
        //Llamo al servico de localizacion
        handle = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        //Clase criteria permite decidir mejor poveedor de posicion
        Criteria c = new Criteria();
        //obtiene el mejor proveedor en funci�n del criterio asignado
        //ACCURACY_FINE(La mejor presicion)--ACCURACY_COARSE(PRESISION MEDIA)
        c.setAccuracy(Criteria.ACCURACY_COARSE);
        //Indica si es necesaria la altura por parte del proveedor
        c.setAltitudeRequired(false);
        provider = handle.getBestProvider(c, false);
        //Se activan las notificaciones de localizaci�n con los par�metros:
        //proveedor, tiempo m�nimo de actualizaci�n, distancia m�nima, Locationlistener
        handle.requestLocationUpdates(LocationListener, 60000, 5, this);
        //Obtiene la ultima posicion conocida por el proveedor
        loc = handle.getLastKnownLocation(provider);

        return loc;
    }

    @Override
    public void onLocationChanged(android.location.Location location) {
        Miubicacion = new LatLng(loc.getLatitude(),loc.getLongitude());

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
