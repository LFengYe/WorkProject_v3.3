package cn.com.caronwer.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;

import cn.com.caronwer.R;

public class SelectSexPopupWindow extends PopupWindow {

    private Button manBtn, womanBtn, cancelBtn;
    private View mSexView;

    @SuppressLint("InflateParams")
    public SelectSexPopupWindow(Context context, OnClickListener sexOnClick) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mSexView = inflater.inflate(R.layout.dialog_sex, null);
        manBtn = (Button) mSexView.findViewById(R.id.manBtn);
        womanBtn = (Button) mSexView.findViewById(R.id.womanBtn);
        cancelBtn = (Button) mSexView.findViewById(R.id.cancelBtn);

        cancelBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        manBtn.setOnClickListener(sexOnClick);
        womanBtn.setOnClickListener(sexOnClick);


        this.setContentView(mSexView);

        this.setWidth(LayoutParams.MATCH_PARENT);

        this.setHeight(LayoutParams.WRAP_CONTENT);

        this.setFocusable(true);

        this.setAnimationStyle(R.style.PopupAnimation);

        ColorDrawable dw = new ColorDrawable(0x22000000);

        this.setBackgroundDrawable(dw);


        mSexView.setOnTouchListener(new OnTouchListener() {

            @Override
            @SuppressLint("ClickableViewAccessibility")
            public boolean onTouch(View v, MotionEvent event) {

                int height = mSexView.findViewById(R.id.pop_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });

    }

}
