package com.frln.h66.friendtracker.config;

import com.frln.h66.friendtracker.data.BaseClass;
import com.google.gson.annotations.SerializedName;

/**
 * @author fdamilola on 05/02/2017.
 * @contact fdamilola@gmail.com +2348166200715
 */

public class MailingConfig extends BaseClass {
    @SerializedName("sender") public String sender;
    @SerializedName("receiver") public String receiver;
    @SerializedName("generic_receiver") public String generic_receiver;

    public String getSender(String passkey, String emails) {
        return sender.replace("{passkey}", passkey).replace("{emails}", emails);
    }

    public String getReceiver(String receiver_email, String senderemail, String passkey) {
        return receiver.replace("{e}", "").replace("{sender}", "").replace("{passkey}", passkey);
    }

    public String getGeneric_receiver(String senderemail, String passkey) {
        return generic_receiver.replace("{sender}", "").replace("{passkey}", passkey);
    }
}
