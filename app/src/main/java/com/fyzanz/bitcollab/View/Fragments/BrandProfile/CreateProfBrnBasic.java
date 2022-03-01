package com.fyzanz.bitcollab.View.Fragments.BrandProfile;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.fyzanz.bitcollab.ViewModel.ProfileViewModel;
import com.fyzanz.bitcollab.databinding.FragmentCreatePrfBrnBasicBinding;


public class CreateProfBrnBasic extends Fragment {

    FragmentCreatePrfBrnBasicBinding binding;
    ProfileViewModel profileViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCreatePrfBrnBasicBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        profileViewModel = new ViewModelProvider(getActivity()).get(ProfileViewModel.class);

        if(profileViewModel.IS_BRAND_BASIC_SET)
            updateUi();

        binding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gatherData();
            }
        });

        binding.prfileImgChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageChoose("PROFILE");
            }
        });

        binding.prfileCoverChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageChoose("COVER");
            }
        });


    }

    Boolean IS_PROF_CHOOSE = false, IS_COVER_CHOOSE = false;
    void showImageChoose(String type){
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

        if(type.equals("PROFILE")) profSelcResult.launch(chooserIntent);
        else coverSelecResult.launch(chooserIntent);
    }


    ActivityResultLauncher<Intent> profSelcResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if(data != null){
                            Uri selectedImage = data.getData();
                            binding.profLabel.setVisibility(View.GONE);
                            binding.prfileImgChoose.setImageURI(selectedImage);
                            IS_PROF_CHOOSE = true;
                        }
                    }
                }
            });


    ActivityResultLauncher<Intent> coverSelecResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if(data != null){
                            Uri selectedImage = data.getData();
                            binding.coverLabel.setVisibility(View.GONE);
                            binding.prfileCoverChoose.setImageURI(selectedImage);
                            IS_COVER_CHOOSE = true;
                        }
                    }
                }
            });


    void gatherData(){
        String brandName = binding.disNameInp.getText().toString();
        String tagline = binding.taglineInp.getText().toString();
        binding.prfileImgChoose.setDrawingCacheEnabled(true);
        binding.prfileImgChoose.buildDrawingCache();
        Bitmap logoBitmap = ((BitmapDrawable) binding.prfileImgChoose.getDrawable()).getBitmap();

        binding.prfileCoverChoose.setDrawingCacheEnabled(true);
        binding.prfileCoverChoose.buildDrawingCache();
        Bitmap coverBitmap = ((BitmapDrawable) binding.prfileCoverChoose.getDrawable()).getBitmap();

        profileViewModel.setBrnProfBasicData(brandName,tagline,logoBitmap,coverBitmap);
        profileViewModel.nextPage();
    }


    void updateUi(){
        binding.disNameInp.setText(profileViewModel.brand.getBrandName());
        binding.prfileImgChoose.setImageBitmap(profileViewModel.brnLogoImgBit);
        binding.prfileCoverChoose.setImageBitmap(profileViewModel.brnCoverimgBit);
    }
}
