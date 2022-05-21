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
import com.fyzanz.bitcollab.databinding.BrandListLayoutBinding;

import java.util.ArrayList;
import java.util.List;

public class BrandListAdapter extends RecyclerView.Adapter<BrandListAdapter.BrandListVH> {

    Context context;
    List<Brand> brands = new ArrayList<>();
    PopBrandAdapter.BrandListAction listner;

    public BrandListAdapter(Context context, PopBrandAdapter.BrandListAction listner) {
        this.context = context;
        this.listner = listner;
    }

    @NonNull
    @Override
    public BrandListVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BrandListLayoutBinding binding = BrandListLayoutBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new BrandListVH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BrandListVH holder, int position) {

        Brand brand = brands.get(position);

        holder.binding.brandListName.setText(brand.getBrandName());

        Glide.with(context)
                .load(brand.getLogoImgUrl())
               .centerCrop()
                .into(holder.binding.brandLogoImage);

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

        holder.binding.brandListCat.setText(category);

        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listner.onBrandClick(brand);
            }
        });
    }

    public void updateList(List<Brand> brands){
        this.brands = brands;
    }

    @Override
    public int getItemCount() {
        return brands == null ? 0 : brands.size();
    }

    public static class BrandListVH extends RecyclerView.ViewHolder{

        BrandListLayoutBinding binding;
        public BrandListVH(@NonNull BrandListLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
