package com.guugoo.jiapeiteacher.activity;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.guugoo.jiapeiteacher.R;
import com.guugoo.jiapeiteacher.base.BaseActivity;
import com.guugoo.jiapeiteacher.bean.TotalData;
import com.guugoo.jiapeiteacher.base.BaseAsyncTask;
import com.guugoo.jiapeiteacher.util.EncryptUtils;
import com.guugoo.jiapeiteacher.util.HttpUtil;
import com.guugoo.jiapeiteacher.util.NetUtil;

/**
 * Created by gpw on 2016/8/8.
 * --加油
 */
public class PublishActivity extends BaseActivity {
    private EditText et_content;
    private int teacherId;
    private String token;
    @Override
    protected int getLayout() {
        return R.layout.activity_publish;
    }

    @Override
    protected void initView() {

        RelativeLayout rl_head = (RelativeLayout) findViewById(R.id.head);
        TextView tv_center = (TextView) rl_head.findViewById(R.id.tv_center);
        TextView tv_right = (TextView) rl_head.findViewById(R.id.tv_right);
        ImageView iv_back = (ImageView) rl_head.findViewById(R.id.iv_back);
        et_content = (EditText) findViewById(R.id.et_content);

        tv_center.setText(R.string.publish);
        tv_right.setText("发表");

        iv_back.setOnClickListener(this);
        tv_right.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        teacherId = getIntent().getIntExtra("teacherId",0);
        token = getIntent().getStringExtra("token");
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.iv_back){
            finish();
        }
        else if(v.getId()==R.id.tv_right){
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("StudentId", teacherId);
            jsonObject.addProperty("Type", 2);
            jsonObject.addProperty("Content", et_content.getText().toString());
            System.out.println(jsonObject.toString());
            jsonObject = EncryptUtils.encryptDES(jsonObject.toString());
            if (!NetUtil.checkNetworkConnection(PublishActivity.this)){
                Toast.makeText(PublishActivity.this, R.string.Net_error, Toast.LENGTH_SHORT).show();
                return;
            }
            new PublishAsyncTask(PublishActivity.this,HttpUtil.url_publish,token).execute(jsonObject);
        }

    }


    class PublishAsyncTask extends BaseAsyncTask {


        public PublishAsyncTask(Context mContext, String url, String token) {
            super(mContext, url, token);
        }



        @Override
        protected void dealResults(String s) {
            if (s.isEmpty()) {
                Toast.makeText(PublishActivity.this, R.string.servlet_error, Toast.LENGTH_SHORT).show();
                return;
            }
            Gson gson = new Gson();
            TotalData totalData = gson.fromJson(s, TotalData.class);
            if (totalData.getStatus() == 0) {
                Toast.makeText(PublishActivity.this, totalData.getMessage(), Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK,getIntent());
                finish();
            } else {
                Toast.makeText(PublishActivity.this, totalData.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
