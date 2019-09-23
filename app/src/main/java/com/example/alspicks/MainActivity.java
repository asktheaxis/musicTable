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

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
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

    private EditText edtArtist, edtAlbum, edtYear, edtStyle;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private static final String TAG = "MainActivity";
    private ListView albumsListView;

    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


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

    private void signIn() {
        // Launches the sign in flow, the result is returned in onActivityResult
        Intent intent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(intent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task =
                    GoogleSignIn.getSignedInAccountFromIntent(data);
            if (task.isSuccessful()) {
                // Sign in succeeded, proceed with account
                GoogleSignInAccount acct = task.getResult();
            } else {
                // Sign in failed, handle failure and update UI
                // ...
            }
        }
    }

    private void SignOut(){
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
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
