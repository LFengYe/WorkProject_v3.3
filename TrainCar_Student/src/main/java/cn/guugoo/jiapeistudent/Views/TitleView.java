package cn.guugoo.jiapeistudent.Views;

/**
 * Created by Administrator on 2016/4/20.
 */


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.guugoo.jiapeistudent.R;

/**
 * Created by Administrator on 2016/4/16.
 */
public class TitleView extends LinearLayout {

    private View LeftView;
    private ImageView BackImage;
    private TextView LeftText;
    private TextView RightText;
    private TextView MiddleText;
    public TitleView(Context context){
        super(context);
        init(context);

    }
    public TitleView(Context context, AttributeSet attr) {
        super(context, attr);
        init(context);

    }
    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_title, this);
        initView();
    }


    private void initView() {
        BackImage = (ImageView) findViewById(R.id.title_image_back);
        LeftText = (TextView) findViewById(R.id.title_left_text);
        RightText = (TextView) findViewById(R.id.title_right_text);
        MiddleText = (TextView) findViewById(R.id.title_center_text);
        LeftView = findViewById(R.id.title_back);
    }



    public void setLeftViewListener(OnClickListener listener ){
        LeftView.setOnClickListener(listener);
    }
    public void setBackImageListener(OnClickListener listener){
        BackImage.setOnClickListener(listener);
    }

    public void setLeftTextListener(OnClickListener listener){
        LeftText.setOnClickListener(listener);
    }

    public void setRightTextListenter(OnClickListener listenter){
        RightText.setOnClickListener(listenter);
    }
    public void setLeftViewVisible(boolean visible ){
        LeftView.setVisibility(visible? VISIBLE : GONE);
        LeftText.setVisibility(visible? VISIBLE : GONE);
        BackImage.setVisibility(visible? VISIBLE : GONE);
    }

    public void setBackImageVisible(boolean visible){
        if(visible==true){
            LeftView.setVisibility(VISIBLE);
            BackImage.setVisibility(VISIBLE);
            LeftText.setVisibility(GONE);
        }else {
            BackImage.setVisibility(GONE);
        }
    }

    public void setLeftTextVisible(boolean visible){
        LeftText.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void setMiddleTextVisible(boolean visible){
        MiddleText.setVisibility(visible ? View.VISIBLE : View.GONE);
    }
    public void setRightTextVisible(boolean visible){
        RightText.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void setLeftText(String s){
        LeftText.setText(s);
    }
    public void setLeftText(int data){
        LeftText.setText(data);
    }
    public void setMiddleText(String s){
        MiddleText.setText(s);
    }
    public void setMiddleText(int data){
        MiddleText.setText(data);
    }

    public void setRightText(String s){
        RightText.setText(s);
    }
    public void setRightText(int data) {
        RightText.setText(data);
    }



}
