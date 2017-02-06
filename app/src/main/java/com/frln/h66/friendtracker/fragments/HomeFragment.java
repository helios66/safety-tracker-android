package com.frln.h66.friendtracker.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;

import com.frln.h66.friendtracker.R;
import com.frln.h66.friendtracker.misc.LocationHelper;
import com.frln.h66.friendtracker.misc.ViewHacks;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.actionButton) Button buttonAction;
    @BindView(R.id.switchOk) Switch aSwitch;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new LocationHelper().displayLocationSettingsRequest(getActivity());
        buttonAction.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        new LocationHelper().displayLocationSettingsRequest(getActivity());
        if(aSwitch.isChecked()){
            getFragmentManager().beginTransaction().replace(R.id.frame, SetUpFragment.instantiate(null)).commit();
        }else{
            ViewHacks.createAlertDialog(getActivity(), "Oops!", "You have to agree with the rubbish above to use the app. :)");
        }
    }
}
