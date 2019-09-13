package com.example.alspicks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
/*import com.loopj.android.http.*;
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
*/
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtArtist, edtAlbum, edtYear, edtStyle;
    private Button btnSave;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private static final String TAG = "MainActivity";

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


        final ListView recordsView = (ListView)findViewById(R.id.records_view);


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

        // Create a new album with Artist, Album, Year, and Genre
        Map<String, Object> album = new HashMap<>();
        album.put("Artist", albumArtist);
        album.put("Album", albumName);
        album.put("Year", albumYear);
        album.put("Genre", albumStyle);

        // Add a new document with a generated ID
        db.collection("albums")
                .add(album)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);

                    }
                });

    }



}
