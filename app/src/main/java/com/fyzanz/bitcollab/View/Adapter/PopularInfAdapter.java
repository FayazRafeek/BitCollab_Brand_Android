package com.fyzanz.bitcollab.View.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fyzanz.bitcollab.BaseApplication;
import com.fyzanz.bitcollab.Model.Data.Influencer;
import com.fyzanz.bitcollab.R;
import com.fyzanz.bitcollab.databinding.PopularInfLayoutBinding;

import java.util.ArrayList;
import java.util.List;

public class PopularInfAdapter extends RecyclerView.Adapter<PopularInfAdapter.PopularInfVH> {

    Context context;
    List<Influencer> items = new ArrayList<>();
    String type = "HOME";
    InfluencerListAdapter.OnInfluencerClick listner;

    public PopularInfAdapter(String type, InfluencerListAdapter.OnInfluencerClick listner) {
        this.context = BaseApplication.getContext();
        this.type = type;
        this.listner = listner;
    }

    @NonNull
    @Override
    public PopularInfVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PopularInfLayoutBinding binding = PopularInfLayoutBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new PopularInfVH(binding, listner);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularInfVH holder, int position) {

        Influencer item = items.get(position);

        String fullName = item.getDisplayName();
        if(fullName.length() > 13){
            String[] names = fullName.split(" ");
            fullName = names[0];
        }
        holder.binding.popInfName.setText(fullName);

        StringBuilder catSt = new StringBuilder();
        if(item.getCategory() != null){
            int i=0;
            for(String s : item.getCategory()){
                catSt.append(i > 0 ? " | " : "").append(s);
                i++;
            }
        }
        String category = catSt.toString();
        if(category.length() > 20)
            category = category.substring(0,17);

        holder.binding.popInfCat.setText(category);

        if(type.equals("CATEGORY")) holder.binding.popInfCat.setVisibility(View.GONE);

        Log.d(TAG, "onBindViewHolder: Img " + item.getProfileUrl());
        if(item.getProfileUrl() != null){
            Glide
                .with(context)
                .load(item.getProfileUrl())
                .centerCrop()
                .into(holder.binding.popInfImage);
        }

        for(String social : item.getSocials()){
            switch (social){
                case "Instagram" : holder.binding.instaIc.setVisibility(View.VISIBLE); break;
                case "Youtube" : holder.binding.youtIc.setVisibility(View.VISIBLE); break;
                case "Twitter" : holder.binding.twitIc.setVisibility(View.VISIBLE); break;
            }
        }
    }

    private static final String TAG = "333";

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    public void updateAdapter(List<Influencer> influencers){
        this.items = influencers;
        notifyDataSetChanged();
    }

    public class PopularInfVH extends RecyclerView.ViewHolder{

        PopularInfLayoutBinding binding;
        InfluencerListAdapter.OnInfluencerClick listner;

        public PopularInfVH(@NonNull PopularInfLayoutBinding binding, InfluencerListAdapter.OnInfluencerClick listner) {
            super(binding.getRoot());
            this.binding = binding;
            this.listner = listner;

            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listner.onClickInf( items.get(getAdapterPosition()));
                }
            });
        }
    }


}
