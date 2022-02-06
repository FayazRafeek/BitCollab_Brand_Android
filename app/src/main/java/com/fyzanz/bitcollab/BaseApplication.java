package com.fyzanz.bitcollab;

import android.app.Application;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.core.Amplify;

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        //Amplify Configs
        try {
            Amplify.addPlugin(new AWSCognitoAuthPlugin());
            Amplify.configure(getApplicationContext());
        } catch (AmplifyException e) {
            e.printStackTrace();
        }
    }

    private static BaseApplication context;
    public static BaseApplication getContext() {
        return context;
    }
}
