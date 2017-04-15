package com.DLPort.OurActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.DLPort.R;
import com.DLPort.myactivity.BaseActivity;
import com.DLPort.myview.MyDialog;
import com.DLPort.myview.TitleView;

/**
 * Created by fuyzh on 16/7/13.
 */
public class RechargeActivity extends BaseActivity {

    private RelativeLayout rechargeAmtLayout;

    private TextView rechargeAmt;
    private Button rechargeConfrim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);
        initTitle();
    }

    private void initTitle() {
        TitleView titleView = (TitleView) findViewById(R.id.recharge_title);
        titleView.setLeftViewVisible(true);
        titleView.setMiddleTextVisible(true);
        titleView.setWineText(R.string.statistics);
        titleView.setMiddleText(R.string.statistics_recharge);
        View view =findViewById(R.id.title_back);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        rechargeAmtLayout = (RelativeLayout) findViewById(R.id.recharge_amt_layout);
        rechargeAmtLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog dialog = new MyDialog(RechargeActivity.this, 1);
                dialog.setContent(getString(R.string.recharge_amt));
                dialog.sethineText(getString(R.string.recharge_amt_input));
                dialog.setDialogCallback(new MyDialog.Dialogcallback() {
                    @Override
                    public void dialogdo(String string) {
                        rechargeAmt.setText(string);
                    }
                });
            }
        });

        rechargeAmt = (TextView) findViewById(R.id.recharge_amt);
        rechargeConfrim = (Button) findViewById(R.id.recharge_btn);
        rechargeConfrim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
