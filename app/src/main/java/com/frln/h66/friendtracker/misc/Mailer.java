package com.frln.h66.friendtracker.misc;

import android.content.Context;

import com.frln.h66.friendtracker.config.Config;


/**
 * @author fdamilola on 05/02/2017.
 * @contact fdamilola@gmail.com +2348166200715
 */

public class Mailer {

    private int count = 0;
    private Context context;
    public Config cfg;

    private Mailer(Context context){
        this.context = context;
        this.cfg = Config.getInstance(this.context);
    }

    public static Mailer getInstance(Context context){
        return new Mailer(context);
    }

//    public void sendEmailQuietly(ArrayList<String> emails, String passkey, String sender){
//        count = 0;
//        final String personalMessage = this.cfg.mailingConfig.getSender(passkey, Arrays.toString(emails.toArray()));
//        Address personal = Address.addressWithMailbox(sender);
//        personal.setDisplayName("Dear User");
//
//        this.mailSender(personal, personalMessage);
//
//        for(String e:emails){
//            String message = this.cfg.mailingConfig.getReceiver(e, sender, passkey);
//            Address p = Address.addressWithMailbox(e);
//            personal.setDisplayName("Dear User");
//            this.mailSender(p, message);
//        }
//
//    }
//
//
//    private void mailSender(final Address address, String messageHtml){
//        Log.e("GotHeare", messageHtml);
//        MessagesSender messagesSender = new MessagesSender(this.cfg.mailConfig);
//        MessageBuilder messageBuilder = new MessageBuilder();
//
//        Address sender = Address.addressWithMailbox(messagesSender.session.username());
//        sender.setDisplayName(messagesSender.messageSender);
//
//        if(count == 0){
//            ArrayList<Address> bcc = new ArrayList<>();
//            bcc.add(sender);
//            messageBuilder.header().setBcc(bcc);
//            count+=1;
//        }
//        messageBuilder.header().setFrom(sender);
//        ArrayList<Address> receivers = new ArrayList<>();
//        receivers.add(address);
//        messageBuilder.header().setTo(receivers);
//
//        messageBuilder.header().setSubject("Safety Tracker Notice");
//        messageBuilder.setHTMLBody(messageHtml);
//
//        byte[] rfc822Data = messageBuilder.data();
//        messagesSender.session.sendMessageOperation(rfc822Data).start(new OperationCallback() {
//            @Override
//            public void succeeded() {
//                Log.i("Email", "Sent!");
//            }
//
//            @Override
//            public void failed(MailException e) {
//                Log.i("Email", "Not Sent!");
//            }
//        });
//
//    }
//
//    private class MessagesSender{
//        public SMTPSession session;
//        public String messageSender;
//
//        public MessagesSender(MailConfig mailingConfig) {
//            session = new SMTPSession();
//            session.setUsername(mailingConfig.S_USERNAME);
//            session.setPassword(mailingConfig.S_PASSWORD);
//            session.setHostname(mailingConfig.S_HOSTNAME);
//            session.setPort(Integer.parseInt(mailingConfig.S_PORT));
//            session.setAuthType(AuthType.AuthTypeSASLPlain);
//            session.setConnectionType(ConnectionType.ConnectionTypeTLS);
//            this.messageSender = mailingConfig.SENDER;
//            Log.e("Created", this.session.hostname());
//        }
//    }
}
