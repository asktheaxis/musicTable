package com.example.alspicks;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RecyclerResultsAdapter extends RecyclerView.Adapter<RecyclerResultsAdapter.ResultsViewHolder> {

    private SharedViewModel sharedViewModel;
    private ArrayList<Album> albumArrayList;
    private Context context;
    private String m_text = "";
    private String albumAPI, masterUri;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "MainActivity";
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private ActivityCallback mCallback;

    public RecyclerResultsAdapter(ArrayList<Album> albums) {
        this.albumArrayList = albums;
    }

    class ResultsViewHolder extends RecyclerView.ViewHolder {
        TextView album, artist, year, style, genre;
        ImageButton albumArt, popMenu;

        ResultsViewHolder(View itemView) {
            super(itemView);
            album = itemView.findViewById(R.id.tvAlbum);
            artist = itemView.findViewById(R.id.tvArtist);
            year = itemView.findViewById(R.id.tvYear);
            style = itemView.findViewById(R.id.tvStyle);
            genre = itemView.findViewById(R.id.tvGenre);
            albumArt = itemView.findViewById(R.id.albumButton);
            popMenu = itemView.findViewById(R.id.popMenu);
        }
    }

    @NonNull
    @Override
    public ResultsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.results_layout, parent, false);
        return new ResultsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ResultsViewHolder holder, final int position) {

        String resource;
        Album album = albumArrayList.get(position);
        resource = album.resource;
        albumAPI = album.resource;
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
        holder.setIsRecyclable(false);

        holder.albumArt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FirebaseAuth.getInstance().getCurrentUser() == null){showLogInDialog(holder.albumArt.getContext());}
                else{showAddItemDialog(holder.albumArt.getContext(), album);}
            }
        });

        holder.popMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(holder.popMenu.getContext(), holder.popMenu);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu_layout, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        //discogs.com
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharedViewModel.getAlbumResources()));
                        holder.popMenu.getContext().startActivity(browserIntent);
                        return true;
                    }
                });

                popupMenu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return albumArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    private void showLogInDialog(Context c){
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle("Please Log In")
                .setPositiveButton("Log In", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mCallback.openAccountFragment();

                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();


    }
    private void showAddItemDialog(Context c, Album a) {
        final EditText taskEditText = new EditText(c);
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle("Send a Recommendation")
                .setMessage("Enter an email")
                .setView(taskEditText)
                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String task = String.valueOf(taskEditText.getText());


                        final Map<String, Object> album = new HashMap<>();
                        album.put("artist", a.artist);
                        album.put("name", a.name);
                        album.put("year", a.year);
                        album.put("style", a.style);
                        album.put("genre", a.genre);
                        album.put("coverImage", a.coverImage);

                        album.put("sender", user.getEmail());
                        album.put("receiver", task);



                        // Add a new document with a generated ID

            db.collection("albums")
                    .document(a.name)
                    .set(album)
                    .addOnSuccessListener(documentReference -> {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        //albumAdded.show();
                    })
                    .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));



                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

    public void getAlbumURL() {
        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(context));
        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                albumAPI,
                null,
                response -> {
                    Log.w("Retrieving album's master url ", response.toString());
                    try {
                        JSONArray jsonArray = response.getJSONArray("uri");
                        masterUri = jsonArray.getString(0);
                        mCallback.onSucess(masterUri);
                    } catch (JSONException e) {
                        Log.w("No uri found with for this album", e);
                    }
                },
                error -> Log.w("Error requesting Json data", error.toString())
        );
        requestQueue.add(objectRequest);
    }




}