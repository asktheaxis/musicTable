package com.example.alspicks;

import java.util.ArrayList;

public interface ActivityCallback {

    //prototypes are grouped  roughly by the app structure
    void openResultsFragment(ArrayList<Album> results);
    void openHomeFragment();

    void openDashboardFragment();

    void openAccountFragment();
    void openLoginFragment();
    void openNotificationsFragment();

    void onSucess(String result);


}
