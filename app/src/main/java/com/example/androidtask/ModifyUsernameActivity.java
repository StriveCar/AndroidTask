package com.example.androidtask;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.androidtask.response.LoginData;

import java.util.Objects;

public class ModifyUsernameActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView tvNum;
    private EditText etUsername;
    private LoginData mloginData = LoginData.getMloginData();
    private int mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_username);

        toolbar = findViewById(R.id.tool_bar);
        etUsername = findViewById(R.id.et_username);
        tvNum = findViewById(R.id.tv_num);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        Bundle bundle = getIntent().getExtras();
        mode = bundle.getInt(UserInfoActivity.MODE);
        addComplete();
        initData();
    }

    public void addComplete() {
        TextView tvComplete = new TextView(this);
        tvComplete.setText("完成");
        tvComplete.setTextColor(ContextCompat.getColor(this, R.color.white));
        tvComplete.setTextSize(18);

        Toolbar.LayoutParams layoutParams = new Toolbar.LayoutParams(
                Toolbar.LayoutParams.WRAP_CONTENT,
                60
        );
        layoutParams.gravity = Gravity.CENTER_VERTICAL | Gravity.END;
        layoutParams.rightMargin = 20;
        tvComplete.setLayoutParams(layoutParams);

        toolbar.addView(tvComplete);

        tvComplete.setOnClickListener(view -> {
            if (etUsername.getText().toString().trim().equals("")) {
                Toast.makeText(this, "输入不能为空", Toast.LENGTH_SHORT).show();
            } else if (30 * mode - etUsername.getText().length() < 0) {
                Toast.makeText(this, "超出长度限制", Toast.LENGTH_SHORT).show();
            } else {
                if (mode == 1) {
                    mloginData.setUsername(String.valueOf(etUsername.getText()));
                } else {
                    mloginData.setIntroduce(String.valueOf(etUsername.getText()));
                }
                finish();
            }
        });

        etUsername.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.close, 0);
        etUsername.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                Drawable rightDrawable = etUsername.getCompoundDrawables()[2];
                if (rightDrawable != null) {
                    int touchX = (int) event.getX();
                    int drawableWidth = rightDrawable.getBounds().width();
                    int paddingRight = etUsername.getPaddingRight();

                    if (touchX >= (etUsername.getWidth() - paddingRight - drawableWidth)) {
                        etUsername.setText("");
                        return true;
                    }
                }
            }
            return false;
        });

        etUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    etUsername.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.close, 0);
                } else {
                    etUsername.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0); // 隐藏图标
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                tvNum.setText("" + (30 * mode - etUsername.getText().length()));
                if (30 * mode - etUsername.getText().length() < 0) {
                    tvNum.setTextColor(Color.parseColor("#FF0000"));
                }
            }
        });
    }

    public void initData() {
        etUsername.requestFocus();
        if (mode == 1) {
            toolbar.setTitle("修改我的用户名");
            etUsername.setText(mloginData.getUsername());
            etUsername.setSelection(mloginData.getUsername().length());
            tvNum.setText("" + (30 - mloginData.getUsername().length()));
        } else if (mode == 2) {
            toolbar.setTitle("修改我的个性签名");
            etUsername.setText(mloginData.getIntroduce());
            tvNum.setText("" + (60 - mloginData.getIntroduce().length()));
        }
    }
}