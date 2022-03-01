package com.fyzanz.bitcollab.View.Fragments.InfluencerProfile;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fyzanz.bitcollab.R;
import com.fyzanz.bitcollab.ViewModel.ProfileViewModel;
import com.fyzanz.bitcollab.databinding.FragmentCreateProInfAboutBinding;
import com.fyzanz.bitcollab.databinding.ProfCatSelectBinding;

import java.util.ArrayList;
import java.util.List;

public class CreateProfInfAbout extends Fragment {

    FragmentCreateProInfAboutBinding binding;
    ProfileViewModel viewModel;
    ProfCatSelectAdapter profCatSelectAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCreateProInfAboutBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(getActivity()).get(ProfileViewModel.class);

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

        setupCatRecycler();
    }

    void gatherData(){
        String bio = binding.bioInp.getText().toString();
        List<String> selecCat = profCatSelectAdapter.getSelecCat();
        viewModel.setInfBioData(bio,selecCat);
        viewModel.nextPage();
    }

    void setupCatRecycler(){
        List<String> categories = new ArrayList<>();
        categories.add("Action");categories.add("Fitness");categories.add("Travel");categories.add("Film");categories.add("Creative");categories.add("Sports");
        profCatSelectAdapter = new ProfCatSelectAdapter(categories,getContext());
        binding.infProfCatRecycler.setAdapter(profCatSelectAdapter);
        binding.infProfCatRecycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
    }

    public static class ProfCatSelectAdapter extends RecyclerView.Adapter<ProfCatSelectAdapter.ProfCatSelectVH> {

        List<String> categories = new ArrayList<>();
        List<String> selecCat = new ArrayList<>();
        Context context;

        public ProfCatSelectAdapter(List<String> categories, Context context) {
            this.categories = categories;
            this.context = context;
        }

        @NonNull
        @Override
        public ProfCatSelectVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ProfCatSelectBinding binding = ProfCatSelectBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
            return new ProfCatSelectVH(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull ProfCatSelectVH holder, int position) {
            String item = categories.get(position);
            Boolean IS_SELECTED = false;
            if(selecCat.contains(item))
                IS_SELECTED = true;

            if(IS_SELECTED){
                holder.binding.getRoot().setCardBackgroundColor(ContextCompat.getColor(context, R.color.orange_200));
            } else holder.binding.getRoot().setCardBackgroundColor(ContextCompat.getColor(context, R.color.grey_2));

          holder.binding.profCatSelectTitle.setText(item);

          holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                if(selecCat.contains(item))
                    selecCat.remove(item);
                else
                    selecCat.add(item);

                notifyDataSetChanged();
              }
          });
        }

        public List<String> getSelecCat(){return selecCat;}

        @Override
        public int getItemCount() {
            return categories.size();
        }

        public class ProfCatSelectVH extends RecyclerView.ViewHolder{
            ProfCatSelectBinding binding;
            public ProfCatSelectVH(@NonNull ProfCatSelectBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
    }
}
