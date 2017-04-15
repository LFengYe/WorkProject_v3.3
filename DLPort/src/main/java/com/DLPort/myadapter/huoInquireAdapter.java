package com.DLPort.myadapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.DLPort.R;
import com.DLPort.app.Constant;
import com.DLPort.myactivity.HuoInquireActivity;
import com.DLPort.mydata.HuoInquire;
import com.DLPort.mytool.GlobalParams;
import com.DLPort.mytool.MyHandler;
import com.DLPort.mytool.MyThread;
import com.DLPort.mytool.MyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2016/5/15.
 */
public class huoInquireAdapter extends BaseAdapter{
    private int resourceId;
    private Context context;
    List<HuoInquire> datas;

    public huoInquireAdapter(Context context, int resource,List<HuoInquire> objects) {

        this.context=context;
        resourceId=resource;
        datas=objects;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private Handler handler =new MyHandler(context) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                try {
                    JSONObject jsonUser = new JSONObject((String) msg.obj);
                    int status = jsonUser.getInt("Status");
                    MyToast.makeText(context, jsonUser.getString("Message"));
                    ((HuoInquireActivity)context).refreshData();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {

                MyToast.makeText(context, "服务器连接异常");
            }
        }

    };

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.Principal= (TextView) convertView.findViewById(R.id.de_Principal);
            viewHolder.CarNo = (TextView) convertView.findViewById(R.id.de_CarNo);
            viewHolder.SuitCaseNo = (TextView) convertView.findViewById(R.id.de_SuitCaseNo);
            viewHolder.Tel = (TextView) convertView.findViewById(R.id.de_Tel);
            viewHolder.OrderStatus = (TextView) convertView.findViewById(R.id.de_OrderStatus);
            viewHolder.ChargeStatus = (TextView) convertView.findViewById(R.id.de_ChargeStatus);
            viewHolder.button= (Button) convertView.findViewById(R.id.de_button);
            viewHolder.PresentNumber = (TextView) convertView.findViewById(R.id.de_PresentNumber);
            viewHolder.PutBoxID = (TextView) convertView.findViewById(R.id.de_PutBoxID);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.Principal.setText(datas.get(position).getPrincipal());
        viewHolder.CarNo.setText(datas.get(position).getCarNo());
        viewHolder.SuitCaseNo.setText(datas.get(position).getSuitCaseNo());
        viewHolder.Tel.setText(datas.get(position).getTel());
        viewHolder.OrderStatus.setText(datas.get(position).getOrderStatus());
        viewHolder.ChargeStatus.setText(datas.get(position).getChargeStatus());
        viewHolder.PresentNumber.setText(datas.get(position).getPresentNumber());
        viewHolder.PutBoxID.setText(datas.get(position).getPutBoxID());
        if (datas.get(position).getOrderStatusValue() == 3 ||
                datas.get(position).getChargeStatusValue() == 1) {
            viewHolder.button.setVisibility(View.GONE);
        } else {
            viewHolder.button.setVisibility(View.VISIBLE);
        }

        viewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GlobalParams.isNetworkAvailable(context)) {

                    JSONObject json = new JSONObject();
                    try {
                        json.put("Id", datas.get(position).getOrderId());

                        new MyThread(Constant.URL_CargoOwnerPostCharge, handler, json,context).start();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(context, "亲,请连接网络！！！", Toast.LENGTH_SHORT)
                            .show();
                }

            }
        });
        return convertView;
    }

    class ViewHolder{

        public TextView  Principal;
        public TextView  CarNo;
        public TextView  SuitCaseNo;
        public TextView  Tel;
        public TextView  OrderStatus;
        public TextView  ChargeStatus;
        public TextView  PresentNumber;
        public TextView  PutBoxID;
        public Button button;


    }


}
