package com.example.alspicks;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.*;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.PusherEvent;
import com.pusher.client.channel.SubscriptionEventListener;

import org.json.JSONArray;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtArtist, edtAlbum, edtYear, edtStyle;
    private Button btnSave;
    private static final String RECORDS_ENDPOINT = "http://localhost:3003/my-channel";
    private RecordsAdapter recordAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



            edtArtist = (EditText)findViewById(R.id.edtArtist);
            edtAlbum = (EditText)findViewById(R.id.edtAlbum);
            edtYear = (EditText)findViewById(R.id.edtYear);
            edtStyle = (EditText)findViewById(R.id.edtStyle);

        btnSave = (Button)findViewById(R.id.BtnSave);

        btnSave.setOnClickListener(this);

        recordAdapter = new RecordsAdapter(this, new ArrayList<Record>());
        final ListView recordsView = (ListView)findViewById(R.id.records_view);
        recordsView.setAdapter(recordAdapter);


        PusherOptions options = new PusherOptions();
        options.setCluster("us2");
        Pusher pusher = new Pusher("39233d6b9a974060a27d", options);
        Channel channel = pusher.subscribe("my-channel");
        channel.bind("my-event", new SubscriptionEventListener() {
            @Override
            public void onEvent(final PusherEvent event) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson = new Gson();
                        Record record = gson.fromJson(event.getData(), Record.class);
                        recordAdapter.add(record);
                        recordsView.setSelection(recordAdapter.getCount() - 1);
                    }
                });
            }
        });
        pusher.connect();
    }

    @Override
    public void onClick(View v) {
        addAlbum();
    }

    private void addAlbum() {

        String albumArtist = edtArtist.getText().toString();
        String albumName = edtAlbum.getText().toString();
        String albumYear = edtYear.getText().toString();
        String albumStyle = edtStyle.getText().toString();

        // return if input fields are empty
        if (albumArtist.equals("") && albumName.equals("") && albumYear.equals("") && albumStyle.equals("")){
            return;
        }

        RequestParams params = new RequestParams();

        // set our JSON object
        params.put("artist", albumArtist);
        params.put("album", albumName);
        params.put("year", albumYear);
        params.put("style", albumStyle);

        // create our HTTP client
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(RECORDS_ENDPOINT, params, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        edtAlbum.setText("");
                        edtArtist.setText("");
                        edtYear.setText("");
                        edtStyle.setText("");
                    }
                });
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(
                        getApplicationContext(),
                        "Something went wrong",
                        Toast.LENGTH_LONG
                ).show();
            }
        });

    }


}
