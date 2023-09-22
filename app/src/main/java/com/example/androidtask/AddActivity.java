package com.example.androidtask;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.androidtask.network.RetrofitClient;


import com.example.androidtask.network.service.PhotoService;
import com.example.androidtask.response.ImageText;
import com.example.androidtask.response.BaseResponse;
import com.example.androidtask.response.ImageUrl;
import com.example.androidtask.response.LoginData;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddActivity extends AppCompatActivity implements View.OnClickListener {

    //照片选取类
    PhotoPopupWindow mPhotoPopupWindow;

    private Integer choose = -1;
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

    private Toolbar toolbar;

    private ActivityResultLauncher<Intent> mResultLauncher;
    private List<byte[]> imageBytesList;
    private LinearLayout imageContainer, imageContainer1;

    LinearLayout horizontalLayout = null;

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

        imageContainer = findViewById(R.id.image_container);
        imageContainer1 = findViewById(R.id.image_container1);

        publish_button.setOnClickListener(this);
        pb_picture.setOnClickListener(this);
        sc_button.setOnClickListener(this);
        imageBytesList = new ArrayList<>();
        horizontalLayout = new LinearLayout(this);
        horizontalLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        horizontalLayout.setOrientation(LinearLayout.HORIZONTAL);


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
                    ImageView imageView = new ImageView(this);
                    imageView.setLayoutParams(new LinearLayout.LayoutParams(
                            320,
                            320
                    ));
                    imageView.setPadding(10, 4, 4, 10);
                    imageView.setBackgroundColor(getResources().getColor(R.color.white));
                    if (choose == TAKE_PHOTO) {
                        Bitmap image = intent.getExtras().getParcelable("data");
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                        imageBytes = byteArrayOutputStream.toByteArray();
                        imageView.setImageBitmap(image);
                        imageContainer.addView(imageView, imageBytesList.size() % 3);
                    } else if (choose == CHOOSE_PHOTO) {
                        mUri = intent.getData();
                        if (mUri != null) {
                            imageView.setImageURI(mUri);
                            imageBytes = getImageBytes(mUri);
                            imageContainer.addView(imageView, imageBytesList.size() % 3);
                        }
                    }

                    Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                    if (imageBytesList.size() % 3 == 0) {
                        horizontalLayout = new LinearLayout(this);
                        horizontalLayout.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        ));
                        horizontalLayout.setOrientation(LinearLayout.HORIZONTAL);
                        ImageView imageView2 = new ImageView(this);
                        imageView2.setLayoutParams(new LinearLayout.LayoutParams(
                                320,
                                320
                        ));
                        imageView2.setPadding(10, 4, 4, 10);
                        imageView2.setBackgroundColor(getResources().getColor(R.color.white));
                        imageView2.setImageBitmap(bitmap);
                        horizontalLayout.addView(imageView2);
                    } else if (imageBytesList.size() % 3 == 2) {
                        imageContainer.removeView(imageContainer.getChildAt(0));
                        imageContainer.removeView(imageContainer.getChildAt(0));
                        imageContainer.removeView(imageContainer.getChildAt(0));
                        ImageView imageView2 = new ImageView(this);
                        imageView2.setLayoutParams(new LinearLayout.LayoutParams(
                                320,
                                320
                        ));
                        imageView2.setPadding(10, 4, 4, 10);
                        imageView2.setBackgroundColor(getResources().getColor(R.color.white));
                        imageView2.setImageBitmap(bitmap);
                        horizontalLayout.addView(imageView2);
                        imageContainer1.addView(horizontalLayout, 0);
                    } else {
                        ImageView imageView2 = new ImageView(this);
                        imageView2.setLayoutParams(new LinearLayout.LayoutParams(
                                320,
                                320
                        ));
                        imageView2.setPadding(10, 4, 4, 10);
                        imageView2.setBackgroundColor(getResources().getColor(R.color.white));
                        imageView2.setImageBitmap(bitmap);
                        horizontalLayout.addView(imageView2);
                    }
                    imageBytesList.add(imageBytes);
                }
            }
        });

        toolbar = findViewById(R.id.tool_bar);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        toolbar.setTitle("发布");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.teal_200));
        }
    }


    // 当要退出此Activity时，再运行特定函数
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.add_picture) {
            Drawable defaultDrawable = getResources().getDrawable(R.drawable.add2);
            if (pb_picture.getDrawable().getConstantState().equals(defaultDrawable.getConstantState())) {
                showSelectDialog();
            } else {
                return;
            }
        }
        if (view.getId() == R.id.addPic) {
            LoadingDialog.getInstance(this).show();
            uploadPic(1);
            LoadingDialog.getInstance().dismiss();
        }
    }

    private void uploadPic(int mode) {
        MediaType mediaType = MediaType.Companion.parse("multipart/form-data");
        List<MultipartBody.Part> imageParts = new ArrayList<>();
        for (int i = 0; i < imageBytesList.size(); i++) {
            RequestBody requestBody = RequestBody.Companion.create(imageBytesList.get(i), mediaType);
            MultipartBody.Part imagePart = MultipartBody.Part.createFormData("fileList", "image" + i + ".jpg", requestBody);
            imageParts.add(imagePart);
        }

        photoService.uploadImage(imageParts).enqueue(new Callback<BaseResponse<ImageUrl>>() {
            @Override
            public void onResponse(Call<BaseResponse<ImageUrl>> call, Response<BaseResponse<ImageUrl>> response) {
                if (response.body() != null && response.body().getCode() == 200) {
                    imageCode = response.body().getData().getImageCode();
                    Toast.makeText(AddActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                    if (mode == 1) uploadAdd();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<ImageUrl>> call, Throwable t) {
                Toast.makeText(AddActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //新增图文分享
    private void uploadAdd() {

        ImageText imageText = new ImageText();
        imageText.setImageCode(imageCode);
        imageText.setPUserId(mlogindata.getId());
        imageText.setContent(pb_content.getText().toString());
        imageText.setTitle(pb_title.getText().toString());

        String textTitle = pb_title.getText().toString().trim();
        String textContent = pb_content.getText().toString().trim();
        if (textTitle.trim().isEmpty() || textContent.trim().isEmpty()) {
            Toast.makeText(AddActivity.this, "主题或内容未输入！", Toast.LENGTH_SHORT).show();
        } else {
            if (imageCode == null) {
                Toast.makeText(AddActivity.this, "请上传图片！", Toast.LENGTH_SHORT).show();
            } else {
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

                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }

        }

    }

    //弹出选择框
    private void showSelectDialog() {
        mPhotoPopupWindow = new PhotoPopupWindow(AddActivity.this, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.icon_btn_select) {
                    if (ContextCompat.checkSelfPermission(AddActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(AddActivity.this, new String[]{Manifest.permission.CAMERA}, 200);
                    } else {
                        choose = CHOOSE_PHOTO;
                        mPhotoPopupWindow.dismiss();
                        Intent intentPickPicture = new Intent();
                        intentPickPicture.setAction(Intent.ACTION_GET_CONTENT);
                        intentPickPicture.setType("image/*");
                        mResultLauncher.launch(intentPickPicture);
                    }
                }
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.icon_btn_camera) {
                    if (ContextCompat.checkSelfPermission(AddActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        // 如果没有相机权限，请求权限
                        ActivityCompat.requestPermissions(AddActivity.this, new String[]{Manifest.permission.CAMERA}, 100);
                    } else {
                        choose = TAKE_PHOTO;
                        mPhotoPopupWindow.dismiss();
                        Intent intent = new Intent();
                        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                        mResultLauncher.launch(intent);
                    }
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
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
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


    //退出时弹出是否保存
    @Override
    public void onBackPressed() {
        ImageText imageText = new ImageText();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //
        String textTitle = pb_title.getText().toString().trim();
        String textContent = pb_content.getText().toString().trim();
        if (textTitle.isEmpty() && textContent.isEmpty() && imageCode == null) {
            finish();
        } else {
            builder.setMessage("是否要保存当前内容？")
                    .setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MediaType mediaType = MediaType.Companion.parse("multipart/form-data");
                            List<MultipartBody.Part> imageParts = new ArrayList<>();
                            for (int i = 0; i < imageBytesList.size(); i++) {
                                RequestBody requestBody = RequestBody.Companion.create(imageBytesList.get(i), mediaType);
                                MultipartBody.Part imagePart = MultipartBody.Part.createFormData("fileList", "image" + i + ".jpg", requestBody);
                                imageParts.add(imagePart);
                            }

                            photoService.uploadImage(imageParts).enqueue(new Callback<BaseResponse<ImageUrl>>() {
                                @Override
                                public void onResponse(Call<BaseResponse<ImageUrl>> call, Response<BaseResponse<ImageUrl>> response) {
                                    if (response.body() != null && response.body().getCode() == 200) {
                                        imageCode = response.body().getData().getImageCode();
                                        Toast.makeText(AddActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                                        LoadingDialog.getInstance(AddActivity.this).show();
                                        imageText.setImageCode(imageCode);
                                        imageText.setPUserId(mlogindata.getId());
                                        imageText.setContent(pb_content.getText().toString());
                                        imageText.setTitle(pb_title.getText().toString());

                                        photoService.saveAdd(imageText).enqueue(new Callback<BaseResponse<Object>>() {
                                            @Override
                                            public void onResponse(Call<BaseResponse<Object>> call, Response<BaseResponse<Object>> response) {
                                                if (response.body().getCode() == 200) {
                                                    Toast.makeText(AddActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                                                    System.out.println("保存成功!  " + response.body());
                                                    System.out.println(imageCode + mlogindata.getId() + pb_content.getText().toString() + pb_title.getText().toString());
                                                    finish();
                                                } else if (response.body().getCode() == 500) {
                                                    Toast.makeText(AddActivity.this, "保存失败！保存图文时每一项内容都要填写", Toast.LENGTH_SHORT).show();
                                                    System.out.println(response.body().getMsg());
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<BaseResponse<Object>> call, Throwable t) {
                                                Toast.makeText(AddActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onFailure(Call<BaseResponse<ImageUrl>> call, Throwable t) {
                                    Toast.makeText(AddActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                                }
                            });
                            LoadingDialog.getInstance().dismiss();
                        }
                    })
                    .setNegativeButton("否", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 点击取消按钮，对话框消失
                            dialog.cancel();
                            finish();
                        }
                    })
                    .show();
        }

    }

}