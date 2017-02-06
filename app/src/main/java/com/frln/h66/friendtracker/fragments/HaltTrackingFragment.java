package com.frln.h66.friendtracker.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.frln.h66.friendtracker.ApplicationE;
import com.frln.h66.friendtracker.Completed;
import com.frln.h66.friendtracker.R;
import com.frln.h66.friendtracker.data.TrackSession;
import com.frln.h66.friendtracker.misc.ViewHacks;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.frln.h66.friendtracker.TrackLocationService.LOCATION;

/**
 * @author fdamilola on 04/02/2017.
 * @contact fdamilola@gmail.com +2348166200715
 */

public class HaltTrackingFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.tStatus)
    TextView status;
    @BindView(R.id.stopButton)
    Button stopButton;
    @BindView(R.id.purgeButton)
    Button purgeButton;

    private static final String tSessionKey = "tSessionKey";
    private TrackSession trackSession;

    public static HaltTrackingFragment instantiate(TrackSession trackSession){
        HaltTrackingFragment fragment = new HaltTrackingFragment();
        if(trackSession != null){
            Bundle args = new Bundle();
            args.putString(tSessionKey, trackSession.toJsonObject().toString());
            fragment.setArguments(args);
        }
        return fragment;
    }

    public HaltTrackingFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_terminate, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(this.getArguments() != null){
            if (this.getArguments().getString(tSessionKey) != null) {
                this.trackSession = new Gson().fromJson(this.getArguments().getString(tSessionKey), TrackSession.class);
            }
        }
        if(this.trackSession == null){
            getFragmentManager().beginTransaction().replace(R.id.frame, SetUpFragment.instantiate(null)).commit();
        }else{
            if(((ApplicationE)getActivity().getApplication()).service == null){
                this.stopButton.setText(String.valueOf("START TRACKING"));
                ViewHacks.hideView(this.purgeButton);
            }
            this.purgeButton.setOnClickListener(this);
            this.stopButton.setOnClickListener(this);
        }

    }


    @Override
    public void onClick(View view) {
        if(view == this.stopButton){
            if(((ApplicationE)getActivity().getApplication()).service == null){
                getFragmentManager().beginTransaction().replace(R.id.frame, SetUpFragment.instantiate(this.trackSession)).commit();
            }else{
                ((ApplicationE)getActivity().getApplication()).service.stopSession(new Completed() {
                    @Override
                    public void done() {
                        HaltTrackingFragment.this.stopButton.setText(String.valueOf("START TRACKING"));
                        HaltTrackingFragment.this.status.setText(String.valueOf("STATUS: TRACKING HALTED"));
                    }
                });
            }
        }else if(view == this.purgeButton) {

            final EditText input = new EditText(getActivity());
            final AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            String passkey = input.getText().toString();
                            if(Integer.parseInt(HaltTrackingFragment.this.trackSession.randomKey) == Integer.parseInt(passkey)){
                                if(((ApplicationE)getActivity().getApplication()).service != null){
                                    ((ApplicationE)getActivity().getApplication()).service.purgeSession(new Completed() {
                                        @Override
                                        public void done() {
                                            HaltTrackingFragment.this.stopButton.setText(String.valueOf("START TRACKING"));
                                            HaltTrackingFragment.this.purgeButton.setText(String.valueOf("PURGED!"));
                                            HaltTrackingFragment.this.status.setText(String.valueOf("STATUS: LOCATION HISTORY PURGED!"));
                                        }
                                    });
                                }else{
                                    FirebaseDatabase.getInstance().getReference().child(HaltTrackingFragment.this.trackSession.user_email)
                                            .child(LOCATION).setValue(null);
                                    HaltTrackingFragment.this.stopButton.setText(String.valueOf("START TRACKING"));
                                    HaltTrackingFragment.this.purgeButton.setText(String.valueOf("PURGED!"));
                                    HaltTrackingFragment.this.status.setText(String.valueOf("STATUS: LOCATION HISTORY PURGED!"));
                                }
                            }else{
                                new AlertDialog.Builder(getActivity()).setMessage("Invalid passcode").setPositiveButton("OK", null).create().show();
                            }
                        }
                    }).setNegativeButton("NO", null).create();
            input.setHint("Passcode");
            input.setInputType(InputType.TYPE_CLASS_NUMBER);
            input.setFocusable(true);
            input.requestFocus();
            input.setGravity(Gravity.CENTER_HORIZONTAL);
            alertDialog.setTitle("Please enter your passcode");
            alertDialog.setView(input, 10, 0, 10, 0);
            alertDialog.show();



        }
    }
}
