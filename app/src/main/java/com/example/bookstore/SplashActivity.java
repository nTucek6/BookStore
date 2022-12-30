package com.example.bookstore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.Toast;

import com.example.bookstore.Fragments.Startup.NoInternetFragment;
import com.example.bookstore.Fragments.Startup.SplashFragment;

import java.util.Objects;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); // Removes top bar
        setContentView(R.layout.activity_splash);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.flStartUp,new SplashFragment());
        fragmentTransaction.commit();

        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(isConnectedToInternet())
                {
                    startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                    finish();
                }
                else
                {
                    NoInternetFragment();
                }


            }
        },1500);

    }

    public boolean isConnectedToInternet() {

        ConnectivityManager cm = (ConnectivityManager)SplashActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;

    }

    private void NoInternetFragment()
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left,R.anim.enter_left_to_right,R.anim.exit_left_to_right);
        fragmentTransaction.replace(R.id.flStartUp,new NoInternetFragment());
        fragmentTransaction.commit();

    }

}