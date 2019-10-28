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
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.alspicks.BuildConfig;
import com.example.alspicks.NewTunes;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class HomeFragment extends Fragment implements View.OnClickListener{

    //----copied from MainActivity----//
    private EditText edtArtist, edtAlbum, edtYear;
    private Spinner edtStyle;
    private ImageView imageView1, imageView2, imageView3;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String albumNameEncoded, artistNameEncoded;
    private RequestQueue requestQueue;

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
        edtYear = root.findViewById(R.id.edtYear);
        edtStyle = root.findViewById(R.id.edtStyle);
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
        switch (v.getId()) {

            case R.id.BtnSave:
                addAlbum();
                edtStyle.setSelection(0);
                edtYear.getText().clear();
            break;

            case R.id.BtnTunes:
                openNewTunes();
                break;

            default:
                break;
        }
    }

    //we can probably relocate new tunes to one of the open fragments instead of another activity
    private void openNewTunes() {
        Intent intent = new Intent(getActivity(), NewTunes.class);
        startActivity(intent);
    }



    private void addAlbum() {

        SharedViewModel sharedViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(SharedViewModel.class);
        final String albumArtist = edtArtist.getText().toString();
        final String albumName = edtAlbum.getText().toString();
        final String albumYear = edtYear.getText().toString();
        final String albumStyle = edtStyle.getSelectedItem().toString();
        final String userID = sharedViewModel.getUid();
        final String albumPath;

        searchArtist(albumArtist, albumName);


        // Create a new album with Artist, Album, Year, and Genre
        final Map<String, Object> album = new HashMap<>();
        album.put("artist", albumArtist);
        album.put("name", albumName);
        album.put("year", albumYear);
        album.put("style", albumStyle);
        album.put("origUser", sharedViewModel.getUid());


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


    /*private String createAlbumSearchURL(String name){
        try {
            albumNameEncoded = URLEncoder.encode(name, "UTF-8");
        } catch (UnsupportedEncodingException e){
            Log.w(TAG, "Error encoding URL", e);
        }
        String url = "https://api.discogs.com/database/search?type=release_title&q=" + albumNameEncoded + "&key=" + BuildConfig.CONSUMER_KEY + "&secret=" + BuildConfig.CONSUMER_SECRET;
        return url;
    }*/

    private String createArtistSearchURL(String artist, String album){
        try {
            artistNameEncoded = URLEncoder.encode(artist, "UTF-8");
            albumNameEncoded = URLEncoder.encode(album, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.w(TAG, "Error encoding URL", e);
        }
        //FIXME https://api.discogs.com/database/search?type=album&q=clear%20spot&key=JmAzdrpGIbiMhPzTgmQc&secret=ucIcmmBonEOxgCqmxlwsEPfRfArHigfK
        String url = "https://api.discogs.com/database/search?q={?title=" + artistNameEncoded + " - " + albumNameEncoded + "}&key=" + BuildConfig.CONSUMER_KEY + "&secret=" + BuildConfig.CONSUMER_SECRET;
        return url;
    }

    /*private void buildRequest(String albumName){
        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                createAlbumSearchURL(albumName),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response){
                        //Convert Json to Album Object to add to DB
                        Log.w("Rest Response", response.toString());
                        try {
                            JSONArray jsonArray = response.getJSONArray("results");
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            Log.w("The first result from Discogs is ", jsonObject.toString());
                            String title = jsonObject.getString("title");
                            Log.w("It's title is ", title);
                            JSONArray styles = jsonObject.getJSONArray("style");
                            Log.w("It's style(s) are ", styles.getString(0) + ", ");
                            String year = jsonObject.getString("year");
                            Log.w("Year of release ", year);
                            String url = jsonObject.getString("thumb");
                            //fetchAlbumImages(url);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.w("Error requesting Json data", error.toString());
                    }
                }
        );

        requestQueue.add(objectRequest);
    }*/

    private void searchArtist(String artist, String album){
        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                createArtistSearchURL(artist, album),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response){
                        //Convert Json to Album Object to add to DB
                        Log.w("Rest Response", response.toString());
                        try {
                            JSONArray jsonArray = response.getJSONArray("results");
                            JSONObject overallResultsObject = jsonArray.getJSONObject(0);
                            overallResultsObject.get("year");
                            ArrayList<String> urls = new ArrayList<>();
                            final Map<String, Object> album = new HashMap<>();
                            //album.put("artist", albumArtist);
                            //album.put("name", albumName);
                            //album.put("year", albumYear);
                            //album.put("style", albumStyle);
                            //album.put("origUser", sharedViewModel.getUid());
                            for (int i = 0; i < 4; i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String url = jsonObject.getString("thumb");
                                urls.add(url);
                                Log.w("Discogs result ", jsonObject.toString());
                                JSONArray styles = jsonObject.getJSONArray("style");
                                Log.w("It's style(s) are ", styles.getString(0) + ", ");
                                String year = jsonObject.getString("year");
                                Log.w("Year of release ", year);
                            }
                            Picasso.with(getContext()).load(urls.get(0)).fit().into(imageView1);
                            Picasso.with(getContext()).load(urls.get(1)).fit().into(imageView2);
                            Picasso.with(getContext()).load(urls.get(2)).fit().into(imageView3);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.w("Error requesting Json data", error.toString());
                    }
                }
        );

        requestQueue.add(objectRequest);
    }

    /*private void searchAlbum(String albumName){
        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                createAlbumSearchURL(albumName),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response){
                        //Convert Json to Album Object to add to DB
                        Log.w("Rest Response", response.toString());
                        try {
                            JSONArray jsonArray = response.getJSONArray("results");
                            ArrayList<String> urls = new ArrayList<>();
                            for (int i = 0; i < 4; i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String url = jsonObject.getString("thumb");
                                urls.add(url);
                            }

                            Picasso.with(getContext()).load(urls.get(0)).fit().into(imageView1);
                            Picasso.with(getContext()).load(urls.get(1)).fit().into(imageView2);
                            Picasso.with(getContext()).load(urls.get(2)).fit().into(imageView3);
                            /*Log.w("The first result from Discogs is ", jsonObject.toString());
                            String title = jsonObject.getString("title");
                            Log.w("It's title is ", title);
                            JSONArray styles = jsonObject.getJSONArray("style");
                            Log.w("It's style(s) are ", styles.getString(0) + ", ");
                            String year = jsonObject.getString("year");
                            Log.w("Year of release ", year);
                            String url = jsonObject.getString("thumb");
                            fetchAlbumImages(url);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.w("Error requesting Json data", error.toString());
                    }
                }
        );

        requestQueue.add(objectRequest);
    }*/

    /*private void fetchAlbumImages(String url) {
        Picasso.with(getContext()).load(url).fit().into(imageView);
    }*/

    /*public void buildRequest(String albumName) {

        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
        StringRequest stringRequest = new StringRequest(createAlbumSearchURL(albumName), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jo = jsonArray.getJSONObject(i);
                        // Do you fancy stuff
                        // Example: String gifUrl = jo.getString("url");
                        Log.w("Successfully returned JSON", response);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.w("Volley JSON error", error);
            }
        });
        requestQueue.add(stringRequest);
    }*/

}
