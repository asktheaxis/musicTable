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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.alspicks.NewTunes;
import com.example.alspicks.R;
import com.example.alspicks.SharedViewModel;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class HomeFragment extends Fragment implements View.OnClickListener{

    //----copied from MainActivity----//
    private EditText edtArtist, edtAlbum, edtYear, edtStyle;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

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
                edtAlbum.getText().clear();
                edtArtist.getText().clear();
                edtStyle.getText().clear();
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
        final String albumStyle = edtStyle.getText().toString();
        final String userID = sharedViewModel.getUid();
        final String albumPath;

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
        album.put("origUser", sharedViewModel.getUid());



        // Add a new document with a generated ID
        if(!userID.equals("")){
            db.collection("albums")
                    .document(albumName)
                    .set(album)
                    .addOnSuccessListener(documentReference -> {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        albumAdded.show();
                    })
                    .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));

        }

    }

}
