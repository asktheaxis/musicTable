package com.example.alspicks.ui.account;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


public class AccountViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<String> userName;

    public AccountViewModel() {
        mText = new MutableLiveData<>();
        userName = new MutableLiveData<>();




    }

    public void setUserName(String name){
        userName.setValue(name);
    }

    public void clearUserName(){
        userName.setValue(null);
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<String> getUserName() { return userName; }



}