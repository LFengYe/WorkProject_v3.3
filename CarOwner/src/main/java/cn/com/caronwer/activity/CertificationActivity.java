package cn.com.caronwer.activity;


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
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.File;
import java.util.Map;

import cn.com.caronwer.R;
import cn.com.caronwer.base.BaseActivity;
import cn.com.caronwer.base.Contants;
import cn.com.caronwer.util.BitmapUtil;
import cn.com.caronwer.util.EncryptUtil;
import cn.com.caronwer.util.HttpUtil;
import cn.com.caronwer.util.SPtils;
import cn.com.caronwer.util.VolleyInterface;
import cn.com.caronwer.view.BorderTextView;
import cn.com.caronwer.view.InputNameView;
import cn.com.caronwer.view.SelectPicPopupWindow;

public class CertificationActivity extends BaseActivity {


    private SelectPicPopupWindow menuWindow;
    private static final int REQUESTCODE_PICK = 0;        // 相册选图标记
    private static final int REQUESTCODE_TAKE = 1;        // 相机拍照标记
    private static final int REQUESTCODE_CUTTING = 2;    // 图片裁切标记
    private static final String[] IMAGE_FILE_NAME = {"sfz.jpg", "jsz.jpg", "xsz.jpg", "cl.jpg"};// 头像文件名称
    private static int imgType = 0;
    private static boolean isimg0 = false;
    private static boolean isimg1 = false;
    private static boolean isimg2 = false;
    private static boolean isimg3 = false;
    private boolean isChange;


    private TextView tv_title;
    private TextView tv_right;
    private ImageView iv_left_white;


    private BorderTextView mBv_queren;
    private InputNameView mPv_sfz;
    private InputNameView mPv_xsz;
    private InputNameView mPv_jsz;
    private InputNameView mPv_cl;

    private ImageView mIv_sf;
    private ImageView mIv_js;
    private ImageView mIv_xs;
    private ImageView mIv_che;

    private BorderTextView mBv_next;
    private EditText mEt_cardNo;
    private EditText mEt_drivingLicense;
    private EditText mEt_roadPermit;
    private EditText mEt_carNumber;
    //private TextView mEt_gps;
    private EditText mEt_name;
    private LinearLayout mLl_cer1;
    private LinearLayout mLl_cer2;
    private JsonObject mJsonObject;
    private Spinner mDdlCity;
    private String[] items = new String[]{"零担", "小面包车", "中面包车", "小型货车", "中型货车"};
    private Animation myAnimation = null;
    private int VehType = 0;

    @Override
    protected int getLayout() {
        return R.layout.activity_certification;
    }

