package com.guugoo.jiapeistudent.MainActivity;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.guugoo.jiapeistudent.App.Constant;
import com.guugoo.jiapeistudent.Data.ReturnData;
import com.guugoo.jiapeistudent.Data.SubjectInfo;
import com.guugoo.jiapeistudent.Fragment.CoachFragment;
import com.guugoo.jiapeistudent.Fragment.ConditionFragment;
import com.guugoo.jiapeistudent.Fragment.TimeFragment;
import com.guugoo.jiapeistudent.Fragment.WhereFragment;
import com.guugoo.jiapeistudent.Interface.TimeRefreshListenter;
import com.guugoo.jiapeistudent.MinorActivity.CHScrollViewActivity;
import com.guugoo.jiapeistudent.R;
import com.guugoo.jiapeistudent.Tools.DES;
import com.guugoo.jiapeistudent.Tools.MyThread;
import com.guugoo.jiapeistudent.Views.TitleView;

import java.util.List;

public class ReserveTrainActivity extends CHScrollViewActivity implements View.OnClickListener {
    private static final String TAG = "ReserveTrainActivity";

    private SharedPreferences preferences;

    private TextView[] textViews;
    private TextView[] subjectViews;
    private RelativeLayout[] relativeLayouts;

    private TimeFragment timeFragment;
    private CoachFragment coachFragment;
    private WhereFragment whereFragment;
    private ConditionFragment conditionFragment;
    private Fragment[] fragments;
    private int index = 0;
    private int currentTabIndex = 0;
    private TitleView titleView;
    private TimeRefreshListenter timeRefresh;
    private PopupWindow popupWindow;
    private float alpha;
    private String bookType;
    private List<SubjectInfo> list;

    @Override
    public TimeFragment getTimeFragment() {
        return timeFragment;
    }

