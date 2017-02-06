package com.frln.h66.friendtracker.misc;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.frln.h66.friendtracker.BuildConfig;
import com.frln.h66.friendtracker.MainActivity;


/**
 * @author fdamilola on 14/12/2016.
 * @contact fdamilola@gmail.com +2348166200715
 */

public class ViewHacks {
    public static void hideView(View view) {
        if (view == null) {
            return;
        }

        view.setVisibility(View.GONE);
    }

    public static void showView(View view) {
        if (view == null) {
            return;
        }

        view.setVisibility(View.VISIBLE);
    }

    public static void hideView(View... views) {
        for (View view : views) {
            if (view != null) {
                view.setVisibility(View.GONE);
            }

        }
    }

    public static void showView(View... views) {
        for (View view : views) {
            if (view != null) {
                view.setVisibility(View.VISIBLE);
            }
        }
    }

    public static void invisibleViews(View view) {
        if (view == null) {
            return;
        }

        view.setVisibility(View.INVISIBLE);
    }

    public static void createAlertDialog(Context context, String title, String message) {

        if (context == null) {
            return;
        }
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setNeutralButton("OK", null)
                .create().show();
    }

    public static void useSnackBar(View view, String message) {
        if (view == null) {
            return;
        }
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }

    public static ProgressDialog createProgressDialog(Context context, String message, boolean showNow) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        if(showNow){
            if(!progressDialog.isShowing()){
                try {
                    progressDialog.show();
                }catch (Exception e){

                }
            }
        }
        return progressDialog;
    }



    public static void leaveApp(final Context activity){
        Intent i = new Intent(activity, MainActivity.class);
        i.addCategory(Intent.CATEGORY_LAUNCHER);
        i.setAction(Intent.ACTION_MAIN);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(i);
    }


    public static void hideKeyboard(final Activity activity){
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static  void setAppVersion(TextView textView){
        if(textView == null){
            return;
        }else{
            int versionCode = BuildConfig.VERSION_CODE;
            String versionName = BuildConfig.VERSION_NAME;
            textView.setText("App Version: "+versionName+ "("+versionCode+")");
        }
    }
}
