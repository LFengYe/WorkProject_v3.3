package cn.guugoo.jiapeistudent.MainActivity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import cn.guugoo.jiapeistudent.Fragment.CoachFragment;
import cn.guugoo.jiapeistudent.Fragment.TimeFragment;
import cn.guugoo.jiapeistudent.Fragment.WhereFragment;
import cn.guugoo.jiapeistudent.Interface.TimeRefreshListenter;
import cn.guugoo.jiapeistudent.R;
import cn.guugoo.jiapeistudent.Views.TitleView;

public class ReserveTrainActivity extends BaseActivity implements View.OnClickListener {
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

    public TimeFragment getTimeFragment() {
        /*
        switch (index) {
            case 0:
                return timeFragment;
            case 1:
                return coachFragment;
            case 2:
                return whereFragment;
        }
        */
        return timeFragment;
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
        timeFragment.setArguments(bundle);
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
    }

    private void BarChange() {
        if (currentTabIndex != index) {
            FragmentTransaction trx = getSupportFragmentManager()
                    .beginTransaction();
            trx.hide(fragments[currentTabIndex]);

            if (!fragments[index].isAdded()) {
                trx.add(R.id.main_content, fragments[index]);
            }
            trx.show(fragments[index]).commit();
        }
        //  把当前tab设为选中状态
        textViews[currentTabIndex].setSelected(false);
        textViews[index].setSelected(true);
        currentTabIndex = index;
    }
}
