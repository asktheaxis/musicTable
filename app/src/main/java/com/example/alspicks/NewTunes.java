package com.example.alspicks;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

public class NewTunes extends AppCompatActivity {
    private ListView albumsListView;
    private EditText edtArtist, edtAlbum, edtYear, edtStyle;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newtunes_style);
        edtArtist = findViewById(R.id.edtArtist);
        edtAlbum = findViewById(R.id.edtAlbum);
        edtYear = findViewById(R.id.edtYear);
        edtStyle = findViewById(R.id.edtStyle);
        albumsListView = findViewById(R.id.records_view);
        Button btnMain = findViewById(R.id.BtnMain);

        btnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMain();
            }
        });

        db.collection("albums").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                ArrayList<Album> albumArrayList = new ArrayList<Album>();
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Album albums = document.toObject(Album.class);
                        albumArrayList.add(albums);
                    }
                    ArrayList<Album> artistSort = defaultSort(albumArrayList);
                    AlbumArrayAdapter albumArrayAdapter = new AlbumArrayAdapter(NewTunes.this, artistSort);
                    albumArrayAdapter.notifyDataSetChanged();
                    albumsListView.setAdapter(albumArrayAdapter);
                }

            }
        });


    }

    public void openMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public ArrayList<Album> defaultSort(ArrayList<Album> albumArrayList){
        return (ArrayList<Album>) albumArrayList.stream().sorted(Comparator.comparing(Album::getArtist)).collect(Collectors.toList());
    }
}
