package com.fyzanz.bitcollab.View.Fragments.InfluencerProfile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.fyzanz.bitcollab.Model.Data.Influencer;
import com.fyzanz.bitcollab.View.Fragments.SelectDialog;
import com.fyzanz.bitcollab.ViewModel.ProfileViewModel;
import com.fyzanz.bitcollab.databinding.DialogSelectBinding;
import com.fyzanz.bitcollab.databinding.FragmentCreateProInfContactBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class CreateProfInfContact extends Fragment implements SelectDialog.OnDialogSelect {


    FragmentCreateProInfContactBinding binding;
    ProfileViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCreateProInfContactBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(getActivity()).get(ProfileViewModel.class);

        if(viewModel.IS_CONTACT_SET) updateUi();
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
        String country = binding.countryInp.getText().toString();
        String pincode = binding.pincodeInp.getText().toString();
        viewModel.setInfProfContactdata(phone,address,country,state,pincode);
        viewModel.nextPage();
    }



    void updateUi(){
        Influencer influencer = viewModel.influencer;
        binding.phoneInp.setText(influencer.getPhone());
        binding.addressInp.setText(influencer.getAddress());
        binding.stateInp.setText(influencer.getState());
        binding.countryInp.setText(influencer.getCountry());
        binding.pincodeInp.setText(influencer.getPincode() + "");
    }
}
