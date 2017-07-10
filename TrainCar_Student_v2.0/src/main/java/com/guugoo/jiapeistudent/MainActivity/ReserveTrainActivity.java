package com.guugoo.jiapeistudent.MainActivity;


import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.guugoo.jiapeistudent.Fragment.CoachFragment;
import com.guugoo.jiapeistudent.Fragment.TimeFragment;
import com.guugoo.jiapeistudent.Fragment.WhereFragment;
import com.guugoo.jiapeistudent.Interface.TimeRefreshListenter;
import com.guugoo.jiapeistudent.MinorActivity.CHScrollViewActivity;
import com.guugoo.jiapeistudent.R;
import com.guugoo.jiapeistudent.Views.TitleView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReserveTrainActivity extends CHScrollViewActivity implements View.OnClickListener {
    private static final String TAG ="ReserveTrainActivity";

    private TextView[] textViews;
    private TimeFragment timeFragment;
    private CoachFragment coachFragment;
    private WhereFragment whereFragment;
    private Fragment[] fragments;
    private int index=0;
    private int currentTabIndex=0;
    private  TitleView titleView;
    private TimeRefreshListenter timeRefresh;
    private PopupWindow popupWindow;
    private float alpha;

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
        textViews[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showPopupWindow(v);
            }
        });
        textViews[1] = (TextView) findViewById(R.id.reserve_coach);
        textViews[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showPopupWindow(v);
            }
        });
        textViews[2] = (TextView) findViewById(R.id.reserve_where);
        textViews[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showPopupWindow(v);
            }
        });
        textViews[3] = (TextView) findViewById(R.id.reserve_condition);
        textViews[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showPopupWindow(v);
            }
        });


        timeFragment = new TimeFragment();
        coachFragment = new CoachFragment();
        whereFragment = new WhereFragment();
        fragments = new Fragment[]{timeFragment,coachFragment,whereFragment};
        timeRefresh=timeFragment;
        Bundle bundle = new Bundle();
        bundle.putInt("type",0);
        //timeFragment.setArguments(bundle);
        timeFragment.setBundle(bundle);
    }

    @Override
    protected void init() {
        textViews[0].setSelected(true);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.reserve_fragment,timeFragment)
                .add(R.id.reserve_fragment,coachFragment)
                .add(R.id.reserve_fragment,whereFragment)
                .hide(coachFragment).hide(whereFragment)
                .show(timeFragment).commit();
        textViews[0].setOnClickListener(this);
        textViews[1].setOnClickListener(this);
        textViews[2].setOnClickListener(this);
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
                break;
        }
        BarChange();
        setTextViewsColor(index);
    }

    private void showPopupWindow(View view) {
        View contentView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_list_popup, null);
        popupWindow = new PopupWindow(contentView, 300, ViewGroup.LayoutParams.WRAP_CONTENT);

        ListView listView = (ListView) contentView.findViewById(R.id.list_content);
        String[] subjects = {"全部", "科目一", "科目二", "科目三", "科目四"};
        List<String> list = new ArrayList<>(Arrays.asList(subjects));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
        int[] location = new int[2];
        view.getLocationOnScreen(location);

        //popupWindow.update();
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());//不添加这一句, popupwindow消失不了
        popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, location[0], location[1] + view.getHeight());
        popupWindow.setOnDismissListener(new PopupDismissListener());

        alpha = 1f;
        backgroundChange();
    }

    /**
     * 返回或者点击空白位置的时候将背景透明度改回来
     */
    class PopupDismissListener implements PopupWindow.OnDismissListener{

        @Override
        public void onDismiss() {
            new Thread(new Runnable(){
                @Override
                public void run() {
                    //此处while的条件alpha不能<= 否则会出现黑屏
                    while(alpha < 1f){
                        try {
                            Thread.sleep(4);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Message msg = mHandler.obtainMessage();
                        msg.what = 1;
                        alpha += 0.01f;
                        msg.obj = alpha ;
                        mHandler.sendMessage(msg);
                    }
                }

            }).start();
        }
    }

    private void backgroundChange() {
        new Thread(new Runnable(){
            @Override
            public void run() {
                while(alpha > 0.5f){
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
                    msg.obj = alpha ;
                    mHandler.sendMessage(msg);
                }
            }

        }).start();
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    backgroundAlpha((float)msg.obj);
                    break;
            }
        }
    };

    /**
     * 设置添加屏幕的背景透明度
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
        for (int i = 0; i < 3; i++) {
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
            FragmentTransaction trx = getSupportFragmentManager()
                    .beginTransaction();
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
            Log.i(TAG, fragments[index].toString());
            trx.show(fragments[index]).commit();
            if (index == 0) {
                Bundle bundle = new Bundle();
                bundle.putInt("type",0);
                timeFragment.setBundle(bundle);
            }
        }
        //  把当前tab设为选中状态
        textViews[currentTabIndex].setSelected(false);
        textViews[index].setSelected(true);
        currentTabIndex = index;
    }
}
