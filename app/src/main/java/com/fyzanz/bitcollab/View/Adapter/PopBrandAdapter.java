package com.fyzanz.bitcollab.View.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fyzanz.bitcollab.Model.Data.Brand;
import com.fyzanz.bitcollab.databinding.BrandListItemBinding;

import java.util.ArrayList;
import java.util.List;

public class PopBrandAdapter extends RecyclerView.Adapter<PopBrandAdapter.PopBrandVh> {

    List<Brand> items = new ArrayList<>();
    Context context;
    BrandListAction listner;

    public PopBrandAdapter(Context context, BrandListAction listner) {
        this.context = context;
        this.listner = listner;
    }

    @NonNull
    @Override
    public PopBrandVh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BrandListItemBinding binding = BrandListItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new PopBrandVh(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PopBrandVh holder, int position) {

        Brand brand = items.get(position);

        holder.binding.brandTitle.setText(brand.getBrandName());
        Glide.with(context)
                .load(brand.getLogoImgUrl())
                .centerCrop()
                .into(holder.binding.brandLogo);

        String location = brand.getState() + ", " + brand.getCountry();
        holder.binding.brandProfLocation.setText(location);

        StringBuilder catSt = new StringBuilder();
        if(brand.getCategories() != null){
            int i=0;
            for(String s : brand.getCategories()){
                catSt.append(i > 0 ? " | " : "").append(s);
                i++;
            }
        }
        String category = catSt.toString();
        if(category.length() > 20)
            category = category.substring(0,17);

        holder.binding.tagline.setText(category);

        holder.binding.getRoot()
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listner.onBrandClick(brand);
                    }
                });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void updateList(List<Brand> brands){
        items = brands;
        notifyDataSetChanged();
    }

    public static class PopBrandVh extends RecyclerView.ViewHolder{

        BrandListItemBinding binding;
        public PopBrandVh(@NonNull BrandListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface BrandListAction {
        void onBrandClick(Brand brand);
    }
}
