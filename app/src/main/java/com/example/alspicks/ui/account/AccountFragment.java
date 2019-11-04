package com.example.alspicks.ui.account;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.alspicks.R;
import com.example.alspicks.SharedViewModel;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Objects.requireNonNull;

public class AccountFragment extends Fragment implements View.OnClickListener {

    private AccountViewModel accountViewModel;
    private static final int RC_SIGN_IN = 9001;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "MainActivity";
    private TextView userTextView;
    FirebaseUser user;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        accountViewModel = ViewModelProviders.of(this).get(AccountViewModel.class);
        View root = inflater.inflate(R.layout.fragment_account, container, false);

        user = FirebaseAuth.getInstance().getCurrentUser();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(requireNonNull(getActivity()), gso);

        final TextView textView = root.findViewById(R.id.text_account);
        userTextView = root.findViewById(R.id.userName);
        accountViewModel.getText().observe(this, textView::setText);

        SignInButton btnGoogleSignIn = root.findViewById(R.id.sign_in_button);
        btnGoogleSignIn.setOnClickListener(this);

        Button btnSignOut = root.findViewById(R.id.btnSignOut);
        btnSignOut.setOnClickListener(this);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SharedViewModel sharedViewModel = ViewModelProviders.of(requireNonNull(getActivity())).get(SharedViewModel.class);
        sharedViewModel.setUserNameText().observe(getViewLifecycleOwner(), s -> userTextView.setText(s));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnSignOut: //each button gets a case
                signOut();
                break;

            case R.id.sign_in_button:
                createSignInIntent();
                break;

            default:
                break;
        }
    }

    private void createSignInIntent() {
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
                user = FirebaseAuth.getInstance().getCurrentUser();
                SharedViewModel sharedViewModel = ViewModelProviders.of(requireNonNull(getActivity())).get(SharedViewModel.class);
                sharedViewModel.setUser(user);
                sharedViewModel.setUserId(requireNonNull(user));
                ArrayList<String> userInfo = buildUserInfo(user);
                String userName = userInfo.get(1);
                accountViewModel = ViewModelProviders.of(getActivity()).get(AccountViewModel.class);
                accountViewModel.setUserName(userName);
                Toast.makeText(getActivity(), ""+user.getEmail(), Toast.LENGTH_SHORT).show();
                FirebaseUserMetadata metadata = user.getMetadata();
                assert metadata != null;
                if (metadata.getCreationTimestamp() == metadata.getLastSignInTimestamp()){
                    customUser(user);
                } else {
                    //Welcome Back Toast?? FIXME
                }
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
                assert response != null;
                Toast.makeText(getActivity(), ""+ requireNonNull(response.getError()).getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
    // [END auth_fui_result]

    private ArrayList<String> buildUserInfo(FirebaseUser user){
        String uid = user.getUid();
        String email = user.getEmail();
        ArrayList<String> userInfo = new ArrayList<>();
        userInfo.add(uid);
        userInfo.add(email);
        return userInfo;
    }

    private void customUser(FirebaseUser user){
        SharedViewModel sharedViewModel = ViewModelProviders.of(requireNonNull(getActivity())).get(SharedViewModel.class);

        String username = sharedViewModel.buildUserName();

        Map<String, Object> newUser = new HashMap<>();
        newUser.put("Username", username);
        newUser.put("UID", user.getUid());



        db.collection("Users").document(username)
                .set(newUser)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });

        // Create a dummy album with Artist, Album, Year, and Genre
        final Map<String, Object> firstAlbum = new HashMap<>();
        firstAlbum.put("username", username);

        //creates Users/USERNAME/Incoming collection with 1 blank doc
        db.collection("Users")
                .document(username)
                .collection("Incoming")
                .add(firstAlbum)
                    .addOnSuccessListener(documentReference -> {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    })
                    .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));

        //creates Users/USERNAME/Outgoing collection with 1 blank doc
        db.collection("Users")
                .document(username)
                .collection("Outgoing")
                .document("DUMMY")
                .set(firstAlbum)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "DocumentSnapshot successfully written!");
                })
                .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));



    }

    private void signOut() {
        SharedViewModel sharedViewModel = ViewModelProviders.of(requireNonNull(getActivity())).get(SharedViewModel.class);
        // [START auth_fui_signout]
        AuthUI.getInstance()
                .signOut(requireNonNull(getActivity()))
                .addOnCompleteListener(task -> {
                    sharedViewModel.clearUserTextBox();
                    sharedViewModel.signOut();
                    sharedViewModel.clearUserId();
                });
        // [END auth_fui_signout]
    }

    public void delete() {
        // [START auth_fui_delete]
        AuthUI.getInstance()
                .delete(requireNonNull(getActivity()))
                .addOnCompleteListener(task -> {
                    // ...
                });
        // [END auth_fui_delete]
    }

}