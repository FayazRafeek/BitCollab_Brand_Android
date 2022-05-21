package com.fyzanz.bitcollab.View.Fragments.CampaignCreate;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fyzanz.bitcollab.R;
import com.fyzanz.bitcollab.View.Adapter.CategoryAdapter;
import com.fyzanz.bitcollab.View.Fragments.InfluencerProfile.CreateProfInfAbout;
import com.fyzanz.bitcollab.ViewModel.CampaignViewModel;
import com.fyzanz.bitcollab.databinding.FragmentCampStep2Binding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CampCreateStep2Fragment extends Fragment {

    FragmentCampStep2Binding binding;
    CampaignViewModel campaignViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCampStep2Binding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        campaignViewModel = new ViewModelProvider(getActivity()).get(CampaignViewModel.class);

        binding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gatherdata();
            }
        });

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                campaignViewModel.prevPage();
            }
        });

        setupCatRecycler();
        setUpPlatformUi();
        setUpDateUi();
    }


    CreateProfInfAbout.ProfCatSelectAdapter categoryAdapter;
    void setupCatRecycler(){
        List<String> categories = new ArrayList<>();
        categories.add("Action");categories.add("Fitness");categories.add("Travel");categories.add("Film");categories.add("Creative");categories.add("Sports");
        categoryAdapter = new CreateProfInfAbout.ProfCatSelectAdapter(categories,getContext());
        binding.campCatRecycler.setAdapter(categoryAdapter);
        binding.campCatRecycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL,false));
    }

    //Platform UI
    Boolean INSTA_ACTIVE =false, YOUT_ACTIVE = false,TWITT_ACTIVE = false;
    void setUpPlatformUi(){

        binding.instaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                INSTA_ACTIVE = !INSTA_ACTIVE;
                updatePalformUi();
            }
        });

        binding.youtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                YOUT_ACTIVE = !YOUT_ACTIVE;
                updatePalformUi();
            }
        });

        binding.twitterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TWITT_ACTIVE = !TWITT_ACTIVE;
                updatePalformUi();
            }
        });
    }
    void updatePalformUi(){
        if(INSTA_ACTIVE){
            binding.instaBtn.setStrokeWidth(4);
            binding.instaBtn.setStrokeColor(ContextCompat.getColor(getContext(),R.color.green));
            binding.instaBtn.setCardElevation(6);
        } else {
            binding.instaBtn.setStrokeWidth(0);
            binding.instaBtn.setStrokeColor(ContextCompat.getColor(getContext(),R.color.green));
            binding.instaBtn.setCardElevation(3);
        }

        if(YOUT_ACTIVE){
            binding.youtBtn.setStrokeWidth(4);
            binding.youtBtn.setStrokeColor(ContextCompat.getColor(getContext(),R.color.green));
            binding.youtBtn.setCardElevation(6);
        } else {
            binding.youtBtn.setStrokeWidth(0);
            binding.youtBtn.setStrokeColor(ContextCompat.getColor(getContext(),R.color.green));
            binding.youtBtn.setCardElevation(3);
        }

        if(TWITT_ACTIVE){
            binding.twitterBtn.setStrokeWidth(4);
            binding.twitterBtn.setStrokeColor(ContextCompat.getColor(getContext(),R.color.green));
            binding.twitterBtn.setCardElevation(6);
        } else {
            binding.twitterBtn.setStrokeWidth(0);
            binding.twitterBtn.setStrokeColor(ContextCompat.getColor(getContext(),R.color.green));
            binding.twitterBtn.setCardElevation(3);
        }
    }
    //


    //Date Ui
    void setUpDateUi(){
        calendar = Calendar.getInstance();
        binding.campStartDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                DatePickerDialog dialog = new DatePickerDialog(getActivity(),startDateListener, calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
                dialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
                dialog.show();
            }
        });

        binding.campEndDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                DatePickerDialog dialog = new DatePickerDialog(getActivity(),endDateListener, calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
                if(startCal != null)
                    dialog.getDatePicker().setMinDate(startCal.getTimeInMillis());
                else
                    dialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
                dialog.show();
            }
        });
    }

    Calendar calendar, startCal, endCal;
    String startDate = "", endDate = "";
    DatePickerDialog.OnDateSetListener startDateListener =new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {

            startCal = Calendar.getInstance();
            startCal.set(Calendar.YEAR, year);
            startCal.set(Calendar.MONTH,month);
            startCal.set(Calendar.DAY_OF_MONTH,day);

            String date = "";
            month++;
            if(day < 10) date = "0" + day; else date = "" + day;
            if(month < 10) date += "-0" + month; else date += "-" + month;
            date += "-" + year;
            startDate = date;
            binding.campStartDateTxt.setText(date);

            binding.campEndDateTxt.setText("End Date");
            endCal = null;
            endDate = "";
        }
    };

    DatePickerDialog.OnDateSetListener endDateListener =new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {

            endCal = Calendar.getInstance();
            endCal.set(Calendar.YEAR, year);
            endCal.set(Calendar.MONTH,month);
            endCal.set(Calendar.DAY_OF_MONTH,day);

            String date = "";
            month++;
            if(day < 10) date = "0" + day; else date = "" + day;
            if(month < 10) date += "-0" + month; else date += "-" + month;
            date += "-" + year;
            endDate = date;
            binding.campEndDateTxt.setText(date);
        }
    };


    void gatherdata(){

        String budget = binding.campBudgetInp.getText().toString();
        campaignViewModel.setNewCampStep2Data(INSTA_ACTIVE,YOUT_ACTIVE,TWITT_ACTIVE,new ArrayList<>(categoryAdapter.getSelecCat()),startDate,endDate,budget);
        campaignViewModel.nextPage();
    }
}
