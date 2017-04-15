package com.DLPort.FindActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.DLPort.R;
import com.DLPort.myactivity.BaseActivity;
import com.DLPort.myview.MyDialog;
import com.DLPort.myview.TitleView;

/**
 * Created by Administrator on 2016/5/30.
 */
public class BaoXianMyActivity extends BaseActivity{
    private static final String TAG="BaoXianMyActivity";
    private View views;
    private TextView textViews;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.baoxian_my_content);
        InitTitle();
        findById();
        init();
    }

    private void init() {
        views.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog myDialog = new MyDialog(BaoXianMyActivity.this, 1);
                myDialog.setContent("车牌号");
                myDialog.setDialogCallback(new MyDialog.Dialogcallback() {
                    @Override
                    public void dialogdo(String string) {
                        textViews.setText(string);
                    }
                });
                myDialog.sethineText("例如：辽12345");
                myDialog.show();

            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BaoXianMyActivity.this, baoxianActivity.class);
                startActivity(intent);
            }
        });
    }

    private void findById() {
        views= findViewById(R.id.baoxian_A);
        textViews = (TextView) findViewById(R.id.baoxian_B);
        button = (Button) findViewById(R.id.baoxian_C);

    }


    private void InitTitle() {
        TitleView titleView = (TitleView) findViewById(R.id.baoxian_my_title);
        titleView.setBackImageVisible(true);
        titleView.setMiddleTextVisible(true);
        titleView.setMiddleText(R.string.baoxian);
        titleView.setBackImageListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }



}
