package cn.com.goodsowner.activity;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

import cn.com.goodsowner.adapter.ConvoyAdapter;
import cn.com.goodsowner.base.BaseActivity;
import cn.com.goodsowner.base.Contants;
import cn.com.goodsowner.bean.ConvoyInfo;
import cn.com.goodsowner.util.EncryptUtil;
import cn.com.goodsowner.util.HttpUtil;
import cn.com.goodsowner.util.LogUtil;
import cn.com.goodsowner.util.VolleyInterface;

public class MyConvoyActivity extends BaseActivity {

    private TextView tv_title;
    private TextView tv_right;
    private TextView tv_empty;
    private ImageView iv_left_white;
    private ListView lv_my_convoy;
    private ConvoyAdapter mConvoyAdapter;
    ArrayList<ConvoyInfo> mConvoyInfos;
    private String userId;

    @Override
    protected int getLayout() {
        return cn.com.goodsowner.R.layout.activity_my_convoy;
    }

    @Override
    protected void findById() {
        RelativeLayout rl_head = (RelativeLayout) findViewById(cn.com.goodsowner.R.id.in_head);
        assert rl_head != null;
        tv_title = (TextView) rl_head.findViewById(cn.com.goodsowner.R.id.tv_title);
        tv_right = (TextView) rl_head.findViewById(cn.com.goodsowner.R.id.tv_right);
        tv_empty = (TextView)findViewById(cn.com.goodsowner.R.id.tv_empty);
        iv_left_white = (ImageView) rl_head.findViewById(cn.com.goodsowner.R.id.iv_left_white);
        lv_my_convoy = (ListView) findViewById(cn.com.goodsowner.R.id.lv_my_convoy);
    }

    @Override
    protected void initData() {
        userId = getIntent().getStringExtra("UserId");
        mConvoyInfos = new ArrayList<>();
        mConvoyAdapter = new ConvoyAdapter(mConvoyInfos,this);
    }

    @Override
    protected void initView() {
        tv_title.setText(cn.com.goodsowner.R.string.myConvoy);
        tv_right.setVisibility(View.GONE);
        lv_my_convoy.setAdapter(mConvoyAdapter);
        lv_my_convoy.setEmptyView(tv_empty);
        lv_my_convoy.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                deleteTransportTeam(mConvoyInfos.get(i).getTransporterId(),i);
                return true;
            }
        });
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("UserId",userId);
        Map<String,String> map = EncryptUtil.encryptDES(jsonObject.toString());

        HttpUtil.doPost(MyConvoyActivity.this, Contants.url_getUserVehicleTeam, "getUserVehicleTeam", map, new VolleyInterface(MyConvoyActivity.this,VolleyInterface.mListener,VolleyInterface.mErrorListener) {
            @Override
            public void onSuccess(JsonElement result) {
                LogUtil.i(result.toString());
                Gson gson = new Gson();
                Type listType = new TypeToken<ArrayList<ConvoyInfo>>() {
                }.getType();
                ArrayList<ConvoyInfo> convoyInfos = gson.fromJson(result,listType);
                mConvoyInfos.addAll(convoyInfos);
                mConvoyAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(VolleyError error) {
                LogUtil.i(error.toString());
//                LogUtil.i("register",error.networkResponse.headers.toString());
//                LogUtil.i("register",error.networkResponse.statusCode+"");
            }

            @Override
            public void onStateError() {

            }
        });
        iv_left_white.setOnClickListener(this);
        tv_right.setOnClickListener(this);
    }


    private void deleteTransportTeam(final String TransporterId, final int position){
    new AlertDialog.Builder(MyConvoyActivity.this).
    setTitle("提示").
    setMessage("确定删除此信息").
    setPositiveButton("确定", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("SendUserId", Contants.userId);
            jsonObject.addProperty("TransporterId", TransporterId);
            Map<String, String> map = EncryptUtil.encryptDES(jsonObject.toString());
            HttpUtil.doPost(MyConvoyActivity.this, Contants.url_deleteTransportTeam, "deleteTransportTeam", map, new VolleyInterface(MyConvoyActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
                @Override
                public void onSuccess(JsonElement result) {
                    LogUtil.i(result.toString());
                    mConvoyInfos.remove(position);
                    mConvoyAdapter.notifyDataSetChanged();

                }

                @Override
                public void onError(VolleyError error) {
                    showShortToastByString(getString(cn.com.goodsowner.R.string.timeoutError));
//                LogUtil.i("hint",error.networkResponse.headers.toString());
//                LogUtil.i("hint",error.networkResponse.statusCode+"");
                }

                @Override
                public void onStateError() {
                }
            });
        }
    }).
    setNegativeButton("取消", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            // TODO Auto-generated method stub
        }
    }).show();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case cn.com.goodsowner.R.id.iv_left_white:
                finish();
                break;
        }
    }
}
