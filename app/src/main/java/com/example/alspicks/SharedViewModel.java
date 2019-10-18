package com.example.alspicks;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;

public class SharedViewModel extends ViewModel {
    String uid = "";
    FirebaseUser user;
    private MutableLiveData<String> userTextBox;
    String userName;

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

}
