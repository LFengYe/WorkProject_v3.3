package com.guugoo.jiapeiteacher.activity;

import android.widget.HorizontalScrollView;

import com.guugoo.jiapeiteacher.base.BaseActivity;

/**
 * Created by LFeng on 2017/5/28.
 */

public abstract class CHScrollViewActivity extends BaseActivity {
    public HorizontalScrollView mTouchView;
    public abstract void onScrollChanged(int l, int t, int oldl, int oldt);
}
