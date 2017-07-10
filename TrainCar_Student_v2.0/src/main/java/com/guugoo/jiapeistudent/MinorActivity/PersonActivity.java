package com.guugoo.jiapeistudent.MinorActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.guugoo.jiapeistudent.App.Constant;
import com.guugoo.jiapeistudent.Data.ReturnData;
import com.guugoo.jiapeistudent.Data.UserInformation;
import com.guugoo.jiapeistudent.MainActivity.BaseActivity;
import com.guugoo.jiapeistudent.R;
import com.guugoo.jiapeistudent.Tools.BitmapUtil;
import com.guugoo.jiapeistudent.Tools.DES;
import com.guugoo.jiapeistudent.Tools.MyHandler;
import com.guugoo.jiapeistudent.Tools.MyThread;
import com.guugoo.jiapeistudent.Tools.MyToast;
import com.guugoo.jiapeistudent.Tools.Utils;
import com.guugoo.jiapeistudent.Views.SelectPicPopupWindow;
import com.guugoo.jiapeistudent.Views.TitleView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.File;

public class PersonActivity extends BaseActivity {
    private static final String TAG = "PersonActivity";
    private SharedPreferences sp;
    private TextView[] textviews;
    private RelativeLayout[] relativeLayouts;
    private ImageView myhead;
    private ImageLoader imageLoader = ImageLoader.getInstance();

    private SelectPicPopupWindow menuWindow;
    private static final int REQUESTCODE_PICK = 0;        // 相册选图标记
    private static final int REQUESTCODE_TAKE = 1;        // 相机拍照标记
    private static final int REQUESTCODE_CUTTING = 2;    // 图片裁切标记
    private static final int REQUESTCODE_NICKNAME =3;    //昵称返回
    private static final String IMAGE_FILE_NAME = "head.jpg";// 头像文件名称

    private static final int  MY_PERMISSIONS_REQUEST_READ_CONTACTS=100; //获取权限

    private Bitmap bitmap;

