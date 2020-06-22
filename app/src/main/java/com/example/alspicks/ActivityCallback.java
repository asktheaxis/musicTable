package com.example.alspicks;

import java.util.ArrayList;

public interface ActivityCallback {

    //prototypes are grouped  roughly by the app structure
    void openResultsFragment(ArrayList<Album> results);
    void openHomeFragment();

    void openDashboardFragment();
    void openDialogueFragment();
    void openAccountFragment();
    void openLoginFragment();
    void openNotificationsFragment(ArrayList<Album> albums);

    void onSucess(String result);

    void onComplete(ArrayList<Album> albums);


}
