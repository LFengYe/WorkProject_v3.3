package cn.com.caronwer.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Map;

import cn.com.caronwer.R;
import cn.com.caronwer.adapter.ChargeScheduleAdapter;
import cn.com.caronwer.base.BaseActivity;
import cn.com.caronwer.base.Contants;
import cn.com.caronwer.bean.BalanceInfo;
import cn.com.caronwer.util.EncryptUtil;
import cn.com.caronwer.util.HttpUtil;
import cn.com.caronwer.util.NetworkUtil;
import cn.com.caronwer.util.SPtils;
import cn.com.caronwer.util.VolleyInterface;
import cn.com.caronwer.view.XRecyclerView;

public class ChargeScheduleActivity extends BaseActivity {

    private ImageView iv_left_white;
    private ChargeScheduleAdapter chargeScheduleAdapter;
    private BalanceInfo balanceInfo;
    private ArrayList<BalanceInfo.ListBean> listBean;
    private TextView tv_sumConsum;
    private XRecyclerView rv_charge_schedule;
    private int CurrentPage = 1;

    @Override
    protected int getLayout() {
        return R.layout.activity_charge_schedule;
    }

    @Override
    protected void findById() {
        iv_left_white = (ImageView) findViewById(R.id.iv_left_white);
        tv_sumConsum = (TextView) findViewById(R.id.tv_sumConsum);
        rv_charge_schedule = (XRecyclerView) findViewById(R.id.rv_charge_schedule);
    }

    @Override
    protected void initData() {
        listBean = new ArrayList<>();
        chargeScheduleAdapter = new ChargeScheduleAdapter(listBean);

    }

    @Override
    protected void initView() {
        iv_left_white.setOnClickListener(this);
        getChargeSchedule(CurrentPage, 0);
        rv_charge_schedule.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                CurrentPage = 1;
                getChargeSchedule(CurrentPage, 0);
            }

            @Override
            public void onLoadMore() {
                CurrentPage++;
                getChargeSchedule(CurrentPage, 1);
            }
        });
    }


    private void getChargeSchedule(int PageIndex, final int ways) {
        if (!NetworkUtil.isConnected(ChargeScheduleActivity.this)) {
            showShortToastByString(getString(R.string.Neterror));
            if (ways == 0) {
                rv_charge_schedule.refreshComplete("fail");
            } else {
                rv_charge_schedule.loadMoreComplete();
            }
            return;
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("UserId", SPtils.getString(this, "UserId", "00000000-0000-0000-0000-000000000000"));
        jsonObject.addProperty("UserType", 2);
        jsonObject.addProperty("PageIndex", PageIndex);
        jsonObject.addProperty("PageSize", 15);

        Map<String, String> map = EncryptUtil.encryptDES(jsonObject.toString());

        HttpUtil.doPost(ChargeScheduleActivity.this, Contants.url_getUserBalanceList, "getUserBalanceList", map, new VolleyInterface(ChargeScheduleActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onSuccess(JsonElement result) {
                Gson gson = new Gson();

                BalanceInfo balanceInfo = gson.fromJson(result, BalanceInfo.class);
                tv_sumConsum.setText(String.format("总支出：¥%s元", balanceInfo.getSumConsum()));
                ArrayList<BalanceInfo.ListBean> newListBeen = (ArrayList<BalanceInfo.ListBean>) balanceInfo.getList();
                if (ways == 0) {
                    rv_charge_schedule.refreshComplete("success");
                    CurrentPage = 1;
                    listBean.clear();
                    listBean.addAll(newListBeen);
                } else {
                    rv_charge_schedule.loadMoreComplete();
                    if (newListBeen.size() < 15) {
                        rv_charge_schedule.setNoMore(true);
                    }
                    listBean.addAll(newListBeen);
                }
                chargeScheduleAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(VolleyError error) {

                if (ways == 0) {
                    rv_charge_schedule.refreshComplete("fail");

                } else {
                    rv_charge_schedule.loadMoreComplete();
                }

            }

            @Override
            public void onStateError(int sta, String msg) {
                if (!TextUtils.isEmpty(msg)) {
                    showShortToastByString(msg);
                }
                if (ways == 0) {
                    rv_charge_schedule.refreshComplete("fail");
                } else {
                    rv_charge_schedule.loadMoreComplete();
                }
            }
        });
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
