package com.example.alspicks.ui.home;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alspicks.ActivityCallback;
import com.example.alspicks.Album;
import com.example.alspicks.R;
import com.example.alspicks.SharedViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.requireNonNull;

public class RecyclerResultsAdapter extends RecyclerView.Adapter<RecyclerResultsAdapter.ResultsViewHolder> {

    public SharedViewModel sharedViewModel;
    private ArrayList<Album> albumArrayList;
    private Context context;
    private String m_text = "";
    protected String userName;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "MainActivity";
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private ActivityCallback mCallback;

    public RecyclerResultsAdapter(ArrayList<Album> albums) {
        this.albumArrayList = albums;
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
    public ResultsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.results_layout, parent, false);

        return new ResultsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ResultsViewHolder holder, final int position) {

        Album album = albumArrayList.get(position);
        holder.artist.setText(album.getArtist());
        holder.album.setText(album.getName());
        holder.year.setText(album.getYear());
        Picasso.with(holder.albumArt.getContext()).load(album.coverImage).into(holder.albumArt);
        holder.setIsRecyclable(false);

        holder.albumArt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FirebaseAuth.getInstance().getCurrentUser() == null){showLogInDialog(holder.albumArt.getContext());}
                else{showAddItemDialog(holder.albumArt.getContext(), album);}
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

                        String username = buildUserName();


                        final Map<String, Object> album = new HashMap<>();
                        album.put("artist", a.artist);
                        album.put("name", a.name);
                        album.put("year", a.year);
                        album.put("style", a.style);

                        album.put("sender", user.getUid());
                        album.put("receiver", task);



                        // Add a new document to master Albums Collection

                        db.collection("albums")
                                .document(a.name)
                                .set(album)
                                .addOnSuccessListener(documentReference -> {
                                    Log.d(TAG, "DocumentSnapshot successfully written!");
                                    //albumAdded.show();
                                })
                                .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));

                        /* TODO: .document(username) needs to be .document(receiverUID)
                            // need to get the UID that matches the input email
                        //Add new document to receivers INCOMING sub-collection
                        db.collection("Users")
                                .document(username)// TODO: here
                                .collection("Incoming")
                                .document(a.name)
                                .set(album)
                                .addOnSuccessListener(documentReference -> {
                                    Log.d(TAG, "DocumentSnapshot successfully written!");
                                })
                                .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));*/


                        //Add new document to senders OUTGOING sub-collection
                        db.collection("Users")
                                .document(username)
                                .collection("Outgoing")
                                .document(a.name)
                                .set(album)
                                .addOnSuccessListener(documentReference -> {
                                    Log.d(TAG, "DocumentSnapshot successfully written!");
                                })
                                .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));


                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

    public String buildUserName(){
        String email = user.getEmail();
        int index = email.indexOf('@');
        String userName = email.substring(0, index);
        return userName;
    }



}