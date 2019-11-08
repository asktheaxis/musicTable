package com.example.alspicks;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.alspicks.ui.home.HomeFragment;
import com.example.alspicks.ui.home.ResultsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import static java.util.Objects.requireNonNull;


public class MainActivity extends AppCompatActivity implements ActivityCallback{

    private SharedViewModel sharedViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_darkTheme);
        setContentView(R.layout.activity_main);


        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications, R.id.navigation_account)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);


        sharedViewModel = ViewModelProviders.of(requireNonNull(this)).get(SharedViewModel.class);
        //sharedViewModel.setUserId(requireNonNull(FirebaseAuth.getInstance().getCurrentUser()));
    }


    //Callback Methods
    @Override
    public void openDashboardFragment(){

    }

    @Override
    public void openAccountFragment(){

    }

    @Override
    public void openLoginFragment(){


    }

    @Override
    public void openResultsFragment(ArrayList<Album> results) {

        sharedViewModel.setAlbumResults(results);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, ResultsFragment.newInstance())
                .commit();
    }


    @Override
    public void openHomeFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, HomeFragment.newInstance())
                .commit();
    }


}
