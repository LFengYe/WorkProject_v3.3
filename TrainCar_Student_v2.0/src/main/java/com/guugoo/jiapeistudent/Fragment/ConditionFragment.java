package com.guugoo.jiapeistudent.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.guugoo.jiapeistudent.App.Constant;
import com.guugoo.jiapeistudent.Data.BranchInfo;
import com.guugoo.jiapeistudent.Data.ReturnData;
import com.guugoo.jiapeistudent.Data.SubjectInfo;
import com.guugoo.jiapeistudent.R;
import com.guugoo.jiapeistudent.Tools.DES;
import com.guugoo.jiapeistudent.Tools.MyThread;

import java.util.List;

/**
 * Created by LFeng on 2017/8/1.
 */

public class ConditionFragment extends Fragment implements View.OnClickListener {

    private TextView subjectView;
    private TextView timeSlotView;
    private TextView branchView;

    private PopupWindow popupWindow;
    private float alpha;
    private List<SubjectInfo> subjectInfos;
    private List<BranchInfo> branchInfos;
    private String[] timeSlot = new String[]{"6:00-7:00", "7:00-8:00", "8:00-9:00", "9:00-10:00", "10:00-11:00",
            "11:00-12:00", "12:00-13:00", "13:00-14:00", "14:00-15:00", "15:00-16:00", "16:00-17:00",
            "17:00-18:00", "18:00-19:00", "19:00-20:00", "20:00-21:00", "21:00-22:00", "22:00-23:00"};

