package com.fyzanz.bitcollab.View.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

        String fullName = item.getFirstName() + " " + item.getLastName();

        holder.binding.popInfName.setText(fullName);

        if(type.equals("CATEGORY")) holder.binding.popInfCat.setVisibility(View.GONE);

        switch (position){
            case 0 : holder.binding.popInfImage.setImageDrawable(context.getDrawable(R.drawable.avatar_2)); break;
            case 1 : holder.binding.popInfImage.setImageDrawable(context.getDrawable(R.drawable.avatar_3)); break;
            case 2 : holder.binding.popInfImage.setImageDrawable(context.getDrawable(R.drawable.avatar_4)); break;
            case 3 : holder.binding.popInfImage.setImageDrawable(context.getDrawable(R.drawable.profile_image)); break;
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
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
