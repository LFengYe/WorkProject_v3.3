package cn.guugoo.jiapeiteacher.activity;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import cn.guugoo.jiapeiteacher.R;
import cn.guugoo.jiapeiteacher.base.BaseActivity;
import cn.guugoo.jiapeiteacher.bean.NoticeDetailsData;
import cn.guugoo.jiapeiteacher.bean.TotalData;
import cn.guugoo.jiapeiteacher.base.BaseAsyncTask;
import cn.guugoo.jiapeiteacher.util.EncryptUtils;
import cn.guugoo.jiapeiteacher.util.HttpUtil;
import cn.guugoo.jiapeiteacher.util.NetUtil;

/**
 * Created by gpw on 2016/8/7.
 * --加油
 */
public class NoticeActivity extends BaseActivity {
    private TextView tv_title;
    private TextView tv_content;
    private int noticeId;
    private String token;


    @Override
    protected int getLayout() {
        return R.layout.activity_notice_details;
    }

    @Override
    protected void initView() {
        RelativeLayout rl_head = (RelativeLayout) findViewById(R.id.head);
        TextView tv_center = (TextView) rl_head.findViewById(R.id.tv_center);
        ImageView iv_back = (ImageView) rl_head.findViewById(R.id.iv_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_content = (TextView) findViewById(R.id.tv_content);
        tv_center.setText("通知");
        iv_back.setOnClickListener(this);




        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("NoticeId", noticeId);
        jsonObject = EncryptUtils.encryptDES(jsonObject.toString());
        if (!NetUtil.checkNetworkConnection(NoticeActivity.this)){
            Toast.makeText(NoticeActivity.this, R.string.Net_error, Toast.LENGTH_SHORT).show();
            return;
        }
        new GetContentAsyncTask(NoticeActivity.this,HttpUtil.url_noticeDetails,token).execute(jsonObject);


    }

    @Override
    protected void initData() {
        noticeId = getIntent().getIntExtra("noticeId", 0);
        token = getIntent().getStringExtra("token");
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_back) {
            finish();
        }
    }

    class GetContentAsyncTask extends BaseAsyncTask {


        public GetContentAsyncTask(Context mContext, String url, String token) {
            super(mContext, url, token);
        }




        @Override
        protected void dealResults(String s) {
            if (s.isEmpty()) {
                Toast.makeText(NoticeActivity.this, R.string.servlet_error, Toast.LENGTH_SHORT).show();
                return;
            }
            System.out.println(s);
            Gson gson = new Gson();
            TotalData totalData = gson.fromJson(s, TotalData.class);
            if (totalData.getStatus() == 0) {
                NoticeDetailsData noticeDetailsData = gson.fromJson(totalData.getData(), NoticeDetailsData.class);
                tv_title.setText(noticeDetailsData.getTitle());
                tv_content.setText(noticeDetailsData.getContent());
            } else {
                Toast.makeText(NoticeActivity.this, totalData.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

}
