package com.example.alspicks.ui.home;


import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.appcompat.app.AppCompatActivity;

import com.example.alspicks.NewTunes;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import com.example.alspicks.R;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment implements View.OnClickListener{

    private HomeViewModel homeViewModel;

    //----copied from MainActivity----//
    private EditText edtArtist, edtAlbum, edtYear, edtStyle;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private static final String TAG = "MainActivity";
    private ListView albumsListView;

    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;
    //----copied from MainActivity----//


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //----copied from MainActivity----//
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        //----copied from MainActivity----//


    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);


        //vvv-copied from MainActivity-vvv//
        edtArtist = root.findViewById(R.id.edtArtist);
        edtAlbum = root.findViewById(R.id.edtAlbum);
        edtYear = root.findViewById(R.id.edtYear);
        edtStyle = root.findViewById(R.id.edtStyle);


        //all our buttons should use the same on click, each has it's own case below
        Button btnSave = root.findViewById(R.id.BtnSave);
        btnSave.setOnClickListener(this);

        Button btnTunes = root.findViewById(R.id.BtnTunes);
        btnTunes.setOnClickListener(this);

        SignInButton btnGoogleSignIn = root.findViewById(R.id.sign_in_button);
        btnGoogleSignIn.setOnClickListener(this);

        Button btnSignOut = root.findViewById(R.id.btnSignOut);
        btnSignOut.setOnClickListener(this);



        //^^^-copied from MainActivity-^^^//

        return root;
    }


    //vvv-copied from MainActivity-vvv//

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnSignOut: //each button gets a case
                signOut();
                break;

            case R.id.BtnSave:
                addAlbum();
                break;

            case R.id.BtnTunes:
                openNewTunes();
                break;

            case R.id.sign_in_button:
                createSignInIntent();
                break;

            default:
                break;
        }
    }


    public void createSignInIntent() {
        // [START auth_fui_create_intent]
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                //new AuthUI.IdpConfig.PhoneBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
        // [END auth_fui_create_intent]
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) customUser(user);
                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }
    // [END auth_fui_result]

    public void customUser(FirebaseUser user){

        Map<String, Object> newUser = new HashMap<>();
        newUser.put("Name", user.getEmail());
        newUser.put("UID", user.getUid());
        newUser.put("Music", Collections.emptyList());
        db.collection("Users").add(newUser)
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

    public void signOut() {
        // [START auth_fui_signout]
        AuthUI.getInstance()
                .signOut(getActivity())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
        // [END auth_fui_signout]
    }

    public void delete() {
        // [START auth_fui_delete]
        AuthUI.getInstance()
                .delete(getActivity())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
        // [END auth_fui_delete]
    }

    //we can probably relocate new tunes to one of the open fragments instead of another activity
    public void openNewTunes() {
        Intent intent = new Intent(getActivity(), NewTunes.class);
        startActivity(intent);
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