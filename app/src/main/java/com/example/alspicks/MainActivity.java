package com.example.alspicks;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //bjhgjkhjhkj
    private EditText edtArtist, edtAlbum, edtYear, edtStyle;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private static final String TAG = "MainActivity";
    private ListView albumsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtArtist = findViewById(R.id.edtArtist);
        edtAlbum = findViewById(R.id.edtAlbum);
        edtYear = findViewById(R.id.edtYear);
        edtStyle = findViewById(R.id.edtStyle);

        Button btnSave = findViewById(R.id.BtnSave);
        btnSave.setOnClickListener(this);
        Button btnTunes = findViewById(R.id.BtnTunes);

        btnTunes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewTunes();
            }
        });

        albumsListView = findViewById(R.id.records_view);

        db.collection("albums").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                ArrayList<Album> albumArrayList = new ArrayList<Album>();
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        Album albums = document.toObject(Album.class);
                        albumArrayList.add(albums);
                    }
                    AlbumArrayAdapter albumArrayAdapter = new AlbumArrayAdapter(MainActivity.this, albumArrayList);
                    albumArrayAdapter.notifyDataSetChanged();
                    albumsListView.setAdapter(albumArrayAdapter);
                }

            }
        });
    }

    public void openNewTunes() {
        Intent intent = new Intent(this, NewTunes.class);
        startActivity(intent);
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
        final Map<String, Object> album = new HashMap<>();
        album.put("artist", albumArtist);
        album.put("name", albumName);
        album.put("year", albumYear);
        album.put("style", albumStyle);

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
