package cn.guugoo.jiapeistudent.MainActivity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import cn.guugoo.jiapeistudent.Fragment.CoachFragment;
import cn.guugoo.jiapeistudent.Fragment.TimeFragment;
import cn.guugoo.jiapeistudent.Fragment.WhereFragment;
import cn.guugoo.jiapeistudent.Interface.TimeRefreshListenter;
import cn.guugoo.jiapeistudent.MinorActivity.CHScrollViewActivity;
import cn.guugoo.jiapeistudent.R;
import cn.guugoo.jiapeistudent.Views.TitleView;

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
        textViews = new TextView[3];
        textViews[0] = (TextView) findViewById(R.id.reserve_time);
        textViews[1] = (TextView) findViewById(R.id.reserve_coach);
        textViews[2] = (TextView) findViewById(R.id.reserve_where);
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
        }
        BarChange();
        setTextViewsColor(index);
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
