package com.frln.h66.friendtracker.config;

import android.content.Context;
import android.content.res.Resources;

import com.frln.h66.friendtracker.R;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.InputStream;

/**
 * @author fdamilola on 05/02/2017.
 * @contact fdamilola@gmail.com +2348166200715
 */

public class Config  {
    public MailingConfig mailingConfig;
    public MailConfig mailConfig;

    public static Config getInstance(Context context){

        return new Config(context);
    }

    private Config(Context context){
        this.mailingConfig = new Gson().fromJson(loadFromResources(context, R.raw.config), MailingConfig.class);
        this.mailConfig = new Gson().fromJson(loadFromResources(context, R.raw.secret), MailConfig.class);

    }

    private String loadFromResources(Context context, int resf){
        try {
            Resources res = context.getResources();
            InputStream in_s = res.openRawResource(resf);

            byte[] b = new byte[in_s.available()];
            in_s.read(b);
            return new String(b);
        } catch (Exception e) {

        }
        return  new JSONObject().toString();
    }

}
