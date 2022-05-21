package com.fyzanz.bitcollab.View.Fragments.BrandProfile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.fyzanz.bitcollab.Model.Data.Brand;
import com.fyzanz.bitcollab.View.Fragments.InfluencerProfile.CreateProfInfAbout;
import com.fyzanz.bitcollab.ViewModel.ProfileViewModel;
import com.fyzanz.bitcollab.databinding.FragmentCreatePrfBrnAboutBinding;

import java.util.ArrayList;
import java.util.List;

public class CreateProfBrnAbout extends Fragment {

    FragmentCreatePrfBrnAboutBinding binding;
    ProfileViewModel profileViewModel;

    String type = "CREATE";

    public CreateProfBrnAbout(String type) {
        this.type = type;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCreatePrfBrnAboutBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        profileViewModel = new ViewModelProvider(getActivity()).get(ProfileViewModel.class);
        if(profileViewModel.IS_BRN_BIO_SET)
            updateUi();
        setupCatRecycler();

        binding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gatherData();
            }
        });
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileViewModel.prevPage();
            }
        });

    }



    CreateProfInfAbout.ProfCatSelectAdapter profCatSelectAdapter;
    void setupCatRecycler(){
        List<String> categories = new ArrayList<>();
        categories.add("Skin Care");
        categories.add("Fashion");
        categories.add("Tourism");
        categories.add("Food");
        categories.add("Film");
        categories.add("Creative");
        categories.add("Sports");
        categories.add("Fitness");
        profCatSelectAdapter = new CreateProfInfAbout.ProfCatSelectAdapter(categories,getContext());
        binding.infProfCatRecycler.setAdapter(profCatSelectAdapter);
        binding.infProfCatRecycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));


        if(type.equals("EDIT"))
            profCatSelectAdapter.setAllSelection(profileViewModel.brand.getCategories());
    }

    void gatherData(){
        String bio = binding.bioInp.getText().toString();
        String website = binding.websiteInp.getText().toString();
        List<String> selecCat = profCatSelectAdapter.getSelecCat();
        profileViewModel.setBrnProfBioData(bio,website,selecCat);
        profileViewModel.nextPage();
    }

    void updateUi(){
        Brand brand = profileViewModel.brand;
        binding.bioInp.setText(brand.getBio());
        binding.websiteInp.setText(brand.getWebsiteUrl());

        if(type.equals("EDIT"))
            binding.nextBtn.setText("SUBMIT");
    }
}
