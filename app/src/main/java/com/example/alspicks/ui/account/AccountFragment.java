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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //----copied from MainActivity----//
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(requireNonNull(getActivity()), gso);
        //----copied from MainActivity----//


    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        accountViewModel = ViewModelProviders.of(this).get(AccountViewModel.class);
        View root = inflater.inflate(R.layout.fragment_account, container, false);
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
        accountViewModel = ViewModelProviders.of(requireNonNull(getActivity())).get(AccountViewModel.class);
        accountViewModel.getUserName().observe(getViewLifecycleOwner(), s -> userTextView.setText(s));
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
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                SharedViewModel sharedViewModel = ViewModelProviders.of(requireNonNull(getActivity())).get(SharedViewModel.class);
                sharedViewModel.setUserId(requireNonNull(user));
                ArrayList<String> userInfo = buildUserInfo(user);
                String userName = userInfo.get(1);
                accountViewModel = ViewModelProviders.of(getActivity()).get(AccountViewModel.class);
                accountViewModel.setUserName(userName);
                Toast.makeText(getActivity(), ""+user.getEmail(), Toast.LENGTH_SHORT).show();
                customUser(user);
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

        Map<String, Object> newUser = new HashMap<>();
        newUser.put("Name", user.getEmail());
        newUser.put("UID", user.getUid());
        newUser.put("Music", Collections.emptyList());
        db.collection("Users").add(newUser)
                .addOnSuccessListener(documentReference -> Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId()))
                .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));
    }

    private void signOut() {
        // [START auth_fui_signout]
        AuthUI.getInstance()
                .signOut(requireNonNull(getActivity()))
                .addOnCompleteListener(task -> accountViewModel.clearUserName());
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