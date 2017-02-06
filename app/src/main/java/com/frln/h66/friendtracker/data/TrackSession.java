package com.frln.h66.friendtracker.data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * @author fdamilola on 04/02/2017.
 * @contact fdamilola@gmail.com +2348166200715
 */

public class TrackSession extends BaseClass {
    @SerializedName("user_email")
    public String user_email;
    @SerializedName("trackers")
    public ArrayList<String> trackers;
    @SerializedName("is_default")
    public boolean is_default;
    @SerializedName("randomKey")
    public String randomKey;

}