    protected Handler handler2 = new MyHandler(PersonActivity.this){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
                Log.d(TAG, "handleMessage: "+msg.obj);
                ReturnData data= JSONObject.parseObject((String) msg.obj,ReturnData.class);
                if(data.getStatus()==0){
                    MyToast.makeText(PersonActivity.this,data.getMessage());
                    Drawable drawable = new BitmapDrawable(null, bitmap);
                    myhead.setImageDrawable(drawable);

                }else {
                    MyToast.makeText(PersonActivity.this,data.getMessage());
                }

            }
        }
    };


    @Override
    protected void processingData(ReturnData data) {
        UserInformation user= JSONObject.parseObject(data.getData(),UserInformation.class);
        imageLoader.getInstance().displayImage(user.getHeadPortrait(),myhead);
        textviews[0].setText(user.getNickname());
        textviews[1].setText(user.getName());
        textviews[2].setText(user.getSex());
        textviews[3].setText(user.getCardNo());
        textviews[4].setText(user.getTel());
        textviews[5].setText(user.getAddress());
    }

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_person);
    }

    @Override
    protected void initTitle() {
        TitleView titleView = (TitleView) findViewById(R.id.person_title);
        titleView.setMiddleTextVisible(true);
        titleView.setBackImageVisible(true);
        titleView.setMiddleText(R.string.person);
        titleView.setBackImageListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void findView() {
        textviews = new TextView[6];
        sp = getSharedPreferences("user", Context.MODE_PRIVATE);
        textviews[0] = (TextView) findViewById(R.id.person_text1);
        textviews[1] = (TextView) findViewById(R.id.person_text2);
        textviews[2] = (TextView) findViewById(R.id.person_text3);
        textviews[3] = (TextView) findViewById(R.id.person_text4);
        textviews[4] = (TextView) findViewById(R.id.person_text5);
        textviews[5] = (TextView) findViewById(R.id.person_text6);
        relativeLayouts = new RelativeLayout[3];
        relativeLayouts[0] = (RelativeLayout) findViewById(R.id.person_line1);
        relativeLayouts[1] = (RelativeLayout) findViewById(R.id.person_line2);
        relativeLayouts[2] = (RelativeLayout) findViewById(R.id.person_line8);
        myhead = (ImageView) findViewById(R.id.person_hand);
        imageLoader.init(ImageLoaderConfiguration.createDefault(PersonActivity.this));
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    menuWindow = new SelectPicPopupWindow(PersonActivity.this, itemsOnClick);
                    menuWindow.showAtLocation(findViewById(R.id.mainLayout), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

                } else {
                        MyToast.makeText(PersonActivity.this,"你没有权限！请先获取权限");
                }
                return;
            }
        }
    }

    @Override
    protected void init() {
        getData();
        relativeLayouts[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Utils.isNetworkAvailable(PersonActivity.this)) {
                    if (ContextCompat.checkSelfPermission(PersonActivity.this,
                            Manifest.permission.READ_CONTACTS)
                            != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(PersonActivity.this,
                                new String[]{Manifest.permission.CAMERA,
                                        Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                    }else{
                        menuWindow = new SelectPicPopupWindow(PersonActivity.this, itemsOnClick);
                        menuWindow.showAtLocation(findViewById(R.id.mainLayout), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                    }
                }else {
                    MyToast.makeText(PersonActivity.this,R.string.Toast_internet);
                }
            }
        });
        relativeLayouts[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonActivity.this,ChangeNameActivity.class);
                intent.putExtra("name",textviews[1].getText().toString());
                startActivityForResult(intent,3);
            }
        });
        relativeLayouts[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonActivity.this,ChangePasswordActivity.class);
                startActivityForResult(intent,5);
            }
        });
    }

    private void getData(){
        if(Utils.isNetworkAvailable(PersonActivity.this)){
            JSONObject json= new JSONObject(true);
            json.put("StudentId",sp.getInt("Id",0));
            new MyThread(Constant.URL_PersonalInformationit, handler, DES.encryptDES(json.toString())).start();
        }else {
            MyToast.makeText(PersonActivity.this,R.string.Toast_internet);
        }
    }


    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()) {
                // 拍照
                case R.id.takePhotoBtn:
                    Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    //下面这句指定调用相机拍照后的照片存储的路径
                    takeIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME)));
                    startActivityForResult(takeIntent, REQUESTCODE_TAKE);
                    break;
                // 相册选择图片
                case R.id.pickPhotoBtn:
                    Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
                    // 如果朋友们要限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
                    pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(pickIntent, REQUESTCODE_PICK);

                    break;
                default:
                    break;
            }
        }
    };


    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        String filename = BitmapUtil.getPath(this, uri);
        assert filename != null;
        uri = Uri.fromFile(new File(filename));
        intent.setDataAndType(uri, "image/*");
        // crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", true);
        intent.putExtra("scale", true);// 保留比例
        intent.putExtra("scaleUpIfNeeded", true);// 去黑边
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("return-data", true);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        System.out.println("start intent");
        startActivityForResult(intent, REQUESTCODE_CUTTING);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Uri uri;
        switch (requestCode) {
            case REQUESTCODE_PICK:// 直接从相册获取
                if(data!=null){
                    uri = data.getData();
                    if (uri != null) {
                        startPhotoZoom(uri);
                    }
                }
                break;
            case REQUESTCODE_TAKE:// 调用相机拍照
                if(resultCode!=RESULT_CANCELED){
                    File temp = new File(Environment.getExternalStorageDirectory() + "/" + IMAGE_FILE_NAME);
                    uri = Uri.fromFile(temp);
                    startPhotoZoom(uri);
                }
                break;
            case REQUESTCODE_CUTTING:// 取得裁剪后的图片
                if (data == null) {
                    return;
                } else {
                    setPicToView(data);
                }
                break;
            case REQUESTCODE_NICKNAME:
                if(resultCode==RESULT_OK){
                    Log.d(TAG, "onActivityResult: "+data.getStringExtra("change"));
                    textviews[0].setText(data.getStringExtra("change"));
                    break;
                }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setPicToView(Intent data) {

        Bundle extras = data.getExtras();
        if (extras != null) {
            bitmap = extras.getParcelable("data");
            String HeadPortrait = BitmapUtil.getImgStr(bitmap);
            String Suffix = ".JPEG";
            JSONObject json = new JSONObject();
            json.put("UserId", sp.getInt("Id",0));
            json.put("HeadPortrait", HeadPortrait);
            json.put("Suffix", Suffix);
            Log.d(TAG, "setPicToView: "+json.toString());
            new MyThread(Constant.URL_HeadPortrait, handler2, DES.encryptDES(json.toString())).start();
        }
    }

}