    @Override
    public void onScrollChanged(int l, int t, int oldl, int oldt) {
        timeFragment.onScrollChanged(l, t, oldl, oldt);
    }

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_reserve_train);
    }

    @Override
    protected void initTitle() {
        titleView = (TitleView) findViewById(R.id.reserve_title);
        titleView.setMiddleTextVisible(true);
        titleView.setBackImageVisible(true);
        titleView.setMiddleText(R.string.home_text3);
        titleView.setRightTextVisible(true);
        titleView.setRightText(R.string.refresh);
        titleView.setRightTextListenter(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeRefresh.onMainAction();
            }
        });

        titleView.setBackImageListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void findView() {
        textViews = new TextView[4];
        textViews[0] = (TextView) findViewById(R.id.reserve_time);
        textViews[1] = (TextView) findViewById(R.id.reserve_coach);
        textViews[2] = (TextView) findViewById(R.id.reserve_where);
        textViews[3] = (TextView) findViewById(R.id.reserve_condition);

        relativeLayouts = new RelativeLayout[3];
        relativeLayouts[0] = (RelativeLayout) findViewById(R.id.reserve_time_layout);
        relativeLayouts[1] = (RelativeLayout) findViewById(R.id.reserve_coach_layout);
        relativeLayouts[2] = (RelativeLayout) findViewById(R.id.reserve_where_layout);

        subjectViews = new TextView[3];
        subjectViews[0] = (TextView) findViewById(R.id.reserve_time_subject);
        subjectViews[1] = (TextView) findViewById(R.id.reserve_coach_subject);
        subjectViews[2] = (TextView) findViewById(R.id.reserve_where_subject);

        timeFragment = new TimeFragment();
        coachFragment = new CoachFragment();
        whereFragment = new WhereFragment();
        conditionFragment = new ConditionFragment();
        fragments = new Fragment[]{timeFragment, coachFragment, whereFragment, conditionFragment};
        timeRefresh = timeFragment;
        Bundle bundle = new Bundle();
        bundle.putInt("type", 0);
        timeFragment.setBundle(bundle);

        SharedPreferences sp = getSharedPreferences("user", Context.MODE_PRIVATE);
        bookType = sp.getString("BookType", "0");
        if (Integer.valueOf(bookType) == 1) {
            subjectViews[0].setVisibility(View.GONE);
            subjectViews[1].setVisibility(View.GONE);
            subjectViews[2].setVisibility(View.GONE);
        }

        getSubjectInfo(0);
    }

    @Override
    protected void init() {
        textViews[0].setSelected(true);
        relativeLayouts[0].setSelected(true);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.reserve_fragment, timeFragment)
                .add(R.id.reserve_fragment, coachFragment)
                .add(R.id.reserve_fragment, whereFragment)
                .hide(coachFragment).hide(whereFragment)
                .show(timeFragment).commit();
        textViews[0].setOnClickListener(this);
        textViews[1].setOnClickListener(this);
        textViews[2].setOnClickListener(this);
        textViews[3].setOnClickListener(this);

        preferences = getSharedPreferences("user", MODE_PRIVATE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reserve_time:
                index = 0;
                titleView.setRightTextVisible(true);
                break;
            case R.id.reserve_coach:
                index = 1;
                titleView.setRightTextVisible(false);
                break;
            case R.id.reserve_where:
                index = 2;
                titleView.setRightTextVisible(false);
                break;
            case R.id.reserve_condition:
                index = 3;
                titleView.setRightTextVisible(false);
                break;
        }
        if (index < 3 && (bookType.isEmpty() || Integer.valueOf(bookType) == 0)) {
            showPopupWindow(v);
        } else {
            BarChange();
            setTextViewsColor(index);
        }
    }

    @Override
    protected void processingData(ReturnData data) {
        //Log.i("获取科目信息", JSONObject.toJSONString(data));
        if (data.getStatus() == 0) {
            list = JSONObject.parseArray(data.getData(), SubjectInfo.class);
            SubjectInfo totalSubject = new SubjectInfo();
            totalSubject.setSubjectName("全部科目");
            totalSubject.setSubjectId(0);
            list.add(0, totalSubject);
        } else {
            Toast.makeText(this, getString(R.string.get_subject_failed), Toast.LENGTH_SHORT).show();
        }
    }

    private void showPopupWindow(View view) {
        View contentView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_list_popup, null);
        popupWindow = new PopupWindow(contentView, 600, 960);

        ListView listView = (ListView) contentView.findViewById(R.id.list_content);
        if (list != null && list.size() > 0) {
            ArrayAdapter<SubjectInfo> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    SubjectInfo subjectInfo = list.get(position);
                    preferences.edit().putInt("CurrentSubject", subjectInfo.getSubjectId()).commit();
                    preferences.edit().putString("CurrentSubjectName", subjectInfo.getSubjectName()).commit();
                    BarChange();
                    setTextViewsColor(index);
                    popupWindow.dismiss();
                }
            });
            int[] location = new int[2];
            view.getLocationOnScreen(location);

            popupWindow.setFocusable(true);
            popupWindow.setBackgroundDrawable(new BitmapDrawable());//不添加这一句, popupwindow消失不了
            popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, location[0], location[1] + view.getHeight());
            popupWindow.setOnDismissListener(new PopupDismissListener());

            alpha = 1f;
            backgroundChange();
        } else {
            getSubjectInfo(0);
            Toast.makeText(this, getString(R.string.subject_empty), Toast.LENGTH_SHORT).show();
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
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    public void setTitleRightTextVisible(boolean isVisible) {
        titleView.setRightTextVisible(isVisible);
    }

    private void setTextViewsColor(int index) {
        for (int i = 0; i <= 3; i++) {
            if (i == index)
                textViews[i].setTextColor(getResources().getColor(R.color.login_color));
            else textViews[i].setTextColor(getResources().getColor(R.color.text_black));
        }
    }

    private void BarChange() {
        if (currentTabIndex != index) {
            boolean timeIsHidden = timeFragment.isHidden();
            boolean coachIsHidden = coachFragment.isHidden();
            boolean whereIsHidden = whereFragment.isHidden();
            boolean conditionHidden = conditionFragment.isHidden();
            FragmentTransaction trx = getSupportFragmentManager()
                    .beginTransaction();
            if (currentTabIndex == 3) {
                if (!conditionHidden)
                    trx.hide(conditionFragment);
            }
            if (currentTabIndex == 2) {//上一个选中为按场地
                if (!timeIsHidden)
                    trx.hide(timeFragment);
                if (!whereIsHidden)
                    trx.hide(whereFragment);
            }
            if (currentTabIndex == 1) {
                if (!coachIsHidden)
                    trx.hide(coachFragment);
                if (!timeIsHidden)
                    trx.hide(timeFragment);
            }
            if (currentTabIndex == 0)
                trx.hide(timeFragment);

            if (!fragments[index].isAdded()) {
                trx.add(R.id.reserve_fragment, fragments[index]);
            }

            trx.show(fragments[index]).commit();
            if (index == 0) {
                Bundle bundle = new Bundle();
                bundle.putInt("type", 0);
                timeFragment.setBundle(bundle);
            }
        } else {
            switch (index) {
                case 0:
                    break;
                case 1:
                    coachFragment.firstLoaded();
                    break;
                case 2:
                    break;
            }
        }
        //  把当前tab设为选中状态
        textViews[currentTabIndex].setSelected(false);
        textViews[index].setSelected(true);
        if (currentTabIndex < 3)
            relativeLayouts[currentTabIndex].setSelected(false);
        if (index < 3)
            relativeLayouts[index].setSelected(true);
        currentTabIndex = index;
    }

    private void getSubjectInfo(int subjectId) {
        JSONObject json = new JSONObject(true);
        json.put("SubjectId", subjectId);
        new MyThread(Constant.URL_S_Basic_GetSubjectInfo, handler, DES.encryptDES(json.toString())).start();
    }
}
