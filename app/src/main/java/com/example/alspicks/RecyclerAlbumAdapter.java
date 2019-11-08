package com.example.alspicks;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAlbumAdapter extends RecyclerView.Adapter<RecyclerAlbumAdapter.MyViewHolder> {

    private ArrayList<Album> albumArrayList = new ArrayList<>();

    public RecyclerAlbumAdapter(ArrayList<Album> albums){
        this.albumArrayList = albums;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView album, artist, style, year;
        ImageButton albumArt;

        MyViewHolder(View itemView) {
            super(itemView);
            album = itemView.findViewById(R.id.tvAlbum);
            artist = itemView.findViewById(R.id.tvArtist);
            style = itemView.findViewById(R.id.tvStyle);
            year = itemView.findViewById(R.id.tvYear);
            albumArt = itemView.findViewById(R.id.albumButton);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Album album = albumArrayList.get(position);
        holder.artist.setText(album.getArtist());
        holder.album.setText(album.getName());
        holder.year.setText(album.getYear());
        for (int i = 0; i < album.style.size(); i++) {
            holder.style.append(album.style.get(i));
            holder.style.append(", ");
        }
    }

    @Override
    public int getItemCount() {
        return albumArrayList.size();
    }
}