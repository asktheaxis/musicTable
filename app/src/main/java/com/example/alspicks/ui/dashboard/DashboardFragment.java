package com.example.alspicks.ui.dashboard;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.alspicks.ActivityCallback;
import com.example.alspicks.Album;
import com.example.alspicks.AlbumArrayAdapter;
import com.example.alspicks.R;
import com.example.alspicks.SharedViewModel;
import com.example.alspicks.ui.home.HomeFragment;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class DashboardFragment extends Fragment {

    private ListView albumsListView;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private View root;
    private ActivityCallback mCallback;

    //create instance of this fragment
    public static DashboardFragment newInstance() {
        return new DashboardFragment();
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
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.newtunes_style, container, false);
        albumsListView = root.findViewById(R.id.records_view);
        SharedViewModel sharedViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(SharedViewModel.class);
        String userID = sharedViewModel.getUid();


        TextView artist = root.findViewById(R.id.header_artist);
        artist.setOnClickListener(v -> onClick(v));
        TextView year = root.findViewById(R.id.header_year);
        year.setOnClickListener(v -> onClick(v));
        TextView style = root.findViewById(R.id.header_style);
        style.setOnClickListener(v -> onClick(v));
        TextView album = root.findViewById(R.id.header_album);
        album.setOnClickListener(v -> onClick(v));

        getAlbums(userID);

        return root;
    }


    private void getAlbums(String userID){
        if(!userID.equals("")) {
            db.collection("albums")
                    .whereEqualTo("origUser", userID)
                    .get().addOnCompleteListener(task -> {
                ArrayList<Album> albumArrayList = new ArrayList<>();
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                        Album albums = document.toObject(Album.class);
                        albumArrayList.add(albums);
                    }
                    AlbumArrayAdapter albumArrayAdapter = new AlbumArrayAdapter(root.getContext(), albumArrayList);
                    albumArrayAdapter.notifyDataSetChanged();
                    albumsListView.setAdapter(albumArrayAdapter);
                }

            });
        }
    }

    private void getAlbums(String userID, int sort){
        if(!userID.equals("")) {
            db.collection("albums")
                    .whereEqualTo("origUser", userID)
                    .get().addOnCompleteListener(task -> {
                ArrayList<Album> albumArrayList = new ArrayList<>();
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                        Album albums = document.toObject(Album.class);
                        albumArrayList.add(albums);
                    }
                    AlbumArrayAdapter albumArrayAdapter = new AlbumArrayAdapter(root.getContext(), albumArrayList);
                    albumArrayAdapter.notifyDataSetChanged();
                    albumsListView.setAdapter(albumArrayAdapter);
                }

            });
        }
    }

    public void artistClick(View v) {
        SharedViewModel sharedViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(SharedViewModel.class);
        String userID = sharedViewModel.getUid();
        getAlbums(userID);
    }


    private void yearClick(View v){
        SharedViewModel sharedViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(SharedViewModel.class);
        String userID = sharedViewModel.getUid();
        getAlbums(userID, 1);
    }

    private void styleClick(View v) {
        SharedViewModel sharedViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(SharedViewModel.class);
        String userID = sharedViewModel.getUid();
        getAlbums(userID, 2);
    }


    private void albumClick(View v) {
        SharedViewModel sharedViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(SharedViewModel.class);
        String userID = sharedViewModel.getUid();
        getAlbums(userID, 3);
    }

    private void onClick(View v) {
        switch (v.getId()) {

            case R.id.header_artist:
                artistClick(v);
                break;

            case R.id.header_year:
                yearClick(v);
                break;

            case R.id.header_style:
                styleClick(v);
                break;

            case R.id.header_album:
                albumClick(v);
                break;

            default:
                break;
        }
    }


}