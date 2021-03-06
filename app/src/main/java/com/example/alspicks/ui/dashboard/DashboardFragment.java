package com.example.alspicks.ui.dashboard;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.alspicks.ActivityCallback;
import com.example.alspicks.Album;
import com.example.alspicks.AlbumDialog;
import com.example.alspicks.ArtistDialog;
import com.example.alspicks.BuildConfig;
import com.example.alspicks.R;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class DashboardFragment extends Fragment implements View.OnClickListener{


    private EditText edtArtist, edtAlbum;
    private ImageView imageView1, imageView2, imageView3;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private int artistId = 0;
    private ArrayList<Album> arrayResults = new ArrayList<>();
    private ArrayList<Album> arrayDisplayResults = new ArrayList<>();
    private Set<String> titles = new HashSet<String>();
    //Activity callback
    private ActivityCallback mCallback;
    private static final String TAG = "MainActivity";
    private String masterUri;




    //create instance of this fragment
    public static DashboardFragment newInstance() {
        return new DashboardFragment();
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
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        edtArtist = root.findViewById(R.id.edtArtist);
        edtAlbum = root.findViewById(R.id.edtAlbum);

        //all our buttons should use the same on click, each has it's own case below
        Button btnSave = root.findViewById(R.id.BtnSearch);
        btnSave.setOnClickListener(this);

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                mCallback.openHomeFragment();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);


        return root;
    }




    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.BtnSearch) {
            addAlbum();}

    }


    private void addAlbum() {
        final String artistInput = edtArtist.getText().toString();
        final String albumInput = edtAlbum.getText().toString();
        String searchType;
        String query;

        //
        if (!artistInput.equals("") && !albumInput.equals("")){
            searchType = "title";
            query = artistInput + " - " + albumInput;
            searchAlbums(searchType, query);
        } else if (artistInput.equals("") && !albumInput.equals("")) {
            searchType = "album";
            query = albumInput;
            searchAlbums(searchType, query);
        } else {
            searchType = "artist";
            query = artistInput;
            searchArtist(searchType, query);
        }

    }

    private String createSearchURL(String search, String query){
        String searchEncoded = null;
        String queryEncoded = null;
        try {
            searchEncoded = URLEncoder.encode(search, "UTF-8");
            queryEncoded = URLEncoder.encode(query, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.w(TAG, "Error encoding URL", e);
        }
        String url = "https://api.discogs.com/database/search?type=" + searchEncoded + "&q=" + queryEncoded + "&key=" + BuildConfig.CONSUMER_KEY + "&secret=" + BuildConfig.CONSUMER_SECRET;
        return url;
    }

    private String createArtistSearchURL(int id) throws Exception {
        if (id < 0){
            throw new Exception("artist id must be greater than zero!");
        } else {
            String artistId = Integer.toString(id);
            String idEncoded = null;
            try {
                idEncoded = URLEncoder.encode(artistId, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                Log.w(TAG, "Error encoding URL", e);
            }
            String url = "https://api.discogs.com/artists/" + idEncoded + "/releases?sort=year&sort_order=asc&per_page=100&key=" + BuildConfig.CONSUMER_KEY + "&secret=" + BuildConfig.CONSUMER_SECRET;
            return url;
        }
    }

    private void searchAlbums(String searchType, String query){
        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                createSearchURL(searchType, query),
                null,
                response -> {
                    //Convert Json to Album Object to add to DB
                    Log.w("Rest Response", response.toString());
                    try {
                        JSONArray jsonArray = response.getJSONArray("results");
                        for (int i = 0; i < 50; i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            try {
                                if (!jsonObject.getString("title").isEmpty() && !jsonObject.getString("year").isEmpty()
                                        && !jsonObject.getString("thumb").isEmpty() && !jsonObject.getJSONArray("style").isNull(0)
                                        && !jsonObject.getJSONArray("genre").isNull(0)) {
                                    Log.w("Discogs Album result ", jsonObject.toString());
                                    String artist, album, year, title, url, masterUrl;
                                    ArrayList<String> styles = new ArrayList<>();
                                    ArrayList<String> genres = new ArrayList<>();
                                    url = jsonObject.getString("thumb");
                                    title = jsonObject.getString("title");
                                    masterUrl = jsonObject.getString("master_url");
                                    getMasterUrl(masterUrl);
                                    String[] parseString = title.split(" - ", 2);
                                    artist = parseString[0];
                                    album = parseString[1];
                                    year = jsonObject.getString("year");
                                    for (int j = 0; j < jsonObject.getJSONArray("style").length(); j++){
                                        styles.add(jsonObject.getJSONArray("style").getString(j));
                                    }
                                    for (int k = 0; k < jsonObject.getJSONArray("genre").length(); k++){
                                        genres.add(jsonObject.getJSONArray("genre").getString(k));
                                    }
                                    Album albuml = new Album(artist, album, year, styles, genres, url, masterUri);
                                    arrayResults.add(albuml);
                                }
                            } catch (JSONException e){
                                Log.w("Result was not an album", e);
                            }
                        }
                        for (Album item : arrayResults) {
                            if (titles.add(item.year)){
                                arrayDisplayResults.add(item); //FIXME
                            }
                        }
                    } catch (JSONException e) {
                        openAlbumDialog();
                    }
                    mCallback.openResultsFragment(arrayDisplayResults);

                },
                error -> Log.w("Error requesting Json data", error.toString())
        );
        requestQueue.add(objectRequest);

    }

    private void searchArtist(String searchType, String query){
        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                createSearchURL(searchType, query),
                null,
                response -> {
                    Log.w("Searching Artists...looking for id: ", response.toString());
                    try {
                        JSONArray jsonArray = response.getJSONArray("results");
                        JSONObject overallResultsObject = jsonArray.getJSONObject(0);
                        artistId = overallResultsObject.getInt("id");
                        String albumsUrl = createArtistSearchURL(artistId);
                        searchAlbumByArtist(albumsUrl);
                    } catch (Exception e) {
                        openArtistDialog();
                    }
                },
                error -> Log.w("Error requesting Json data", error.toString())
        );
        requestQueue.add(objectRequest);
    }

    private void searchAlbumByArtist(String url){
        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    //Convert Json to Album Object to add to DB
                    Log.w("Searching Albums by Artist w/ id ", response.toString());
                    try {
                        JSONArray jsonArray = response.getJSONArray("releases");
                        for (int i = 0; i < 100; i++) {
                            String urll, artist, album, year, masterUrl, style;
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            if (jsonObject.getString("type").equals("master")){
                                urll = jsonObject.getString("thumb");
                                masterUrl = jsonObject.getString("resource_url");
                                getMasterUrl(masterUrl);
                                artist = jsonObject.getString("artist");
                                album = jsonObject.getString("title");
                                year = jsonObject.getString("year");
                                Album albuml = new Album(artist, album, year, urll, masterUrl);
                                arrayResults.add(albuml);
                            }
                        }
                    } catch (JSONException e) {
                        Log.w("No albums found with that artistID", e);
                    }
                    mCallback.openResultsFragment(arrayResults);
                },
                error -> Log.w("Error requesting Json data", error.toString())
        );
        requestQueue.add(objectRequest);
    }

    public void getMasterUrl(String url){
        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    Log.w("Retrieving album's master url ", response.toString());
                    try {
                        JSONArray jsonArray = response.getJSONArray("uri");
                        masterUri = jsonArray.getString(0);
                        mCallback.onSucess(masterUri);
                    } catch (JSONException e) {
                        Log.w("No uri found with for this album", e);
                    }
                },
                error -> Log.w("Error requesting Json data", error.toString())
        );
        requestQueue.add(objectRequest);
    }

    public void openArtistDialog() {
        ArtistDialog artistDialog = new ArtistDialog();
        artistDialog.show(getFragmentManager(), "No Artists");
    }

    public void openAlbumDialog() {
        AlbumDialog albumDialog = new AlbumDialog();
        albumDialog.show(getFragmentManager(), "No Results");
    }
}
