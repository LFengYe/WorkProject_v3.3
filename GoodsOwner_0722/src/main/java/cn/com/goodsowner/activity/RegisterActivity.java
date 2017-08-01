package cn.com.goodsowner.activity;


import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import cn.com.goodsowner.R;
import cn.com.goodsowner.base.BaseActivity;
import cn.com.goodsowner.base.Contants;
import cn.com.goodsowner.util.BitmapUtil;
import cn.com.goodsowner.util.EncryptUtil;
import cn.com.goodsowner.util.HttpUtil;
import cn.com.goodsowner.util.LogUtil;
import cn.com.goodsowner.util.VolleyInterface;
import cn.com.goodsowner.view.MyDialog;


public class RegisterActivity extends BaseActivity {

    private EditText et_idNumber;
    private EditText et_tel;
    private EditText et_tel2;
    private EditText et_company;
    private EditText et_name;
    private ImageView iv_up;
    private Button bt_finish;
    private TextView tv_title;
    private TextView tv_right;
    private ImageView iv_left_white;
    private RadioButton rb_man;
    private RadioButton rb_woman;
    private String HeadPortrait;
    private String uid;
    private int state;
    private MyDialog selectPicDialog;
    private static final int REQUESTCODE_PICK = 0;        // 相册选图标记
    private static final int REQUESTCODE_TAKE = 1;        // 相机拍照标记
    private static final String IMAGE_FILE_NAME = "zhizhao.jpg";// 头像文件名称

    @Override
    protected int getLayout() {
        return R.layout.activity_register;
    }

    @Override
    protected void findById() {

        RelativeLayout rl_head = (RelativeLayout) findViewById(R.id.in_head);
        assert rl_head != null;
        tv_title = (TextView) rl_head.findViewById(R.id.tv_title);
        tv_right = (TextView) rl_head.findViewById(R.id.tv_right);
        iv_left_white = (ImageView) rl_head.findViewById(cn.com.goodsowner.R.id.iv_left_white);
        et_idNumber = (EditText) findViewById(R.id.et_idNumber);
        et_tel = (EditText) findViewById(R.id.et_tel);
        et_tel2 = (EditText) findViewById(R.id.et_tel2);
        et_company = (EditText) findViewById(R.id.et_company);
        et_name = (EditText) findViewById(R.id.et_name);
        iv_up = (ImageView) findViewById(R.id.iv_up);
        bt_finish = (Button) findViewById(R.id.bt_finish);
        rb_man = (RadioButton) findViewById(R.id.rb_man);
        rb_woman = (RadioButton) findViewById(R.id.rb_woman);


    }

    @Override
    protected void initData() {
        uid = getIntent().getStringExtra("Uid");
        state = getIntent().getIntExtra("state", 0);
    }

    @Override
    protected void initView() {
        bt_finish.setOnClickListener(this);
        iv_left_white.setOnClickListener(this);
        iv_up.setOnClickListener(this);
        tv_right.setVisibility(View.GONE);
        tv_title.setText(R.string.goods_register);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left_white:
                finish();
                break;
            case R.id.bt_finish:
                finishRegister();
                break;
            case R.id.iv_up:

                selectPicDialog = MyDialog.selectPicDialog(RegisterActivity.this, itemsOnClick);
                selectPicDialog.show();
                break;

        }
    }

    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case cn.com.goodsowner.R.id.takePhotoBtn:
                    selectPicDialog.dismiss();
                    if (ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(RegisterActivity.this,
                                new String[]{Manifest.permission.CAMERA,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
                    } else {
                        Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        //下面这句指定调用相机拍照后的照片存储的路径
                        takeIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME)));
                        startActivityForResult(takeIntent, REQUESTCODE_TAKE);
                    }

                    break;
                // 相册选择图片
                case cn.com.goodsowner.R.id.pickPhotoBtn:
                    selectPicDialog.dismiss();
                    if (ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(RegisterActivity.this,
                                new String[]{Manifest.permission.CAMERA,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, 200);
                    } else {
                        Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
                        pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                        startActivityForResult(pickIntent, REQUESTCODE_PICK);
                    }
                    break;
                default:
                    break;
            }
        }
    };


    private void finishRegister() {
        final String name = et_name.getText().toString();
        boolean isMan = rb_man.isChecked();
        String sex = "男";
        if (!isMan) {
            sex = "女";
        }
        String idNumber = et_idNumber.getText().toString();
        String tel = et_tel.getText().toString();
        String tel2 = et_tel2.getText().toString();
        String company = et_company.getText().toString();

        if (name.equals(" ") || tel.equals(" ") || tel2.equals(" ") || company.equals(" ") || idNumber.equals(" ") || HeadPortrait.isEmpty()) {
            showShortToastByString(getString(R.string.hint_message));
            return;
        }
        if (idNumber.length()!=18){
            showShortToastByString("身份证格式不对");
            return;
        }


        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("UserID", uid);
        jsonObject.addProperty("Name", name);
        jsonObject.addProperty("Sex", sex);
        jsonObject.addProperty("IdNumber", idNumber);
        jsonObject.addProperty("Phone1", tel);
        jsonObject.addProperty("Phone2", tel2);
        jsonObject.addProperty("Company", company);
        jsonObject.addProperty("CompanyZP", HeadPortrait);
        LogUtil.i(jsonObject.toString());
        Map<String, String> map = EncryptUtil.encryptDES(jsonObject.toString());
        final String finalSex = sex;
        HttpUtil.doPost(RegisterActivity.this, Contants.url_consignor_register, "consignorRegister", map, new VolleyInterface(RegisterActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onSuccess(JsonElement result) {
                LogUtil.i(result.toString());
                showShortToastByString("注册成功");

                if (state == 1) {
                    getIntent().putExtra("Name",name);
                    getIntent().putExtra("Sex", finalSex);
                    setResult(RESULT_OK, getIntent());
                }
                finish();

            }

            @Override
            public void onError(VolleyError error) {
                showShortToastByString(getString(cn.com.goodsowner.R.string.timeoutError));
            }

            @Override
            public void onStateError() {
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri uri;
        switch (requestCode) {
            case REQUESTCODE_PICK:// 直接从相册获取
                if (data == null) {
                    return;
                }
                uri = data.getData();
                if (uri != null) {
                    setPicToView(uri);
                }

                break;
            case REQUESTCODE_TAKE:// 调用相机拍照
                if (resultCode == RESULT_CANCELED) {
                    return;
                }
                File temp = new File(Environment.getExternalStorageDirectory() + "/" + IMAGE_FILE_NAME);
                uri = Uri.fromFile(temp);

                System.out.println(uri);
                if (uri != null) {
                    setPicToView(uri);
                }

                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void setPicToView(Uri uri) {
        iv_up.setImageURI(uri);
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(RegisterActivity.this.getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HeadPortrait = BitmapUtil.getImgStr(bitmap);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 100: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    //下面这句指定调用相机拍照后的照片存储的路径
                    takeIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME)));
                    startActivityForResult(takeIntent, REQUESTCODE_TAKE);
                } else {
                    Toast.makeText(RegisterActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case 200: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
                    pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(pickIntent, REQUESTCODE_PICK);
                } else {
                    Toast.makeText(RegisterActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
