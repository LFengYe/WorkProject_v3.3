package cn.guugoo.jiapeistudent.MinorActivity;

import android.os.Bundle;

import cn.guugoo.jiapeistudent.Fragment.TimeFragment;
import cn.guugoo.jiapeistudent.MainActivity.BaseActivity;

/**
 * Created by LFeng on 2017/5/28.
 */

public abstract class CHScrollViewActivity extends BaseActivity {

    public abstract TimeFragment getTimeFragment();

    public abstract void onScrollChanged(int l, int t, int oldl, int oldt);
}
