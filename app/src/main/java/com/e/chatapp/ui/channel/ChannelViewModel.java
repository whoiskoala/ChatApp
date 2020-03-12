package com.e.chatapp.ui.channel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ChannelViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ChannelViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is channel fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
