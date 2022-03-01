package com.fyzanz.bitcollab.View.Fragments.BrandProfile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.fyzanz.bitcollab.Model.Data.Brand;
import com.fyzanz.bitcollab.Model.Data.Influencer;
import com.fyzanz.bitcollab.View.Fragments.SelectDialog;
import com.fyzanz.bitcollab.ViewModel.ProfileViewModel;
import com.fyzanz.bitcollab.databinding.FragmentCreatePrfBrnContactBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class CreateProfBrnContact extends Fragment implements SelectDialog.OnDialogSelect {

    FragmentCreatePrfBrnContactBinding binding;
    ProfileViewModel profileViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCreatePrfBrnContactBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        profileViewModel = new ViewModelProvider(getActivity()).get(ProfileViewModel.class);

        if(profileViewModel.IS_BRN_CONTACT_SET)
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
                profileViewModel.prevPage();
            }
        });


        getInitDatas();
        binding.countryInp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSelectDialog();
            }
        });

    }



    void showSelectDialog(){
        new SelectDialog(countries,"Select country : ", this,"COUNTRY").show(getChildFragmentManager(),"TAG");
    }

    ArrayList<String> countries = new ArrayList<>();

    void getInitDatas(){
        Locale[] locale = Locale.getAvailableLocales();
        String country;
        for( Locale loc : locale ){
            country = loc.getDisplayCountry();
            if( country.length() > 0 && !countries.contains(country) ){
                countries.add( country );
            }
        }
        Collections.sort(countries, String.CASE_INSENSITIVE_ORDER);
    }

    String country = "";
    @Override
    public void onSelectItem(String item, String tag) {
        if(tag.equals("COUNTRY")){
            country = item;
            binding.countryInp.setText(country);
        }

    }
    void gatherData(){
        String phone = binding.phoneInp.getText().toString();
        String address = binding.addressInp.getText().toString();
        String state = binding.stateInp.getText().toString();
        String pincode = binding.pincodeInp.getText().toString();
        profileViewModel.setBrnProfContactdata(phone,address,country,state,pincode);
        profileViewModel.nextPage();
    }


    void updateUi(){
        Brand brand = profileViewModel.brand;
        binding.phoneInp.setText(brand.getPhone());
        binding.addressInp.setText(brand.getAddress());
        binding.stateInp.setText(brand.getState());
        binding.countryInp.setText(brand.getCountry());
        binding.pincodeInp.setText(brand.getPincode());
    }
}
