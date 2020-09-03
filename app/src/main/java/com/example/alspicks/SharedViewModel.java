package com.example.alspicks;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.alspicks.ui.spotify.SpotifyFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Map;

public class SharedViewModel extends ViewModel {
    String uid = "";
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private MutableLiveData<String> userTextBox;
    String userName;
    private static final String TAG = "MainActivity";
    private String albumNameEncoded, albumResource;
    private ArrayList<Album> albumResults = new ArrayList<>();
    private ArrayList<Album> userList = new ArrayList<>();
    private ArrayList<SpotifyFragment.SpotifyAlbum> userLibraryAlbums = new ArrayList<>();
    private ArrayList<String> currentUsers = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String definedUser;

    public SharedViewModel(){
        userTextBox = new MutableLiveData<>();

    }

    public void setUser(FirebaseUser fbUser){
        user = fbUser;
    }

    public void setUserId(FirebaseUser user) {
        uid = user.getUid();
    }

    public void setDefinedUser(String user) {
        definedUser = user;
    }

    public String getUid(){
        return uid;
    }

    public void clearUserId(){
        uid = "";
    }

    public void setAlbumResults(ArrayList<Album> results){
        albumResults = results;
    }

    public void setUserList(ArrayList<Album> albums){
        userList = albums;
    }

    public void setUserLibraryAlbums(ArrayList<SpotifyFragment.SpotifyAlbum> albums) { userLibraryAlbums = albums; }

    public ArrayList<SpotifyFragment.SpotifyAlbum> getUserLibraryAlbums() {
        return userLibraryAlbums;
    }

    public ArrayList<Album> getAlbumResults() {
        return albumResults;
    }

    public ArrayList<Album> getUserList() { return userList; }

    public String getDefinedUser() { return definedUser; }

    public void signOut(){
        user = null;
    }

    public String buildUserName(){
        String email = user.getEmail();
        int index = email.indexOf('@');
        userName = email.substring(0, index);
        return userName;
    }

    public LiveData<String> setUserNameText(){
        if(user != null) {
            String email = user.getEmail();
            int index = email.indexOf('@');
            userTextBox.setValue(email.substring(0, index));
            return userTextBox;
        }
        userTextBox.setValue("");
        return userTextBox;
    }

    public void clearUserTextBox(){
        userTextBox.setValue(null);
    }

    private String createAlbumSearchURL(String name){
        try {
            albumNameEncoded = URLEncoder.encode(name, "UTF-8");
        } catch (UnsupportedEncodingException e){
            Log.w(TAG, "Error encoding URL", e);
        }
        String url = "http://api.discogs.com/database/search?type=album&q=" + albumNameEncoded;
        return url;
    }

    public void setAlbumResource(String url){

        albumResource = url;
    }

    public String getAlbumResources(){
        return albumResource;
    }

    public void buildCurrentUserList() {
        db.collection("Users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot document : task.getResult()){
                            Map data = document.getData();
                            if (data.get("Name") != null) {
                                String userEmail = String.valueOf(data.get("Name"));
                                currentUsers.add(userEmail);
                            }
                        }
                    }
                });
    }

    public ArrayList<String> getCurrentUsers() {
        return currentUsers;
    }



}
