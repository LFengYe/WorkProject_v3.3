package cn.com.goodsowner.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.com.goodsowner.R;


public class LoadingMoreFooter extends LinearLayout {

    private RelativeLayout mContainer;
    private TextView mText;
    private ProgressBar progressCon;
    public final static int STATE_LOADING = 0;
    public final static int STATE_COMPLETE = 1;
    public final static int STATE_NOMORE = 2;

    public LoadingMoreFooter(Context context) {
        super(context);
        initView();
    }
    /**
     * @param context
     * @param attrs
     */
    public LoadingMoreFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public void initView() {

        mContainer = (RelativeLayout) LayoutInflater.from(getContext()).inflate(R.layout.refresh_footer, null);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        setLayoutParams(lp);
        addView(mContainer, lp);
        setGravity(Gravity.CENTER);
        mText = (TextView) findViewById(R.id.tv_tip);
        progressCon = (ProgressBar) findViewById(R.id.rv_progressbar);

    }
    public void setState(int state) {
        switch (state) {
            case STATE_LOADING:
                progressCon.setVisibility(View.VISIBLE);
                mText.setText("正在加载中");
                this.setVisibility(View.VISIBLE);
                break;
            case STATE_COMPLETE:
                //mText.setText("正在加载中");
                this.setVisibility(View.GONE);
                break;
            case STATE_NOMORE:
                mText.setText("已经没有数据了");
                progressCon.setVisibility(View.GONE);
                this.setVisibility(View.VISIBLE);
                break;
        }
    }
}
