package com.example.alspicks.ui.notifications;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alspicks.ActivityCallback;
import com.example.alspicks.Album;
import com.example.alspicks.R;
import com.example.alspicks.RecyclerResultsAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class DialogueFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerResultsAdapter mAdapter;
    private ArrayList<Album> mArrayList = new ArrayList<>();
    private String user;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirestoreCallback firestoreCallback;
    private ArrayList<Album> userAlbums = new ArrayList<>();
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    //Activity callback
    private ActivityCallback mCallback;

    //create instance of this fragment
    public static DialogueFragment newInstance() {
        return new DialogueFragment();
    }




    //override the onAttach and onDetach methods in fragment lifecycle
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallback = (ActivityCallback) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                mCallback.openHomeFragment();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

        readData(new FirestoreCallback() {
            @Override
            public void onCallback(ArrayList<Album> albums) {
                mCallback.openNotificationsFragment(albums);
            }
        });

        return root;
    }

    public void readData(FirestoreCallback firestoreCallback) {
        final EditText taskEditText = new EditText(getContext());
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle("Who's list would you like to view?")
                .setMessage("Enter an email")
                .setView(taskEditText)
                .setPositiveButton("View", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        user = String.valueOf(taskEditText.getText());
                        db.collection("albums")
                                .whereEqualTo("receiver", currentUser.getEmail())
                                .whereEqualTo("sender", user)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()){
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                Map data = document.getData();
                                                String artist = String.valueOf(data.get("artist"));
                                                String albumName = String.valueOf(data.get("name"));
                                                String year = String.valueOf(data.get("year"));
                                                String coverImage = String.valueOf(data.get("coverImage"));
                                                ArrayList<String> styles = (ArrayList<String>) document.get("style");
                                                ArrayList<String> genres = (ArrayList<String>) document.get("genre");
                                                Album album = new Album(artist, albumName, year, styles, genres, coverImage);
                                                userAlbums.add(album);
                                                Log.d("Main Activity", document.getId() + " => " + document.getData());
                                            }
                                            firestoreCallback.onCallback(userAlbums);
                                        } else {
                                            Log.d("Main Activity", "Error getting documents: ", task.getException());
                                        }
                                    }
                                });
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

    public interface FirestoreCallback {
        void onCallback(ArrayList<Album> albums);
    }
}
