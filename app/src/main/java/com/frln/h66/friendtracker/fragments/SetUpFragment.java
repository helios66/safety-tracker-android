package com.frln.h66.friendtracker.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.frln.h66.friendtracker.R;
import com.frln.h66.friendtracker.TrackLocationService;
import com.frln.h66.friendtracker.TrackingSessionManager;
import com.frln.h66.friendtracker.data.TrackSession;
import com.frln.h66.friendtracker.misc.Mailer;
import com.frln.h66.friendtracker.misc.ViewHacks;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.frln.h66.friendtracker.TrackLocationService.LOCATION;

/**
 * @author fdamilola on 04/02/2017.
 * @contact fdamilola@gmail.com +2348166200715
 */

public class SetUpFragment extends Fragment implements View.OnClickListener {


    @BindView(R.id.actionButton) Button startTracking;
    @BindView(R.id.randomKey)
    TextView randomKey;
    @BindView(R.id.yourEmail) EditText eYourEmail;
    @BindView(R.id.email1) EditText eEmail1;
    @BindView(R.id.email2) EditText eEmail2;
    @BindView(R.id.email3) EditText eEmail3;

    static final int SENDEMAIL = 34;

    private static final String tSessionKey = "tSessionKey";
    private TrackSession trackSession;



    public static SetUpFragment instantiate(TrackSession trackSession){
        SetUpFragment setUpFragment = new SetUpFragment();
        if(trackSession != null){
            Bundle args = new Bundle();
            args.putString(tSessionKey, trackSession.toJsonObject().toString());
            setUpFragment.setArguments(args);
        }
        return setUpFragment;
    }

    public SetUpFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setup, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(this.getArguments() != null){
            if (this.getArguments().getString(tSessionKey) != null) {
                this.trackSession = new Gson().fromJson(this.getArguments().getString(tSessionKey), TrackSession.class);
                this.setUpData(this.trackSession);

            }
        }
        this.generateRandomKey();
        this.startTracking.setOnClickListener(this);
    }


    private void generateRandomKey(){
        int randomPIN = (int)(Math.random()*9000)+1000;
        this.randomKey.setText(String.valueOf(randomPIN));
    }

    private void setUpData(TrackSession trackSession){
        this.eYourEmail.setText(trackSession.user_email.replace("_com", ".com"));
        EditText[] editTexts = {eEmail1, eEmail2, eEmail3};
        for(int i = 0; i < trackSession.trackers.size(); i++){
            editTexts[i].setText(trackSession.trackers.get(i));
        }
    }

    @Override
    public void onClick(View view) {
        final String youremail = this.eYourEmail.getText().toString().toLowerCase();
        if(android.util.Patterns.EMAIL_ADDRESS.matcher(youremail).matches()){
            this.trackSession = new TrackSession();
            this.trackSession.trackers = new ArrayList<String>();
            this.trackSession.user_email = normalize(youremail);
            boolean hasSingleValue = false;
            EditText[] editTexts = {eEmail1, eEmail2, eEmail3};
            for (EditText e:editTexts){
                if(android.util.Patterns.EMAIL_ADDRESS.matcher(e.getText().toString()).matches()){
                    hasSingleValue = true;
                    this.trackSession.trackers.add(e.getText().toString().toLowerCase());
                }
            }
            if(hasSingleValue){
                this.trackSession.randomKey = this.randomKey.getText().toString();
                TrackingSessionManager.getInstance(getActivity()).saveSession(this.trackSession.toJsonObject().toString());
                final Mailer mailer = Mailer.getInstance(getActivity());
                Object[] objDays = this.trackSession.trackers.toArray();
                String[] recepients = Arrays.copyOf(objDays, objDays.length, String[].class);
                this.composeEmail(recepients, "Safety Tracker Notice", mailer.cfg.mailingConfig.getGeneric_receiver(youremail, this.trackSession.randomKey));
//                try {
//                    //Crashes like mad. Automatic email sending is a no go.
//                    //mailer.sendEmailQuietly(this.trackSession.trackers, this.trackSession.randomKey, youremail);
//                }catch (Exception e){
//
//                }
                FirebaseDatabase.getInstance().getReference().child(this.trackSession.user_email)
                        .child(LOCATION).setValue(null);
                getActivity().startService(new Intent(getActivity(), TrackLocationService.class));

            }else {
                ViewHacks.createAlertDialog(getActivity(), "Oops", "You need to provide at least one valid contact email address!");
            }
        }else{
            ViewHacks.createAlertDialog(getActivity(), "Oops", "You need to provide a valid email address!");
        }
    }


    private String normalize(String string){
        return string.replaceAll("\\.", "_");
    }

    public void composeEmail(String[] addresses, String subject, String body) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(body, Html.FROM_HTML_MODE_COMPACT));
        }else{
            intent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(body));
        }
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(intent, SENDEMAIL);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SENDEMAIL){
            getFragmentManager().beginTransaction().replace(R.id.frame, HaltTrackingFragment.instantiate(trackSession)).commit();
        }
    }
}
