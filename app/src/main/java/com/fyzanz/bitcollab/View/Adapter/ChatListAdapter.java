package com.fyzanz.bitcollab.View.Adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatListVH> {


    @NonNull
    @Override
    public ChatListVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListVH holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ChatListVH extends RecyclerView.ViewHolder{

        public ChatListVH(@NonNull View itemView) {
            super(itemView);
        }
    }
}
