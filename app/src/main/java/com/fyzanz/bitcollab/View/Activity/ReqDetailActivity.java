package com.fyzanz.bitcollab.View.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.fyzanz.bitcollab.Model.Data.Chat;
import com.fyzanz.bitcollab.Model.Data.CollabRequest;
import com.fyzanz.bitcollab.Model.Utils.AppSingleton;
import com.fyzanz.bitcollab.R;
import com.fyzanz.bitcollab.View.Adapter.ChatAdapter;
import com.fyzanz.bitcollab.databinding.ActivityReqDetailBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ReqDetailActivity extends AppCompatActivity {


    ActivityReqDetailBinding binding;
    CollabRequest collabRequest;
    String userType = "INFLUENCER";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityReqDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        collabRequest = AppSingleton.getInstance().getSelectedReq();
        if (collabRequest == null) finish();

        userType = AppSingleton.getInstance().getUSER_TYPE();

        if(userType.equals("INFLUENCER")){
            setInfUi();
        } else setBrandUi();

        updateSeenStatus();

        binding.reqAccptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acceptReq();
            }
        });

        binding.reqRejBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rejectReq();
            }
        });
    }

    void setInfUi(){

        binding.brandLayout.brandListName.setText(collabRequest.getBrandName());

        Glide.with(this)
                .load(collabRequest.getBrandLogoUrl())
                .centerCrop().into(binding.brandLayout.brandLogoImage);

        binding.brandLayout.brandListCat.setText(collabRequest.getBrandCat());
        binding.brandLayout.brandProfLocation.setText(collabRequest.getBrandLoc());

        binding.reqTitle.setText(collabRequest.getReqTitle());
        binding.reqBody.setText(collabRequest.getReqBody());

        if(collabRequest.getStatus() == null) collabRequest.setStatus("Pending");

        if(collabRequest.getStatus().equals("Pending") ||collabRequest.getStatus().equals("Seen")){
            if(collabRequest.getType().equals("B_2_I"))
                binding.actionParent.setVisibility(View.VISIBLE);
            else binding.actionParent.setVisibility(View.GONE);
        } else binding.actionParent.setVisibility(View.GONE);


        binding.status.setText(collabRequest.getStatus());
        switch (collabRequest.getStatus()){
            case "Pending" : binding.status.setTextColor(ContextCompat.getColor(this, R.color.orange_300)); break;
            case "Seen" : binding.status.setTextColor(ContextCompat.getColor(this, R.color.purple_200)); break;
            case "Accepted" : binding.status.setTextColor(ContextCompat.getColor(this, R.color.darkGreen));break;
            case "Rejected" : binding.status.setTextColor(ContextCompat.getColor(this, R.color.red_300)); break;
        }

        if(collabRequest.getStatus().equals("Accepted")) setupchat();

    }

    void setBrandUi(){


        binding.brandLayout.brandListName.setText(collabRequest.getInfName());

        Glide.with(this)
                .load(collabRequest.getInfLogoUrl())
                .centerCrop().into(binding.brandLayout.brandLogoImage);

        binding.brandLayout.brandListCat.setText(collabRequest.getInfCat());
        binding.brandLayout.brandProfLocation.setText(collabRequest.getInfLocation());

        binding.reqTitle.setText(collabRequest.getReqTitle());
        binding.reqBody.setText(collabRequest.getReqBody());


        if(collabRequest.getStatus() == null) collabRequest.setStatus("Pending");

        if(collabRequest.getStatus().equals("Seen") || collabRequest.getStatus().equals("Pending")) {
            if (collabRequest.getType().equals("B_2_I"))
                binding.actionParent.setVisibility(View.GONE);
            else binding.actionParent.setVisibility(View.VISIBLE);
        } else binding.actionParent.setVisibility(View.GONE);



        binding.status.setText(collabRequest.getStatus());
        switch (collabRequest.getStatus()){
            case "Pending" : binding.status.setTextColor(ContextCompat.getColor(this, R.color.orange_300)); break;
            case "Seen" : binding.status.setTextColor(ContextCompat.getColor(this, R.color.purple_200)); break;
            case "Accepted" : binding.status.setTextColor(ContextCompat.getColor(this, R.color.darkGreen)); break;
            case "Rejected" : binding.status.setTextColor(ContextCompat.getColor(this, R.color.red_300)); break;
        }


        if(collabRequest.getStatus().equals("Accepted")) setupchat();
    }


    void updateSeenStatus(){
        if(collabRequest.getStatus() == null || collabRequest.getStatus().equals("Pending")){
            FirebaseFirestore.getInstance().collection("Collabs")
                    .document(collabRequest.getReqId())
                    .update("status","Seen");
        }
    }


    void acceptReq(){

        FirebaseFirestore.getInstance().collection("Collabs")
                .document(collabRequest.getReqId())
                .update("status","Accepted")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){
                            Toast.makeText(ReqDetailActivity.this, "Request accepted successfully", Toast.LENGTH_SHORT).show();
                            refetchRequest();
                        } else {
                            Toast.makeText(ReqDetailActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }

    void rejectReq(){
        FirebaseFirestore.getInstance().collection("Collabs")
                .document(collabRequest.getReqId())
                .update("status","Rejected")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){
                            Toast.makeText(ReqDetailActivity.this, "Request has been rejected", Toast.LENGTH_SHORT).show();
                            refetchRequest();
                        } else {
                            Toast.makeText(ReqDetailActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    void refetchRequest(){

        FirebaseFirestore.getInstance().collection("Collabs")
                .document(collabRequest.getReqId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if(task.isSuccessful()){
                            collabRequest = task.getResult().toObject(CollabRequest.class);
                            if(userType.equals("INFLUENCER")){
                                setInfUi();
                            } else setBrandUi();
                        }
                    }
                });
    }


    void setupchat(){

        binding.chatParent.setVisibility(View.VISIBLE);

        binding.chatSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.chatSendBtn.setEnabled(false);
                String content = binding.chatInp.getText().toString();
                sendChat(content);
            }
        });

        listenChat();
    }


    void listenChat(){

        FirebaseFirestore.getInstance().collection("Collabs")
                .document(collabRequest.getReqId())
                .collection("Chat")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error != null){
                            Toast.makeText(ReqDetailActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        List<Chat> chats = new ArrayList<>();
                        for(DocumentSnapshot doc : value){
                            chats.add(doc.toObject(Chat.class));
                        }

                        chats = sorting(chats);

                        updateChatRecycler(chats);
                    }
                });
    }

    ChatAdapter chatAdapter;
    void updateChatRecycler(List<Chat> chats){

        if(chatAdapter == null){
            chatAdapter = new ChatAdapter(this);
            binding.chatRecycler.setAdapter(chatAdapter);
            binding.chatRecycler.setLayoutManager(new LinearLayoutManager(this));
        }

        chatAdapter.updateList(chats);

        if(chats.size() > 0)
            binding.chatRecycler.scrollToPosition(chats.size() -1);

    }

    List<Chat> sorting(List<Chat> list){

        Collections.sort(list, new Comparator<Chat>() {
            @Override
            public int compare(Chat chat, Chat t1) {
                return chat.getTs().compareTo(t1.getTs());
            }
        });

        return list;

    }

    void sendChat(String content){

        Chat chat = new Chat();
        String id = System.currentTimeMillis() + "";

        chat.setMsgId(id);
        chat.setBrandName(collabRequest.getBrandName());
        chat.setBrandId(collabRequest.getBrandId());
        chat.setInfName(collabRequest.getInfName());
        chat.setInfId(collabRequest.getInfId());

        if(userType.equals("INFLUENCER"))
            chat.setType("I_2_B");
        else
            chat.setType("B_2_I");

        chat.setTs(System.currentTimeMillis());

        chat.setContent(content);

        FirebaseFirestore.getInstance().collection("Collabs")
                .document(collabRequest.getReqId())
                .collection("Chat")
                .document(chat.getMsgId())
                .set(chat)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        binding.chatSendBtn.setEnabled(true);
                        if(task.isSuccessful()){
                            binding.chatInp.setText("");
                        } else {
                            Toast.makeText(ReqDetailActivity.this, "failed to send message", Toast.LENGTH_SHORT).show();
                            Toast.makeText(ReqDetailActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
