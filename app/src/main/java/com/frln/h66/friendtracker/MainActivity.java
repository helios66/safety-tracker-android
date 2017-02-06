package com.frln.h66.friendtracker;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.frln.h66.friendtracker.data.TrackSession;
import com.frln.h66.friendtracker.fragments.HaltTrackingFragment;
import com.frln.h66.friendtracker.fragments.HomeFragment;
import com.frln.h66.friendtracker.fragments.SetUpFragment;
import com.frln.h66.friendtracker.misc.LocationHelper;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {


    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        new LocationHelper().displayLocationSettingsRequest(this);
        fragmentManager = getSupportFragmentManager();
        TrackSession ts = TrackingSessionManager.getInstance(this).getLastTrackingSession();
        if(ts == null){
            fragmentManager.beginTransaction().replace(R.id.frame, new HomeFragment()).commit();
        }else{
            if(((ApplicationE)getApplication()).service == null){
                //Go to setup screen
                fragmentManager.beginTransaction().replace(R.id.frame, SetUpFragment.instantiate(ts)).commit();
            }else{
                //Go to termination screen
                fragmentManager.beginTransaction().replace(R.id.frame, HaltTrackingFragment.instantiate(ts)).commit();
            }

        }
    }
}
