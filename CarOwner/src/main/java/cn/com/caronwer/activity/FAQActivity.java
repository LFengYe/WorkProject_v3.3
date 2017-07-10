package cn.com.caronwer.activity;


import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Map;

import cn.com.caronwer.R;
import cn.com.caronwer.base.BaseActivity;
import cn.com.caronwer.base.Contants;
import cn.com.caronwer.bean.MessageInfo;
import cn.com.caronwer.util.EncryptUtil;
import cn.com.caronwer.util.HttpUtil;
import cn.com.caronwer.util.VolleyInterface;

public class FAQActivity extends BaseActivity {
    private TextView tv_title;
    private TextView tv_right;
    private ImageView iv_left_white;
    private TextView tv_msgtitle;
    private TextView tv_content;


    @Override
    protected int getLayout() {
        return R.layout.activity_faq;
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

        tv_title.setText(R.string.faq);
        tv_right.setVisibility(View.GONE);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("Type", 1);
        jsonObject.addProperty("UserType", 2);
        Map<String, String> map = EncryptUtil.encryptDES(jsonObject.toString());
        HttpUtil.doPost(FAQActivity.this, Contants.url_obtainMessage, "obtainMessage", map, new VolleyInterface(FAQActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onSuccess(JsonElement result) {
                System.out.println("result:" + result);
                Gson gson = new Gson();
                MessageInfo messageInfo = gson.fromJson(result, MessageInfo.class);
                String msgtitle = messageInfo.getTitle();
                String content = messageInfo.getArticleContent();
                try {
                    msgtitle = EncryptUtil.decryptDES(msgtitle);
                    content = EncryptUtil.decryptDES(content);
                    tv_content.setText(Html.fromHtml(content));
                    tv_msgtitle.setText(msgtitle);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {
                showShortToastByString(getString(R.string.timeoutError));
            }

            @Override
            public void onStateError(int sta, String msg) {
                if (!TextUtils.isEmpty(msg)) {
                    showShortToastByString(msg);
                }
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
