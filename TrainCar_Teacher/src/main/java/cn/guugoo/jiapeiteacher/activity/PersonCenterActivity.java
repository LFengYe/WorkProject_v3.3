package cn.guugoo.jiapeiteacher.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import cn.guugoo.jiapeiteacher.R;
import cn.guugoo.jiapeiteacher.base.BaseActivity;
import cn.guugoo.jiapeiteacher.base.Constants;
import cn.guugoo.jiapeiteacher.bean.PersonInfo;
import cn.guugoo.jiapeiteacher.bean.TotalData;
import cn.guugoo.jiapeiteacher.util.ACache;
import cn.guugoo.jiapeiteacher.base.BaseAsyncTask;
import cn.guugoo.jiapeiteacher.util.BitmapUtil;
import cn.guugoo.jiapeiteacher.util.EncryptUtils;
import cn.guugoo.jiapeiteacher.util.HttpUtil;
import cn.guugoo.jiapeiteacher.util.NetUtil;
import cn.guugoo.jiapeiteacher.view.CircleImageView;
import cn.guugoo.jiapeiteacher.view.MyDialog;
import cn.guugoo.jiapeiteacher.view.SelectPicPopupWindow;

import java.io.File;


/**
 * Created by gpw on 2016/8/6.
 * --加油
 */
public class PersonCenterActivity extends BaseActivity {


    private TextView tv_nickName;
    private TextView tv_name;
    private TextView tv_cardId;
    private TextView tv_tel;

    private SelectPicPopupWindow menuWindow;

    private CircleImageView civ_head;
    private ACache mCache;

    private static final int REQUESTCODE_PICK = 0;        // 相册选图标记
    private static final int REQUESTCODE_TAKE = 1;        // 相机拍照标记
    private static final int REQUESTCODE_CUTTING = 2;    // 图片裁切标记

    private static final String IMAGE_FILE_NAME = "head.jpg";// 头像文件名称

    private MyDialog psdDialog;
    private MyDialog nickDialog;

    private int teacherId;

    private String HeadPortrait;
    private String NewnNickme;
    private String token;
    private SharedPreferences prefs;


    @Override
    protected int getLayout() {
        return R.layout.activity_person_center;
    }

    @Override
    protected void initData() {
        prefs = getSharedPreferences(Constants.SHARED_NAME, MODE_PRIVATE);
        teacherId = prefs.getInt("Id", 0);
        token = prefs.getString("token", "");
        mCache = ACache.get(this, "cacheData");
        HeadPortrait = "";
    }

