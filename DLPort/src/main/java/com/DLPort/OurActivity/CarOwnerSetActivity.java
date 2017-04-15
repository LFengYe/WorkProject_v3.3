package com.DLPort.OurActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.DLPort.R;
import com.DLPort.myactivity.BaseActivity;
import com.DLPort.myview.TitleView;

/**
 * Created by Administrator on 2016/5/13.
 */
public class CarOwnerSetActivity extends BaseActivity {
    private View view1,view2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carowner_set);
        initTitle();
        init();
    }

    private void init() {
        view1= findViewById(R.id.set_me);
        view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CarOwnerSetActivity.this, CarOwnerSetInfoActivity.class);
                startActivity(intent);
            }
        });

        view2= findViewById(R.id.set_add_car);
        view2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CarOwnerSetActivity.this,CarOwnerSetAddCarActivity.class);
                startActivity(intent);
            }
        });

    }

    private void initTitle() {
        TitleView titleView = (TitleView) findViewById(R.id.set_title);
        titleView.setLeftViewVisible(true);
        titleView.setMiddleTextVisible(true);
        titleView.setWineText(R.string.My);
        titleView.setMiddleText(R.string.set);
        View view =findViewById(R.id.title_back);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}

