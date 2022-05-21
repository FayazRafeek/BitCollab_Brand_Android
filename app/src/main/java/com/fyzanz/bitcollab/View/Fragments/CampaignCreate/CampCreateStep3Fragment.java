package com.fyzanz.bitcollab.View.Fragments.CampaignCreate;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.FileUtils;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fyzanz.bitcollab.Model.Data.BasicResponse;
import com.fyzanz.bitcollab.Model.Data.FileItem;
import com.fyzanz.bitcollab.R;
import com.fyzanz.bitcollab.View.Adapter.FileAdapter;
import com.fyzanz.bitcollab.ViewModel.CampaignViewModel;
import com.fyzanz.bitcollab.databinding.FileListLayoutBinding;
import com.fyzanz.bitcollab.databinding.FragmentCampStep3Binding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class CampCreateStep3Fragment extends Fragment implements FileAdapter.FileListAction {


    FragmentCampStep3Binding binding;
    CampaignViewModel campaignViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCampStep3Binding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        campaignViewModel = new ViewModelProvider(getActivity()).get(CampaignViewModel.class);

        binding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gatherData();
            }
        });

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                campaignViewModel.prevPage();
            }
        });


        binding.campBannerChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageChoose();
            }
        });

        binding.campRefChooseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!IS_UPLOAD_IN_PROGRESS)
                    showRefChoose();
                else
                    Toast.makeText(getActivity(), "Upload in progress", Toast.LENGTH_SHORT).show();
            }
        });

    }

    void gatherData(){

        if(!IS_BANNER_CHOOSE) return;
        binding.campBannerImage.setDrawingCacheEnabled(true);
        binding.campBannerImage.buildDrawingCache();
        Bitmap bannerBitmap = ((BitmapDrawable) binding.campBannerImage.getDrawable()).getBitmap();
        ArrayList<String> refUrls = new ArrayList<>();
        if(!refFiles.isEmpty())
            for(FileItem file : refFiles){
                if(file.getStatus().equals("UPLOADED"))
                    refUrls.add(file.getDownloadUrl());
            }
        campaignViewModel.setNewCampStep3Data(bannerBitmap,refUrls);
        campaignViewModel.nextPage();
    }


    void showImageChoose(){
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});
        bannerSelectResult.launch(chooserIntent);
    }

    void showRefChoose(){

        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("*/*");
        getIntent.addCategory(Intent.CATEGORY_OPENABLE);

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("*/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Reference files");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});
        refSelectResult.launch(chooserIntent);
    }


    Boolean IS_BANNER_CHOOSE = false;
    ActivityResultLauncher<Intent> bannerSelectResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if(data != null){
                            Uri selectedImage = data.getData();
                            binding.campBannerImage.setImageURI(selectedImage);
                            IS_BANNER_CHOOSE = true;
                        }
                    }
                }
            });



    /* Ref File Logic */
    private static final String TAG = "333";
    ArrayList<FileItem> refFiles = new ArrayList<>();
    ActivityResultLauncher<Intent> refSelectResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if(data != null){

                            Uri selectedFile = data.getData();
                            String name = getFileName(selectedFile);
                            String path = getFilePathForN(selectedFile,getContext());

                            if(name != null){
                                FileItem fileItem = new FileItem();
                                fileItem.setFilePath(path);
                                fileItem.setFileName(name);
                                fileItem.setStatus("UPLOADING");

                                refFiles.add(fileItem);
                                updateFileList();

                                uploadFile(fileItem);

                            } else Toast.makeText(getContext(), "Failed to read file", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });

    FileAdapter fileAdapter;
    void updateFileList(){

        if(fileAdapter == null){
            fileAdapter = new FileAdapter(this);
            binding.fileList.setAdapter(fileAdapter);
            binding.fileList.setLayoutManager(new LinearLayoutManager(getContext()));
        }

        fileAdapter.updateList(refFiles);
    }

    Boolean IS_UPLOAD_IN_PROGRESS = false;
    void uploadFile(FileItem fileItem){
        Log.d(TAG, "uploadFile: Uploading level 1");
        IS_UPLOAD_IN_PROGRESS = true;
        campaignViewModel.uploadFile(fileItem.getFilePath(),fileItem.getFileName())
                .observe(getActivity(), new Observer<BasicResponse>() {
                    @Override
                    public void onChanged(BasicResponse basicResponse) {
                        Log.d(TAG, "uploadFile: Uploading status level 1 : " + basicResponse.getStatus());
                        switch (basicResponse.getStatus()){

                            case "SUCCESS" :
                                Log.d(TAG, "onChanged: Case success called");
                                fileItem.setStatus("UPLOADED");
                                fileItem.setDownloadUrl((String)basicResponse.getData());
                                updateFileListItem(fileItem);
                                IS_UPLOAD_IN_PROGRESS = false; break;
                            case "ERROR" :
                                IS_UPLOAD_IN_PROGRESS = false;
                                Toast.makeText(getContext(), "Error uploading..", Toast.LENGTH_SHORT).show(); break;
                        }
                    }
                });
    }

    void updateFileListItem(FileItem fileItem){

        Log.d(TAG, "updateFileListItem: Update list called");

        if(refFiles.isEmpty()) return;
        int pos = 0;
        for(FileItem file : refFiles){
            if(file.getFilePath().equals(fileItem.getFilePath()))
                break;
            else
                pos++;
        }

        Log.d(TAG, "updateFileListItem: Update list called " + pos + fileItem.getStatus());
        refFiles.set(pos,fileItem);
        updateFileList();
    }

    @Override
    public void onDelete(FileItem fileName) {
        campaignViewModel.deleteFile(fileName.getFileName());
        refFiles.remove(fileName);
        updateFileList();
    }

    private String getFilePathForN(Uri uri, Context context) {
        Uri returnUri = uri;
        Cursor returnCursor = context.getContentResolver().query(returnUri, null, null, null, null);
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
        returnCursor.moveToFirst();
        String name = (returnCursor.getString(nameIndex));
        String size = (Long.toString(returnCursor.getLong(sizeIndex)));
        File file = new File(context.getFilesDir(), name);
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            FileOutputStream outputStream = new FileOutputStream(file);
            int read = 0;
            int maxBufferSize = 1 * 1024 * 1024;
            int bytesAvailable = inputStream.available();

            //int bufferSize = 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);

            final byte[] buffers = new byte[bufferSize];
            while ((read = inputStream.read(buffers)) != -1) {
                outputStream.write(buffers, 0, read);
            }
            inputStream.close();
            outputStream.close();
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
        return file.getPath();
    }
    public String getFileName(Uri uri) {
        Cursor cursor = getActivity().getContentResolver()
                .query(uri, null, null, null, null, null);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                @SuppressLint("Range") String displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                Log.i(TAG, "Display Name: " + displayName);

                return displayName;

            }
        } finally {
            cursor.close();
        }

        return null;
    }
    /*          */




}
