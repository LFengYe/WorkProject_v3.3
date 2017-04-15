package cn.com.caronwer.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.com.caronwer.R;
import cn.com.caronwer.adapter.ListAdapter;
import cn.com.caronwer.base.BaseActivity;
import cn.com.caronwer.base.Contants;
import cn.com.caronwer.util.EncryptUtil;
import cn.com.caronwer.util.HttpUtil;
import cn.com.caronwer.util.SPtils;
import cn.com.caronwer.util.VolleyInterface;
import cn.com.caronwer.view.BorderTextView;

public class WithdrawActivity extends BaseActivity {

    private TextView tv_title;
    private TextView tv_right;

    private EditText et_jine;  //金额
    private EditText et_yinhnag;   //银行
    private EditText et_kahao;      //卡号
    private EditText et_name;      //持卡人
    private EditText et_phone;      //手机号
    private BorderTextView bv_queren;   // 确认
    private ImageView iv_left_white;
    private ImageView mIv_down;

    public static WithdrawActivity sWithdrawActivity;
    public PopupWindow popupWindow;
    private ListView dropview_list;
    private ListAdapter listAdapter;
    private List<String> listStr;
    private Set<String> setStr;
    private int biaozhi = 0;

    @Override
    protected int getLayout() {
        return R.layout.activity_withdraw;
    }

    @Override
    protected void findById() {

        RelativeLayout rl_head = (RelativeLayout) findViewById(R.id.in_head);
        assert rl_head != null;
        tv_title = (TextView) rl_head.findViewById(R.id.tv_title);
        tv_right = (TextView) rl_head.findViewById(R.id.tv_right);
        iv_left_white = (ImageView) rl_head.findViewById(R.id.iv_left_white);


        et_jine = (EditText) findViewById(R.id.et_jine);
        et_yinhnag = (EditText) findViewById(R.id.et_yinhnag);
        et_kahao = (EditText) findViewById(R.id.et_kahao);
        et_name = (EditText) findViewById(R.id.et_name);
        et_phone = (EditText) findViewById(R.id.et_phone);
        bv_queren = (BorderTextView) findViewById(R.id.bv_queren);
        iv_left_white = (ImageView) findViewById(R.id.iv_left_white);
        mIv_down = (ImageView) findViewById(R.id.iv_down);
    }

    @Override
    protected void initData() {
        sWithdrawActivity = this;
        //初始化下拉列表中数据，以防getSharedPreferences抛null异常
        setStr = new HashSet<String>();
    }

    @Override
    protected void initView() {
        tv_title.setText("提现");
        bv_queren.setOnClickListener(this);
        iv_left_white.setOnClickListener(this);
        mIv_down.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_left_white:
                finish();
                break;
            case R.id.bv_queren:
                postData();
                // 保存数据
                //restoreRecord(et_kahao.getText().toString());
                break;
            case R.id.iv_down:
                //获取所有列表中的数据
//                restoreRecord("35437645865");
//                restoreRecord("79568568435");
//                initRecord();
//                showPopupWindow(v);
//                biaozhi = (biaozhi + 1) % 2;
//                mIv_down.setImageResource(R.mipmap.up);
//                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
//                    @Override
//                    public void onDismiss() {
//                        mIv_down.setImageResource(R.mipmap.down);
//                    }
//                });

                Intent intent = new Intent(WithdrawActivity.this, BankUserActivity.class);
                startActivityForResult(intent, 44);
                break;
        }
    }

    private void postData() {
        String phone = et_phone.getText().toString();
        String jine = et_jine.getText().toString();
        String kahao = et_kahao.getText().toString();
        String name = et_name.getText().toString();
        String yinhnag[] = et_yinhnag.getText().toString().split(" ");

        if (phone.isEmpty() || jine.isEmpty() || kahao.isEmpty() || name.isEmpty() || yinhnag[0].isEmpty()) {
            Toast.makeText(this, "信息不完整", Toast.LENGTH_SHORT).show();
        } else {

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("UserId", SPtils.getString(this, "UserId", "00000000-0000-0000-0000-000000000000"));
            jsonObject.addProperty("UserType", 2);
            jsonObject.addProperty("Tel", phone);
            jsonObject.addProperty("Amount", jine);
            jsonObject.addProperty("BankName", yinhnag[0]);
            jsonObject.addProperty("BranchBank", yinhnag[1]);
            jsonObject.addProperty("BranchBank", name);
            jsonObject.addProperty("AccountNumber", kahao);
            Map<String, String> map = EncryptUtil.encryptDES(jsonObject.toString());
            HttpUtil.doPost(WithdrawActivity.this, Contants.url_applaywithdrawals, "applaywithdrawals", map, new VolleyInterface(WithdrawActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
                @Override
                public void onSuccess(JsonElement result) {
                    showShortToastByString("提现成功");
                    finish();
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
        }
    }


    /**
     * 存储editText里的信息到SharePreferences
     *
     * @param str 需要保存的数据
     */
    private void restoreRecord(String str) {
        SharedPreferences sha = getSharedPreferences("ForME",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sha.edit();
        setStr = sha.getStringSet("userName", setStr);
        setStr.add(str);
        editor.putStringSet("userName", setStr);
        editor.commit();
        initRecord();
    }

    /**
     * 初始化下拉列表的数据，以备下拉列表时使用
     */
    private void initRecord() {
        listStr = new ArrayList<String>();
        SharedPreferences sha = getSharedPreferences("ForME",
                Context.MODE_PRIVATE);
        setStr = sha.getStringSet("userName", setStr);
        Iterator<String> it = setStr.iterator();
        while (it.hasNext()) {
            String str = (String) it.next();
            listStr.add(str);
        }
    }

    /**
     * 删除Sharepreferences中选中的数据
     *
     * @param str 需要删除的数据
     */
    public void deleteRecord(String str) {
        SharedPreferences sha = getSharedPreferences("ForME",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sha.edit();
        setStr = sha.getStringSet("userName", setStr);
        if (setStr.contains(str)) {
            setStr.remove(str);
        }
        editor.putStringSet("userName", setStr);
        editor.commit();
        initRecord();
    }

    /**
     * 显示下拉列表的popupWindow弹框
     *
     * @param view 弹出的popupWindow参照的view
     */
    @SuppressWarnings("deprecation")
    private void showPopupWindow(View view) {
        View contentView = LayoutInflater.from(WithdrawActivity.this).inflate(
                R.layout.popup_list, null);
        dropview_list = (ListView) contentView.findViewById(R.id.dropview_list);
        listAdapter = new ListAdapter(listStr);
        dropview_list.setAdapter(listAdapter);
        dropview_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position,
                                    long arg3) {
                et_kahao.setText(listStr.get(position).toString());
                popupWindow.dismiss();
            }
        });
        popupWindow = new PopupWindow(contentView,
                500, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setOutsideTouchable(true);

        popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
                return false;
            }
        });
        //如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.white));
        // 设置好参数之后再show
        popupWindow.showAsDropDown(view, 0, 0);
        popupWindow.setAnimationStyle(R.style.AnimationFade);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 44){

        }
    }
}
