package com.example.alspicks.ui.spotify;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.alspicks.ActivityCallback;
import com.example.alspicks.MainActivity;
import com.example.alspicks.R;
import com.example.alspicks.SharedViewModel;
import com.example.alspicks.ui.spotify.connectors.UserService;
import com.google.gson.Gson;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.requireNonNull;


public class SpotifyFragment extends Fragment {

    private ActivityCallback mCallback;
    private static final String CLIENT_ID = "078e741a074d47ff81a168dfd8226259";
    private static final String REDIRECT_URI = "com.example.alspicks://callback";
    private static final String REDIRECT_URI_ROOT = "com.example.aplspicks";
    private static final String SCOPES = "user-library-read";
    private static final String CODE = "code";
    private static final String ERROR_CODE = "error";
    private static final int REQUEST_CODE = 1337;
    private String error;
    private String code;
    private ArrayList<SpotifyAlbum> userAlbums = new ArrayList<SpotifyAlbum>();

    private SharedPreferences.Editor editor;
    private SharedPreferences msharedPreferences;

    private RequestQueue queue;

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

        authenticateSpotify();

        msharedPreferences = requireNonNull(getContext()).getSharedPreferences("SPOTIFY", 0);
        queue = Volley.newRequestQueue(getContext());

        //getUserLibrary();

        SharedViewModel svModel = ViewModelProviders.of(requireNonNull(getActivity())).get(SharedViewModel.class);
        svModel.buildCurrentUserList();


        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);

            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    editor = requireNonNull(getContext()).getSharedPreferences("SPOTIFY", 0).edit();
                    editor.putString("token", response.getAccessToken());
                    Log.d("STARTING", "GOT AUTH TOKEN");
                    editor.apply();
                    waitForUserInfo();
                    break;

                // Auth flow returned an error
                case ERROR:
                    Log.d("ERROR", "ERROR GETTING SPOTIFY TOKEN");
                    break;

                // Most likely auth flow was cancelled
                default:
                    // Handle other cases
            }
        }
    }

    private void getUserLibrary(){
        String endpoint = "https://api.spotify.com/v1/me/albums";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, endpoint, null, response -> {
                    Gson gson = new Gson();
                    JSONArray jsonArray = response.optJSONArray("items");
                    for (int n = 0; n < jsonArray.length(); n++) {
                        try {
                            JSONObject object = jsonArray.getJSONObject(n);
                            Log.d("READ", object.toString());
                            JSONObject name = object.getJSONObject("album");
                            String albumName = name.getString("name");
                            String artistName = object.getJSONObject("album")
                                    .getJSONArray("artists")
                                    .getJSONObject(0)
                                    .getString("name");
                            SpotifyAlbum album = new SpotifyAlbum(artistName, albumName);
                            userAlbums.add(album);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    mCallback.openLibraryFragment(userAlbums);
                }, error -> {
                    // TODO: Handle error
                    Log.d("ERROR", "error reading library");
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String token = msharedPreferences.getString("token", "");
                String auth = "Bearer " + token;
                headers.put("Authorization", auth);
                return headers;
            }
        };
        queue.add(jsonObjectRequest);
    }

    private void authenticateSpotify() {
        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);
        builder.setScopes(new String[]{SCOPES});
        AuthenticationRequest request = builder.build();
        //AuthenticationClient.openLoginActivity(requireNonNull(getActivity()), REQUEST_CODE, request);

        Intent intent = AuthenticationClient.createLoginActivityIntent(requireNonNull(getActivity()), request);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @SuppressLint({"CommitPrefEdits", "ApplySharedPref"})
    private void waitForUserInfo() {
        UserService userService = new UserService(queue, msharedPreferences);
        userService.get(() -> {
            User user = userService.getUser();
            editor = requireNonNull(getActivity()).getSharedPreferences("SPOTIFY", 0).edit();
            editor.putString("userid", user.id);
            Log.d("starting", "got user information");
            editor.commit();
            getUserLibrary();
            //startMainActivity();
        });
    }

    private void startMainActivity() {
        Intent newintent = new Intent(getContext(), MainActivity.class);
        startActivity(newintent);
    }

    public class SpotifyAlbum {
        public String name;
        public String artist;

        public SpotifyAlbum(String artist, String name) {
            this.artist = artist;
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public String getArtist() {
            return artist;
        }
    }




}
