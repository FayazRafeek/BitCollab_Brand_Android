package com.fyzanz.bitcollab.View.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fyzanz.bitcollab.BaseApplication;
import com.fyzanz.bitcollab.Model.Data.Influencer;
import com.fyzanz.bitcollab.databinding.InfluencerListItemBinding;

import java.util.ArrayList;
import java.util.List;

public class InfluencerListAdapter extends RecyclerView.Adapter<InfluencerListAdapter.InfListVH> {


    InfluencerListItemBinding binding;
    List<Influencer> items = new ArrayList<>();
    Context context;
    OnInfluencerClick listner;
    String type = "CATEGORY";

    public InfluencerListAdapter(OnInfluencerClick listner, String type) {
        this.context = BaseApplication.getContext();
        this.listner = listner;
        this.type = type;
    }

    @NonNull
    @Override
    public InfListVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = InfluencerListItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new InfListVH(binding, listner);
    }

    @Override
    public void onBindViewHolder(@NonNull InfListVH holder, int position) {

        Influencer item = items.get(position);
        holder.binding.infListName.setText(item.getDisplayName());

        if(type.equals("CATEGORY")){
            holder.binding.socialInfo.setVisibility(View.VISIBLE);
            holder.binding.listFavBtn.setVisibility(View.GONE);
        } else {
            holder.binding.socialInfo.setVisibility(View.GONE);
            holder.binding.listFavBtn.setVisibility(View.VISIBLE);
        }

        StringBuilder catSt = new StringBuilder();
        if(item.getCategory() != null){
            int i=0, size = item.getCategory().size();
            for(String s : item.getCategory()){
                catSt.append(s);
                if(i < size - 1)
                    catSt.append(" | ");
                i++;
            }
        }
        String category = catSt.toString();

        if(category.length() > 24) category = category.substring(0,24);
        holder.binding.infListCat.setText(category);

        Glide
            .with(context).load(item.getProfileUrl()).centerCrop()
                .into(holder.binding.infListImage);

        if(item.getSocials() != null){
            for(String s : item.getSocials()){
                switch (s){
                    case "Instagram" : holder.binding.instaIc.setVisibility(View.VISIBLE); break;
                    case "Youtube" : holder.binding.youtIc.setVisibility(View.VISIBLE); break;
                    case "Twitter" : holder.binding.twitIc.setVisibility(View.VISIBLE); break;
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void updateAdapter(List<Influencer> items){
        this.items = items;
        notifyDataSetChanged();
    }

    public class InfListVH extends RecyclerView.ViewHolder{

        InfluencerListItemBinding binding;
        OnInfluencerClick listner;

        public InfListVH(@NonNull InfluencerListItemBinding binding, OnInfluencerClick listner) {
            super(binding.getRoot());
            this.binding = binding;
            this.listner = listner;

            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listner.onClickInf(items.get(getAdapterPosition()));
                }
            });
        }
    }

    public interface OnInfluencerClick {
        void onClickInf(Influencer influencer);
    }
}
