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
import android.view.View;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener, GenderBottomSheetFragment.OnGenderSelectedListener {

    private Toolbar toolbar;
    private CircleImageView iv_head_image;
    private CardView small_icon;
    private TextView tv_user_name, tv_introduce, tv_sex;
    private RelativeLayout rl_account, rl_sex, rl_signature;
    private LoginData mloginData = LoginData.getMloginData();

    private Uri mUri;

    private final PhotoService photoService = RetrofitClient.getInstance().getService(PhotoService.class);
    private ActivityResultLauncher<Intent> mResultLauncher;
    private Intent intent;
    private Bundle bundle;

    private Integer choose=-1;
    //拍照
    private static final int TAKE_PHOTO = 1;
    //获取相册的图片
    private static final int CHOOSE_PHOTO = 2;

    public static String MODE = "mode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        toolbar = findViewById(R.id.tool_bar);
        iv_head_image = findViewById(R.id.iv_head_image);
        small_icon = findViewById(R.id.small_icon);
        tv_user_name = findViewById(R.id.tv_user_name);
        tv_introduce = findViewById(R.id.tv_introduce);
        tv_sex = findViewById(R.id.tv_sex);
        rl_account = findViewById(R.id.rl_account);
        rl_signature = findViewById(R.id.rl_signature);
        rl_sex = findViewById(R.id.rl_sex);

        initData();

        rl_account.setOnClickListener(this);
        rl_sex.setOnClickListener(this);
        rl_signature.setOnClickListener(this);
        iv_head_image.setOnClickListener(this);
        small_icon.setOnClickListener(this);
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (mloginData.getAvatar() != null) {
            Glide.with(this).load(mloginData.getAvatar()).diskCacheStrategy(DiskCacheStrategy.NONE).into(iv_head_image);
        } else {
            Glide.with(this).load(R.drawable.bysl).diskCacheStrategy(DiskCacheStrategy.NONE).into(iv_head_image);
        }
        tv_user_name.setText(mloginData.getUsername());
        tv_introduce.setText(mloginData.getIntroduce());
        if (mloginData.getSex() % 2 == 0) {
            tv_sex.setText("女");
        } else {
            tv_sex.setText("男");
        }
    }

    public void initData() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("编辑资料");

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

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
                        iv_head_image.setImageBitmap(image);
                    } else if (choose==CHOOSE_PHOTO) {
                        mUri = intent.getData();
                        if (mUri != null) {
                            iv_head_image.setImageURI(mUri);
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


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.rl_account) {
            intent = new Intent(this, ModifyUsernameActivity.class);
            bundle = new Bundle();
            bundle.putInt(MODE, 1);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (v.getId() == R.id.rl_signature) {
            intent = new Intent(this, ModifyUsernameActivity.class);
            bundle = new Bundle();
            bundle.putInt(MODE, 2);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (v.getId() == R.id.rl_sex) {
            GenderBottomSheetFragment bottomSheetFragment = new GenderBottomSheetFragment();
            bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
        } else if (v.getId() == R.id.iv_head_image || v.getId() == R.id.small_icon) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                // 权限还没有授予，进行申请
//                ActivityCompat.requestPermissions((Activity) this,
//                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 200); // 申请的 requestCode 为 200
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 100);
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                // 如果没有相机权限，请求权限
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 100);
            } else {
                intent = new Intent(Intent.ACTION_GET_CONTENT);
                choose = CHOOSE_PHOTO;
                intent.setType("image/*");
//                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                choose=TAKE_PHOTO;
                mResultLauncher.launch(intent);
            }
        }
    }

    @Override
    public void onBackPressed() {
        photoService.updateInfo(mloginData).enqueue(new Callback<BaseResponse<Object>>() {
            @Override
            public void onResponse(@NonNull Call<BaseResponse<Object>> call, @NonNull Response<BaseResponse<Object>> response) {
                if (response.body() != null && response.body().getCode() == 200) {
                    Toast.makeText(UserInfoActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResponse<Object>> call, @NonNull Throwable t) {
                Toast.makeText(UserInfoActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
            }
        });
        super.onBackPressed();
    }

    @Override
    public void onGenderSelected(String gender) {
        if (!tv_sex.getText().equals(gender)) {
            tv_sex.setText(gender);
        }
        if (gender.equals("男")) {
            mloginData.setSex(1);
        } else {
            mloginData.setSex(2);
        }
    }
}