package com.e.chatapp.ui.friendlist;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FriendlistViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public FriendlistViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is friendlist fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}