package com.example.alspicks.ui.home;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.alspicks.Album;
import com.example.alspicks.BuildConfig;
import com.example.alspicks.R;
import com.example.alspicks.SharedViewModel;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Objects;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class HomeFragment extends Fragment implements View.OnClickListener{

    //----copied from MainActivity----//
    private EditText edtArtist, edtAlbum;
    private ImageView imageView1, imageView2, imageView3;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String finalYear, finalArtist, finalAlbum;
    private ArrayList<String> albumStyles = new ArrayList<>();
    private int artistId = 0;
    ArrayList<Album> albumArrayList = new ArrayList<>();

    private static final String TAG = "MainActivity";

    //private GoogleSignInClient mGoogleSignInClient;
    //private static final int RC_SIGN_IN = 9001;
    //----copied from MainActivity----//
     @SuppressLint("RestrictedApi")
     private
    Context context = getApplicationContext();

    private CharSequence text = "Album added";
    private int duration = Toast.LENGTH_SHORT;

    private Toast albumAdded = Toast.makeText(context, text, duration);

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        edtArtist = root.findViewById(R.id.edtArtist);
        edtAlbum = root.findViewById(R.id.edtAlbum);

        //for testing
        imageView1 = root.findViewById(R.id.imageView3);
        imageView2 = root.findViewById(R.id.imageView4);
        imageView3 = root.findViewById(R.id.imageView5);

        //all our buttons should use the same on click, each has it's own case below
        Button btnSave = root.findViewById(R.id.BtnSave);
        btnSave.setOnClickListener(this);

        Button btnTunes = root.findViewById(R.id.BtnTunes);
        btnTunes.setOnClickListener(this);
        //^^^-copied from MainActivity-^^^//

        return root;
    }


    //vvv-copied from MainActivity-vvv//

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.BtnSave) {
            addAlbum();
        }
    }

    private void openResultsFragment() {
        Intent intent = new Intent(getActivity(), ResultsFragment.class);
        startActivity(intent);
    }

    private void addAlbum() {

        SharedViewModel sharedViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(SharedViewModel.class);
        final String artistInput = edtArtist.getText().toString();
        final String albumInput = edtAlbum.getText().toString();
        final String userID = sharedViewModel.getUid();
        final String albumPath;

        String searchType = null;
        String query = null;
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

        /*// Create a new album with Artist, Album, Year, and Genre
        final Map<String, Object> album = new HashMap<>();
        album.put("artist", albumArtist);
        album.put("name", albumName);
        album.put("year", albumYear);
        album.put("style", albumStyle);
        album.put("origUser", sharedViewModel.getUid());*/


        // Add a new document with a generated ID
        /*if(!userID.equals("")){
            db.collection("albums")
                    .document(albumName)
                    .set(album)
                    .addOnSuccessListener(documentReference -> {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        albumAdded.show();
                    })
                    .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));

        }*/

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
            String url = "https://api.discogs.com/artists/" + idEncoded + "/releases?sort=year&sort_order=asc&key=" + BuildConfig.CONSUMER_KEY + "&secret=" + BuildConfig.CONSUMER_SECRET;
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
                        SharedViewModel svModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(SharedViewModel.class);
                        for (int i = 0; i < 50; i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            try {
                                if (!jsonObject.getString("title").isEmpty() && !jsonObject.getString("year").isEmpty()
                                        && !jsonObject.getString("cover_image").isEmpty() && !jsonObject.getJSONArray("style").isNull(0)
                                        && !jsonObject.getJSONArray("genre").isNull(0)) {
                                    Log.w("Discogs Album result ", jsonObject.toString());
                                    String artist, album, year, title, url;
                                    ArrayList<String> styles = new ArrayList<>();
                                    ArrayList<String> genres = new ArrayList<>();
                                    url = jsonObject.getString("cover_image");
                                    title = jsonObject.getString("title");
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
                                    Album albuml = new Album(artist, album, year, styles, genres, url);
                                    albumArrayList.add(albuml);
                                    //svModel.setAlbumResults(albumArrayList); NEEDS TO BE ON CALLBACK
                                }
                            } catch (JSONException e){
                                Log.w("Result was not an album", e);
                            }
                        }
                    } catch (JSONException e) {
                        Log.w("Zero search results", e);
                    }
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
                        e.printStackTrace();
                    }
                },
                error -> Log.w("Error requesting Artists by id", error.toString())
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
                        ArrayList<String> urls = new ArrayList<>();
                        for (int i = 0; i < 4; i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String url1 = jsonObject.getString("thumb");
                            urls.add(url1);
                        }
                        Picasso.with(getContext()).load(urls.get(0)).fit().into(imageView1);
                        Picasso.with(getContext()).load(urls.get(1)).fit().into(imageView2);
                        Picasso.with(getContext()).load(urls.get(2)).fit().into(imageView3);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.w("Error requesting Albums by Artist", error.toString())
        );
        requestQueue.add(objectRequest);
    }
}
