package com.frln.h66.friendtracker.misc;

import android.location.Location;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.HashSet;

/**
 * @author fdamilola on 14/12/2016.
 * @contact fdamilola@gmail.com +2348166200715
 */

public class GpsHelper implements LocationListener {

    private static final String TAG = LocationHelper.class.getSimpleName();

    private static GpsHelper mLocationHelper;

    private LocationRequest mLocationRequest;
    private Location mCurrentLocation;
    private HashSet<LocationListener> listeners = new HashSet<>();
    private boolean isSubscribed = false;

    private GpsHelper() {
        mLocationRequest = new LocationRequest()
                .setInterval(30000)
                .setFastestInterval(1000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    public static GpsHelper getInstance() {
        if (mLocationHelper == null) {
            mLocationHelper = new GpsHelper();
        }
        return mLocationHelper;
    }

    public void subscribeLocationUpdates(GoogleApiClient client, LocationListener listener) {
        listeners.add(listener);
        if (mCurrentLocation == null) {
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(client);
        }
        if (mCurrentLocation != null) {
            listener.onLocationChanged(mCurrentLocation);
        }
        if (!isSubscribed) {
            LocationServices.FusedLocationApi.requestLocationUpdates(client, mLocationRequest, this);
            isSubscribed = true;
        }
    }

    public void unsubscribeLocationUpdates(GoogleApiClient client, LocationListener listener) {
        listeners.remove(listener);
        if (listeners.isEmpty() && isSubscribed) {
            LocationServices.FusedLocationApi.removeLocationUpdates(client, this);
            isSubscribed = false;
        }
    }

    public Location getLastLocation() {
        return mCurrentLocation;
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        for (LocationListener listener : listeners) {
            listener.onLocationChanged(location);
        }
    }
}
