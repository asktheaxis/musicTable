package com.example.alspicks.ui.home;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;


class HomeFragmentTest extends HomeFragment {

    @Test
    void createSearchURL() throws IOException {
        String url = createSearchURL("artist", "frank zappa");
        final URL urll = new URL(url);
        HttpURLConnection huc = (HttpURLConnection) urll.openConnection();
        huc.setRequestMethod("HEAD");
        int responseCode = huc.getResponseCode();

        Assert.assertEquals(HttpURLConnection.HTTP_OK, responseCode);

    }

    @Test
    void searchAlbums() throws IOException {
        //searchAlbums("album", "sleep dirt");
        ArrayList<String> urls = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                createSearchURL("album", "sleep dirt"),
                null,
                response -> {
                    try {
                        JSONArray jsonArray = response.getJSONArray("results");
                        for (int i = 0; i < 4; i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String url = jsonObject.getString("cover_image");
                            urls.add(url);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.w("Error requesting Json data", error.toString())
        );
        requestQueue.add(objectRequest);

        for(String link : urls){
            final URL url = new URL(link);
            HttpURLConnection huc = (HttpURLConnection) url.openConnection();
            huc.setRequestMethod("HEAD");
            String result = huc.getContentType();
            if (!result.equals("image")){
                break;
            }
        }
    }


}