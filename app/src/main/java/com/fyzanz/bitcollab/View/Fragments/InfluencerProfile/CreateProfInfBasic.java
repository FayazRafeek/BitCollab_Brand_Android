package com.fyzanz.bitcollab.View.Fragments.InfluencerProfile;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.palette.graphics.Palette;

import com.bumptech.glide.Glide;
import com.fyzanz.bitcollab.R;
import com.fyzanz.bitcollab.ViewModel.ProfileViewModel;
import com.fyzanz.bitcollab.databinding.FragmentCreatePrfInfBasicBinding;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;

public class CreateProfInfBasic extends Fragment {

    FragmentCreatePrfInfBasicBinding binding;
    ProfileViewModel viewModel;

    Boolean IS_PROF_CHOOSE = false, IS_COVER_CHOOSE = false;
    String type = "CREATE";

    public CreateProfInfBasic(String type) {
        this.type = type;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCreatePrfInfBasicBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(getActivity()).get(ProfileViewModel.class);

        if(viewModel.IS_BASIC_SET)
            updateUi();

        dobInputListen();

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

    void dobInputListen(){
        calendar = Calendar.getInstance();
        binding.dobInp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getActivity(),listner, calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
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


    private static final String TAG = "333";
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


    Calendar calendar;
    String dob = "";
    DatePickerDialog.OnDateSetListener listner =new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {

            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH,month);
            calendar.set(Calendar.DAY_OF_MONTH,day);

            String date = "";
            month++;
            if(day < 10) date = "0" + day; else date = "" + day;
            if(month < 10) date += "-0" + month; else date += "-" + month;
            date += "-" + year;
            dob = date;
            binding.dobInp.setText(date);

        }
    };


    void gatherData(){

        String displayName = binding.disNameInp.getText().toString();
//        if(dob.equals("")) {
//            binding.dobLay.setError("Required!");
//            return;
//        };
//        if(!IS_PROF_CHOOSE) {
//            Toast.makeText(getActivity(), "Select a profile image", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if(!IS_COVER_CHOOSE) {
//            Toast.makeText(getActivity(), "Select a cover image", Toast.LENGTH_SHORT).show();
//            return;
//        }

        binding.prfileImgChoose.setDrawingCacheEnabled(true);
        binding.prfileImgChoose.buildDrawingCache();
        Bitmap profileBitmap = ((BitmapDrawable) binding.prfileImgChoose.getDrawable()).getBitmap();

        binding.prfileCoverChoose.setDrawingCacheEnabled(true);
        binding.prfileCoverChoose.buildDrawingCache();
        Bitmap coverBitmap = ((BitmapDrawable) binding.prfileCoverChoose.getDrawable()).getBitmap();

        String gender = "";
        switch (binding.genderRadioCreateProfile.getCheckedRadioButtonId()){
            case R.id.male_radio_inf: gender = "Male"; break;
            case R.id.female_radio_inf: gender = "Female"; break;
            case R.id.other_radio_inf: gender = "Other"; break;
            default: gender = "Male";
        }

        viewModel.setInfProfBasicData(displayName,dob,gender,profileBitmap,coverBitmap);
        viewModel.nextPage();
    }


    void updateUi(){
        binding.disNameInp.setText(viewModel.influencer.getDisplayName());
        binding.dobInp.setText(viewModel.influencer.getDob());

        dob = viewModel.influencer.getDob();
        binding.prfileImgChoose.setImageBitmap(viewModel.profImgData);
        binding.prfileCoverChoose.setImageBitmap(viewModel.coverImgData);
        IS_COVER_CHOOSE = true; IS_PROF_CHOOSE = true;

        binding.coverLabel.setVisibility(View.GONE);
        binding.profLabel.setVisibility(View.GONE);

        if(type.equals("EDIT")){
            if(viewModel.profImgData == null){
                Glide.with(getActivity())
                        .load(viewModel.influencer.getProfileUrl())
                        .centerCrop()
                        .into(binding.prfileImgChoose);
                Glide.with(getActivity())
                        .load(viewModel.influencer.getCoverImgUrl())
                        .centerCrop()
                        .into(binding.prfileCoverChoose);
            }


            if(binding.genderRadioCreateProfile.getCheckedRadioButtonId() != -1) return;
            if(viewModel.influencer.getGender() != null){
                if(viewModel.influencer.getGender().equals("Male"))
                    binding.maleRadioInf.setChecked(true);
                else if(viewModel.influencer.getGender().equals("Female"))
                    binding.femaleRadioInf.setChecked(true);
                else
                    binding.otherRadioInf.setChecked(true);
            }

        }
    }
}
