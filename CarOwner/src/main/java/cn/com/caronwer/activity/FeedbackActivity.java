package cn.com.caronwer.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Map;

import cn.com.caronwer.R;
import cn.com.caronwer.base.BaseActivity;
import cn.com.caronwer.base.Contants;
import cn.com.caronwer.util.EncryptUtil;
import cn.com.caronwer.util.HttpUtil;
import cn.com.caronwer.util.SPtils;
import cn.com.caronwer.util.VolleyInterface;

public class FeedbackActivity extends BaseActivity {


    private TextView tv_title;
    private TextView tv_right;
    private ImageView iv_left_white;
    private EditText et_comment;
    private EditText et_account;

    @Override
    protected int getLayout() {
        return R.layout.activity_feedback;
    }

    @Override
    protected void findById() {
        RelativeLayout rl_head = (RelativeLayout) findViewById(R.id.in_head);
        assert rl_head != null;
        tv_title = (TextView) rl_head.findViewById(R.id.tv_title);
        tv_right = (TextView) rl_head.findViewById(R.id.tv_right);
        iv_left_white = (ImageView) rl_head.findViewById(R.id.iv_left_white);
        et_account = (EditText) findViewById(R.id.et_account);
        et_comment = (EditText) findViewById(R.id.et_comment);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        tv_title.setText(R.string.feed_back);
        tv_right.setText(R.string.propose);
        iv_left_white.setOnClickListener(this);
        tv_right.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left_white:
                finish();
                break;
            case R.id.tv_right:
                String title = et_account.getText().toString();
                String SContent = et_comment.getText().toString();
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("UserId", SPtils.getString(this, "UserId", "00000000-0000-0000-0000-000000000000"));
                jsonObject.addProperty("Title", title);
                jsonObject.addProperty("SContent", SContent);
                jsonObject.addProperty("UserType", 2);
                Map<String, String> map = EncryptUtil.encryptDES(jsonObject.toString());
                HttpUtil.doPost(FeedbackActivity.this, Contants.url_saveSuggest, "saveSuggest", map, new VolleyInterface(FeedbackActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
                    @Override
                    public void onSuccess(JsonElement result) {
                        showShortToastByString("提交成功");
                        et_account.setText("");
                        et_comment.setText("");
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
                break;

        }
    }
}
