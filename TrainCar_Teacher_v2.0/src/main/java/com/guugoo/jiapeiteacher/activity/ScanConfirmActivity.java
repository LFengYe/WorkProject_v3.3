package com.guugoo.jiapeiteacher.activity;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.guugoo.jiapeiteacher.R;
import com.guugoo.jiapeiteacher.adapter.BlanchInfoAdapter;
import com.guugoo.jiapeiteacher.base.BaseActivity;
import com.guugoo.jiapeiteacher.base.BaseAsyncTask;
import com.guugoo.jiapeiteacher.bean.BlanchInfo;
import com.guugoo.jiapeiteacher.bean.LoginInfo;
import com.guugoo.jiapeiteacher.bean.TotalData;
import com.guugoo.jiapeiteacher.util.EncryptUtils;
import com.guugoo.jiapeiteacher.util.HttpUtil;
import com.guugoo.jiapeiteacher.view.CircleImageView;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by LFeng on 2017/7/28.
 */

public class ScanConfirmActivity extends BaseActivity {

    private PopupWindow popupWindow;
    private float alpha;

    private TextView spaceName;
    private String carNo;
    private LoginInfo loginInfo;
    private int spaceId = -1;

    @Override
    protected int getLayout() {
        return R.layout.activity_scan_confirm;
    }

    @Override
    protected void initView() {
        CircleImageView civ_head = (CircleImageView) findViewById(R.id.civ_head);
        if (ContextCompat.checkSelfPermission(ScanConfirmActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ScanConfirmActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    100);
        } else {
            Glide.with(this)
                    .load(loginInfo.getIcon())
                    .crossFade()
                    .skipMemoryCache(false)
                    .error(R.mipmap.icon_head)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(civ_head);
        }

        TextView tv_nickName = (TextView) findViewById(R.id.tv_nickName);
        tv_nickName.setText(loginInfo.getNicKname());

        TextView tv_name = (TextView) findViewById(R.id.name_text);
        tv_name.setText(loginInfo.getName());

        TextView tv_cardId = (TextView) findViewById(R.id.cardId_text);
        tv_cardId.setText(loginInfo.getCardNo());

        TextView tv_phone = (TextView) findViewById(R.id.phone_text);
        tv_phone.setText(loginInfo.getTel());

        TextView tv_school = (TextView) findViewById(R.id.school_text);
        tv_school.setText(loginInfo.getSchoolName());

        TextView tv_carNo = (TextView) findViewById(R.id.carNo_text);
        tv_carNo.setText(carNo);

        spaceName = (TextView) findViewById(R.id.space_select_text);

        findViewById(R.id.space_select_layout).setOnClickListener(this);
        findViewById(R.id.coach_login_confirm).setOnClickListener(this);
    }

    @Override
    protected void initData() {
        carNo = getIntent().getStringExtra("VehNof");
        loginInfo = getIntent().getParcelableExtra("loginInfo");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.space_select_layout: {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("SchoolId", loginInfo.getSchoolId());
                jsonObject = EncryptUtils.encryptDES(jsonObject.toString());
                new GetBlanch(this, HttpUtil.url_StudentsGetBlanch, loginInfo.getToken()).execute(jsonObject);
                break;
            }
            case R.id.coach_login_confirm: {
                if (spaceId == -1) {
                    Toast.makeText(this, getString(R.string.space_select_hint), Toast.LENGTH_SHORT).show();
                    return;
                }
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("VehNof", carNo);
                jsonObject.addProperty("CoachID", loginInfo.getId());
                jsonObject.addProperty("CDID", spaceId);
                //Log.i("签到参数", jsonObject.toString());
                jsonObject = EncryptUtils.encryptDES(jsonObject.toString());
                new CoachLogin(ScanConfirmActivity.this, HttpUtil.url_CoachLogin, loginInfo.getToken()).execute(jsonObject);
                break;
            }
        }
    }

    private void showPopupWindow(final ArrayList<BlanchInfo> blanchInfos) {
        View contentView = LayoutInflater.from(this).inflate(R.layout.layout_list_popup, null);

        popupWindow = new PopupWindow(contentView
                ,ViewGroup.LayoutParams.MATCH_PARENT
                ,ViewGroup.LayoutParams.WRAP_CONTENT);

        ListView listView = (ListView) contentView.findViewById(R.id.list_content);
        BlanchInfoAdapter adapter = new BlanchInfoAdapter(blanchInfos, this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BlanchInfo blanchInfo = blanchInfos.get(position);
                spaceName.setText(blanchInfo.getBranchSchoolName());
                spaceId = blanchInfo.getId();
                popupWindow.dismiss();
            }
        });

        popupWindow.update();
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.showAtLocation(contentView, Gravity.BOTTOM, 0, 0);
        popupWindow.setOnDismissListener(new ScanConfirmActivity.PopupDismissListener());

        alpha = 1f;
        backgroundChange();
    }

    /**
     * 返回或者点击空白位置的时候将背景透明度改回来
     */
    class PopupDismissListener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //此处while的条件alpha不能<= 否则会出现黑屏
                    while (alpha < 1f) {
                        try {
                            Thread.sleep(4);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Message msg = mHandler.obtainMessage();
                        msg.what = 1;
                        alpha += 0.01f;
                        msg.obj = alpha;
                        mHandler.sendMessage(msg);
                    }
                }

            }).start();
        }
    }

    private void backgroundChange() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (alpha > 0.5f) {
                    try {
                        //4是根据弹出动画时间和减少的透明度计算
                        Thread.sleep(4);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Message msg = mHandler.obtainMessage();
                    msg.what = 1;
                    //每次减少0.01，精度越高，变暗的效果越流畅
                    alpha -= 0.01f;
                    msg.obj = alpha;
                    mHandler.sendMessage(msg);
                }
            }

        }).start();
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    backgroundAlpha((float) msg.obj);
                    break;
            }
        }
    };

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    class CoachLogin extends BaseAsyncTask {
        public CoachLogin(Context mContext, String url, String token) {
            super(mContext, url, token);
        }

        @Override
        protected void dealResults(String s) {
            Log.i("签到返回", s);
            if (TextUtils.isEmpty(s)) {
                Toast.makeText(ScanConfirmActivity.this, R.string.servlet_error, Toast.LENGTH_SHORT).show();
                return;
            }
            Gson gson = new Gson();
            TotalData totalData = gson.fromJson(s, TotalData.class);
            Toast.makeText(ScanConfirmActivity.this, totalData.getMessage(), Toast.LENGTH_SHORT).show();
            if (totalData.getStatus() == 0) {
                setResult(RESULT_OK);
                finish();
            }
        }
    }

    class GetBlanch extends BaseAsyncTask {
        public GetBlanch(Context mContext, String url, String token) {
            super(mContext, url, token);
        }

        @Override
        protected void dealResults(String s) {
            if (s.isEmpty()) {
                Toast.makeText(ScanConfirmActivity.this, R.string.servlet_error, Toast.LENGTH_SHORT).show();
                return;
            }
            Gson gson = new Gson();
            TotalData totalData = gson.fromJson(s, TotalData.class);
            if (totalData.getStatus() == 0) {
                Type listType = new TypeToken<ArrayList<BlanchInfo>>() {}.getType();
                ArrayList<BlanchInfo> blanchInfos = gson.fromJson(
                        totalData.getData().getAsJsonObject().getAsJsonArray("Blanchlist"), listType);
                showPopupWindow(blanchInfos);
            } else {
                Toast.makeText(ScanConfirmActivity.this, totalData.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
