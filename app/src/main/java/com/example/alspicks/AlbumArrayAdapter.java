package com.example.alspicks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class AlbumArrayAdapter extends ArrayAdapter<Album> {

    public AlbumArrayAdapter(Context context, ArrayList<Album> albums) {
        super(context, 0, albums);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Album album = getItem(position);
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.record, parent, false);
        }
        TextView name = convertView.findViewById(R.id.record_album);
        TextView artist = convertView.findViewById(R.id.record_artist);
        TextView year = convertView.findViewById(R.id.record_year);
        TextView style = convertView.findViewById(R.id.record_style);

        name.setText(album.getName());
        artist.setText(album.getArtist());
        year.setText(album.getYear());
        style.setText(album.getStyle());

        return convertView;
    }

}
