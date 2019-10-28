package com.example.alspicks;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class SharedViewModel extends ViewModel {
    String uid = "";
    FirebaseUser user;
    private MutableLiveData<String> userTextBox;
    String userName;
    private static final String TAG = "MainActivity";
    private String albumNameEncoded;

    /*public SharedViewModel(MutableLiveData<String> userName) {
        this.userName = userName;
    }*/

    public SharedViewModel(){
        userTextBox = new MutableLiveData<>();
    }

    public void setUser(FirebaseUser fbUser){
        user = fbUser;
    }

    public void setUserId(FirebaseUser user) {
        uid = user.getUid();
    }

    public String getUid(){
        return uid;
    }

    public void clearUserId(){
        uid = "";
    }

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

}
