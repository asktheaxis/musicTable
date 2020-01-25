package com.example.alspicks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerAlbumAdapter extends RecyclerView.Adapter<RecyclerAlbumAdapter.MyViewHolder> {

    private ArrayList<Album> albumArrayList;
    private Context context;

    public RecyclerAlbumAdapter(ArrayList<Album> albums){
        this.albumArrayList = albums;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView album, artist, style, year, genre;
        ImageButton albumArt;

        MyViewHolder(View itemView) {
            super(itemView);
            album = itemView.findViewById(R.id.tvAlbum);
            artist = itemView.findViewById(R.id.tvArtist);
            style = itemView.findViewById(R.id.tvStyle);
            genre = itemView.findViewById(R.id.tvGenre);
            year = itemView.findViewById(R.id.tvYear);
            albumArt = itemView.findViewById(R.id.albumButton);
        }
    }

    class ResultsViewHolder extends RecyclerView.ViewHolder {
        TextView album, artist, year;
        ImageButton albumArt;

        ResultsViewHolder(View itemView) {
            super(itemView);
            album = itemView.findViewById(R.id.tvAlbum);
            artist = itemView.findViewById(R.id.tvArtist);
            year = itemView.findViewById(R.id.tvYear);
            albumArt = itemView.findViewById(R.id.albumButton);
        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.results_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        Album album = albumArrayList.get(position);
        holder.artist.setText(album.getArtist());
        holder.album.setText(album.getName());
        holder.year.setText(album.getYear());
        holder.style.setText("");
        holder.genre.setText("");
        for (int i = 0; i < album.style.size(); i++) {
            if (i < album.style.size() - 1) {
                holder.style.append(album.style.get(i));
                holder.style.append(", ");
            } else {
                holder.style.append(album.style.get(i));
            }
        }
        for (int i = 0; i < album.genre.size(); i++) {
            if (i < album.genre.size() - 1) {
                holder.genre.append(album.genre.get(i));
                holder.genre.append(", ");
            } else {
                holder.genre.append(album.genre.get(i));
            }
        }
        Picasso.with(holder.albumArt.getContext()).load(album.coverImage).into(holder.albumArt);
    }

    @Override
    public int getItemCount() {
        return albumArrayList.size();
    }
}
