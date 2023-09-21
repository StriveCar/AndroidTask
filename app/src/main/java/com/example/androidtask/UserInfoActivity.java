package com.example.androidtask;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.androidtask.network.RetrofitClient;
import com.example.androidtask.network.service.PhotoService;
import com.example.androidtask.response.BaseResponse;
import com.example.androidtask.response.ImageUrl;
import com.example.androidtask.response.LoginData;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener, GenderBottomSheetFragment.OnGenderSelectedListener {

    private Toolbar toolbar;
    private ImageView ivHeadImage;
    private CardView smallIcon;
    private TextView tvUsername, tvIntroduce, tvSex;
    private RelativeLayout rlAccount, rlSex, rlSignature;
    private LoginData mloginData = LoginData.getMloginData();
    private Uri mUri;
    private final PhotoService photoService = RetrofitClient.getInstance().getService(PhotoService.class);
    private ActivityResultLauncher<Intent> mResultLauncher;
    private Intent intent;
    private Bundle bundle;
    private PhotoPopupWindow mPhotoPopupWindow;
    private Integer choose = -1;
    //拍照
    private static final int TAKE_PHOTO = 1;
    //获取相册的图片
    private static final int CHOOSE_PHOTO = 2;

    public static String MODE = "mode";

    private int insert = 0;

    private LoginData loginData;
    private String jsonString;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        toolbar = findViewById(R.id.tool_bar);
        ivHeadImage = findViewById(R.id.iv_head_image);
        smallIcon = findViewById(R.id.small_icon);
        tvUsername = findViewById(R.id.tv_user_name);
        tvIntroduce = findViewById(R.id.tv_introduce);
        tvSex = findViewById(R.id.tv_sex);
        rlAccount = findViewById(R.id.rl_account);
        rlSignature = findViewById(R.id.rl_signature);
        rlSex = findViewById(R.id.rl_sex);

        initData();

        rlAccount.setOnClickListener(this);
        rlSex.setOnClickListener(this);
        rlSignature.setOnClickListener(this);
        ivHeadImage.setOnClickListener(this);
        smallIcon.setOnClickListener(this);
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (mloginData.getAvatar() != null) {
            Glide.with(this).load(mloginData.getAvatar()).diskCacheStrategy(DiskCacheStrategy.NONE).into(ivHeadImage);
        } else {
            Glide.with(this).load(R.drawable.bysl).diskCacheStrategy(DiskCacheStrategy.NONE).into(ivHeadImage);
        }
        tvUsername.setText(mloginData.getUsername());
        tvIntroduce.setText(mloginData.getIntroduce());
        if (mloginData.getSex() % 2 == 0) {
            tvSex.setText("女");
        } else {
            tvSex.setText("男");
        }
    }

    public void initData() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("编辑资料");

        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        gson = new Gson();
        jsonString = gson.toJson(mloginData);

        mResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                Intent intent = result.getData();
                if (intent != null) {
                    byte[] imageBytes = null;
                    if (choose == TAKE_PHOTO) {
                        Bitmap image = intent.getExtras().getParcelable("data");
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                        imageBytes = byteArrayOutputStream.toByteArray();
                        ivHeadImage.setImageBitmap(image);
                    } else if (choose == CHOOSE_PHOTO) {
                        mUri = intent.getData();
                        if (mUri != null) {
                            ivHeadImage.setImageURI(mUri);
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
                                insert++;
                                mloginData.setAvatar(response.body().getData().getImageUrlList().get(0));
                                Toast.makeText(UserInfoActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<BaseResponse<ImageUrl>> call, @NonNull Throwable t) {
                            Toast.makeText(UserInfoActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.teal_200));
        }
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


    private void showSelectDialog() {
        mPhotoPopupWindow = new PhotoPopupWindow(UserInfoActivity.this, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.icon_btn_select) {
                    if (ContextCompat.checkSelfPermission(UserInfoActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(UserInfoActivity.this, new String[]{Manifest.permission.CAMERA}, 200);
                    }
                    else {
                        insert++;
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
                    if (ContextCompat.checkSelfPermission(UserInfoActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        // 如果没有相机权限，请求权限
                        ActivityCompat.requestPermissions(UserInfoActivity.this, new String[]{Manifest.permission.CAMERA}, 100);
                    }else {
                        insert++;
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
        View rootView = LayoutInflater.from(UserInfoActivity.this).inflate(R.layout.activity_add, null);
        mPhotoPopupWindow.showAtLocation(rootView,
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.rl_account) {
            insert++;
            intent = new Intent(this, ModifyUsernameActivity.class);
            bundle = new Bundle();
            bundle.putInt(MODE, 1);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (v.getId() == R.id.rl_signature) {
            insert++;
            intent = new Intent(this, ModifyUsernameActivity.class);
            bundle = new Bundle();
            bundle.putInt(MODE, 2);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (v.getId() == R.id.rl_sex) {
            GenderBottomSheetFragment bottomSheetFragment = new GenderBottomSheetFragment();
            bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
        } else if (v.getId() == R.id.iv_head_image || v.getId() == R.id.small_icon) {
                showSelectDialog();
        }
    }

    @Override
    public void onBackPressed() {
        if (insert > 0) {
            photoService.updateInfo(mloginData).enqueue(new Callback<BaseResponse<Object>>() {
                @Override
                public void onResponse(@NonNull Call<BaseResponse<Object>> call, @NonNull Response<BaseResponse<Object>> response) {
                    if (response.body() != null && response.body().getCode() == 200) {
                        Toast.makeText(UserInfoActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                    } else {
                        LoginData.setMloginData(gson.fromJson(jsonString, LoginData.class));
                        Toast.makeText(UserInfoActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<BaseResponse<Object>> call, @NonNull Throwable t) {
                    Toast.makeText(UserInfoActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                }
            });
        }
        super.onBackPressed();
    }

    @Override
    public void onGenderSelected(String gender) {
        if (!tvSex.getText().equals(gender)) {
            insert++;
            tvSex.setText(gender);
        }
        if (gender.equals("男")) {
            mloginData.setSex(1);
        } else {
            mloginData.setSex(2);
        }
    }
}