    private SharedPreferences sp;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Log.i("条件查询返回", String.valueOf(msg.obj));
                    ReturnData data= JSONObject.parseObject((String) msg.obj, ReturnData.class);
                    break;
                default:
                    break;
            }
        }
    };
    private Handler subjectHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    ReturnData data= JSONObject.parseObject((String) msg.obj, ReturnData.class);
                    if (data.getStatus() == 0) {
                        subjectInfos = JSONObject.parseArray(data.getData(), SubjectInfo.class);
                        SubjectInfo totalSubject = new SubjectInfo();
                        totalSubject.setSubjectName("全部科目");
                        totalSubject.setSubjectId(0);
                        subjectInfos.add(0, totalSubject);
                    } else {
                        Toast.makeText(getActivity(), getString(R.string.get_subject_failed), Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
        }
    };
    private Handler branchHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    ReturnData data= JSONObject.parseObject((String) msg.obj, ReturnData.class);
                    if (data.getStatus() == 0) {
                        JSONObject object = JSONObject.parseObject(data.getData());
                        branchInfos = JSONObject.parseArray(object.getString("Blanchlist"), BranchInfo.class);
                    } else {
                        Toast.makeText(getActivity(), getString(R.string.get_branch_failed), Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_condition,container,false);
        findViewById(fragmentView);
        init();
        return fragmentView;
    }

    @Override
    public void onClick(View v) {
        ArrayAdapter adapter;
        switch (v.getId()) {
            case R.id.textView_subject:
                if (subjectInfos != null && subjectInfos.size() > 0) {
                    adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, subjectInfos);
                    showPopupWindow(v, 0, adapter);
                }
                break;
            case R.id.textView_TimeSlot:
                adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, timeSlot);
                showPopupWindow(v, 1, adapter);
                break;
            case R.id.textView_Branch:
                if (branchInfos != null && branchInfos.size() > 0) {
                    adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, branchInfos);
                    showPopupWindow(v, 2, adapter);
                }
                break;
        }
    }

    private void findViewById(View rootView) {
        subjectView = (TextView) rootView.findViewById(R.id.textView_subject);
        subjectView.setOnClickListener(this);
        timeSlotView = (TextView) rootView.findViewById(R.id.textView_TimeSlot);
        timeSlotView.setOnClickListener(this);
        branchView = (TextView) rootView.findViewById(R.id.textView_Branch);
        branchView.setOnClickListener(this);
    }

    private void init() {
        sp = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        getSubjectInfo(0);
        getBranchInfo();
    }

    public void GetTeacherBySame() {
        JSONObject json = new JSONObject();
        json.put("StudentId", sp.getInt("Id", 0));
        json.put("SchoolId", sp.getInt("SchoolId", 0));
        json.put("Subject", 0);
        json.put("TeacherName", "");
        json.put("BranchId", 0);
        json.put("BookingDay", "2017-08-01");
        json.put("TimeSlot", "");
        Log.i("条件查询发送", json.toJSONString());
        new MyThread(Constant.URL_S_Booking_GetTeacherBySame, handler, DES.encryptDES(json.toString())).start();
    }

    private void getSubjectInfo(int subjectId) {
        JSONObject json = new JSONObject(true);
        json.put("SubjectId", subjectId);
        new MyThread(Constant.URL_S_Basic_GetSubjectInfo, subjectHandler, DES.encryptDES(json.toString())).start();
    }

    private void getBranchInfo() {
        JSONObject json = new JSONObject(true);
        json.put("SchoolId", sp.getInt("SchoolId", 0));
        new MyThread(Constant.URL_Students_GetBlanch, branchHandler, DES.encryptDES(json.toString())).start();
    }

    private void showPopupWindow(View view, final int type, ArrayAdapter adapter) {
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_list_popup, null);
        popupWindow = new PopupWindow(contentView, 600, 960);

        ListView listView = (ListView) contentView.findViewById(R.id.list_content);
        if (subjectInfos != null && subjectInfos.size() > 0) {
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (type == 0) {
                        SubjectInfo subjectInfo = subjectInfos.get(position);
                        subjectView.setText(subjectInfo.getSubjectName());
                        subjectView.setTag(1, subjectInfo.getSubjectId());
                    }
                    if (type == 1) {
                        timeSlotView.setText(timeSlot[position]);
                    }
                    if (type == 3) {
                        BranchInfo branchInfo = branchInfos.get(position);
                        branchView.setText(branchInfo.getBranchSchoolName());
                        branchView.setTag(1, branchInfo.getId());
                    }
                    popupWindow.dismiss();
                }
            });
            int[] location = new int[2];
            view.getLocationOnScreen(location);

            popupWindow.setFocusable(true);
            popupWindow.setBackgroundDrawable(new BitmapDrawable());//不添加这一句, popupwindow消失不了
            popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, location[0], location[1] + view.getHeight());
            popupWindow.setOnDismissListener(new ConditionFragment.PopupDismissListener());

            alpha = 1f;
            backgroundChange();
        } else {
            if (type == 0) {
                getSubjectInfo(0);
                Toast.makeText(getActivity(), getString(R.string.subject_empty), Toast.LENGTH_SHORT).show();
            }
            if (type == 2) {
                getBranchInfo();
            }
        }
    }

    /**
     * 返回或者点击空白位置的时候将背景透明度改回来
     */
    class PopupDismissListener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //此处while的条件alpha不能<= 否则会出现黑屏
                    while (alpha < 1f) {
                        try {
                            Thread.sleep(4);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Message msg = mHandler.obtainMessage();
                        msg.what = 1;
                        alpha += 0.01f;
                        msg.obj = alpha;
                        mHandler.sendMessage(msg);
                    }
                }

            }).start();
        }
    }

    private void backgroundChange() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (alpha > 0.5f) {
                    try {
                        //4是根据弹出动画时间和减少的透明度计算
                        Thread.sleep(4);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Message msg = mHandler.obtainMessage();
                    msg.what = 1;
                    //每次减少0.01，精度越高，变暗的效果越流畅
                    alpha -= 0.01f;
                    msg.obj = alpha;
                    mHandler.sendMessage(msg);
                }
            }

        }).start();
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    backgroundAlpha((float) msg.obj);
                    break;
            }
        }
    };

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getActivity().getWindow().setAttributes(lp);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }
}
