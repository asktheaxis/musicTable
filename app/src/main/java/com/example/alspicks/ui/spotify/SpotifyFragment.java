package com.example.alspicks.ui.spotify;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.alspicks.ActivityCallback;
import com.example.alspicks.R;
import com.example.alspicks.SharedViewModel;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import static java.util.Objects.requireNonNull;


public class SpotifyFragment extends Fragment {

    private ActivityCallback mCallback;
    private static final String CLIENT_ID = "078e741a074d47ff81a168dfd8226259";
    private static final String REDIRECT_URI = "com.example.alspicks://callback";
    private SpotifyAppRemote mSpotifyAppRemote;

    public static SpotifyFragment newInstance() {
        return new SpotifyFragment();
    }

    //override the onAttach and onDetach methods in fragment lifecycle
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallback = (ActivityCallback) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);


        ConnectionParams connectionParams = new ConnectionParams.Builder(CLIENT_ID)
                .setRedirectUri(REDIRECT_URI)
                .showAuthView(true)
                .build();

        SpotifyAppRemote.connect(getContext(), connectionParams, new Connector.ConnectionListener() {
            @Override
            public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                mSpotifyAppRemote = spotifyAppRemote;
                Log.d("SpotifyFragment", "Connected to Spotify!");


            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.e("SpotifyFragment", throwable.getMessage(), throwable);
            }
        });

        SharedViewModel svModel = ViewModelProviders.of(requireNonNull(getActivity())).get(SharedViewModel.class);
        svModel.buildCurrentUserList();


        return root;
    }
}
