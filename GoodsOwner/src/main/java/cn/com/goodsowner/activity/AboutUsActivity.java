package cn.com.goodsowner.activity;


import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Map;

import cn.com.goodsowner.R;
import cn.com.goodsowner.base.BaseActivity;
import cn.com.goodsowner.base.Contants;
import cn.com.goodsowner.bean.MessageInfo;
import cn.com.goodsowner.util.EncryptUtil;
import cn.com.goodsowner.util.HttpUtil;
import cn.com.goodsowner.util.LogUtil;
import cn.com.goodsowner.util.VolleyInterface;

public class AboutUsActivity extends BaseActivity {

    private TextView tv_title;
    private TextView tv_right;
    private ImageView iv_left_white;
    private TextView tv_msgtitle;
    private TextView tv_content;

    @Override
    protected int getLayout() {
        return R.layout.activity_about_us;
    }

    @Override
    protected void findById() {
        RelativeLayout rl_head = (RelativeLayout) findViewById(R.id.in_head);
        assert rl_head != null;
        tv_title = (TextView) rl_head.findViewById(R.id.tv_title);
        tv_right = (TextView) rl_head.findViewById(R.id.tv_right);
        iv_left_white = (ImageView) rl_head.findViewById(R.id.iv_left_white);

        tv_content = (TextView) findViewById(R.id.tv_content);
        tv_msgtitle = (TextView) findViewById(R.id.tv_msgtitle);
    }

    @Override
    protected void initData() {
    }
    @Override
    protected void initView() {
        tv_title.setText(R.string.about_us);
        tv_right.setVisibility(View.GONE);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("Type", 3);
        jsonObject.addProperty("UserType", 1);
        Map<String, String> map = EncryptUtil.encryptDES(jsonObject.toString());
        HttpUtil.doPost(AboutUsActivity.this, Contants.url_obtainMessage, "obtainMessage", map, new VolleyInterface(AboutUsActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onSuccess(JsonElement result) {
                LogUtil.i(result.toString());
                Gson gson = new Gson();
                MessageInfo messageInfo = gson.fromJson(result, MessageInfo.class);
                String msgtitle = messageInfo.getTitle();
                String content = messageInfo.getArticleContent();
                try {
                    msgtitle = EncryptUtil.decryptDES(msgtitle);
                    content = EncryptUtil.decryptDES(content);
                    tv_content.setText(content);
                    tv_msgtitle.setText(msgtitle);
                } catch (Exception e) {
                    e.printStackTrace();
                }

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


        iv_left_white.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left_white:
                finish();
                break;

        }
    }
}
