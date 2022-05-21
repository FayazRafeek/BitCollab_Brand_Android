package com.fyzanz.bitcollab.View.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.fyzanz.bitcollab.Model.Data.Influencer;
import com.fyzanz.bitcollab.databinding.ActivityInfluencerProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class InfluencerProfileActivity extends AppCompatActivity {

    ActivityInfluencerProfileBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityInfluencerProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.infBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        fetchProfile();
    }


    void setUpUi(){

        Glide.with(this)
                .load(influencer.getProfileUrl()).centerCrop()
                .into(binding.infProfileImage);

        Glide.with(this)
                .load(influencer.getCoverImgUrl()).centerCrop()
                .into(binding.infProfBack);

        binding.infProfName.setText(influencer.getDisplayName());
        StringBuilder catSt = new StringBuilder();
        if(influencer.getCategory() != null){
            int i=0;
            for(String s : influencer.getCategory()){
                catSt.append(i > 0 ? " | " : "").append(s);
                i++;
            }
        }
        String category = catSt.toString();
        binding.infProfCat.setText(category);

        String location = influencer.getState() + ", " + influencer.getCountry();
        binding.infProfLocation.setText(location);

        binding.infProfBio.setText(influencer.getBio());

        if(influencer.getInstaId() != null && !influencer.getInstaId().isEmpty()){
            binding.instaCardParent.setVisibility(View.VISIBLE);
            binding.instaId.setText("@" + influencer.getInstaId());
        } else binding.instaCardParent.setVisibility(View.GONE);
        if(influencer.getYoutube() != null && !influencer.getYoutube().isEmpty()){
            binding.youtubeCardParent.setVisibility(View.VISIBLE);
            binding.youtubeId.setText(influencer.getYoutube());
        } else binding.youtubeCardParent.setVisibility(View.GONE);

    }

    Influencer influencer;
    void fetchProfile(){

        binding.dataParent.setVisibility(View.GONE);
        FirebaseFirestore.getInstance()
                .collection("Influencers")
                .document(FirebaseAuth.getInstance().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        binding.progress.setVisibility(View.GONE);
                        if(task.isSuccessful()){
                            binding.dataParent.setVisibility(View.VISIBLE);
                            influencer = task.getResult().toObject(Influencer.class);
                            setUpUi();
                        } else {
                            Toast.makeText(InfluencerProfileActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
