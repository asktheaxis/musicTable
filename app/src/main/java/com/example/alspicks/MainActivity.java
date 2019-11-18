package com.example.alspicks;

import android.os.Bundle;
import android.widget.CompoundButton;
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


public class MainActivity extends AppCompatActivity implements ActivityCallback{

    private SharedViewModel sharedViewModel;
    private Switch switcher = findViewById(R.id.themeSwitch);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {switcher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked)
                        setTheme(R.style.Theme_darkTheme);
                    else
                        setTheme(R.style.Theme_light);
                }
            });
        } catch(java.lang.NullPointerException e) {
            Toast testToast = Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT);
            testToast.show();
        }

        setContentView(R.layout.activity_main);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, HomeFragment.newInstance())
                    .commit();
        }
        else{
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
    public void openDashboardFragment(){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, DashboardFragment.newInstance())
                .commit();
    }

    @Override
    public void openAccountFragment(){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, AccountFragment.newInstance())
                .commit();
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
