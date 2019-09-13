package com.example.alspicks; //FIXME

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//import android.support.annotation.Nullable;

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


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtArtist, edtAlbum, edtYear, edtStyle;
    private TextView recordArtist, recordAlbum, recordYear, recordStyle;
    private Button btnSave;
    private ArrayList<String> albumCollection = new ArrayList<String>();
    //ArrayAdapter<String> recordAdapter = new ArrayAdapter<String>(this, android.R.layout.records_view,);
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

        recordAlbum = (TextView)findViewById(R.id.record_artist);
        recordArtist = (TextView)findViewById(R.id.record_artist);
        recordYear = (TextView)findViewById(R.id.record_year);
        recordStyle = (TextView)findViewById(R.id.record_style);

        btnSave = (Button)findViewById(R.id.BtnSave);
        btnSave.setOnClickListener(this);


        final ListView recordsView = (ListView)findViewById(R.id.records_view);


    }

    @Override
    public void onClick(View v) {
        addAlbum();
    }

    private void addAlbum() {



        final String albumArtist = edtArtist.getText().toString();

        final String albumName = edtAlbum.getText().toString();
        final String albumYear = edtYear.getText().toString();
        final String albumStyle = edtStyle.getText().toString();

        // return if input fields are empty
        if (albumArtist.equals("") && albumName.equals("") && albumYear.equals("") && albumStyle.equals("")){
            return;
        }

        // Create a new album with Artist, Album, Year, and Genre
        final CollectionReference albums = db.collection("albums");

        final Map<String, Object> album = new HashMap<>();
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
                        //albums.document(albumName).set(album);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);

                    }
                });

        /*DocumentReference docRef = db.collection("albums").document(albumName);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        //POPULATE LISTVIEW WITH ARRAYADAPTER

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });*/

    }



}
