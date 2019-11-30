package com.example.alspicks;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.alspicks.ui.account.AccountFragment;
import com.example.alspicks.ui.dashboard.DashboardFragment;
import com.example.alspicks.ui.home.HomeFragment;
import com.example.alspicks.ui.home.ResultsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import static java.util.Objects.requireNonNull;


public class MainActivity extends AppCompatActivity implements ActivityCallback {

    private SharedViewModel sharedViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        themeChooser.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_main);
        setupSpinnerItemSelection();

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, HomeFragment.newInstance())
                    .commit();
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, AccountFragment.newInstance())
                    .commit();

        }


        sharedViewModel = ViewModelProviders.of(requireNonNull(this)).get(SharedViewModel.class);
        //sharedViewModel.setUserId(requireNonNull(FirebaseAuth.getInstance().getCurrentUser()));
    }


    //Callback Methods
    @Override
    public void openDashboardFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, DashboardFragment.newInstance())
                .commit();
    }

    @Override
    public void openAccountFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, AccountFragment.newInstance())
                .commit();
    }

    @Override
    public void openLoginFragment() {


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

    private void setupSpinnerItemSelection() {
        Spinner switcher = findViewById(R.id.themeSwitch);
        switcher.setSelection(ThemeApplication.currentPosition);
        ThemeApplication.currentPosition = switcher.getSelectedItemPosition();

        switcher.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                if (ThemeApplication.currentPosition != position) {
                    themeChooser.changeToTheme(MainActivity.this, position);
                }
                ThemeApplication.currentPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /*public int darkTheme() {
        setTheme(R.style.Theme_darkTheme);
        TypedValue name = new TypedValue();
        getTheme().resolveAttribute(R.attr.ThemeName, name, true);
        if ("dark".equals(name.string))
            return 1;
        else
            return 0;
    }

    public int lightTheme() {
        setTheme(R.style.Theme_light);
        TypedValue name = new TypedValue();
        getTheme().resolveAttribute(R.attr.ThemeName, name, true);
        if ("light".equals(name.string))
            return 1;
        else
            return 0;
    }*/
}


