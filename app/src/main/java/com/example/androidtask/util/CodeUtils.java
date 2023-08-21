package com.example.androidtask.util;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;

/**
 * 用于图片验证码的工具类
 */

public class CodeUtils {

    private static final char[] CHARS = {'1', '2', '3', '4', '5', '6', '7', '8',
            '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
            'k', 'l', 'm', 'n', 'p', 'q', 'r', 's', 't', 'u', 'v',
            'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G',
            'H', 'I', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S',
            'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

    //  绘图准备
    private static CodeUtils mCodeUtils;
    private int mPaddingLeft, mPaddingTop;
    private StringBuilder mBuilder = new StringBuilder();
    private Random mRandom = new Random();

    private static final int DEFAULT_CODE_LENGTH = 5;//验证码的长度  这里6位，可以简单点，简单一点
    private static final int DEFAULT_FONT_SIZE = 30;//字体大小
    private static final int DEFAULT_LINE_NUMBER = 3;//多少条干扰线
    private static final int BASE_PADDING_LEFT = 15; //左边距
    private static final int RANGE_PADDING_LEFT = 15;//左边距范围值
    private static final int BASE_PADDING_TOP = 20;//上边距
    private static final int RANGE_PADDING_TOP = 10;//上边距范围值
    private static final int DEFAULT_WIDTH = 140;//默认宽度.图片的总宽
    private static final int DEFAULT_HEIGHT = 34;//默认高度.图片的总高
    private static final int DEFAULT_COLOR = 0xDF;//默认背景颜色值

    private String code;

    public static CodeUtils getInstance() {
        if (mCodeUtils == null) {
            mCodeUtils = new CodeUtils();
        }
        return mCodeUtils;
    }


    public Bitmap createBitmap() {
        mPaddingLeft = 0;
        mPaddingTop = 0;
        Bitmap bitmap = Bitmap.createBitmap(DEFAULT_WIDTH, DEFAULT_HEIGHT, Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        code = createCode();

        canvas.drawColor(Color.rgb(DEFAULT_COLOR, DEFAULT_COLOR, DEFAULT_COLOR));

        Paint paint = new Paint();

        paint.setTextSize(DEFAULT_FONT_SIZE);

        for (int i = 0; i < code.length(); i++) {
            randomTextStyle(paint);
            randomPadding();
            canvas.drawText(code.charAt(i) + "", mPaddingLeft, mPaddingTop, paint);

        }

        for (int i = 0; i < DEFAULT_LINE_NUMBER; i++) {
            drawLine(canvas, paint);
        }

        canvas.save();
        canvas.restore();
        return bitmap;
    }


    public String getCode() {
        return code.toLowerCase();
    }


    public String createCode() {
        mBuilder.delete(0, mBuilder.length());
        for (int i = 0; i < DEFAULT_CODE_LENGTH; i++) {
            mBuilder.append(CHARS[mRandom.nextInt(CHARS.length)]);
        }
        return mBuilder.toString();
    }

    private void drawLine(Canvas canvas, Paint paint) {

        int color = randomColor();

        int startX = mRandom.nextInt(DEFAULT_WIDTH);

        int startY = mRandom.nextInt(DEFAULT_HEIGHT);

        int stopX = mRandom.nextInt(DEFAULT_WIDTH);

        int stopY = mRandom.nextInt(DEFAULT_HEIGHT);


        paint.setStrokeWidth(1);
        paint.setColor(color);

        canvas.drawLine(startX, startY, stopX, stopY, paint);
    }

    private int randomColor() {
        mBuilder.delete(0, mBuilder.length());

        String haxString;
        for (int i = 0; i < 3; i++) {
            haxString = Integer.toHexString(mRandom.nextInt(0xFF));

            if (haxString.length() == 1) {
                haxString = "0" + haxString;
            }

            mBuilder.append(haxString);
        }
        return Color.parseColor("#" + mBuilder.toString());
    }


    private void randomTextStyle(Paint paint) {

        int color = randomColor();

        paint.setColor(color);

        paint.setFakeBoldText(mRandom.nextBoolean());

        float skewX = mRandom.nextInt(9) / 8;

        skewX = mRandom.nextBoolean() ? skewX : -skewX;

        paint.setTextSkewX(skewX);
    }

    private void randomPadding() {
        mPaddingLeft += BASE_PADDING_LEFT + mRandom.nextInt(RANGE_PADDING_LEFT);

        mPaddingTop = BASE_PADDING_TOP + mRandom.nextInt(RANGE_PADDING_TOP);
    }
}