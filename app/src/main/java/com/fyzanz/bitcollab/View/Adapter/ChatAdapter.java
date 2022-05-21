package com.fyzanz.bitcollab.View.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fyzanz.bitcollab.Model.Data.Chat;
import com.fyzanz.bitcollab.Model.Utils.AppSingleton;
import com.fyzanz.bitcollab.databinding.ChatLayoutItemBinding;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatVH> {


    Context context;
    String userType;
    List<Chat> chats = new ArrayList<>();

    public ChatAdapter(Context context) {
        this.context = context;
        userType = AppSingleton.getInstance().getUSER_TYPE();
    }

    @NonNull
    @Override
    public ChatVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ChatLayoutItemBinding binding = ChatLayoutItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ChatVH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatVH holder, int position) {

        Chat chat = chats.get(position);

        String type = "";
        if(userType.equals("INFLUENCER")){
            if(chat.getType().equals("I_2_B"))
                type = "to";
            else type = "from";
        } else {
            if(chat.getType().equals("I_2_B"))
                type = "from";
            else type = "to";
        }

        PrettyTime prettyTime = new PrettyTime(Locale.getDefault());
        String ago = prettyTime.format(new Date(chat.getTs()));

        if(type.equals("from")) {
            holder.binding.fromChatContent.setText(chat.getContent());
            holder.binding.fromTime.setText(ago);
            holder.binding.toFromParent.setVisibility(View.GONE); holder.binding.chatFromParent.setVisibility(View.VISIBLE);
        } else {
            holder.binding.toChatContent.setText(chat.getContent());
            holder.binding.toTime.setText(ago);
            holder.binding.toFromParent.setVisibility(View.VISIBLE); holder.binding.chatFromParent.setVisibility(View.GONE);
        }

    }

    public void updateList(List<Chat> list){
        chats = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    class ChatVH extends RecyclerView.ViewHolder{

        ChatLayoutItemBinding binding;
        public ChatVH(@NonNull ChatLayoutItemBinding itemView) {
            super(itemView.getRoot());

            binding = itemView;
        }
    }
}
