package com.example.alspicks;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Collectors;

public class NewTunes extends AppCompatActivity {
    private ListView albumsListView;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
sort
        //setContentView(R.layout.newtunes_style);
        setTheme(R.style.Theme_darkTheme);
master
        albumsListView = findViewById(R.id.records_view);
        Button btnMain = findViewById(R.id.BtnMain);

        btnMain.setOnClickListener(v -> openMain());

        db.collection("albums").get().addOnCompleteListener(task -> {
            ArrayList<Album> albumArrayList = new ArrayList<>();
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                    Album albums = document.toObject(Album.class);
                    albumArrayList.add(albums);
                }
                ArrayList<Album> artistSort = defaultSort(albumArrayList);
                AlbumArrayAdapter albumArrayAdapter = new AlbumArrayAdapter(NewTunes.this, artistSort);
                albumArrayAdapter.notifyDataSetChanged();
                albumsListView.setAdapter(albumArrayAdapter);
            }

        });


    }

    public void openMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    //Our default sorts by Artist Name
    public static ArrayList<Album> defaultSort(ArrayList<Album> albumArrayList){
        return (ArrayList<Album>) albumArrayList.stream().sorted(Comparator.comparing(Album::getArtist)).collect(Collectors.toList());
    }

    public static ArrayList<Album> yearSort(ArrayList<Album> albumArrayList) {
        return (ArrayList<Album>) albumArrayList.stream().sorted(Comparator.comparing(Album::getYear)).collect(Collectors.toList());
    }

    public static ArrayList<Album> styleSort(ArrayList<Album> albumArrayList) {
        return (ArrayList<Album>) albumArrayList.stream().sorted(Comparator.comparing(Album::getStyle)).collect(Collectors.toList());
    }
}
