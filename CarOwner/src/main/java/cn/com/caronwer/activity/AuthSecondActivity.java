package cn.com.caronwer.activity;

import android.Manifest;
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
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.File;
import java.util.Map;

import cn.com.caronwer.R;
import cn.com.caronwer.base.BaseActivity;
import cn.com.caronwer.base.Contants;
import cn.com.caronwer.bean.VehicleAuth1;
import cn.com.caronwer.bean.VehicleAuth2;
import cn.com.caronwer.util.BitmapUtil;
import cn.com.caronwer.util.EncryptUtil;
import cn.com.caronwer.util.HttpUtil;
import cn.com.caronwer.util.LogUtil;
import cn.com.caronwer.util.VolleyInterface;
import cn.com.caronwer.view.SelectPicPopupWindow;
import cn.com.caronwer.view.UploadTextView;

/**
 * Created by LFeng on 2017/7/10.
 */

public class AuthSecondActivity extends BaseActivity {

    private TextView tv_title;
    private TextView tv_right;
    private ImageView iv_left_white;

    private SelectPicPopupWindow menuWindow;
    private static final int REQUEST_CODE_PICK = 0;        // 相册选图标记
    private static final int REQUEST_CODE_TAKE = 1;        // 相机拍照标记
    private static final int REQUEST_CODE_CUTTING = 2;    // 图片裁切标记
    private static final String[] IMAGE_FILE_NAME = {"cardNo.jpg", "drivingLicense.jpg"
            , "carHead.jpg", "carTail.jpg", "carLeft.jpg", "carRight.jpg"};// 头像文件名称
    private static int imgType = 0;

    private boolean isCardNoImgSuccess;
    private boolean isDrivingLicenseImgSuccess;
    private boolean isCarHeadImgSuccess;
    private boolean isCarTailImgSuccess;
    private boolean isCarLeftImgSuccess;
    private boolean isCarRightImgSuccess;

    private UploadTextView cardNoUpload;
    private UploadTextView drivingLicenseUpload;
    private UploadTextView carHeadUpload;
    private UploadTextView carTailUpload;
    private UploadTextView carLeftUpload;
    private UploadTextView carRightUpload;

    private ImageView cardNoImg;
    private ImageView drivingLicenseImg;
    private ImageView carHeadImg;
    private ImageView carTailImg;
    private ImageView carLeftImg;
    private ImageView carRightImg;

    private JsonObject jsonObject;

    //region 弹出菜单选择
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.takePhotoBtn://拍照
                    menuWindow.dismiss();
                    if (ContextCompat.checkSelfPermission(AuthSecondActivity.this, Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(AuthSecondActivity.this,
                                new String[]{Manifest.permission.CAMERA,
                                        Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
                    } else {
                        Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        //下面这句指定调用相机拍照后的照片存储的路径
                        takeIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME[imgType])));
                        startActivityForResult(takeIntent, REQUEST_CODE_TAKE);
                    }

