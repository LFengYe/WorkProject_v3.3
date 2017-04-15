package com.DLPort.myview;

/**
 * Created by Administrator on 2016/4/20.
 */


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.DLPort.R;

/**
 * Created by Administrator on 2016/4/16.
 */
public class TitleView extends LinearLayout  {

    private View LeftView;
    private View MiddleView;

    private ImageView BackImage;
    private TextView WineText;

    private TextView LeftText;
    private TextView RightText;
    private TextView MiddleText;
    private Button[] buttons;
    private int index;
    private int currindex;
    public TitleView(Context context){
        super(context);
        init(context);

    }
    public TitleView(Context context, AttributeSet attr) {
        super(context, attr);
        init(context);

    }
    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.title, this);
        initView();
        setListener();
    }


    private void initView() {
        BackImage = (ImageView) findViewById(R.id.title_Image_In);
        WineText = (TextView) findViewById(R.id.title_Text_In);
        LeftText = (TextView) findViewById(R.id.title_Left_text);
        RightText = (TextView) findViewById(R.id.title_Right_text);
        MiddleText = (TextView) findViewById(R.id.title_name);
        LeftView = findViewById(R.id.title_back);
        MiddleView = findViewById(R.id.title_convet);


        buttons = new Button[2];
        buttons[0] = (Button) findViewById(R.id.title_car);
        buttons[1] = (Button) findViewById(R.id.title_huo);
        buttons[0].setTextColor(0xFFFF5252);
        buttons[0].setSelected(true);
    }



    private void setListener() {
        buttons[0].setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                index = 0;
                setButton();
            }
        });
        buttons[1].setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                index = 1;
                setButton();
            }
        });

    }

    private void setButton(){
        if (currindex!=index) {
            buttons[index].setTextColor(0xFFFF5252);
            buttons[index].setSelected(true);
            buttons[currindex].setSelected(false);
            buttons[currindex].setTextColor(0xFFFFFFFF);
            currindex = index;
        }
    }
    public void setLeftViewListener(OnClickListener listener ){
        LeftView.setOnClickListener(listener);
    }
    public void setBackImageListener(OnClickListener listener){
        BackImage.setOnClickListener(listener);
    }
    public void setButton0Listenr(OnClickListener listenr){
        buttons[0].setOnClickListener(listenr);
    }
    public void setButton1Listener(OnClickListener listener){
        buttons[1].setOnClickListener(listener);
    }
    public void setLeftTextListener(OnClickListener listener){
        LeftText.setOnClickListener(listener);
    }

    public void setRightTextListenter(OnClickListener listenter){
        RightText.setOnClickListener(listenter);
    }
    public void setLeftViewVisible(boolean visible ){
        LeftView.setVisibility(visible? View.VISIBLE : View.GONE);
    }

    public void setMiddleViewVisible(boolean visible){
        MiddleView.setVisibility(visible ? View.VISIBLE : View.GONE);
    }
    public void setBackImageVisible(boolean visible){
        if(visible==true){
            LeftView.setVisibility(View.VISIBLE);
            BackImage.setVisibility(View.VISIBLE);
            WineText.setVisibility(View.GONE);
        }else {
            BackImage.setVisibility(View.GONE);
        }


    }

    public void setWineTextVisible(boolean visible){
        WineText.setVisibility(visible ? View.VISIBLE : View.GONE);
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

    public void setLeftText(String string){
        LeftText.setText(string);
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

    public void setWineText(int data){
        WineText.setText(data);
    }



}
