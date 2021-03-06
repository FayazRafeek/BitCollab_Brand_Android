package com.fyzanz.bitcollab.View.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.fyzanz.bitcollab.Model.Data.BasicResponse;
import com.fyzanz.bitcollab.View.Activity.MainActivity;
import com.fyzanz.bitcollab.View.Activity.OnBoardActivity;
import com.fyzanz.bitcollab.ViewModel.AuthViewModel;
import com.fyzanz.bitcollab.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment {


    FragmentLoginBinding binding;
    AuthViewModel authViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);

        if(authViewModel.getUSER_TYPE().equals("INFLUENCER"))
            binding.loginBtn.setText("LOGIN AS INFLUENCER");
        else
            binding.loginBtn.setText("LOGIN AS BRAND");

        binding.registerSwitchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authViewModel.getAUTH_TYPE().setValue("REGISTER");
            }
        });

        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //:TODO Button disable during login process
                startLogin();
            }
        });

        binding.closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authViewModel.getAUTH_TYPE().setValue("CHOOSE");
            }
        });
    }

    String email,password;
    void startLogin() {

        email = binding.loginEmailInp.getText().toString();
        password = binding.loginPassInp.getText().toString();

        authViewModel.startLogin(email,password)
                .observe(getActivity(), new Observer<BasicResponse>() {
                    @Override
                    public void onChanged(BasicResponse basicResponse) {
                        switch (basicResponse.getStatus()){
                            case "LOADING" :
                                showLoading();
                                break;
                            case "SUCCESS" :
                                hideLoading();
                                Toast.makeText(getActivity(), "Sign in successfull", Toast.LENGTH_SHORT).show();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        startActivity(new Intent(getActivity(),MainActivity.class));
                                        getActivity().finish();
                                    }
                                },600);
                                break;
                            case "ERROR" :
                                hideLoading();
                                Toast.makeText(getActivity(), "Login has failed", Toast.LENGTH_SHORT).show();
                                Toast.makeText(getActivity(), basicResponse.getError().getMessage(), Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });

    }

    void showLoading() { binding.loginProgress.setVisibility(View.VISIBLE); }
    void hideLoading() { binding.loginProgress.setVisibility(View.GONE); }


    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
