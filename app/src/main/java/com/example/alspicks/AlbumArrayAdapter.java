package com.example.alspicks;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class AlbumArrayAdapter extends ArrayAdapter<Album> {

    public AlbumArrayAdapter(Context context, ArrayList<Album> albums) {
        super(context, 0, albums);
    }
}
