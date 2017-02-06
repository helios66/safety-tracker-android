package com.frln.h66.friendtracker;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.frln.h66.friendtracker.data.TrackSession;
import com.google.gson.Gson;

/**
 * @author fdamilola on 04/02/2017.
 * @contact fdamilola@gmail.com +2348166200715
 */

public class TrackingSessionManager {
    private final String AT_CACHE = "TRACKING_SESSION_CACHE";
    private final String AT_KEY = "TRACKING_SESSION_CACHE_KEY";
    private Context context;

    private TrackingSessionManager(Context context){
        this.context = context;
    }

    public static TrackingSessionManager getInstance(Context context){
        return new TrackingSessionManager(context);
    }

    private String getUserTrackingSession() {
        SharedPreferences sp = this.context.getSharedPreferences(AT_CACHE,
                Activity.MODE_PRIVATE);
        return sp.getString(AT_KEY, null);
    }

    public void saveSession(String trackingSession) {
        deleteSession();
        if(trackingSession == null){
            return;
        }
        SharedPreferences sp = this.context.getSharedPreferences(AT_CACHE,
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(AT_KEY, trackingSession);
        editor.apply();
    }

    public void deleteSession(){
        SharedPreferences sp = this.context.getSharedPreferences(AT_CACHE,
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
    }

    public TrackSession getLastTrackingSession(){
        String finalString = getUserTrackingSession();
        Log.e("LastSession", finalString+"");
        return finalString == null ? null: new Gson().fromJson(finalString, TrackSession.class);
    }
}
