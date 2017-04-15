package com.DLPort.myfragment;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.DLPort.OurActivity.AboutUsActivity;
import com.DLPort.OurActivity.CarGoSetActivity;
import com.DLPort.OurActivity.CarOwnerMyAccountActivity;
import com.DLPort.OurActivity.CarOwnerSetActivity;
import com.DLPort.OurActivity.CarOwnerStatisticsActivity;
import com.DLPort.OurActivity.CargoMyAccountActivity;
import com.DLPort.OurActivity.CargoStatisticsActivity;
import com.DLPort.OurActivity.HandbookActivity;
import com.DLPort.OurActivity.InviterActivity;
import com.DLPort.OurActivity.MerchandiseActivity;
import com.DLPort.OurActivity.PrivilegeActivity;
import com.DLPort.R;
import com.DLPort.app.ActivityCollector;
import com.DLPort.app.Constant;
import com.DLPort.myactivity.LoginIn;
import com.DLPort.mytool.MyThread;
import com.DLPort.mytool.MyToast;
import com.DLPort.myview.TitleView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/3/23.
 */
public class Fragment_myself extends Fragment {
    private static final String TAG=" Fragment_myself";
    private View view;
    private SharedPreferences sp,sp1;
    private View[] views;
    private Handler handler =new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){
                try {
                    JSONObject jsonUser = null;
                    jsonUser = new JSONObject((String) msg.obj);
                    int status = jsonUser.getInt("Status");
                    Log.i(TAG, "状态" + status);
                } catch (JSONException e) {
                    Log.d(TAG, "异常——————————————————————————————");
                    e.printStackTrace();
                }
            }else {
                Log.d(TAG,msg.what+" 服务器异常");
                //MyToast.makeText(getActivity(), msg.what + " 服务器异常");
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view =inflater.inflate(R.layout.myself_fragment,container,false);
        initTitle();
        findById();

        initView();
        return view;
    }

    private void findById() {
        views = new View[9];
        views[0] = view.findViewById(R.id.zhanghu);
        views[1] = view.findViewById(R.id.tongji);
        views[2] = view.findViewById(R.id.youhui);
        views[3] = view.findViewById(R.id.jifen);
        views[4] = view.findViewById(R.id.huodong);
        views[5] = view.findViewById(R.id.pingtai);
        views[6] = view.findViewById(R.id.shezhi);
        views[7] = view.findViewById(R.id.women);
        views[8] = view.findViewById(R.id.tuichu);
        sp =getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        sp1 =getContext().getSharedPreferences("huo", Context.MODE_PRIVATE);

    }

    private void initTitle() {
        TitleView titleView = (TitleView) view.findViewById(R.id.title_myself);
        titleView.setMiddleTextVisible(true);
        titleView.setMiddleText(R.string.My);
    }

    private void initView() {
        final int type = getArguments().getInt("Type");

        views[8].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("确认退出吗？");
                builder.setTitle("提示");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        JSONObject json = new JSONObject();
                        new MyThread(Constant.URL_UserPostBack,handler,json,getContext()).start();
                        sp1.edit().putBoolean("LOGINOK",false).apply();
                        sp.edit().putBoolean("LOGINOK",false).apply();
                        ActivityCollector.finishAll();
                        Intent intent =new Intent(getActivity(), LoginIn.class);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                       @Override
                   public void onClick(DialogInterface dialog, int which) {
                           dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });

        views[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type == 0) {
                    Intent intent = new Intent(getActivity(), CarOwnerMyAccountActivity.class);
                    startActivity(intent);
                }
                if (type == 1) {
                    Intent intent = new Intent(getActivity(), CargoMyAccountActivity.class);
                    startActivity(intent);
                }
            }
        });
        views[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type == 0) {
                    Intent intent = new Intent(getActivity(), CarOwnerStatisticsActivity.class);
                    startActivity(intent);
                }
                if (type == 1) {
                    Intent intent = new Intent(getActivity(), CargoStatisticsActivity.class);
                    startActivity(intent);
                }
            }
        });
        views[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PrivilegeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("Type", type);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        views[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MerchandiseActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("Type", type);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        views[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), InviterActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("Type", type);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        views[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HandbookActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("Type", type);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        views[6].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type == 0) {
                    Intent intent = new Intent(getActivity(), CarOwnerSetActivity.class);
                    startActivity(intent);
                }
                if (type == 1) {
                    Intent intent = new Intent(getActivity(), CarGoSetActivity.class);
                    startActivity(intent);
                }
            }
        });
        views[7].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AboutUsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("Type", type);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

}
