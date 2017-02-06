package com.frln.h66.friendtracker.data;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * @author fdamilola on 04/02/2017.
 * @contact fdamilola@gmail.com +2348166200715
 */

public class BaseClass {
    public JsonObject toJsonObject(){
        return new Gson().toJsonTree(this).getAsJsonObject();
    }
}
