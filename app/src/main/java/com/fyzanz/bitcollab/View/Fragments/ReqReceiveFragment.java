package com.fyzanz.bitcollab.View.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.fyzanz.bitcollab.Model.Data.BasicResponse;
import com.fyzanz.bitcollab.Model.Data.CollabRequest;
import com.fyzanz.bitcollab.Model.Repository.UserDataRepo;
import com.fyzanz.bitcollab.Model.Utils.AppSingleton;
import com.fyzanz.bitcollab.View.Activity.ReqDetailActivity;
import com.fyzanz.bitcollab.View.Adapter.CollabReqAdapter;
import com.fyzanz.bitcollab.databinding.FragmentRequestListBinding;

import java.util.List;

public class ReqReceiveFragment extends Fragment implements CollabReqAdapter.CollabReqClick{

    FragmentRequestListBinding binding;
    String USER_TYPE;
    UserDataRepo userDataRepo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRequestListBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        userDataRepo = UserDataRepo.getInstance();
        USER_TYPE = AppSingleton.INSTANCE.getUSER_TYPE();

        if(USER_TYPE.equals("INFLUENCER")){
            fetchInfCollabReq();
        } else {
            fetchBrnCollabReq();
        }


        binding.reqSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(USER_TYPE.equals("INFLUENCER")){
                    fetchInfCollabReq();
                }
            }
        });
    }


    void fetchInfCollabReq(){

        userDataRepo.fetchInfCollabReqList("B_2_I")
                .observe(getViewLifecycleOwner(), new Observer<BasicResponse>() {
                    @Override
                    public void onChanged(BasicResponse basicResponse) {
                        switch (basicResponse.getStatus()){
                            case "LOADING" : showLoading(); break;
                            case "SUCCESS" : updateRecycler((List<CollabRequest>) basicResponse.getData()); break;
                            case "ERROR" :  showError(basicResponse.getError()); break;
                        }
                    }
                });

    }

    CollabReqAdapter collabReqAdapter;
    void updateRecycler(List<CollabRequest> reqs){

        stopLoading();
        if(collabReqAdapter == null){
            collabReqAdapter = new CollabReqAdapter(getContext(),this);
            binding.reqRecycler.setAdapter(collabReqAdapter);
            binding.reqRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        }

        collabReqAdapter.updateList(reqs);
    }

    void fetchBrnCollabReq(){
        userDataRepo.fetchBrnCollabReq("I_2_B")
                .observe(getViewLifecycleOwner(), new Observer<BasicResponse>() {
                    @Override
                    public void onChanged(BasicResponse basicResponse) {
                        switch (basicResponse.getStatus()){
                            case "LOADING" : showLoading(); break;
                            case "SUCCESS" : updateRecycler((List<CollabRequest>) basicResponse.getData()); break;
                            case "ERROR" :  showError(basicResponse.getError()); break;
                        }
                    }
                });
    }

    void showLoading(){
        binding.reqSwipe.setRefreshing(true);
    }
    void stopLoading(){
        binding.reqSwipe.setRefreshing(false);
    }

    void showError(Throwable error){
        stopLoading();
        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onReqClick(CollabRequest collabReq) {
        AppSingleton.getInstance().setSelectedReq(collabReq);
        startActivity(new Intent(getActivity(), ReqDetailActivity.class));
    }
}
