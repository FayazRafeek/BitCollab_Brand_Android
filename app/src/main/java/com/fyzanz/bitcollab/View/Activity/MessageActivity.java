package com.fyzanz.bitcollab.View.Activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.fyzanz.bitcollab.View.Fragments.ChatListFragment;
import com.fyzanz.bitcollab.View.Fragments.ReqReceiveFragment;
import com.fyzanz.bitcollab.View.Fragments.ReqSentFragment;
import com.fyzanz.bitcollab.databinding.ActivityMessageBinding;
import com.google.android.material.tabs.TabLayoutMediator;

public class MessageActivity extends AppCompatActivity {

    ActivityMessageBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMessageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.msgBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        setUpPager();
    }

    MessageFragStateAdapter messageFragStateAdapter;
    void setUpPager(){
        messageFragStateAdapter = new MessageFragStateAdapter(this);
        binding.msgPager.setAdapter(messageFragStateAdapter);

        new TabLayoutMediator(binding.msgTab, binding.msgPager,
                (tab, position) -> tab.setText(position == 0 ? "SENT" : "RECEIVED")
        ).attach();
    }

    public class MessageFragStateAdapter extends FragmentStateAdapter{

        public MessageFragStateAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }


        ReqReceiveFragment chatListFragment; ReqSentFragment requestListFragment;
        @NonNull
        @Override
        public Fragment createFragment(int position) {
            if(position == 1){
                if(chatListFragment == null) chatListFragment = new ReqReceiveFragment();
                return chatListFragment;
            } else {
                if(requestListFragment == null) requestListFragment = new ReqSentFragment();
                return requestListFragment;
            }
        }


        @Override
        public int getItemCount() {
            return 2;
        }
    }
}

