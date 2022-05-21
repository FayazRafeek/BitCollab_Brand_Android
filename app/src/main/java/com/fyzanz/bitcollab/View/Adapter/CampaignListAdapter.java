package com.fyzanz.bitcollab.View.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fyzanz.bitcollab.Model.Data.Campaign;
import com.fyzanz.bitcollab.databinding.CampaignListItemBinding;

import java.util.ArrayList;
import java.util.List;

public class CampaignListAdapter extends RecyclerView.Adapter<CampaignListAdapter.CampaignVH> {

    Context context;
    String type;
    CampaignClick listner;

    List<Campaign> campaigns = new ArrayList<>();

    public CampaignListAdapter(Context context, String type, CampaignClick listner) {
        this.context = context;
        this.type = type;
        this.listner = listner;
    }

    @NonNull
    @Override
    public CampaignVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CampaignListItemBinding binding = CampaignListItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new CampaignVH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CampaignVH holder, int position) {

        Campaign campaign = campaigns.get(position);

        holder.binding.campTitle.setText("\"" + campaign.getPromoteTitle() + "\"");
        holder.binding.campBrandName.setText("By " + campaign.getBrandName());
        String desc = campaign.getCampaignDesc();
        if(desc.length() > 40) desc = desc.substring(0,40) + "...";
        holder.binding.campDescTxt.setText(desc);

        Glide.with(context)
                .load(campaign.getBannerUrl())
                .centerCrop()
                .into(holder.binding.capmBanne);

        if(type.equals("HOME")){
            holder.binding.campDescTxt.setVisibility(View.GONE);
        }

        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listner.onCampaignClick(campaign);
            }
        });

    }

    public void updateList(List<Campaign> campaigns){
        this.campaigns = campaigns;
    }

    @Override
    public int getItemCount() {
        return campaigns.size();
    }

    class CampaignVH extends RecyclerView.ViewHolder{

        CampaignListItemBinding binding;
        public CampaignVH(@NonNull CampaignListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface CampaignClick {
        void onCampaignClick(Campaign campaign);
    }
}
