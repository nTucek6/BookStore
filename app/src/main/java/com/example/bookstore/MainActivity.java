package com.example.bookstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bookstore.Classes.NoInternet;
import com.example.bookstore.Classes.Product;
import com.example.bookstore.Classes.Products;
import com.example.bookstore.Classes.ShoppingCart;
import com.example.bookstore.Fragments.HomeFragment;
import com.example.bookstore.Fragments.HomeFragments.SearchFragment;
import com.example.bookstore.Fragments.ProductInfoFragment;
import com.example.bookstore.Fragments.ProfileFragment;
import com.example.bookstore.Fragments.ProfileFragments.OrdersFragment;
import com.example.bookstore.Fragments.ProfileFragments.UserFragment;
import com.example.bookstore.Fragments.ShoppingCartFragment;
import com.example.bookstore.databinding.ActivityMainBinding;

import java.security.Key;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    public ActivityMainBinding binding;

    private long pressedTime;

    private HomeFragment homeFragment;
    private ShoppingCartFragment shoppingCartFragment;
    private ProfileFragment profileFragment;

    private NoInternet noInternet = new NoInternet();

    public void onConfigurationChanged(Configuration newConfig) {

        super.onConfigurationChanged(newConfig);
        startActivity(new Intent(MainActivity.this, MainActivity.class));
        this.finish();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().findFragmentById(R.id.frame_layout) == homeFragment || getSupportFragmentManager().findFragmentById(R.id.frame_layout) == profileFragment || getSupportFragmentManager().findFragmentById(R.id.frame_layout) == shoppingCartFragment) {
            if (pressedTime + 2000 > System.currentTimeMillis()) {
                super.onBackPressed();
                this.finish();
            } else {
                Toast.makeText(getBaseContext(), getString(R.string.ExitDialog), Toast.LENGTH_SHORT).show();
            }
            pressedTime = System.currentTimeMillis();
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); // Removes top bar

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        noInternet.InternetLost(MainActivity.this,true);

        homeFragment = new HomeFragment();
        shoppingCartFragment = new ShoppingCartFragment();
        profileFragment = new ProfileFragment();

        SetFragments();

        binding.NavigationBar.setOnItemSelectedListener(item ->
        {
            switch (item.getItemId()) {
                case R.id.home:
                    navigationBar("Home");
                    break;
                case R.id.shoppingCart:
                    navigationBar("ShoppingCart");
                    break;
                case R.id.user:
                    navigationBar("Profile");
                    break;
            }
            return true;
        });
    }

    private void SetFragments() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.frame_layout, homeFragment, "homeFragment");
        fragmentTransaction.add(R.id.frame_layout, shoppingCartFragment, "shoppingCartFragment");
        fragmentTransaction.add(R.id.frame_layout, profileFragment, "profileFragment");
        fragmentTransaction.detach(profileFragment);
        fragmentTransaction.detach(shoppingCartFragment);

        fragmentTransaction.commit();
    }


    public void navigationBar(String fragment) {

        FragmentManager fm = getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) //ako korisnik ode s fragmenta u fragmentu da se ukloni iz pozadine
        {
            fm.popBackStack();
        }

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        if (Objects.equals(fragment, "Home")) {

            //fragmentTransaction.detach(getSupportFragmentManager().findFragmentById(R.id.frame_layout));
            fragmentTransaction.detach(profileFragment);
            fragmentTransaction.detach(shoppingCartFragment);
            fragmentTransaction.attach(homeFragment);

        } else if (Objects.equals(fragment, "ShoppingCart")) {

            fragmentTransaction.detach(profileFragment);
            fragmentTransaction.detach(homeFragment);
            //  fragmentTransaction.detach(getSupportFragmentManager().findFragmentById(R.id.frame_layout));
            fragmentTransaction.attach(shoppingCartFragment);


        } else if (Objects.equals(fragment, "Profile")) {

            //   fragmentTransaction.detach(getSupportFragmentManager().findFragmentById(R.id.frame_layout));
            fragmentTransaction.detach(homeFragment);
            fragmentTransaction.detach(shoppingCartFragment);
            fragmentTransaction.attach(profileFragment);
        }

        fragmentTransaction.commit();

    }

    public void ArticleInfo(Product article, String type) {
        // if (type.equals("book")) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left, R.anim.enter_left_to_right, R.anim.exit_left_to_right);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.detach(getSupportFragmentManager().findFragmentById(R.id.frame_layout));
        fragmentTransaction.add(R.id.frame_layout, new ProductInfoFragment(article, type));
        fragmentTransaction.commit();
    }

    public void PopBackStack() {
        getSupportFragmentManager().popBackStack();
    }


    public void DestroyArticleInfo() {
        getSupportFragmentManager().popBackStack();

    }

    public void ProceedToPayment(List<Products> productsList, List<ShoppingCart> shoppingCartList, List<Product> productList) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left, R.anim.enter_left_to_right, R.anim.exit_left_to_right);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.detach(getSupportFragmentManager().findFragmentById(R.id.frame_layout));
        fragmentTransaction.add(R.id.frame_layout, new PaymentTypeFragment(productsList, shoppingCartList, productList));
        fragmentTransaction.commit();
    }

    public void OrdersFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left, R.anim.enter_left_to_right, R.anim.exit_left_to_right);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.detach(getSupportFragmentManager().findFragmentById(R.id.frame_layout));
        fragmentTransaction.add(R.id.frame_layout, new OrdersFragment());
        fragmentTransaction.commit();
    }

    public void ProfileFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left, R.anim.enter_left_to_right, R.anim.exit_left_to_right);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.detach(getSupportFragmentManager().findFragmentById(R.id.frame_layout));
        fragmentTransaction.add(R.id.frame_layout, new UserFragment());
        fragmentTransaction.commit();
    }


    public void DestroyPaymentFragmentInfo() {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.remove(getSupportFragmentManager().findFragmentById(R.id.frame_layout));
        fragmentTransaction.attach(homeFragment);
        fragmentTransaction.commit();
    }

    public void SearchFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left, R.anim.enter_left_to_right, R.anim.exit_left_to_right);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.detach(getSupportFragmentManager().findFragmentById(R.id.frame_layout));
        fragmentTransaction.add(R.id.frame_layout, new SearchFragment());
        fragmentTransaction.commit();
    }


}