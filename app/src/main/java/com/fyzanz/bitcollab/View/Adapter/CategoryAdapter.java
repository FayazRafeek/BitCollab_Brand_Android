package com.fyzanz.bitcollab.View.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fyzanz.bitcollab.BaseApplication;
import com.fyzanz.bitcollab.Model.Data.Category;
import com.fyzanz.bitcollab.R;
import com.fyzanz.bitcollab.databinding.CategoryListItemBinding;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryVH> {

    String[] categories;
    Context context;
    OnCategoryClick listner;

    public CategoryAdapter(OnCategoryClick listner) {
        this.listner = listner;
        this.context = BaseApplication.getContext();
        categories = context.getResources().getStringArray(R.array.categories);
    }

    @NonNull
    @Override
    public CategoryVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CategoryListItemBinding binding = CategoryListItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new CategoryVH(binding, listner);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryVH holder, int position) {
        String item = categories[position];
        holder.binding.catLabel.setText(item);

        switch (item){
            case "Fitness" : holder.binding.catBackImage.setImageDrawable(context.getDrawable(R.drawable.fitness_cat_back)); break;
            case "Travel" : holder.binding.catBackImage.setImageDrawable(context.getDrawable(R.drawable.travel_cat_back)); break;
            case "Fashion" : holder.binding.catBackImage.setImageDrawable(context.getDrawable(R.drawable.fashion_cat_back)); break;
            case "Food" : holder.binding.catBackImage.setImageDrawable(context.getDrawable(R.drawable.food_cat_back)); break;
            case "Creative" : holder.binding.catBackImage.setImageDrawable(context.getDrawable(R.drawable.creative_cat_back)); break;
            case "Sports" : holder.binding.catBackImage.setImageDrawable(context.getDrawable(R.drawable.sports_cat_back)); break;
        }
    }

    @Override
    public int getItemCount() {
        return categories.length;
    }

    public class CategoryVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        CategoryListItemBinding binding;
        OnCategoryClick listner;
        public CategoryVH(@NonNull CategoryListItemBinding binding, OnCategoryClick listner) {
            super(binding.getRoot());
            this.binding = binding;
            this.listner = listner;

            binding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listner.onClickCat(this,categories[getAdapterPosition()]);
        }
    }

    public interface OnCategoryClick{
        void onClickCat(CategoryVH viewHolder, String category);
    }
}
