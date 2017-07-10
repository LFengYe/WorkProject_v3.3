package cn.com.goodsowner.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

import cn.com.goodsowner.adapter.InvoiceInfoAdapter;
import cn.com.goodsowner.base.BaseActivity;
import cn.com.goodsowner.base.Contants;
import cn.com.goodsowner.bean.InvoiceInfo;
import cn.com.goodsowner.util.EncryptUtil;
import cn.com.goodsowner.util.HttpUtil;
import cn.com.goodsowner.util.LogUtil;
import cn.com.goodsowner.util.NetworkUtil;
import cn.com.goodsowner.util.VolleyInterface;
import cn.com.goodsowner.view.XRecyclerView;

public class MyInvoiceActivity extends BaseActivity {

    private Button bt_ok;
    private TextView tv_title;
    private TextView tv_right;
    private ImageView iv_left_white;
    private ArrayList<InvoiceInfo> invoiceInfos;
    private InvoiceInfoAdapter invoiceInfoAdapter;
    private XRecyclerView rv_invoice;
    private int CurrentPage = 1;

    @Override
    protected int getLayout() {
        return cn.com.goodsowner.R.layout.activity_my_invoice;
    }

    @Override
    protected void findById() {
        RelativeLayout rl_head = (RelativeLayout) findViewById(cn.com.goodsowner.R.id.in_head);
        assert rl_head != null;
        tv_title = (TextView) rl_head.findViewById(cn.com.goodsowner.R.id.tv_title);
        tv_right = (TextView) rl_head.findViewById(cn.com.goodsowner.R.id.tv_right);
        iv_left_white = (ImageView) rl_head.findViewById(cn.com.goodsowner.R.id.iv_left_white);
        bt_ok = (Button) findViewById(cn.com.goodsowner.R.id.bt_ok);
        rv_invoice = (XRecyclerView) findViewById(cn.com.goodsowner.R.id.rv_invoice);

    }

    @Override
    protected void initData() {
        invoiceInfos = new ArrayList<>();
        invoiceInfoAdapter = new InvoiceInfoAdapter(this, invoiceInfos);
    }

    @Override
    protected void initView() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_invoice.setLayoutManager(layoutManager);
        rv_invoice.setAdapter(invoiceInfoAdapter);
        getInvoice(1, 0);
        rv_invoice.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                getInvoice(CurrentPage, 0);
            }

            @Override
            public void onLoadMore() {
                CurrentPage++;
                getInvoice(CurrentPage, 1);
            }
        });

        tv_title.setText(cn.com.goodsowner.R.string.myInvoice);
        tv_right.setText(cn.com.goodsowner.R.string.invoice_rule);
        iv_left_white.setOnClickListener(this);
        tv_right.setOnClickListener(this);
        bt_ok.setOnClickListener(this);
    }

    private void getInvoice(int PageIndex, final int ways) {
        if (!NetworkUtil.isConnected(MyInvoiceActivity.this)) {
            showShortToastByString(getString(cn.com.goodsowner.R.string.Neterror));
            if (ways == 0) {
                rv_invoice.refreshComplete("fail");
            } else {
                rv_invoice.loadMoreComplete();
            }
            return;
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("UserId", Contants.userId);
        jsonObject.addProperty("PageIndex", PageIndex);
        jsonObject.addProperty("PageSize", 15);
        LogUtil.i(jsonObject.toString());
        Map<String, String> map = EncryptUtil.encryptDES(jsonObject.toString());

        HttpUtil.doPost(MyInvoiceActivity.this, Contants.url_getNotInvoiceList, "getInvoiceList", map, new VolleyInterface(MyInvoiceActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onSuccess(JsonElement result) {
                LogUtil.i("result" + result.toString());
                Gson gson = new Gson();
                Type listType = new TypeToken<ArrayList<InvoiceInfo>>() {
                }.getType();
                ArrayList<InvoiceInfo> newInvoiceInfos = gson.fromJson(result, listType);
                if (ways == 0) {
                    rv_invoice.refreshComplete("success");
                    CurrentPage = 1;
                    invoiceInfos.clear();
                    invoiceInfos.addAll(newInvoiceInfos);
                } else {
                    rv_invoice.loadMoreComplete();
                    if (newInvoiceInfos.size() < 15) {
                        rv_invoice.setNoMore(true);
                    }
                    invoiceInfos.addAll(newInvoiceInfos);
                }
                invoiceInfoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(VolleyError error) {

                if (ways == 0) {
                    rv_invoice.refreshComplete("fail");

                } else {
                    rv_invoice.loadMoreComplete();
                }

            }

            @Override
            public void onStateError() {
                if (ways == 0) {
                    rv_invoice.refreshComplete("fail");
                } else {
                    rv_invoice.loadMoreComplete();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case cn.com.goodsowner.R.id.iv_left_white:
                finish();
                break;
            case cn.com.goodsowner.R.id.tv_right:
                intent.setClass(MyInvoiceActivity.this, InvoiceRuleActivity.class);
                startActivity(intent);
                break;
            case cn.com.goodsowner.R.id.bt_ok:
                intent.setClass(MyInvoiceActivity.this, ApplyInvoiceActivity.class);
                String orders = "";
                double money = 0;
                int size = invoiceInfos.size();
                for (int i = 0; i < size; i++) {
                    String orderId = invoiceInfos.get(i).getOrderNo();
                    boolean state = invoiceInfos.get(i).ischeck();
                    if (state) {
                        orders = orderId + ",";
                        money = money + invoiceInfos.get(i).getOrderAmount();
                    }
                }

                System.out.println("money" + money);
                if (orders.equals("")) {
                    showShortToastByString("请选择发票");
                    return;
                }
                intent.putExtra("orders", orders);
                intent.putExtra("money", money);
                startActivityForResult(intent, 1);
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            getInvoice(CurrentPage, 0);
        }
    }
}
