package com.fyzanz.bitcollab.View.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fyzanz.bitcollab.Model.Data.FileItem;
import com.fyzanz.bitcollab.View.Fragments.CampaignCreate.CampCreateStep3Fragment;
import com.fyzanz.bitcollab.databinding.FileListLayoutBinding;

import java.util.ArrayList;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.FileVH>{

        ArrayList<FileItem> files = new ArrayList<>();
        FileListAction listner;

        public FileAdapter(FileListAction listner) {
            this.listner = listner;
        }

        @NonNull
        @Override
        public FileVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            FileListLayoutBinding binding = FileListLayoutBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
            return new FileVH(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull FileVH holder, int position) {
            FileItem file = files.get(position);
            String name = file.getFileName();
            if(name.length() > 25) name = name.substring(0,25);
            holder.binding.fileName.setText(name);

            switch (file.getStatus()){
                case "UPLOADING" : holder.binding.fileProgress.setVisibility(View.VISIBLE); holder.binding.deleteIcon.setVisibility(View.INVISIBLE); break;
                case "UPLOADED" : holder.binding.fileProgress.setVisibility(View.GONE); holder.binding.deleteIcon.setVisibility(View.VISIBLE); break;
            }

            Log.d("333", "onBindViewHolder: Status " + file.getStatus());

            holder.binding.deleteIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listner.onDelete(files.get(holder.getAdapterPosition()));
                }
            });
        }

        @Override
        public int getItemCount() {
            return files.size();
        }

        public void updateList(ArrayList<FileItem> list){

            this.files = list;
            for (FileItem item : files){
                Log.d("333", "updateList: UPDATE In Recycler " + item.getStatus());
            }
            notifyDataSetChanged();
        }

        public void updateAtPosition(int pos){
            notifyItemChanged(pos);
        }

        class FileVH extends RecyclerView.ViewHolder{

            FileListLayoutBinding binding;
            public FileVH(@NonNull FileListLayoutBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
        
        public interface FileListAction {
            void onDelete(FileItem fileName);
        }
    }
