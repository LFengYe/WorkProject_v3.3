package com.DLPort.myadapter;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.DLPort.R;
import com.DLPort.app.Constant;
import com.DLPort.mydata.Inform;
import com.DLPort.mytool.GlobalParams;
import com.DLPort.mytool.MyHandler;
import com.DLPort.mytool.MyThread;
import com.DLPort.mytool.MyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by fuyzh on 16/5/20.
 */
public class CarGoBusinessChangeAdapter extends BaseAdapter {
    private int resourceId;
    private Context context;
    private ArrayList<Inform> list;
    private AgreeRefuseBtnClick btnClick;

    public CarGoBusinessChangeAdapter(Context context, ArrayList<Inform> list, int resourceId) {
        this.context = context;
        this.list = list;
        this.resourceId = resourceId;
    }

    public CarGoBusinessChangeAdapter( Context context, ArrayList<Inform> list, int resourceId, AgreeRefuseBtnClick btnClick) {
        this.resourceId = resourceId;
        this.btnClick = btnClick;
        this.context = context;
        this.list = list;
    }

    public ArrayList<Inform> getList() {
        return list;
    }

    public void setList(ArrayList<Inform> list) {
        this.list = list;
    }

    @Override
    public int getCount() {

        if (null != list)
            return list.size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (null == convertView) {
            convertView = View.inflate(context,resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.msgContent = (TextView) convertView.findViewById(R.id.inform_content);
            viewHolder.carNumber = (TextView) convertView.findViewById(R.id.inform_car_number);
            viewHolder.carOwnerName = (TextView) convertView.findViewById(R.id.inform_car_owner_name);
            viewHolder.carOwnerTel = (TextView) convertView.findViewById(R.id.inform_car_owner_tel);
            viewHolder.presentNo = (TextView) convertView.findViewById(R.id.inform_present_no);
            viewHolder.carGoName = (TextView) convertView.findViewById(R.id.inform_car_go_name);
            viewHolder.carGoTel = (TextView) convertView.findViewById(R.id.inform_car_go_tel);
            viewHolder.agreeBtn = (Button) convertView.findViewById(R.id.agree_btn);
            viewHolder.refuseBtn = (Button) convertView.findViewById(R.id.refuse_btn);
            viewHolder.operatePromote = (TextView) convertView.findViewById(R.id.operate_promote);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final Inform inform = list.get(position);
        viewHolder.msgContent.setText(inform.getMsgContent());
        viewHolder.carNumber.setText(inform.getVehNof());
        viewHolder.carOwnerName.setText(inform.getCarOwnerName());
        viewHolder.carOwnerTel.setText(inform.getCarOwnerTel());
        viewHolder.presentNo.setText(context.getResources().getString(R.string.present_no) + inform.getPresentNo());
        viewHolder.carGoName.setText(inform.getCarGoOwnerName());
        viewHolder.carGoTel.setText(inform.getCarGoOwnerTel());
        viewHolder.agreeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                carGoReply(inform.getOrderId(), 0);
                btnClick.agreeBtnClick(position);
            }
        });
        viewHolder.refuseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                carGoReply(inform.getOrderId(), 1);
                btnClick.refuseBtnClick(position);
            }
        });

        if (inform.getFlage() == 0) {
            if (inform.getMsgStatue() == 2) {
                viewHolder.agreeBtn.setVisibility(View.GONE);
                viewHolder.refuseBtn.setVisibility(View.GONE);
                viewHolder.operatePromote.setVisibility(View.VISIBLE);
                viewHolder.operatePromote.setText(inform.getStatueName());
            } else {
                viewHolder.agreeBtn.setVisibility(View.VISIBLE);
                viewHolder.refuseBtn.setVisibility(View.VISIBLE);
                viewHolder.operatePromote.setVisibility(View.GONE);
            }
        } else {
            viewHolder.agreeBtn.setVisibility(View.GONE);
            viewHolder.refuseBtn.setVisibility(View.GONE);
            viewHolder.operatePromote.setVisibility(View.VISIBLE);
            viewHolder.operatePromote.setText(inform.getFlageName());
        }
        return convertView;
    }

    class ViewHolder {
        private TextView msgContent;
        private TextView carNumber;
        private TextView carOwnerName;
        private TextView carOwnerTel;
        private TextView presentNo;
        private TextView carGoName;
        private TextView carGoTel;
        private Button agreeBtn;
        private Button refuseBtn;
        private TextView operatePromote;
    }

    public interface AgreeRefuseBtnClick {
        void agreeBtnClick(int position);
        void refuseBtnClick(int position);

    }
}
