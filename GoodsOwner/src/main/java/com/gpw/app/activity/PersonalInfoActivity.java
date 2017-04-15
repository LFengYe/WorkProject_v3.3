package com.gpw.app.activity;

import android.Manifest;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.File;
import java.util.Map;

import com.gpw.app.R;
import com.gpw.app.base.BaseActivity;
import com.gpw.app.base.Contants;
import com.gpw.app.bean.UserInfo;
import com.gpw.app.util.BitmapUtil;
import com.gpw.app.util.EncryptUtil;
import com.gpw.app.util.HttpUtil;
import com.gpw.app.util.LogUtil;
import com.gpw.app.util.VolleyInterface;
import com.gpw.app.view.CircleImageView;
import com.gpw.app.view.MyDialog;

public class PersonalInfoActivity extends BaseActivity {

    private RelativeLayout rl_address;
    private RelativeLayout rl_head;
    private RelativeLayout rl_name;
    private RelativeLayout rl_sex;
    private CircleImageView civ_head;
    private CircleImageView civ_head1;
    private TextView tv_tel;
    private TextView tv_tel1;
    private TextView tv_name;
    private TextView tv_name1;
    private TextView tv_address;
    private MyDialog nickDialog;
    private TextView tv_sex;
    private ImageView iv_left_white;

    private MyDialog selectSexDialog;
    private MyDialog selectPicDialog;
    private UserInfo userInfo;
    private boolean isChange;
    private static final int REQUESTCODE_PICK = 0;        // 相册选图标记
    private static final int REQUESTCODE_TAKE = 1;        // 相机拍照标记
    private static final int REQUESTCODE_CUTTING = 2;    // 图片裁切标记
    private static final String IMAGE_FILE_NAME = "head.jpg";// 头像文件名称
    private String city;
    private String province;
    private String address;


    @Override
    protected int getLayout() {
        return R.layout.activity_personal_info;
    }

    @Override
    protected void findById() {
        rl_address = (RelativeLayout) findViewById(R.id.rl_address);
        rl_head = (RelativeLayout) findViewById(R.id.rl_head);
        rl_name = (RelativeLayout) findViewById(R.id.rl_name);
        rl_sex = (RelativeLayout) findViewById(R.id.rl_sex);
        civ_head = (CircleImageView) findViewById(R.id.civ_head);
        civ_head1 = (CircleImageView) findViewById(R.id.civ_head1);
        tv_tel = (TextView) findViewById(R.id.tv_tel);
        tv_tel1 = (TextView) findViewById(R.id.tv_tel1);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_name1 = (TextView) findViewById(R.id.tv_name1);
        tv_sex = (TextView) findViewById(R.id.tv_sex);
        tv_address = (TextView) findViewById(R.id.tv_address);
        iv_left_white = (ImageView) findViewById(R.id.iv_left_white);

    }

    @Override
    protected void initData() {
        userInfo = getIntent().getParcelableExtra("userInfo");
    }