    @Override
    protected void findById() {
        RelativeLayout rl_head = (RelativeLayout) findViewById(R.id.in_head);
        assert rl_head != null;
        tv_title = (TextView) rl_head.findViewById(R.id.tv_title);
        tv_right = (TextView) rl_head.findViewById(R.id.tv_right);
        iv_left_white = (ImageView) rl_head.findViewById(R.id.iv_left_white);

        mEt_name = (EditText) findViewById(R.id.et_name);
        mEt_cardNo = (EditText) findViewById(R.id.et_cardNo);
        mEt_drivingLicense = (EditText) findViewById(R.id.et_drivingLicense);
        mEt_roadPermit = (EditText) findViewById(R.id.et_road_transport_permit);
        mEt_carNumber = (EditText) findViewById(R.id.et_carNumber);
        //mEt_gps = (TextView) findViewById(R.id.tV_gps);
        mBv_next = (BorderTextView) findViewById(R.id.bv_next);

        mBv_queren = (BorderTextView) findViewById(R.id.bv_queren);
        mBv_queren.setOnClickListener(this);

        mPv_xsz = (InputNameView) findViewById(R.id.pv_xsz);
        mPv_jsz = (InputNameView) findViewById(R.id.pv_jsz);
        mPv_cl = (InputNameView) findViewById(R.id.pv_cl);
        mPv_sfz = (InputNameView) findViewById(R.id.pv_sfz);


        mIv_sf = (ImageView) findViewById(R.id.iv_sf);
        mIv_js = (ImageView) findViewById(R.id.iv_js);
        mIv_xs = (ImageView) findViewById(R.id.iv_xs);
        mIv_che = (ImageView) findViewById(R.id.iv_che);

        mLl_cer1 = (LinearLayout) findViewById(R.id.ll_cer1);
        mLl_cer2 = (LinearLayout) findViewById(R.id.ll_cer2);

        mDdlCity = (Spinner) findViewById(R.id.vehicleType);
        ArrayAdapter<String> source = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        mDdlCity.setAdapter(source);

        mDdlCity.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                VehType = arg2;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });
    }


    @Override
    protected void initData() {
        getVehicleTypes();
    }

    @Override
    protected void initView() {

        tv_title.setText("车主认证");
        tv_right.setVisibility(View.GONE);
        iv_left_white.setOnClickListener(this);
        mPv_sfz.setOnClickListener(this);
        mPv_xsz.setOnClickListener(this);
        mPv_jsz.setOnClickListener(this);
        mPv_cl.setOnClickListener(this);
        mBv_next.setOnClickListener(this);
        mJsonObject = new JsonObject();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left_white:
                if (mLl_cer1.getVisibility() == View.GONE) {
                    mLl_cer1.setVisibility(View.VISIBLE);
                    mLl_cer2.setVisibility(View.GONE);
                } else {
                    finish();
                }
                break;

            case R.id.bv_queren:  //注册完成

                posData();
                break;

            case R.id.pv_sfz:
//                Toast.makeText(this, "身份证成功", Toast.LENGTH_SHORT).show();
                imgType = 0;
                menuWindow = new SelectPicPopupWindow(CertificationActivity.this, itemsOnClick);
                menuWindow.showAtLocation(findViewById(R.id.activity_rebuild_psd), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;

            case R.id.pv_jsz:
//                Toast.makeText(this, "驾驶证成功", Toast.LENGTH_SHORT).show();
                imgType = 1;
                menuWindow = new SelectPicPopupWindow(CertificationActivity.this, itemsOnClick);
                menuWindow.showAtLocation(findViewById(R.id.activity_rebuild_psd), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.pv_xsz:
//                Toast.makeText(this, "行驶证成功", Toast.LENGTH_SHORT).show();
                imgType = 2;
                menuWindow = new SelectPicPopupWindow(CertificationActivity.this, itemsOnClick);
                menuWindow.showAtLocation(findViewById(R.id.activity_rebuild_psd), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;

            case R.id.pv_cl:
//                Toast.makeText(this, "车辆成功", Toast.LENGTH_SHORT).show();
                imgType = 3;
                menuWindow = new SelectPicPopupWindow(CertificationActivity.this, itemsOnClick);
                menuWindow.showAtLocation(findViewById(R.id.activity_rebuild_psd), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.bv_next:

                String name = mEt_name.getText().toString();
                String cardid = mEt_cardNo.getText().toString();
                String jiasz = mEt_drivingLicense.getText().toString();
                String xingsz = mEt_roadPermit.getText().toString();
                String cheph = mEt_carNumber.getText().toString();
                int chelx = VehType;
                //String gps = mEt_gps.getText().toString();

                if (name.isEmpty() || cardid.isEmpty()
                        || jiasz.isEmpty() || cheph.isEmpty()
                        || xingsz.isEmpty()
                        //|| gps.isEmpty()
                        ) {

                    Toast.makeText(CertificationActivity.this, "信息不完整", Toast.LENGTH_SHORT).show();
                } else {
                    mJsonObject.addProperty("UserName", name);
                    mJsonObject.addProperty("IDNumber", cardid);
                    mJsonObject.addProperty("UserId", SPtils.getString(this, "UserId", "00000000-0000-0000-0000-000000000000"));
                    mJsonObject.addProperty("DriverId", jiasz);
                    mJsonObject.addProperty("VehicleNo", cheph);
                    mJsonObject.addProperty("TravelCard", xingsz);
                    mJsonObject.addProperty("VehType", chelx);
                    //mJsonObject.addProperty("GpsNo", gps);
                    mLl_cer1.setVisibility(View.GONE);
                    mLl_cer2.setVisibility(View.VISIBLE);

                }
                break;
        }

    }

    private void posData() {
        if (isimg0 == false || isimg1 == false || isimg2 == false || isimg3 == false) {
            Toast.makeText(this, "信息不完整", Toast.LENGTH_SHORT).show();
        } else {
            Map<String, String> map = EncryptUtil.encryptDES(mJsonObject.toString());

            HttpUtil.doPost(CertificationActivity.this, Contants.url_savevehicleinfo, "savevehicleinfo", map, new VolleyInterface(CertificationActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
                @Override
                public void onSuccess(JsonElement result) {
                    isChange = true;
                    showShortToastByString("提交成功");
                    finish();
                }

                @Override
                public void onError(VolleyError error) {
                    showShortToastByString("网络异常");
                }

                @Override
                public void onStateError(int sta, String msg) {
                    if (!TextUtils.isEmpty(msg)) {
                        showShortToastByString(msg);
                    }
                }
            });
        }
    }


    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.takePhotoBtn://拍照
                    menuWindow.dismiss();
                    if (ContextCompat.checkSelfPermission(CertificationActivity.this, Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(CertificationActivity.this,
                                new String[]{Manifest.permission.CAMERA,
                                        Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
                    } else {
                        Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        //下面这句指定调用相机拍照后的照片存储的路径
                        takeIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME[imgType])));
                        startActivityForResult(takeIntent, REQUESTCODE_TAKE);
                    }

                    break;
                // 相册选择图片
                case R.id.pickPhotoBtn:
                    menuWindow.dismiss();
                    if (ContextCompat.checkSelfPermission(CertificationActivity.this, Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(CertificationActivity.this,
                                new String[]{Manifest.permission.CAMERA,
                                        Manifest.permission.READ_EXTERNAL_STORAGE,
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


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 100: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    //下面这句指定调用相机拍照后的照片存储的路径
                    takeIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME[imgType])));
                    startActivityForResult(takeIntent, REQUESTCODE_TAKE);
                } else {
                    Toast.makeText(CertificationActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case 200: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
                    pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(pickIntent, REQUESTCODE_PICK);
                } else {
                    Toast.makeText(CertificationActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case 300: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //getServletData();
                } else {
                    Toast.makeText(CertificationActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
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


    /**
     * 修改图像
     *
     * @param data
     */
    private void setPicToView(Intent data) {

        Bundle extras = data.getExtras();
        if (extras != null) {

            Bitmap bitmap = extras.getParcelable("data");

            Drawable drawable = new BitmapDrawable(null, bitmap);


            String HeadPortrait = BitmapUtil.getImgStr(bitmap);
            switch (imgType) {
                case 0:
                    mIv_sf.setImageDrawable(drawable);
                    mJsonObject.addProperty("IDNumberImg", HeadPortrait);//身份证
                    isimg0 = true;
                    break;
                case 1:
                    mIv_js.setImageDrawable(drawable);
                    mJsonObject.addProperty("DriverLicenseImg", HeadPortrait);//驾驶证
                    isimg1 = true;
                    break;
                case 2:
                    mIv_xs.setImageDrawable(drawable);
                    mJsonObject.addProperty("TravelCardImg", HeadPortrait);//行驶证
                    isimg2 = true;
                    break;
                case 3:
                    mIv_che.setImageDrawable(drawable);
                    mJsonObject.addProperty("VehImg", HeadPortrait);//车辆
                    isimg3 = true;
                    break;

            }

        }

    }


    @Override
    public void onBackPressed() {
        if (isChange) {
            setResult(RESULT_OK, getIntent());
        }
        super.onBackPressed();
    }

    /**
     * 获取所有车辆类型
     */

    public void getVehicleTypes() {

        JsonObject mJsonObject = new JsonObject();
        Map<String, String> map = EncryptUtil.encryptDES(mJsonObject.toString());

        HttpUtil.doPost(CertificationActivity.this, Contants.url_getvehicletypes, "getvehicletypes", map, new VolleyInterface(CertificationActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onSuccess(JsonElement result) {
                isChange = true;

            }

            @Override
            public void onError(VolleyError error) {
            }

            @Override
            public void onStateError(int sta, String msg) {
                if (!TextUtils.isEmpty(msg)) {
                    showShortToastByString(msg);
                }
            }
        });
    }
}
