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
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.alspicks.NewTunes;
import com.example.alspicks.R;
import com.example.alspicks.SharedViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class HomeFragment extends Fragment implements View.OnClickListener{

    private HomeViewModel homeViewModel;

    //----copied from MainActivity----//
    private EditText edtArtist, edtAlbum, edtYear, edtStyle;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private static final String TAG = "MainActivity";
    private ListView albumsListView;
    private SharedViewModel sharedViewModel;

    //private GoogleSignInClient mGoogleSignInClient;
    //private static final int RC_SIGN_IN = 9001;
    //----copied from MainActivity----//
     @SuppressLint("RestrictedApi")
    Context context = getApplicationContext();

    CharSequence text = "Album added";
    int duration = Toast.LENGTH_SHORT;

    Toast albumAdded = Toast.makeText(context, text, duration);


    /*@Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //----copied from MainActivity----//
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        //----copied from MainActivity----//


    }*/

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
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

        /*SignInButton btnGoogleSignIn = root.findViewById(R.id.sign_in_button);
        btnGoogleSignIn.setOnClickListener(this);

        Button btnSignOut = root.findViewById(R.id.btnSignOut);
        btnSignOut.setOnClickListener(this);*/



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
    public void openNewTunes() {
        Intent intent = new Intent(getActivity(), NewTunes.class);
        startActivity(intent);
    }



    private void addAlbum() {

        sharedViewModel = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
        final String albumArtist = edtArtist.getText().toString();
        final String albumName = edtAlbum.getText().toString();
        final String albumYear = edtYear.getText().toString();
        final String albumStyle = edtStyle.getText().toString();
        final String userID = sharedViewModel.getUid();

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
        db.collection(userID)
                .add(album)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        sharedViewModel.addAlbum(documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);

                    }
                });
        albumAdded.show();

    }

}
