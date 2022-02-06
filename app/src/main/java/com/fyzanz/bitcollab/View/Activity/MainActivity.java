package com.fyzanz.bitcollab.View.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.fyzanz.bitcollab.R;
import com.fyzanz.bitcollab.View.Fragments.FavoriteFragment;
import com.fyzanz.bitcollab.View.Fragments.HomeFragment;
import com.fyzanz.bitcollab.View.Fragments.LoginFragment;
import com.fyzanz.bitcollab.View.Fragments.MessageFragment;
import com.fyzanz.bitcollab.View.Fragments.ProfileFragment;
import com.fyzanz.bitcollab.View.Fragments.RegisterFragment;
import com.fyzanz.bitcollab.ViewModel.MainViewModel;
import com.fyzanz.bitcollab.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "333";

    ActivityMainBinding binding;
    MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setUpBottNavigation();

        mainViewModel.getCurrentScreen().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                handleScreenChange(s);
            }
        });
    }



    //Screen change handle
    HomeFragment homeFragment;
    void handleScreenChange(String s){

        Fragment fragment = null;

        switch (s){
            case "MESSAGE" : fragment = new MessageFragment(); break;
            case "PROFILE" : fragment = new ProfileFragment(); break;
            case "FAVORITE" : fragment = new FavoriteFragment(); break;
            default :
                if(homeFragment == null)
                    homeFragment = new HomeFragment();
                fragment = homeFragment;
        }

        if(fragment != null)
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(binding.mainFragContainer.getId(),fragment, null)
                .commit();

    }


    void setUpBottNavigation(){
        MaterialShapeDrawable shapeDrawable = (MaterialShapeDrawable) binding.bottomNavigation.getBackground();
//        Float cornerRadius = getResources().getDimension(R.dimen.bottom_nav_corner);
        shapeDrawable.setShapeAppearanceModel(
                shapeDrawable.getShapeAppearanceModel()
                        .toBuilder()
                        .setTopLeftCorner(CornerFamily.ROUNDED,50)
                        .setTopRightCorner(CornerFamily.ROUNDED,50)
                        .build()
        );

        binding.bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.bot_home: mainViewModel.getCurrentScreen().setValue("HOME"); break;
                    case R.id.bot_chat: mainViewModel.getCurrentScreen().setValue("MESSAGE"); break;
                    case R.id.bot_fav: mainViewModel.getCurrentScreen().setValue("FAVORITE"); break;
                    case R.id.bot_profile: mainViewModel.getCurrentScreen().setValue("PROFILE"); break;
                }
                return true;
            }
        });

    }

    void showToast(String msg){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });

    }

}