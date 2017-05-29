package cn.guugoo.jiapeistudent.MinorActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import cn.guugoo.jiapeistudent.Fragment.TimeFragment;
import cn.guugoo.jiapeistudent.Interface.TimeRefreshListenter;
import cn.guugoo.jiapeistudent.MainActivity.BaseActivity;
import cn.guugoo.jiapeistudent.R;
import cn.guugoo.jiapeistudent.Views.TitleView;

public class WhereSelectTimeActivity extends CHScrollViewActivity {

    private static final String TAG = "WhereSelectTimeActivity";
    private TimeFragment timeFragment;
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
        setContentView(R.layout.activity_where_select_time);
    }

    @Override
    protected void initTitle() {
        TitleView titleView = (TitleView) findViewById(R.id.where_select_title);
        titleView.setMiddleTextVisible(true);
        titleView.setBackImageVisible(true);
        titleView.setMiddleText(R.string.select_time);
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
        timeFragment = new TimeFragment();
        timeRefresh = timeFragment;
        Bundle bundle = new Bundle();
        bundle.putInt("type",1);
        bundle.putInt("BranchId",getIntent().getIntExtra("BranchId",0));
        Log.d(TAG, "findView: "+getIntent().getIntExtra("BranchId",0));
        timeFragment.setArguments(bundle);
    }

    @Override
    protected void init() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.where_select_fragment,timeFragment)
                .show(timeFragment).commit();
    }
}
