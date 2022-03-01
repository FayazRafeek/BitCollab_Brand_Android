package com.fyzanz.bitcollab;

import android.app.Application;

import androidx.room.Room;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.core.Amplify;
import com.fyzanz.bitcollab.Model.Repository.UserDataRepo;
import com.fyzanz.bitcollab.Model.Room.AppDatabase;

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        UserDataRepo.getInstance();

        AppDatabase.createDatabase(this);

    }

    private static BaseApplication context;
    public static BaseApplication getContext() {
        return context;
    }
}