    @Override
    protected void initView() {

        tv_tel.setText(userInfo.getTel());
        tv_tel1.setText(userInfo.getTel());
        tv_name.setText(userInfo.getUserName());
        tv_name1.setText(userInfo.getUserName());
        tv_sex.setText(userInfo.getSex());
        tv_address.setText(userInfo.getAddress());

        HttpUtil.setImageLoader(Contants.imagehost + userInfo.getHeadIco(), civ_head, R.mipmap.account2, R.mipmap.account2);

        HttpUtil.setImageLoader(Contants.imagehost + userInfo.getHeadIco(), civ_head1, R.mipmap.account1, R.mipmap.account1);

        iv_left_white.setOnClickListener(this);
        rl_address.setOnClickListener(this);
        rl_head.setOnClickListener(this);
        rl_name.setOnClickListener(this);
        rl_sex.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left_white:
                if (isChange) {
                    getIntent().putExtra("userInfo", userInfo);
                    setResult(RESULT_OK, getIntent());
                }
                finish();
                break;
            case R.id.rl_name:
                nickDialog = MyDialog.nickDialog(PersonalInfoActivity.this);
                nickDialog.show();

                nickDialog.setOnSettingListener(new MyDialog.NickListener() {
                    @Override
                    public void onSetting(String name) {
                        if (name.isEmpty()) {
                            showShortToastByString("名字不能为空");
                            return;
                        }
                        EditUserInfo("name", name);
                    }
                });

                break;
            case R.id.rl_sex:
                selectSexDialog = MyDialog.selectSexDialog(PersonalInfoActivity.this, itemsOnClick);
                selectSexDialog.show();
                break;
            case R.id.rl_address:
                Intent intent = new Intent(PersonalInfoActivity.this, AddMapActivity.class);
                intent.putExtra("type", 3);
                startActivityForResult(intent, 44);
                break;
            case R.id.rl_head:
                selectPicDialog = MyDialog.selectPicDialog(PersonalInfoActivity.this, itemsOnClick);
                selectPicDialog.show();
                break;

        }
    }


    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.takePhotoBtn:
                    selectPicDialog.dismiss();
                    if (ContextCompat.checkSelfPermission(PersonalInfoActivity.this, Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(PersonalInfoActivity.this,
                                new String[]{Manifest.permission.CAMERA,
                                        Manifest.permission.READ_EXTERNAL_STORAGE,
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
                case R.id.pickPhotoBtn:
                    selectPicDialog.dismiss();
                    if (ContextCompat.checkSelfPermission(PersonalInfoActivity.this, Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(PersonalInfoActivity.this,
                                new String[]{Manifest.permission.CAMERA,
                                        Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, 200);
                    } else {
                        Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
                        pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                        startActivityForResult(pickIntent, REQUESTCODE_PICK);
                    }
                    break;
                case R.id.manBtn:
                    selectSexDialog.dismiss();
                    EditUserInfo("sex", "男");
                    break;
                case R.id.womanBtn:
                    selectSexDialog.dismiss();
                    EditUserInfo("sex", "女");
                    break;
                default:
                    break;
            }
        }
    };


    private void EditUserInfo(final String type, final String value) {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("UserId", Contants.userId);
        jsonObject.addProperty("UserType", 1);
        if (type.equals("sex")) {
            jsonObject.addProperty("Sex", value);
        }
        if (type.equals("name")) {
            jsonObject.addProperty("UserName", value);
        }
        if (type.equals("address")) {
            jsonObject.addProperty("Province", province);
            jsonObject.addProperty("City", city);
            jsonObject.addProperty("Address", address);
        }
        LogUtil.i(jsonObject.toString());
        Map<String, String> map = EncryptUtil.encryptDES(jsonObject.toString());

        HttpUtil.doPost(PersonalInfoActivity.this, Contants.url_editUserInfo, "editUserInfo", map, new VolleyInterface(PersonalInfoActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onSuccess(JsonElement result) {
                LogUtil.i(result.toString());
                showShortToastByString("提交成功");
                if (type.equals("sex")) {
                    tv_sex.setText(value);
                    userInfo.setSex(value);
                }
                if (type.equals("name")) {
                    tv_name.setText(value);
                    tv_name1.setText(value);
                    userInfo.setUserName(value);
                    nickDialog.dismiss();
                }
                if (type.equals("address")) {
                    tv_address.setText(address);
                    userInfo.setAddress(address);
                }
                isChange = true;
            }

            @Override
            public void onError(VolleyError error) {
                showShortToastByString(getString(R.string.timeoutError));
//                LogUtil.i("hint",error.networkResponse.headers.toString());
//                LogUtil.i("hint",error.networkResponse.statusCode+"");
            }

            @Override
            public void onStateError() {
            }
        });
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
                    Toast.makeText(PersonalInfoActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case 200: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
                    pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(pickIntent, REQUESTCODE_PICK);
                } else {
                    Toast.makeText(PersonalInfoActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case 300: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //getServletData();
                } else {
                    Toast.makeText(PersonalInfoActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

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
                if (data == null) {
                    return;
                }
                uri = data.getData();
                if (uri != null) {
                    startPhotoZoom(uri);
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
            case 44:
                if (resultCode == RESULT_OK) {
                    city = data.getStringExtra("City");
                    province = data.getStringExtra("Province");
                    address = data.getStringExtra("Address");
                    EditUserInfo("address", null);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void setPicToView(Intent data) {

        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap bitmap = extras.getParcelable("data");
            Drawable drawable = new BitmapDrawable(null, bitmap);
            civ_head.setImageDrawable(drawable);
            civ_head1.setImageDrawable(drawable);
            String HeadPortrait = BitmapUtil.getImgStr(bitmap);
            final JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("UserId", Contants.userId);
            jsonObject.addProperty("Suffix", "JPEG");
            jsonObject.addProperty("HeadPortrait", HeadPortrait);
            LogUtil.i(jsonObject.toString());
            Map<String, String> map = EncryptUtil.encryptDES(jsonObject.toString());

            HttpUtil.doPost(PersonalInfoActivity.this, Contants.url_updateHeadPortrait, "updateHeadPortrait", map, new VolleyInterface(PersonalInfoActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
                @Override
                public void onSuccess(JsonElement result) {
                    isChange = true;
                    LogUtil.i(result.toString());
                    String url = result.toString();
                    url = url.substring(1);
                    url = url.substring(0, url.length() - 1);
                    userInfo.setHeadIco(url);
                    showShortToastByString("提交成功");
                }

                @Override
                public void onError(VolleyError error) {
                    showShortToastByString(getString(R.string.timeoutError));
//                LogUtil.i("hint",error.networkResponse.headers.toString());
//                LogUtil.i("hint",error.networkResponse.statusCode+"");
                }

                @Override
                public void onStateError() {
                }
            });
        }

    }

    @Override
    public void onBackPressed() {
        if (isChange) {
            getIntent().putExtra("userInfo", userInfo);
            setResult(RESULT_OK, getIntent());
        }
        super.onBackPressed();
    }
}
