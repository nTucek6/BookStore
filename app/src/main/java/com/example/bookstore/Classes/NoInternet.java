package com.example.bookstore.Classes;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.bookstore.R;

public class NoInternet {

    public long pressedTime;
    public Dialog dialog;

    public void InternetLost(Activity activity,boolean onBackPressed)
    {

        dialog = new Dialog(activity,  android.R.style.Theme_Holo_Dialog);
        dialog.setTitle("Internet lost");
        dialog.setContentView(R.layout.dialog_internet_lost);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);

        NetworkRequest networkRequest = new NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .build();

        ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(@NonNull Network network) {
                super.onAvailable(network);
                dialog.hide();
            }

            @Override
            public void onLost(@NonNull Network network) {
                super.onLost(network);
                dialog.show();
            }

            @Override
            public void onCapabilitiesChanged(@NonNull Network network, @NonNull NetworkCapabilities networkCapabilities) {
                super.onCapabilitiesChanged(network, networkCapabilities);
                final boolean unmetered = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED);
            }
        };
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(ConnectivityManager.class);
        connectivityManager.requestNetwork(networkRequest, networkCallback);

        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if( KeyEvent.ACTION_DOWN == event.getAction())
                {
                    if(onBackPressed)
                    {
                        if (pressedTime + 2000 > System.currentTimeMillis()) {
                            activity.finish();
                        } else {
                            Toast.makeText(activity, activity.getString(R.string.ExitDialog), Toast.LENGTH_SHORT).show();
                        }
                        pressedTime = System.currentTimeMillis();
                    }
                    else
                    {
                        activity.finish();
                    }

                }

                return false;
            }
        });


    }

}
