package com.fyzanz.bitcollab.View.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fyzanz.bitcollab.Model.Data.CollabRequest;
import com.fyzanz.bitcollab.Model.Utils.AppSingleton;
import com.fyzanz.bitcollab.R;
import com.fyzanz.bitcollab.databinding.RequestLayoutItemBinding;

import java.util.ArrayList;
import java.util.List;

public class CollabReqAdapter extends RecyclerView.Adapter<CollabReqAdapter.CollabReqVH> {

    Context context;
    List<CollabRequest> items = new ArrayList<>();
    String USER_TYPE = "";

    CollabReqClick listner;

    public CollabReqAdapter(Context context,CollabReqClick listner) {
        this.context = context;
        USER_TYPE = AppSingleton.getInstance().getUSER_TYPE();
        this.listner = listner;
    }

    @NonNull
    @Override
    public CollabReqVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RequestLayoutItemBinding binding = RequestLayoutItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new CollabReqVH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CollabReqVH holder, int position) {

        CollabRequest item = items.get(position);

        if(USER_TYPE.equals("INFLUENCER")){
            holder.binding.reqName.setText(item.getBrandName());
            holder.binding.reqTitle.setText(item.getReqTitle());
            Glide.with(context)
                    .load(item.getBrandLogoUrl())
                    .centerCrop()
                    .into(holder.binding.reqLogoImage);
        } else {
            holder.binding.reqName.setText(item.getInfName());
            holder.binding.reqTitle.setText(item.getReqTitle());
            Glide.with(context)
                    .load(item.getInfLogoUrl())
                    .centerCrop()
                    .into(holder.binding.reqLogoImage);

        }

        if(item.getStatus() == null) item.setStatus("Pending");
        holder.binding.reqStatus.setText(item.getStatus());
        switch (item.getStatus()){
            case "Pending" : holder.binding.reqStatus.setTextColor(ContextCompat.getColor(context, R.color.orange_300)); break;
            case "Seen" : holder.binding.reqStatus.setTextColor(ContextCompat.getColor(context, R.color.purple_200)); break;
            case "Accepted" : holder.binding.reqStatus.setTextColor(ContextCompat.getColor(context, R.color.darkGreen)); break;
            case "Rejected" : holder.binding.reqStatus.setTextColor(ContextCompat.getColor(context, R.color.red_300)); break;
        }


        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listner.onReqClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void updateList(List<CollabRequest> items){
        this.items = items;
        notifyDataSetChanged();
    }

    public class CollabReqVH extends RecyclerView.ViewHolder{

        RequestLayoutItemBinding binding;
        public CollabReqVH(@NonNull RequestLayoutItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface CollabReqClick {
        void onReqClick(CollabRequest collabReq);
    }
}
