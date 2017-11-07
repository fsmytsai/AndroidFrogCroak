package com.tsaiweb.frogcroak;


import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import MyMethod.FileChooser;
import MyMethod.MyDBHelper;
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
public class ChatFragment extends MySharedFragment {

    public RecyclerView rv_MessageList;
    private EditText et_Message;
    private MessageListAdapter messageListAdapter;
    private MyDBHelper helper;

    private MainActivity mainActivity;
    private Cursor cursor;
    private List<ChatData> chatDataList;

    private boolean isFirstLoad = true;
    private boolean isLoading = true;
    private boolean isFinishLoad = false;

    private final int REQUEST_EXTERNAL_STORAGE = 18;
    private FileChooser fileChooser;

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        mainActivity = (MainActivity) getActivity();
        helper = new MyDBHelper(getActivity(), "frog.db", null, 1);
        cursor = helper.getReadableDatabase().query("chat", null,
                null, null,
                null, null, "_id DESC");
        chatDataList = new ArrayList<>();
        super.client = mainActivity.client;
        initView(view);
        ReadData();
        return view;
    }

    private void initView(View view) {
        rv_MessageList = (RecyclerView) view.findViewById(R.id.rv_MessageList);
        et_Message = (EditText) view.findViewById(R.id.et_Message);
        final ImageButton ib_SendMessage = (ImageButton) view.findViewById(R.id.ib_SendMessage);
        ib_SendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendMessage();
            }
        });
        final ImageButton ib_SendImage = (ImageButton) view.findViewById(R.id.ib_SendImage);
        ib_SendImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendImage();
            }
        });
        et_Message.post(new Runnable() {
            @Override
            public void run() {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(et_Message.getHeight(), et_Message.getHeight());
                ib_SendMessage.setLayoutParams(params);
                ib_SendImage.setLayoutParams(params);
            }
        });
    }

    private int count = 0;
    private int newcount = 0;

    private void ReadData() {
//        while (newcount != 0) {
//            cursor.moveToNext();
//        }
        while (cursor.moveToNext()) {
            count++;

            chatDataList.add(new ChatData(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getInt(2),
                    cursor.getInt(3))
            );
            if (count % 30 == 0)
                break;
        }
        isFinishLoad = cursor.isAfterLast();
        isLoading = false;

        if (isFirstLoad) {
            isFirstLoad = false;

            rv_MessageList.setLayoutManager(new WrapContentLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true));
            messageListAdapter = new MessageListAdapter();
            rv_MessageList.setAdapter(messageListAdapter);
        } else {
            messageListAdapter.notifyDataSetChanged();
        }
    }

    public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.ViewHolder> {

        private final int left = 87;
        private final int right = 78;

        @Override
        public int getItemViewType(int position) {
            if (chatDataList.get(position).from == 0)
                return left;
            else
                return right;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            View view;
            if (viewType == right) {
                view = LayoutInflater.from(context).inflate(R.layout.rmessage_block, parent, false);
                view.setTag("R");
            } else {
                view = LayoutInflater.from(context).inflate(R.layout.lmessage_block, parent, false);
                view.setTag("L");
            }
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }


        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {

            if (chatDataList.get(position).type == 0) {
                holder.tv_Message.setVisibility(View.VISIBLE);
                if (getItemViewType(position) == right)
                    holder.iv_Frog.setVisibility(View.GONE);
                holder.tv_Message.setText(chatDataList.get(position).message);
            } else {
                holder.tv_Message.setVisibility(View.GONE);
                holder.iv_Frog.setVisibility(View.VISIBLE);
                String path = new File(getActivity().getFilesDir(), chatDataList.get(position).message).getPath();
                Bitmap bitmap = BitmapFactory.decodeFile(path);
                holder.iv_Frog.setImageBitmap(bitmap);
            }

            //避免重複請求
            if (position > chatDataList.size() * 0.6 && !isFinishLoad && !isLoading) {
                isLoading = true;
                ReadData();
            }

        }

        @Override
        public int getItemCount() {
            return chatDataList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView tv_Message;
            private ImageView iv_Frog;

            public ViewHolder(View itemView) {
                super(itemView);
                tv_Message = (TextView) itemView.findViewById(R.id.tv_Message);
                iv_Frog = (ImageView) itemView.findViewById(R.id.iv_Frog);
            }
        }
    }

    private void SendMessage() {
        SharedService.HideKeyboard(getActivity());
        mainActivity.activity_Outer.requestFocus();

        final String Message = et_Message.getText().toString();

        if (!Message.trim().equals("")) {

            if (SharedService.CheckNetWork(getActivity())) {
                et_Message.setText("");
                ContentValues values = new ContentValues();
                values.put("message", Message);
                values.put("isme", 1);
                values.put("type", 0);
                long id = helper.getWritableDatabase().insert("chat", null, values);

                chatDataList.add(0, new ChatData(
                        (int) id,
                        Message,
                        1,
                        0)
                );

                messageListAdapter.notifyItemRangeInserted(0, 1);
                rv_MessageList.scrollToPosition(0);
            }

            Request request = new Request.Builder()
                    .url(getString(R.string.BackEndPath) + "Api/Frog/Ask?message=" + Message)
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
                    getActivity().runOnUiThread(new Runnable() {//这是Activity的方法，会在主线程执行任务
                        @Override
                        public void run() {
                            if (StatusCode == 200) {

                                ContentValues values = new ContentValues();
                                values.put("message", new Gson().fromJson(ResMsg, String.class));
                                values.put("isme", 0);
                                values.put("type", 0);
                                long id = helper.getWritableDatabase().insert("chat", null, values);

                                chatDataList.add(0, new ChatData(
                                        (int) id,
                                        new Gson().fromJson(ResMsg, String.class),
                                        0,
                                        0)
                                );

                                messageListAdapter.notifyItemRangeInserted(0, 1);
                                rv_MessageList.scrollToPosition(0);

                            } else if (StatusCode == 400) {
                                SharedService.ShowErrorDialog(ResMsg, getActivity());
                            } else {
                                SharedService.ShowErrorDialog(StatusCode + "", getActivity());
                            }
                        }
                    });
                }
            });
        } else {
            SharedService.ShowTextToast("請輸入內容", getActivity());
        }
    }

    public void SendImage() {
        int permission = ActivityCompat.checkSelfPermission(getActivity(), READ_EXTERNAL_STORAGE);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            fileChooser = new FileChooser(getActivity(), ChatFragment.this);
            if (!fileChooser.showFileChooser("image/*", null, false, true)) {
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
                    fileChooser = new FileChooser(getActivity(), ChatFragment.this);
                    if (!fileChooser.showFileChooser("image/*", null, false, true)) {
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
                    UploadImg(files);
                }
                return;
        }
    }

    private void UploadImg(File[] files) {
        SharedService.ShowTextToast("圖片上傳中...", getActivity());

        if (SharedService.CheckNetWork(getActivity())) {
            byte[] bytes = new byte[(int) files[0].length()];
            final File file = new File(getActivity().getFilesDir(), files[0].getName());
            try {
                BufferedInputStream buf = new BufferedInputStream(new FileInputStream(files[0]));
                buf.read(bytes, 0, bytes.length);
                buf.close();
                FileOutputStream outputStream = new FileOutputStream(file);
                outputStream.write(bytes);
                outputStream.close();
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            ContentValues values = new ContentValues();
            values.put("message", file.getName());
            values.put("isme", 1);
            values.put("type", 1);
            long id = helper.getWritableDatabase().insert("chat", null, values);

            chatDataList.add(0, new ChatData(
                    (int) id,
                    file.getName(),
                    1,
                    1)
            );

            messageListAdapter.notifyItemRangeInserted(0, 1);
            rv_MessageList.scrollToPosition(0);
        }

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        String FileName = files[0].getName();
        String[] Type = FileName.split(Pattern.quote("."));
        if (Type[Type.length - 1].equals("jpg"))
            Type[Type.length - 1] = "jpeg";
        builder.addFormDataPart("file[0]", files[0].getName(), RequestBody.create(MediaType.parse("image/" + Type[Type.length - 1]), new byte[]{}));


        RequestBody body = builder.build();
        Request request = new Request.Builder()
                .url("https://southcentralus.api.cognitive.microsoft.com/customvision/v1.0/Prediction/0.0/image")
                .header("Prediction-Key", "0.0")
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
                            ImageData imageData = new Gson().fromJson(ResMsg, ImageData.class);
                            String result = "";
                            if (imageData.Predictions.get(0).Probability > 0.5)
                                result = imageData.Predictions.get(0).Tag;
                            else
                                result = "目前僅提供台北樹蛙、澤蛙、面天樹蛙、黑蒙西氏小雨蛙";

                            ContentValues values = new ContentValues();
                            values.put("message", result);
                            values.put("isme", 0);
                            values.put("type", 0);
                            long id = helper.getWritableDatabase().insert("chat", null, values);

                            chatDataList.add(0, new ChatData(
                                    (int) id,
                                    result,
                                    0,
                                    0)
                            );

                            messageListAdapter.notifyItemRangeInserted(0, 1);
                            rv_MessageList.scrollToPosition(0);
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

    public class WrapContentLinearLayoutManager extends LinearLayoutManager {
        public WrapContentLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            try {
                super.onLayoutChildren(recycler, state);
            } catch (IndexOutOfBoundsException e) {
                Log.e("probe", "meet a IOOBE in RecyclerView");
            }
        }
    }
}
