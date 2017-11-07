package com.tsaiweb.frogcroak;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;

import MyMethod.FileChooser;
import MyMethod.SharedService;
import ViewModel.ChatData;
import ViewModel.ImageData;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;


/**
 * A simple {@link Fragment} subclass.
 */
public class SoundRecognitionFragment extends MySharedFragment {
    private FileChooser fileChooser;
    private final int REQUEST_EXTERNAL_STORAGE = 18;
    private ImageView iv_AudioFrog;

    public SoundRecognitionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sound_recognition, container, false);
        MainActivity mainActivity = (MainActivity) getActivity();
        super.client = mainActivity.client;
        mainActivity.SetupUI(getActivity().findViewById(R.id.Activity_Outer));
        view.findViewById(R.id.ib_UploadWav)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SendWav();
                    }
                });
        iv_AudioFrog = (ImageView) view.findViewById(R.id.iv_AudioFrog);
        return view;
    }

    public void SendWav() {
        int permission = ActivityCompat.checkSelfPermission(getActivity(), READ_EXTERNAL_STORAGE);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            fileChooser = new FileChooser(getActivity(), SoundRecognitionFragment.this);
            if (!fileChooser.showFileChooser("audio/x-wav", null, false, true)) {
                SharedService.ShowTextToast("您沒有適合的檔案選取器", getActivity());
            }
        } else {
            requestPermissions(new String[]{READ_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fileChooser = new FileChooser(getActivity(), SoundRecognitionFragment.this);
                    if (!fileChooser.showFileChooser("audio/x-wav", null, false, true)) {
                        SharedService.ShowTextToast("您沒有適合的檔案選取器", getActivity());
                    }
                } else {
                    SharedService.ShowTextToast("您拒絕選取檔案", getActivity());
                }
                return;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case FileChooser.ACTIVITY_FILE_CHOOSER:
                if (fileChooser.onActivityResult(requestCode, resultCode, data)) {
                    File[] files = fileChooser.getChosenFiles();
                    UploadWav(files);
                }
                return;
        }
    }

    private void UploadWav(File[] files) {
        SharedService.ShowTextToast("音檔上傳中...", getActivity());

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("file[0]", files[0].getName(), RequestBody.create(MediaType.parse("audio/wav"), files[0]));
        RequestBody body = builder.build();
        Request request = new Request.Builder()
                .url("http://frogcroak.azurewebsites.net/api/frog/SoundRecognition")
                .post(body)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SharedService.ShowTextToast("請檢察網路連線", getActivity());
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final int StatusCode = response.code();
                final String ResMsg = response.body().string();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (StatusCode == 200) {
                            TextView tv_Result = (TextView) getActivity().findViewById(R.id.tv_Result);
                            String FrogName = new Gson().fromJson(ResMsg, String.class);
                            tv_Result.setText(FrogName);
                            iv_AudioFrog.setVisibility(View.VISIBLE);
                            if (FrogName.equals("台北樹蛙"))
                                iv_AudioFrog.setImageResource(R.drawable.tptreefrog);
                            else if (FrogName.equals("面天樹蛙"))
                                iv_AudioFrog.setImageResource(R.drawable.facefrog);
                            else if (FrogName.equals("澤蛙"))
                                iv_AudioFrog.setImageResource(R.drawable.waterfrog);
                            else if (FrogName.equals("黑蒙西氏小雨蛙"))
                                iv_AudioFrog.setImageResource(R.drawable.rainfrog);
                            else if (FrogName.equals("不要學青蛙叫好ㄅ好"))
                                iv_AudioFrog.setImageResource(R.drawable.people);
                        } else if (StatusCode == 400) {
                            SharedService.ShowErrorDialog(ResMsg, getActivity());
                        } else {
                            SharedService.ShowTextToast(StatusCode + "", getActivity());
                        }
                    }
                });

            }
        });
    }
}
