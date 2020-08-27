package com.example.alspicks;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.alspicks.ui.account.AccountFragment;
import com.example.alspicks.ui.dashboard.DashboardFragment;
import com.example.alspicks.ui.home.HomeFragment;
import com.example.alspicks.ui.home.ResultsFragment;
import com.example.alspicks.ui.notifications.DialogueFragment;
import com.example.alspicks.ui.notifications.NotificationsFragment;
import com.example.alspicks.ui.spotify.SpotifyFragment;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import static java.util.Objects.requireNonNull;


public class MainActivity extends AppCompatActivity implements ActivityCallback{

    private SharedViewModel sharedViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_darkTheme);
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

    @Override
    public void openNotificationsFragment(ArrayList<Album> albums){
        sharedViewModel.setUserList(albums);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, NotificationsFragment.newInstance())
                .commit();
    }

    @Override
    public void openDialogueFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, DialogueFragment.newInstance())
                .commit();
    }

    @Override
    public void openSpotifyFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, SpotifyFragment.newInstance())
                .commit();
    }

    @Override
    public void onSucess(String result) {
        sharedViewModel.setAlbumResource(result);
    }

    @Override
    public void onComplete(ArrayList<Album> albums){
        sharedViewModel.setUserList(albums);
    }


}
