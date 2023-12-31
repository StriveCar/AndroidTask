package com.example.androidtask;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.text.InputType;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.androidtask.network.RetrofitClient;
import com.example.androidtask.network.service.PhotoService;
import com.example.androidtask.response.BaseResponse;
import com.example.androidtask.response.LoginData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private final PhotoService photoService = RetrofitClient.getInstance().getService(PhotoService.class);
    private Boolean bPwdSwitch = false;
    private EditText etPwd, etAccount;
    private CheckBox cbRememberPwd;
    private ImageView ivPwdSwitch;
    private Button btLogin, btRegister;

    private Intent intent;

    private Bundle bundle;
    private ActivityResultLauncher<Intent> register;
    public static final String USER_NAME = "name";
    public static final String USER_PWD = "password";

    private String spFileName, accountKey, passwordKey, rememberPasswordKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        ivPwdSwitch = findViewById(R.id.iv_pwd_switch);
        etPwd = findViewById(R.id.et_pwd);
        etAccount = findViewById(R.id.et_account);
        cbRememberPwd = findViewById(R.id.cb_remember_pwd);
        btLogin = findViewById(R.id.bt_login);
        btRegister = findViewById(R.id.bt_resgiter);

        loadInfo();

        ivPwdSwitch.setOnClickListener(view -> {
            int context = String.valueOf(etPwd.getText()).length();
            bPwdSwitch = !bPwdSwitch;
            if (bPwdSwitch) {
                ivPwdSwitch.setImageResource(R.drawable.baseline_visibility_24);
                etPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            } else {
                ivPwdSwitch.setImageResource(R.drawable.baseline_visibility_off_24);
                etPwd.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                etPwd.setTypeface(Typeface.DEFAULT);
            }
            etPwd.setSelection(context);
        });


        btLogin.setOnClickListener(this);
        btRegister.setOnClickListener(this);

        register = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result != null) {
                if (result.getData() != null) {
                    bundle = result.getData().getExtras();
                    etAccount.setText(bundle.getString(LoginActivity.USER_NAME));
                    etPwd.setText(bundle.getString(LoginActivity.USER_PWD));
                }
            }
        });
    }

    public void loadInfo(){
        spFileName = getResources()
                .getString(R.string.shared_preferences_file_name);
        accountKey = getResources()
                .getString(R.string.login_account_name);
        passwordKey = getResources()
                .getString(R.string.login_password);
        rememberPasswordKey = getResources()
                .getString(R.string.login_remember_password);

        SharedPreferences spFile = getSharedPreferences(
                spFileName,
                MODE_PRIVATE);
        String account = spFile.getString(accountKey, null);
        String password = spFile.getString(passwordKey, null);
        boolean rememberPassword = spFile.getBoolean(
                rememberPasswordKey,
                false);

        if (account != null && !TextUtils.isEmpty(account)) {
            etAccount.setText(account);
        }

        if (password != null && !TextUtils.isEmpty(password)) {
            etPwd.setText(password);
        }

        if (rememberPassword) cbRememberPwd.setChecked(true);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.cyan));
        }
        LoadingDialog.getInstance().dismiss();

    }
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.bt_login) {

            SharedPreferences spFile = getSharedPreferences(
                    spFileName,
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = spFile.edit();

            String password = etPwd.getText().toString();
            String account = etAccount.getText().toString();

            if (account.trim().isEmpty()) {
                Toast.makeText(LoginActivity.this, "账号不能为空", Toast.LENGTH_SHORT).show();
            } else if (password.trim().isEmpty()) {
                Toast.makeText(LoginActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
            } else {
                if (cbRememberPwd.isChecked()) {
                    editor.putString(accountKey, account);
                    editor.putString(passwordKey, password);
                    editor.putBoolean(rememberPasswordKey, true);
                    editor.apply();
                } else {
                    editor.remove(accountKey);
                    editor.remove(passwordKey);
                    editor.remove(rememberPasswordKey);
                    editor.apply();
                }
                LoadingDialog loadingDialog = LoadingDialog.getInstance(LoginActivity.this);
                loadingDialog.show();
                photoService.userLogin(etPwd.getText().toString(), etAccount.getText().toString()).enqueue(new Callback<BaseResponse<LoginData>>() {
                    @Override
                    public void onResponse(@NonNull Call<BaseResponse<LoginData>> call, @NonNull Response<BaseResponse<LoginData>> response) {

                        if (response.body() != null) {
                            if (response.body().getCode() == 200) {
                                Toast.makeText(LoginActivity.this, "登录成功! ", Toast.LENGTH_SHORT).show();
                              
                                LoginData.setMloginData(response.body().getData());
                                LoginData mloginData = LoginData.getMloginData();
                                if (response.body().getData().getIntroduce() == null) {
                                    mloginData.setIntroduce("这是你的个性签名！");
                                }
                                mloginData.setCreateTime(null);
                                mloginData.setLastUpdateTime(null);
                                mloginData.setAppKey(null);
                                intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                loadingDialog.dismiss();
                            } else if (response.body().getCode() == 500) {
                                loadingDialog.dismiss();
                                Toast.makeText(LoginActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<BaseResponse<LoginData>> call, @NonNull Throwable t) {
                        Toast.makeText(LoginActivity.this, "登录失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        } else if (view.getId() == R.id.bt_resgiter) {
            intent = new Intent(LoginActivity.this, RegisterActivity.class);
            register.launch(intent);
        }
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