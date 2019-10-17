package com.example.alspicks.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.alspicks.Album;
import com.example.alspicks.AlbumArrayAdapter;
import com.example.alspicks.NewTunes;
import com.example.alspicks.R;
import com.example.alspicks.SharedViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class DashboardFragment extends Fragment {

    private ListView albumsListView;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        albumsListView = root.findViewById(R.id.records_view);
        SharedViewModel sharedViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(SharedViewModel.class);
        String userID = sharedViewModel.getUid();


        if(!userID.equals("")) {
            db.collection(userID).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    ArrayList<Album> albumArrayList = new ArrayList<Album>();
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            Album albums = document.toObject(Album.class);
                            albumArrayList.add(albums);
                        }
                        ArrayList<Album> artistSort = NewTunes.defaultSort(albumArrayList);
                        AlbumArrayAdapter albumArrayAdapter = new AlbumArrayAdapter(root.getContext(), artistSort);
                        albumArrayAdapter.notifyDataSetChanged();
                        albumsListView.setAdapter(albumArrayAdapter);
                    }

                }
            });
        }

        return root;
    }
}