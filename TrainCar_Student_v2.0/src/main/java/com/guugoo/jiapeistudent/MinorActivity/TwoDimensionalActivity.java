package com.guugoo.jiapeistudent.MinorActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.Hashtable;

import com.guugoo.jiapeistudent.MainActivity.BaseActivity;
import com.guugoo.jiapeistudent.R;
import com.guugoo.jiapeistudent.Views.TitleView;

public class TwoDimensionalActivity extends BaseActivity {
    private static final String TAG ="TwoDimensionalActivity";
    private ImageView imageView;
    private int QR_WIDTH = 200, QR_HEIGHT = 200;
    private SharedPreferences sp;
    private int StudentId;
    private int TeacherId;
    private String BookingId;
    private String BookingDay;
    private String TimeSlot;
    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_two_dimensional);
    }

    @Override
    protected void initTitle() {
        TitleView titleView = (TitleView) findViewById(R.id.Erweima_title);
        titleView.setMiddleTextVisible(true);
        titleView.setBackImageVisible(true);
        titleView.setMiddleText("二维码");
        titleView.setBackImageListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void findView() {
        imageView = (ImageView) findViewById(R.id.Erweima);
        sp = getSharedPreferences("user", Context.MODE_PRIVATE);
        StudentId = sp.getInt("Id",0);
        TeacherId = getIntent().getIntExtra("TeacherId",0);
        BookingId = getIntent().getStringExtra("BookingId");
        BookingDay = getIntent().getStringExtra("BookingDay");
        TimeSlot= getIntent().getStringExtra("TimeSlot");
        Log.d(TAG, "findView:1 "+StudentId);
        Log.d(TAG, "findView:2 "+TeacherId);
        Log.d(TAG, "findView:3"+ BookingId);
        Log.d(TAG, "findView:4 "+BookingDay);
        Log.d(TAG, "findView:5 "+TimeSlot);
    }

    @Override
    protected void init() {
        createQRImage(BookingId+"&"+BookingDay+"&"+TimeSlot+"&"+StudentId+"&"+TeacherId);
    }

    public void createQRImage(String url)
    {
        try
        {
            //判断URL合法性
            if (url == null || "".equals(url) || url.length() < 1)
            {
                return;
            }
            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            //图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = new QRCodeWriter().encode(url, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
            int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
            //下面这里按照二维码的算法，逐个生成二维码的图片，
            //两个for循环是图片横列扫描的结果
            for (int y = 0; y < QR_HEIGHT; y++)
            {
                for (int x = 0; x < QR_WIDTH; x++)
                {
                    if (bitMatrix.get(x, y))
                    {
                        pixels[y * QR_WIDTH + x] = 0xff000000;
                    }
                    else
                    {
                        pixels[y * QR_WIDTH + x] = 0xffffffff;
                    }
                }
            }
            //生成二维码图片的格式，使用ARGB_8888
            Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
            //显示到一个ImageView上面
            imageView.setImageBitmap(bitmap);
        }
        catch (WriterException e)
        {
            e.printStackTrace();
        }
    }
}
