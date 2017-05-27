package cn.guugoo.jiapeistudent.Views;


import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

import cn.guugoo.jiapeistudent.MainActivity.ReserveTrainActivity;

public class CHScrollView2 extends HorizontalScrollView {


	ReserveTrainActivity activity;
	
	public CHScrollView2(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		activity = (ReserveTrainActivity) context;

	}

	
	public CHScrollView2(Context context, AttributeSet attrs) {
		super(context, attrs);
		activity = (ReserveTrainActivity) context;
	}

	public CHScrollView2(Context context) {
		super(context);
		activity = (ReserveTrainActivity) context;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		//进行触摸赋值
		activity.getTimeFragment().mTouchView = this;
		return super.onTouchEvent(ev);
	}
	
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		//当当前的CHSCrollView被触摸时，滑动其它
		if(activity.getTimeFragment().mTouchView == this) {
			activity.getTimeFragment().onScrollChanged(l, t, oldl, oldt);
		}else{
			super.onScrollChanged(l, t, oldl, oldt);
		}
	}
}
