package com.example.alspicks;

import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class SharedViewModel extends ViewModel {
    //FirebaseUser user
    ArrayList<String> userAlbums = new ArrayList<>();
    String uid = "";


    /*public void setUser(FirebaseUser user1) {
        user = user1;
    }*/

   public void setUserId(FirebaseUser user) {
        uid = user.getUid();
    }

    public String getUid(){
        return uid;
    }

    //These albums are referenced by their Document ID's within firestore
    public void addAlbum(String album){
        userAlbums.add(album);
    }

}