                    break;
                // 相册选择图片
                case R.id.pickPhotoBtn:
                    menuWindow.dismiss();
                    if (ContextCompat.checkSelfPermission(AuthSecondActivity.this, Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(AuthSecondActivity.this,
                                new String[]{Manifest.permission.CAMERA,
                                        Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, 200);
                    } else {
                        Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
                        pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                        startActivityForResult(pickIntent, REQUEST_CODE_PICK);
                    }
                    break;
                default:
                    break;
            }
        }
    };
    //endregion

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri;
        switch (requestCode) {
            case REQUEST_CODE_PICK:// 直接从相册获取
                if (data == null) {
                    return;
                }
                uri = data.getData();
                if (uri != null) {
                    startPhotoZoom(uri);
                }

                break;
            case REQUEST_CODE_TAKE:// 调用相机拍照
                if (resultCode == RESULT_CANCELED) {
                    return;
                }
                File temp = new File(Environment.getExternalStorageDirectory() + "/" + IMAGE_FILE_NAME[imgType]);
                uri = Uri.fromFile(temp);

                System.out.println(uri);
                if (uri != null) {
                    startPhotoZoom(uri);
                }

                break;
            case REQUEST_CODE_CUTTING:// 取得裁剪后的图片
                if (data == null) {
                    return;
                } else {
                    setPicToView(data);
                }

                break;
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_auth_second;
    }

    @Override
    protected void findById() {
        RelativeLayout rl_head = (RelativeLayout) findViewById(R.id.in_head);
        assert rl_head != null;
        tv_title = (TextView) rl_head.findViewById(R.id.tv_title);
        tv_right = (TextView) rl_head.findViewById(R.id.tv_right);
        iv_left_white = (ImageView) rl_head.findViewById(R.id.iv_left_white);

        cardNoUpload = (UploadTextView) findViewById(R.id.upload_card_no);
        cardNoUpload.setOnClickListener(this);
        drivingLicenseUpload = (UploadTextView) findViewById(R.id.upload_driving_license);
        drivingLicenseUpload.setOnClickListener(this);
        carHeadUpload = (UploadTextView) findViewById(R.id.upload_car_head);
        carHeadUpload.setOnClickListener(this);
        carTailUpload = (UploadTextView) findViewById(R.id.upload_car_tail);
        carTailUpload.setOnClickListener(this);
        carLeftUpload = (UploadTextView) findViewById(R.id.upload_car_left);
        carLeftUpload.setOnClickListener(this);
        carRightUpload = (UploadTextView) findViewById(R.id.upload_car_right);
        carRightUpload.setOnClickListener(this);

        cardNoImg = (ImageView) findViewById(R.id.card_no_img);
        cardNoImg.setOnClickListener(this);
        drivingLicenseImg = (ImageView) findViewById(R.id.driving_license_img);
        drivingLicenseImg.setOnClickListener(this);
        carHeadImg = (ImageView) findViewById(R.id.car_head_img);
        carHeadImg.setOnClickListener(this);
        carTailImg = (ImageView) findViewById(R.id.car_tail_img);
        carTailImg.setOnClickListener(this);
        carLeftImg = (ImageView) findViewById(R.id.car_left_img);
        carLeftImg.setOnClickListener(this);
        carRightImg = (ImageView) findViewById(R.id.car_right_img);
        carRightImg.setOnClickListener(this);

        Button button = (Button) findViewById(R.id.bt_ok);
        button.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        getAuthInfo();

        SharedPreferences prefs = getSharedPreferences(Contants.SHARED_NAME, MODE_PRIVATE);
        jsonObject = new JsonObject();
        //jsonObject.addProperty("VehNof", getIntent().getStringExtra("VehicleNo"));
        jsonObject.addProperty("UserID", prefs.getString("UserId", ""));
    }

    @Override
    protected void initView() {
        tv_title.setText("车主认证");
        tv_right.setVisibility(View.GONE);
        iv_left_white.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_ok:
                getImg();
                if (isCardNoImgSuccess
                        && isDrivingLicenseImgSuccess
                        && isCarHeadImgSuccess
                        && isCarTailImgSuccess
                        && isCarLeftImgSuccess
                        && isCarRightImgSuccess) {
                    uploadImg();
                } else {
                    showShortToastByString("信息填写不完整");
                }
                break;
            case R.id.iv_left_white:
                finish();
                break;
            case R.id.upload_card_no:
            case R.id.card_no_img:
                imgType = 0;
                menuWindow = new SelectPicPopupWindow(AuthSecondActivity.this, itemsOnClick);
                menuWindow.showAtLocation(findViewById(R.id.auth_second), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.upload_driving_license:
            case R.id.driving_license_img:
                imgType = 1;
                menuWindow = new SelectPicPopupWindow(AuthSecondActivity.this, itemsOnClick);
                menuWindow.showAtLocation(findViewById(R.id.auth_second), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.upload_car_head:
            case R.id.car_head_img:
                imgType = 2;
                menuWindow = new SelectPicPopupWindow(AuthSecondActivity.this, itemsOnClick);
                menuWindow.showAtLocation(findViewById(R.id.auth_second), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.upload_car_tail:
            case R.id.car_tail_img:
                imgType = 3;
                menuWindow = new SelectPicPopupWindow(AuthSecondActivity.this, itemsOnClick);
                menuWindow.showAtLocation(findViewById(R.id.auth_second), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.upload_car_left:
            case R.id.car_left_img:
                imgType = 4;
                menuWindow = new SelectPicPopupWindow(AuthSecondActivity.this, itemsOnClick);
                menuWindow.showAtLocation(findViewById(R.id.auth_second), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.upload_car_right:
            case R.id.car_right_img:
                imgType = 5;
                menuWindow = new SelectPicPopupWindow(AuthSecondActivity.this, itemsOnClick);
                menuWindow.showAtLocation(findViewById(R.id.auth_second), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
        }
    }

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
        startActivityForResult(intent, REQUEST_CODE_CUTTING);
    }

    private void setPicToView(Intent data) {

        Bundle extras = data.getExtras();
        if (extras != null) {

            Bitmap bitmap = extras.getParcelable("data");

            Drawable drawable = new BitmapDrawable(null, bitmap);

            //String HeadPortrait = BitmapUtil.getImgStr(bitmap);
            switch (imgType) {
                case 0:
                    cardNoImg.setImageDrawable(drawable);
                    cardNoImg.setVisibility(View.VISIBLE);
                    cardNoUpload.setVisibility(View.INVISIBLE);
//                    jsonObject.addProperty("Sfz", HeadPortrait);
//                    isCardNoImgSuccess = true;
                    break;
                case 1:
                    drivingLicenseImg.setImageDrawable(drawable);
                    drivingLicenseImg.setVisibility(View.VISIBLE);
                    drivingLicenseUpload.setVisibility(View.INVISIBLE);
//                    jsonObject.addProperty("Driver", HeadPortrait);
//                    isDrivingLicenseImgSuccess = true;
                    break;
                case 2:
                    carHeadImg.setImageDrawable(drawable);
                    carHeadImg.setVisibility(View.VISIBLE);
                    carHeadUpload.setVisibility(View.INVISIBLE);
//                    jsonObject.addProperty("CarFront", HeadPortrait);
//                    isCarHeadImgSuccess = true;
                    break;
                case 3:
                    carTailImg.setImageDrawable(drawable);
                    carTailImg.setVisibility(View.VISIBLE);
                    carTailUpload.setVisibility(View.INVISIBLE);
//                    jsonObject.addProperty("CarBack", HeadPortrait);
//                    isCarTailImgSuccess = true;
                    break;
                case 4:
                    carLeftImg.setImageDrawable(drawable);
                    carLeftImg.setVisibility(View.VISIBLE);
                    carLeftUpload.setVisibility(View.INVISIBLE);
//                    jsonObject.addProperty("CarLeft", HeadPortrait);
//                    isCarLeftImgSuccess = true;
                    break;
                case 5:
                    carRightImg.setImageDrawable(drawable);
                    carRightImg.setVisibility(View.VISIBLE);
                    carRightUpload.setVisibility(View.INVISIBLE);
//                    jsonObject.addProperty("CarRight", HeadPortrait);
//                    isCarRightImgSuccess = true;
                    break;
            }

        }

    }

    private void getImg() {
        BitmapDrawable cardNoDrawable = (BitmapDrawable) cardNoImg.getDrawable();
        if (cardNoDrawable != null) {
            Bitmap cardNoBitmap = cardNoDrawable.getBitmap();
            jsonObject.addProperty("Sfz", BitmapUtil.getImgStr(cardNoBitmap));//身份证
            isCardNoImgSuccess = true;
        }

        BitmapDrawable drivingLicenseDrawable = ((BitmapDrawable) drivingLicenseImg.getDrawable());
        if (drivingLicenseDrawable != null) {
            Bitmap drivingLicenseBitmap = drivingLicenseDrawable.getBitmap();
            jsonObject.addProperty("Driver", BitmapUtil.getImgStr(drivingLicenseBitmap));
            isDrivingLicenseImgSuccess = true;
        }

        BitmapDrawable carHeadDrawable = ((BitmapDrawable) carHeadImg.getDrawable());
        if (carHeadDrawable != null) {
            Bitmap carHeadBitmap = carHeadDrawable.getBitmap();
            jsonObject.addProperty("CarFront", BitmapUtil.getImgStr(carHeadBitmap));
            isCarHeadImgSuccess = true;
        }

        BitmapDrawable carTailDrawable = (BitmapDrawable) carTailImg.getDrawable();
        if (carTailDrawable != null) {
            Bitmap carTailBitmap = carTailDrawable.getBitmap();
            jsonObject.addProperty("CarBack", BitmapUtil.getImgStr(carTailBitmap));
            isCarTailImgSuccess = true;
        }

        BitmapDrawable carLeftDrawable = (BitmapDrawable) carLeftImg.getDrawable();
        if (carLeftDrawable != null) {
            Bitmap carLeftBitmap = carLeftDrawable.getBitmap();
            jsonObject.addProperty("CarLeft", BitmapUtil.getImgStr(carLeftBitmap));
            isCarLeftImgSuccess = true;
        }

        BitmapDrawable carRightDrawable = (BitmapDrawable) carRightImg.getDrawable();
        if (carRightDrawable != null) {
            Bitmap carRightBitmap = carRightDrawable.getBitmap();
            jsonObject.addProperty("CarRight", BitmapUtil.getImgStr(carRightBitmap));
            isCarRightImgSuccess = true;
        }
    }

    private void uploadImg() {
        Map<String, String> map = EncryptUtil.encryptDES(jsonObject.toString());
        HttpUtil.doPost(AuthSecondActivity.this, Contants.url_TransporterVehicleCheck2, "VehicleCheck2", map,
                new VolleyInterface(AuthSecondActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
                    @Override
                    public void onSuccess(JsonElement result) {
                        Intent intent = new Intent(AuthSecondActivity.this, AuthThirdActivity.class);
                        startActivity(intent);
                        finish();
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

    private void getAuthInfo() {
        SharedPreferences prefs = getSharedPreferences(Contants.SHARED_NAME, MODE_PRIVATE);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("UserID", prefs.getString("UserId", ""));

        Map<String, String> map = EncryptUtil.encryptDES(jsonObject.toString());
        HttpUtil.doPost(AuthSecondActivity.this, Contants.url_TransporterGetVehicleCheck2, "GetVehicleCheck2", map,
                new VolleyInterface(AuthSecondActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
                    @Override
                    public void onSuccess(JsonElement result) {
                        Gson gson = new Gson();
                        VehicleAuth2 vehicleAuth = gson.fromJson(result, VehicleAuth2.class);
                        setAuthInfo(vehicleAuth);
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

    private void setAuthInfo(VehicleAuth2 vehicleAuth) {
        if (!TextUtils.isEmpty(vehicleAuth.getSfz())) {
            HttpUtil.setImageLoader(Contants.imagehost + vehicleAuth.getSfz(), cardNoImg, R.mipmap.cir_head, R.mipmap.cir_head);
            cardNoImg.setVisibility(View.VISIBLE);
            cardNoUpload.setVisibility(View.INVISIBLE);
            isCardNoImgSuccess = true;
        }

        if (!TextUtils.isEmpty(vehicleAuth.getDriver())) {
            HttpUtil.setImageLoader(Contants.imagehost + vehicleAuth.getDriver(), drivingLicenseImg, R.mipmap.cir_head, R.mipmap.cir_head);
            drivingLicenseImg.setVisibility(View.VISIBLE);
            drivingLicenseUpload.setVisibility(View.INVISIBLE);
            isDrivingLicenseImgSuccess = true;
        }

        if (!TextUtils.isEmpty(vehicleAuth.getCarRight())) {
            HttpUtil.setImageLoader(Contants.imagehost + vehicleAuth.getCarRight(), carRightImg, R.mipmap.cir_head, R.mipmap.cir_head);
            carRightImg.setVisibility(View.VISIBLE);
            carRightUpload.setVisibility(View.INVISIBLE);
            isCarRightImgSuccess = true;
        }

        if (!TextUtils.isEmpty(vehicleAuth.getCarLeft())) {
            HttpUtil.setImageLoader(Contants.imagehost + vehicleAuth.getCarLeft(), carLeftImg, R.mipmap.cir_head, R.mipmap.cir_head);
            carLeftImg.setVisibility(View.VISIBLE);
            carLeftUpload.setVisibility(View.INVISIBLE);
            isCarLeftImgSuccess = true;
        }

        if (!TextUtils.isEmpty(vehicleAuth.getCarFront())) {
            HttpUtil.setImageLoader(Contants.imagehost + vehicleAuth.getCarFront(), carHeadImg, R.mipmap.cir_head, R.mipmap.cir_head);
            carHeadImg.setVisibility(View.VISIBLE);
            carHeadUpload.setVisibility(View.INVISIBLE);
            isCarHeadImgSuccess = true;
        }

        if (!TextUtils.isEmpty(vehicleAuth.getCarBack())) {
            HttpUtil.setImageLoader(Contants.imagehost + vehicleAuth.getCarBack(), carTailImg, R.mipmap.cir_head, R.mipmap.cir_head);
            carTailImg.setVisibility(View.VISIBLE);
            carTailUpload.setVisibility(View.INVISIBLE);
            isCarTailImgSuccess = true;
        }
    }
}
