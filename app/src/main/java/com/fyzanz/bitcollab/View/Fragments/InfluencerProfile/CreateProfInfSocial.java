package com.fyzanz.bitcollab.View.Fragments.InfluencerProfile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.fyzanz.bitcollab.Model.Data.BasicResponse;
import com.fyzanz.bitcollab.Model.Data.Influencer;
import com.fyzanz.bitcollab.Model.Utils.AppSingleton;
import com.fyzanz.bitcollab.R;
import com.fyzanz.bitcollab.ViewModel.ProfileViewModel;
import com.fyzanz.bitcollab.databinding.FragmentCreateProInfSocialBinding;

public class CreateProfInfSocial extends Fragment {

    FragmentCreateProInfSocialBinding binding;
    ProfileViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCreateProInfSocialBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(getActivity()).get(ProfileViewModel.class);

        if(viewModel.IS_SOCIAL_SET)
            updateUi();

        binding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gatherData();
            }
        });

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.prevPage();
            }
        });


        setUpClickAndAnims();
    }


    Boolean IS_INSTA_EXP = false, IS_YOUT_EXP = false;
    void setUpClickAndAnims(){
        binding.instaClickParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!IS_INSTA_EXP)
                    expandAnim(binding.instExpandParent);
                 else
                    collapseAnim(binding.instExpandParent);
                 IS_INSTA_EXP = !IS_INSTA_EXP;
            }
        });

        binding.youtClickParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!IS_YOUT_EXP)
                    expandAnim(binding.youtExpandParent);
                else
                    collapseAnim(binding.youtExpandParent);
                IS_YOUT_EXP = !IS_YOUT_EXP;
            }
        });

        binding.instaCheckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyInstaAccount();
            }
        });


        binding.youtCheckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyYoutube();
            }
        });
    }

    /* Verification */
    Boolean IS_INSTA_VERIFIED = false, IS_YOUTUBE_VERIFIED = false;
    String instaUserName = "", youtubeChannel = "";
    void verifyInstaAccount(){
        String username = binding.instaUserInp.getText().toString();
        viewModel.verifyInstaAccount(username)
                .observe(this, new Observer<BasicResponse>() {
                    @Override
                    public void onChanged(BasicResponse basicResponse) {
                        switch (basicResponse.getStatus()){
                            case "LOADING" :
                                binding.instaCheckBtn.setVisibility(View.INVISIBLE);
                                binding.instaCheckBtn.setEnabled(false);
                                binding.instaProgress.setVisibility(View.VISIBLE);
                                break;
                            case"SUCCESS" :
                                IS_INSTA_VERIFIED = true;
                                instaUserName = username;
                                binding.instaCheckBtn.setVisibility(View.VISIBLE);
                                collapseAnim(binding.instExpandParent);
                                binding.profInstTitle.setText(username);
                                binding.instaAddIcon.setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.ic_double_check));
                                binding.instaCheckBtn.setEnabled(true);
                                binding.instaProgress.setVisibility(View.INVISIBLE);
                                break;
                         }
                    }
                });

    }

    void verifyYoutube(){
        String channelName = binding.youtUserInp.getText().toString();
        viewModel.verifyYoutubeAccount(channelName)
                .observe(this, new Observer<BasicResponse>() {
                    @Override
                    public void onChanged(BasicResponse basicResponse) {
                        switch (basicResponse.getStatus()){
                            case "LOADING" :
                                binding.youtCheckBtn.setVisibility(View.INVISIBLE);
                                binding.youtCheckBtn.setEnabled(false);
                                binding.youtProgress.setVisibility(View.VISIBLE);
                                break;
                            case"SUCCESS" :
                                IS_YOUTUBE_VERIFIED = true;
                                youtubeChannel = channelName;
                                binding.youtCheckBtn.setVisibility(View.VISIBLE);
                                collapseAnim(binding.youtExpandParent);
                                binding.profYoutTitle.setText(channelName);
                                binding.youtAddIcon.setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.ic_double_check));
                                binding.youtCheckBtn.setEnabled(true);
                                binding.youtProgress.setVisibility(View.INVISIBLE);
                                break;
                        }
                    }
                });
    }



    void gatherData(){

//        if(!IS_INSTA_VERIFIED && !IS_YOUTUBE_VERIFIED){
//            Toast.makeText(getActivity(), "Provide atleast one social media account", Toast.LENGTH_SHORT).show();return;
//        }
        viewModel.setInfProfSocialData(instaUserName,youtubeChannel);
        viewModel.nextPage();

    }

    void expandAnim(View view){
        AppSingleton.expand(view);
    }

    void collapseAnim(View view){
        AppSingleton.collapse(view);
    }


    void updateUi(){

        Influencer influencer = viewModel.influencer;
        if(influencer.getInstaId() != null && !influencer.getInstaId().isEmpty()){
            IS_INSTA_VERIFIED = true;
            instaUserName = influencer.getInstaId();
            binding.profInstTitle.setText(influencer.getInstaId());
            binding.instaAddIcon.setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.ic_double_check));
        }
        if(influencer.getYoutube() != null && !influencer.getYoutube().isEmpty()){
            IS_YOUTUBE_VERIFIED = true;
            youtubeChannel = influencer.getYoutube();
            binding.profYoutTitle.setText(youtubeChannel);
            binding.youtAddIcon.setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.ic_double_check));

        }
    }
}
