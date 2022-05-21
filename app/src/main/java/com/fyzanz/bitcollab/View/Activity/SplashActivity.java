package com.fyzanz.bitcollab.View.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.fyzanz.bitcollab.BaseApplication;
import com.fyzanz.bitcollab.Model.Utils.AppSingleton;
import com.fyzanz.bitcollab.R;
import com.fyzanz.bitcollab.databinding.ActivitySplashBinding;

public class SplashActivity extends AppCompatActivity {

    ActivitySplashBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Boolean is_logged_in = getSharedPreferences(getString(R.string.AUTH_PREF_FILE),MODE_PRIVATE).getBoolean(getString(R.string.IS_LOGIN_KEY),false);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(is_logged_in){
                    String userTpe = getSharedPreferences(getString(R.string.AUTH_PREF_FILE),MODE_PRIVATE).getString("USER_TYPE","");
                    AppSingleton.getInstance().setUSER_TYPE(userTpe);
                    startActivity(new Intent(SplashActivity.this,MainActivity.class));
                }
                else startActivity(new Intent(SplashActivity.this,AuthActivity.class));

                finish();
            }
        },500);

    }
}
