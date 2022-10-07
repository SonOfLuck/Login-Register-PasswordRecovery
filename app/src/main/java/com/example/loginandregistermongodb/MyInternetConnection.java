package com.example.loginandregistermongodb;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class MyInternetConnection {
    private String TAG = "NETWORK";
    public boolean checkInternetApiCall(Activity activity){
        if(isNetworkConnectionAvailable(activity)){
            // Here we set code that repeats for every api call.
            // Like a counter for the amount of api calls before displaying an add.
            // ...
            return true;
        }
        return false;
    }

    public void checkNetworkConnection(Activity activity){
        AlertDialog.Builder builder =new AlertDialog.Builder(activity);
        builder.setTitle("No internet Connection");
        builder.setMessage("Please turn on internet connection to continue");
        builder.setNegativeButton("close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public boolean isNetworkConnectionAvailable(Activity activity){
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnected();
        if(isConnected) {
            Log.d(TAG, "Connected");
            return true;
        }
        else{
            checkNetworkConnection(activity);
            Log.d(TAG,"Not Connected");
            return false;
        }
    }
}
