package com.example.androidtask;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.androidtask.network.RetrofitClient;


import com.example.androidtask.network.service.PhotoService;
import com.example.androidtask.response.ImageText;
import com.example.androidtask.response.BaseResponse;
import com.example.androidtask.response.ImageText;
import com.example.androidtask.response.ImageUrl;
import com.example.androidtask.response.LoginData;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddActivity extends AppCompatActivity implements View.OnClickListener {

    //照片选取类
    PhotoPopupWindow mPhotoPopupWindow;

    DisplayImageOptions options;
    //权限常量
    private static final String TAG = "AddActivity";
    private static final int REQUEST_IMAGE_GET = 0;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_SMALL_IMAGE_CUTTING = 2;

    private Integer choose=-1;
    //拍照
    private static final int TAKE_PHOTO = 1;
    //获取相册的图片
    private static final int CHOOSE_PHOTO = 2;


    private final PhotoService photoService = RetrofitClient.getInstance().getService(PhotoService.class);

    private Uri mUri;
    private Button publish_button;
    private TextView sc_button;
    private ImageView pb_picture;
    private EditText pb_content;
    private EditText pb_title;

    private LoginData mlogindata = LoginData.getMloginData();

    private String title;
    private String content;
    private String imageCode;


    private ActivityResultLauncher<Intent> mResultLauncher;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        mlogindata.getId();
        publish_button = findViewById(R.id.addPic);
        pb_picture = findViewById(R.id.add_picture);
        sc_button = findViewById(R.id.sc_picture);

        pb_content = findViewById(R.id.add_content);
        pb_title = findViewById(R.id.add_title);

        publish_button.setOnClickListener(this);
        pb_picture.setOnClickListener(this);
        sc_button.setOnClickListener(this);

//        ImageLoader imageLoader= ImageLoader.getInstance();
//        imageLoader.init(ImageLoaderConfiguration.createDefault(AddActivity.this));

        //设置 ImageLoader的参数
//        options = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.drawable.kt_myaccount_auth) // 设置图片下载期间显示的图片
//                .showImageForEmptyUri(R.drawable.kt_myaccount_head) // 设置图片Uri为空或是错误的时候显示的图片
//                .showImageOnFail(R.drawable.ic_baseline_broken_image_24) // 设置图片加载或解码过程中发生错误显示的图片
//                .resetViewBeforeLoading(false) // default 设置图片在加载前是否重置、复位
//                .build();

//        ImageLoader.getInstance().displayImage("file://" + file.getPath(),pb_picture, options);


        mResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                Intent intent = result.getData();
                if (intent != null) {
                    byte[] imageBytes = null;
                    if (choose==TAKE_PHOTO) {
                        Bitmap image = intent.getExtras().getParcelable("data");
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                        imageBytes = byteArrayOutputStream.toByteArray();
                        pb_picture.setImageBitmap(image);
                    } else if (choose==CHOOSE_PHOTO) {
                        mUri = intent.getData();
                        if (mUri != null) {
                            pb_picture.setImageURI(mUri);
                            imageBytes = getImageBytes(mUri);
                        }
                    }
                    MediaType mediaType = MediaType.Companion.parse("multipart/form-data");
                    RequestBody requestBody = RequestBody.Companion.create(imageBytes, mediaType);
                    MultipartBody.Part imagePart = MultipartBody.Part.createFormData("fileList", "image.jpg", requestBody);

                    photoService.uploadImage(imagePart).enqueue(new Callback<BaseResponse<ImageUrl>>() {
                        @Override
                        public void onResponse(@NonNull Call<BaseResponse<ImageUrl>> call, @NonNull Response<BaseResponse<ImageUrl>> response) {
                            if (response.body() != null && response.body().getCode() == 200) {
                                imageCode=response.body().getData().getImageCode();
//                                mloginData.setAvatar(response.body().getData().getImageUrlList().get(0));
                                Toast.makeText(AddActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<BaseResponse<ImageUrl>> call, @NonNull Throwable t) {
                            Toast.makeText(AddActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });

    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.add_picture){
            Drawable defaultDrawable = getResources().getDrawable(R.drawable.add2);
            if (pb_picture.getDrawable().getConstantState().equals(defaultDrawable.getConstantState())){
                showSelectDialog();
            }
            else {
               return;
            }
        }
        if (view.getId() == R.id.addPic){
            uploadPic();
        }
    }

    private void uploadPic() {

        ImageText imageText = new ImageText();
        imageText.setImageCode(imageCode);
        imageText.setPUserId(mlogindata.getId());
        imageText.setContent(pb_content.getText().toString());
        imageText.setTitle(pb_title.getText().toString());


        photoService.uploadAdd(imageText).enqueue(new Callback<BaseResponse<Object>>() {
            @Override
            public void onResponse(Call<BaseResponse<Object>> call, Response<BaseResponse<Object>> response) {
                if (response.body().getCode() == 200) {
                    Toast.makeText(AddActivity.this, "新增成功", Toast.LENGTH_SHORT).show();
                    System.out.println("新增成功!  " + response.body());
                } else if (response.body().getCode() == 500) {
                    System.out.println(response.body().getMsg());
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<Object>> call, Throwable t) {
                Toast.makeText(AddActivity.this, "新增失败", Toast.LENGTH_SHORT).show();
            }
        });

        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    private void showSelectDialog() {


        mPhotoPopupWindow = new PhotoPopupWindow(AddActivity.this,new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.icon_btn_select){
                    choose = CHOOSE_PHOTO;
                    mPhotoPopupWindow.dismiss();
                    Intent intentPickPicture = new Intent();
                    intentPickPicture.setAction(Intent.ACTION_GET_CONTENT);
                    intentPickPicture.setType("image/*");
                    mResultLauncher.launch(intentPickPicture);
                }
            }
        }, new View.OnClickListener()
        {
            @Override
            public void onClick (View v){
                if (v.getId() == R.id.icon_btn_camera){
                    choose = TAKE_PHOTO;
                    // 权限已经申请，直接拍照
                    mPhotoPopupWindow.dismiss();
                    Intent intent = new Intent();
                    intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                    mResultLauncher.launch(intent);
                }

            }
        });
        View rootView = LayoutInflater.from(AddActivity.this).inflate(R.layout.activity_add, null);
        mPhotoPopupWindow.showAtLocation(rootView,
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode) {
//
//            case REQUEST_IMAGE_CAPTURE:
//                if (resultCode == RESULT_OK) {
//                    cropPhoto(Uri.fromFile(file));
//                }
//                break;
//            case REQUEST_SMALL_IMAGE_CUTTING:
//                if (resultCode == RESULT_OK) {
//                    cropPhoto(data.getData());
//                }
//                break;
//            case REQUEST_IMAGE_GET:
//                if (resultCode == RESULT_OK) {
//                    setImage(data);
//                }
//
//                break;
//        }
//


//    private void imageCapture() {
//        try {
//            if (file.exists()) {
//                file.delete();
//            }
//            file.createNewFile();
//        } catch (Exception e) {
//        }
//        Intent intent = new Intent();
//        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
//        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
//
//    }


    private Bitmap compressImage(Bitmap originalBitmap, int maxWidth, int maxHeight) {
        int width = originalBitmap.getWidth();
        int height = originalBitmap.getHeight();

        float scale = Math.min((float) maxWidth / width, (float) maxHeight / height);
        int newWidth = (int) (width * scale);
        int newHeight = (int) (height * scale);

        Bitmap compressedBitmap = Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true);
        return compressedBitmap;
    }

    public byte[] getImageBytes(Uri contentUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(contentUri);
            if (inputStream != null) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, bytesRead);
                }
                return byteArrayOutputStream.toByteArray();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 点击非编辑区域收起键盘
     * 获取点击事件
     */
    @CallSuper
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() ==  MotionEvent.ACTION_DOWN ) {
            View view = getCurrentFocus();
            if (isShouldHideKeyBord(view, ev)) {
                hideSoftInput(view.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 判定当前是否需要隐藏
     */
    protected boolean isShouldHideKeyBord(View v, MotionEvent ev) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left + v.getWidth();
            return !(ev.getX() > left && ev.getX() < right && ev.getY() > top && ev.getY() < bottom);
        }
        return false;
    }

    /**
     * 隐藏软键盘
     */
    private void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}