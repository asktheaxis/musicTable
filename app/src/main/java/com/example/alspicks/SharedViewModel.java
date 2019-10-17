package com.example.alspicks;

import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;

public class SharedViewModel extends ViewModel {
    String uid = "";

    public void setUserId(FirebaseUser user) {
        uid = user.getUid();
    }

    public String getUid(){
        return uid;
    }

}
