package com.fyzanz.bitcollab.View.Activity;

import android.os.Bundle;
import android.transition.Explode;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.fyzanz.bitcollab.R;
import com.fyzanz.bitcollab.databinding.ActivityInfluencerBinding;

public class InfluencerActivity extends AppCompatActivity {

    ActivityInfluencerBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.purple_200));

        binding = ActivityInfluencerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.infBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


}
