package com.frln.h66.friendtracker.config;

import com.frln.h66.friendtracker.data.BaseClass;
import com.google.gson.annotations.SerializedName;

/**
 * @author fdamilola on 05/02/2017.
 * @contact fdamilola@gmail.com +2348166200715
 */

public class MailConfig extends BaseClass {
    /*
    {
  "SENDER": "Safety Tracker",
  "S_PORT": "465",
  "S_HOSTNAME": "smtp.gmail.com",
  "S_PASSWORD": "safetytracker12321",
  "S_USERNAME": "safetytracker10@gmail.com"
}
     */

    @SerializedName("SENDER") public String SENDER;
    @SerializedName("S_PORT") public String S_PORT;
    @SerializedName("S_HOSTNAME") public String S_HOSTNAME;
    @SerializedName("S_PASSWORD") public String S_PASSWORD;
    @SerializedName("S_USERNAME") public String S_USERNAME;
}
