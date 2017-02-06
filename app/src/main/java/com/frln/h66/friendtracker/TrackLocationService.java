package com.frln.h66.friendtracker;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.frln.h66.friendtracker.data.Coordinates;
import com.frln.h66.friendtracker.data.TrackSession;
import com.frln.h66.friendtracker.misc.GpsHelper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.FirebaseDatabase;

public class TrackLocationService extends Service implements LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    protected GpsHelper mLocationHelper = GpsHelper.getInstance();
    protected GoogleApiClient mGoogleClient;
    protected TrackSession trackSession;
    public static String LOCATION = "current_location";
    public static String TRACKING_STATUS = "tracking_status";
    public static String AUTH_EMAILS = "authed_emails";
    public static String PASS_KEY = "pass_key";
    private int count = 0;

    public TrackLocationService() {
        Log.e("Init", "LocService");

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("Init", "Start");
        this.count = 0;
        mGoogleClient = new GoogleApiClient.Builder(getApplicationContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleClient.connect();
        ((ApplicationE)getApplication()).service = this;
        this.trackSession = TrackingSessionManager.getInstance(getApplicationContext()).getLastTrackingSession();
        return super.onStartCommand(intent, flags, startId);
    }

    private void subscribeLocationUpdates() {
        mLocationHelper.subscribeLocationUpdates(mGoogleClient, this);
    }

    private void unsubscribeLocationUpdates() {
        if (mGoogleClient.isConnected()) {
            mLocationHelper.unsubscribeLocationUpdates(mGoogleClient, this);
        }
    }

    private double currentlat, currentlong;

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            Log.e("LOCATION", this.trackSession.toJsonObject().toString());
            Log.e("LocationChanged", this.count + " : " + location.getLatitude() + ", " + location.getLongitude
                    () + "");

            FirebaseDatabase.getInstance().getReference().child(this.trackSession.user_email)
                    .child(PASS_KEY).setValue(this.trackSession.randomKey);
            FirebaseDatabase.getInstance().getReference().child(this.trackSession.user_email).
                    child(AUTH_EMAILS).setValue(trackSession.trackers);
            FirebaseDatabase.getInstance().getReference().child(this.trackSession.user_email)
                    .child(TRACKING_STATUS).setValue(true);

            if(this.currentlong != location.getLongitude() || this.currentlat != location.getLatitude()){
                Coordinates coordinates = new Coordinates();
                coordinates.latitude = String.valueOf(location.getLatitude());
                coordinates.longitude = String.valueOf(location.getLongitude());
                FirebaseDatabase.getInstance().getReference().child(this.trackSession.user_email).child(LOCATION)
                        .child(String.valueOf(count)).setValue(coordinates.toJsonObject().toString());

                this.currentlong = location.getLongitude();
                this.currentlat  = location.getLatitude();
                this.count+=1;
            }

        }
    }


    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    public void stopSession(@Nullable Completed completed){
        Log.e("Stopping..", "Stopping");
        unsubscribeLocationUpdates();
        mGoogleClient.disconnect();
        FirebaseDatabase.getInstance().getReference().child(this.trackSession.user_email)
                .child(TRACKING_STATUS).setValue(false);
        ((ApplicationE)getApplication()).service = null;
        if(completed != null){
            completed.done();
        }
    }

    public void purgeSession(Completed completed){
        stopSession(null);
        FirebaseDatabase.getInstance().getReference().child(this.trackSession.user_email)
                .child(LOCATION).setValue(null);
        completed.done();
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        mGoogleClient.disconnect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        subscribeLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e("GoogleConnect", "Google API client connection failed!. " + connectionResult);
    }
}
