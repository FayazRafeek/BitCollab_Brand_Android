package com.fyzanz.bitcollab.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {


    public MainViewModel() {

    }

    MutableLiveData<String> screenLive;
    public MutableLiveData<String> getCurrentScreen() {
        if(screenLive == null) screenLive = new MutableLiveData<>("HOME");
        return screenLive;
    }
}
