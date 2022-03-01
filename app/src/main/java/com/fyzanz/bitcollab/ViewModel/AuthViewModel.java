package com.fyzanz.bitcollab.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fyzanz.bitcollab.Model.Data.BasicResponse;
import com.fyzanz.bitcollab.Model.Repository.UserAuthRepo;

public class AuthViewModel extends ViewModel {

    public UserAuthRepo userAuthRepo;
    public MutableLiveData<String> AUTH_TYPE;
    public String USER_TYPE = "";


    public AuthViewModel() {
        if(AUTH_TYPE == null){
            AUTH_TYPE = new MutableLiveData<>("CHOOSE");
        }
        userAuthRepo = UserAuthRepo.getInstance();
    }

    public MutableLiveData<String> getAUTH_TYPE() {
        if(AUTH_TYPE == null)
            AUTH_TYPE = new MutableLiveData<>("CHOOSE");
        return AUTH_TYPE;
    }

    public String getUSER_TYPE() {
        return USER_TYPE;
    }

    public void setUSER_TYPE(String USER_TYPE) {
        this.USER_TYPE = USER_TYPE;
    }

    String email,password;
    public LiveData<BasicResponse> startRegister(String name, String email, String password){
        this.email = email;
        this.password = password;
        LiveData<BasicResponse> resp =  userAuthRepo.startUserRegister(USER_TYPE,name,email,password);
        return resp;
    }

    public LiveData<BasicResponse> startLogin(String email, String password){
        LiveData<BasicResponse> resp =  userAuthRepo.startUserLogin(USER_TYPE,email,password);
        return resp;
    }


}