    @Override
    protected void initView() {
        RelativeLayout rl_head = (RelativeLayout) findViewById(R.id.rl_head);
        RelativeLayout rl_nickName = (RelativeLayout) findViewById(R.id.rl_nickName);
        RelativeLayout rl_psd = (RelativeLayout) findViewById(R.id.rl_psd);
        RelativeLayout head = (RelativeLayout) findViewById(R.id.head);
        TextView tv_center = (TextView) head.findViewById(R.id.tv_center);
        ImageView iv_back = (ImageView) head.findViewById(R.id.iv_back);

        tv_nickName = (TextView) findViewById(R.id.tv_nickName);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_cardId = (TextView) findViewById(R.id.tv_cardId);
        tv_tel = (TextView) findViewById(R.id.tv_tel);
        civ_head = (CircleImageView) findViewById(R.id.civ_head);

        tv_center.setText("个人中心");


        if (ContextCompat.checkSelfPermission(PersonCenterActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(PersonCenterActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    300);
        } else {
            getServletData();
        }


        rl_psd.setOnClickListener(this);
        rl_nickName.setOnClickListener(this);
        rl_head.setOnClickListener(this);
        iv_back.setOnClickListener(this);

    }

    private void getServletData() {

        String teacherInfo = mCache.getAsString("teacherInfo");

        if (teacherInfo != null) {
            System.out.println("本地+" + teacherInfo);
            Gson gson = new Gson();
            PersonInfo personInfo = gson.fromJson(teacherInfo, PersonInfo.class);
            tv_nickName.setText(personInfo.getNickname());
            tv_name.setText(personInfo.getName());
            tv_cardId.setText(personInfo.getCardNo());
            tv_tel.setText(personInfo.getTel());
            Glide.with(PersonCenterActivity.this)
                    .load(personInfo.getHeadPortrait())
                    .crossFade()
                    .skipMemoryCache(false)
                    .into(civ_head);

        } else {
            if (!NetUtil.checkNetworkConnection(this)) {
                Toast.makeText(PersonCenterActivity.this, R.string.Net_error, Toast.LENGTH_SHORT).show();
            }
        }


        if (NetUtil.checkNetworkConnection(this)) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("TeacherId", teacherId);
            jsonObject = EncryptUtils.encryptDES(jsonObject.toString());
            new GetTeacherInfoAsyncTask(PersonCenterActivity.this,HttpUtil.url_personalInfo,token).execute(jsonObject);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                getIntent().putExtra("HeadPortrait", HeadPortrait);
                getIntent().putExtra("NewnNickme", NewnNickme);
                getIntent().putExtra("backState", 1);
                setResult(RESULT_OK, getIntent());
                finish();
                break;
            case R.id.rl_nickName:
                if (!NetUtil.checkNetworkConnection(this)) {
                    showLongToastByString(R.string.Net_error);
                    return;
                }
                nickDialog = MyDialog.nickDialog(PersonCenterActivity.this);
                nickDialog.show();
                nickDialog.setOnSettingListener(new MyDialog.NickListener() {
                    @Override
                    public void onSetting(String name) {
                        if (name.isEmpty()) {
                            showLongToastByString(R.string.nick_null);
                            return;
                        }

                        if (!NetUtil.checkNetworkConnection(PersonCenterActivity.this)) {
                            showLongToastByString(R.string.Net_error);
                        }
                        NewnNickme = name;
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("TeacherId", teacherId);
                        jsonObject.addProperty("NewnNickme", name);
                        jsonObject = EncryptUtils.encryptDES(jsonObject.toString());
                        new UpdateNickAsyncTask(PersonCenterActivity.this,HttpUtil.url_nickname,token).execute(jsonObject);
                    }
                });
                break;
            case R.id.rl_psd:
                if (!NetUtil.checkNetworkConnection(this)) {
                    showLongToastByString(R.string.Net_error);
                    return;
                }
                psdDialog = MyDialog.psdDialog(PersonCenterActivity.this);
                psdDialog.show();
                psdDialog.setOnSettingListener(new MyDialog.PsdListener() {
                    @Override
                    public void onSetting(String old, String new1, String new2) {
                        if (old.isEmpty() || new1.isEmpty() || new2.isEmpty()) {
                            showLongToastByString(R.string.psd_null);
                            return;
                        }
                        if (new1.length()<6||new1.length()>16){
                            showLongToastByString(R.string.psd_error);
                            return;
                        }
                        if (!new1.equals(new2)) {
                            showLongToastByString(R.string.psd_no);
                            return;
                        }
                        if (!NetUtil.checkNetworkConnection(PersonCenterActivity.this)) {
                            showLongToastByString(R.string.Net_error);
                        }

                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("TeacherId", teacherId);
                        jsonObject.addProperty("PassWord", old);
                        jsonObject.addProperty("NewPass", new1);
                        jsonObject = EncryptUtils.encryptDES(jsonObject.toString());
                        new UpdatePsdAsyncTask(PersonCenterActivity.this,HttpUtil.url_updatePass,token).execute(jsonObject);
                    }
                });

                break;

            case R.id.rl_head:
                if (!NetUtil.checkNetworkConnection(this)) {
                    showLongToastByString(R.string.Net_error);
                    return;
                }
                menuWindow = new SelectPicPopupWindow(PersonCenterActivity.this, itemsOnClick);
                menuWindow.showAtLocation(findViewById(R.id.mainLayout), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;

        }


    }

    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()) {
                // 拍照
                case R.id.takePhotoBtn:
                    if (ContextCompat.checkSelfPermission(PersonCenterActivity.this, Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(PersonCenterActivity.this,
                                new String[]{Manifest.permission.CAMERA,
                                        Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                100);
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
                    if (ContextCompat.checkSelfPermission(PersonCenterActivity.this, Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(PersonCenterActivity.this,
                                new String[]{Manifest.permission.CAMERA,
                                        Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                200);
                    } else {
                        Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
                        // 如果朋友们要限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
                        pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                        startActivityForResult(pickIntent, REQUESTCODE_PICK);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 100: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    //下面这句指定调用相机拍照后的照片存储的路径
                    takeIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME)));
                    startActivityForResult(takeIntent, REQUESTCODE_TAKE);
                } else {
                    Toast.makeText(PersonCenterActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case 200: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
                    // 如果朋友们要限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
                    pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(pickIntent, REQUESTCODE_PICK);
                } else {
                    Toast.makeText(PersonCenterActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case 300: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getServletData();
                } else {
                    Toast.makeText(PersonCenterActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
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
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void setPicToView(Intent data) {


        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap bitmap = extras.getParcelable("data");
            Drawable drawable = new BitmapDrawable(null, bitmap);
            civ_head.setImageDrawable(drawable);
            HeadPortrait = BitmapUtil.getImgStr(bitmap);
            String Suffix = ".JPEG";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("UserId", teacherId);
            jsonObject.addProperty("HeadPortrait", HeadPortrait);
            jsonObject.addProperty("Suffix", Suffix);
            System.out.println(jsonObject);
            jsonObject = EncryptUtils.encryptDES(jsonObject.toString());
            new UpdateHeadAsyncTask(PersonCenterActivity.this,HttpUtil.url_headPortrait,token).execute(jsonObject);
        }


    }


    @Override
    public void onBackPressed() {
        getIntent().putExtra("HeadPortrait", HeadPortrait);
        getIntent().putExtra("NewnNickme", NewnNickme);
        getIntent().putExtra("backState", 1);
        setResult(RESULT_OK, getIntent());
        super.onBackPressed();
    }


    class GetTeacherInfoAsyncTask extends BaseAsyncTask {


        public GetTeacherInfoAsyncTask(Context mContext, String url, String token) {
            super(mContext, url, token);
        }


        @Override
        protected void dealResults(String s) {
            if (s.isEmpty()) {
                Toast.makeText(PersonCenterActivity.this, R.string.servlet_error, Toast.LENGTH_SHORT).show();
                return;
            }
            Gson gson = new Gson();
            TotalData totalData = gson.fromJson(s, TotalData.class);

            if (totalData.getStatus() == 0) {
                PersonInfo personInfo = gson.fromJson(totalData.getData(), PersonInfo.class);
                tv_nickName.setText(personInfo.getNickname());
                tv_name.setText(personInfo.getName());
                tv_cardId.setText(personInfo.getCardNo());
                tv_tel.setText(personInfo.getTel());
                NewnNickme = personInfo.getNickname();
                Glide.with(PersonCenterActivity.this)
                        .load(personInfo.getHeadPortrait())
                        .crossFade()
                        .skipMemoryCache(false)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(civ_head);
                mCache.put("teacherInfo", totalData.getData().toString());
            } else {
                Toast.makeText(PersonCenterActivity.this, totalData.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }


    class UpdatePsdAsyncTask extends BaseAsyncTask {


        public UpdatePsdAsyncTask(Context mContext, String url, String token) {
            super(mContext, url, token);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }


        @Override
        protected void dealResults(String s) {
            if (s.isEmpty()) {
                Toast.makeText(PersonCenterActivity.this, R.string.servlet_error, Toast.LENGTH_SHORT).show();
                return;
            }

            Gson gson = new Gson();
            TotalData totalData = gson.fromJson(s, TotalData.class);
            if (totalData.getStatus() == 0) {
                Toast.makeText(PersonCenterActivity.this, totalData.getMessage(), Toast.LENGTH_SHORT).show();
                psdDialog.dismiss();

                AlertDialog.Builder builder = new AlertDialog.Builder(PersonCenterActivity.this);
                builder.setTitle("提示");
                builder.setMessage("密码已修改，需要重新登录");
                builder.setCancelable(false);
                builder.setIcon(R.mipmap.hint);
                builder.setPositiveButton("重新登录", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putBoolean("autoState", false);
                        editor.apply();  //存入
                        getIntent().putExtra("backState", 2);
                        setResult(RESULT_OK, getIntent());
                        finish();

                    }
                });
                builder.show();

            } else {
                Toast.makeText(PersonCenterActivity.this, totalData.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }


    class UpdateNickAsyncTask extends BaseAsyncTask {


        public UpdateNickAsyncTask(Context mContext, String url, String token) {
            super(mContext, url, token);
        }



        @Override
        protected void dealResults(String s) {
            System.out.println(s);
            if (s.isEmpty()) {
                Toast.makeText(PersonCenterActivity.this, R.string.servlet_error, Toast.LENGTH_SHORT).show();
                return;
            }
            Gson gson = new Gson();
            TotalData totalData = gson.fromJson(s, TotalData.class);
            if (totalData.getStatus() == 0) {
                Toast.makeText(PersonCenterActivity.this, totalData.getMessage(), Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("NicKname", NewnNickme);
                editor.apply();
                tv_nickName.setText(NewnNickme);
                nickDialog.dismiss();
            } else {
                Toast.makeText(PersonCenterActivity.this, totalData.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }


    class UpdateHeadAsyncTask extends BaseAsyncTask {


        public UpdateHeadAsyncTask(Context mContext, String url, String token) {
            super(mContext, url, token);
        }

        @Override
        protected void dealResults(String s) {
            System.out.println(s);
            if (s.isEmpty()) {
                Toast.makeText(PersonCenterActivity.this, R.string.servlet_error, Toast.LENGTH_SHORT).show();
                return;
            }
            Gson gson = new Gson();
            TotalData totalData = gson.fromJson(s, TotalData.class);
            if (totalData.getStatus() == 0) {
                String icon =totalData.getData().toString();
                icon = icon.replace("\"","").replace("\"","");
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("Icon",icon);
                editor.apply();
                Toast.makeText(PersonCenterActivity.this, totalData.getMessage(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(PersonCenterActivity.this, totalData.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

}
