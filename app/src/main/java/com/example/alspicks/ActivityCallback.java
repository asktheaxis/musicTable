package com.example.alspicks;

import com.example.alspicks.ui.spotify.SpotifyFragment;

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
    void openSpotifyFragment();
    void openLibraryFragment(ArrayList<SpotifyFragment.SpotifyAlbum> albums);

    void onSucess(String result);

    void onComplete(ArrayList<Album> albums);


}
