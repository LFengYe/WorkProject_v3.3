package com.DLPort.myactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.DLPort.R;
import com.DLPort.myadapter.InquireAdapter;
import com.DLPort.mydata.inquire;
import com.DLPort.mytool.MyToast;
import com.DLPort.myview.MyDialog;
import com.DLPort.myview.TitleView;
import com.baidu.platform.comapi.map.B;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/14.
 */
public class InquireActivity  extends BaseActivity {
    private static final String TAG="InquireActivity";
    ListView listView;
    private List<inquire> mlist =new ArrayList<inquire>();
    private InquireAdapter adapter;
    private View view;
    private TextView textView;
    private Button button;
    private inquire inquire;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inquire);
        initTitle();
        findById();
        init();

    }

    private void findById() {
        Intent intent =getIntent();
        Bundle bundle = intent.getExtras();
        String data = bundle.getString("data");
        Log.d(TAG,data);
        listView = (ListView) findViewById(R.id.inquire_list);
        adapter = new InquireAdapter(this, R.layout.inquire_content,mlist);
        view = findViewById(R.id.S_A_BookingNumber);
        textView = (TextView) findViewById(R.id.S_BookingNumber);
        button = (Button) findViewById(R.id.inquire_Button);
        listView.setAdapter(adapter);
        display(data);
    }

    private void init() {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog myDialog = new MyDialog(InquireActivity.this, 1);
                myDialog.setContent("提单号");
                myDialog.setDialogCallback(new MyDialog.Dialogcallback() {
                    @Override
                    public void dialogdo(String string) {
                        textView.setText(string);
                    }
                });
                myDialog.sethineText("例如：123456");
                myDialog.show();

            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(textView.getText()))
                {
                    MyToast.makeText(InquireActivity.this, R.string.tidanhao);
                }else {
                    inquire =adapter.IsClichedPosition();
                    Log.d(TAG,inquire.getShipLine());
                    String BillofLading = textView.getText().toString();
                    Bundle bundle1 = getIntent().getExtras();
                    String Type = bundle1.getString("Type");

                    Intent intent = new Intent(InquireActivity.this,FangXiangActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("ShopId",inquire.getId());
                    bundle.putString("BillofLading",BillofLading);
                    bundle.putString("Type",Type);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }

            }
        });




    }

    private void display(String data) {
        try {
            JSONArray jsonArray = new JSONArray(data);
            for (int i= 0 ;i<jsonArray.length();i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                Log.d(TAG,"jdong======="+json.toString());
                String CreateTime =json.getString("InPortTime");
                String[] Str =CreateTime.split("T");
                String InPortTime = Str[0];
                String Id = json.getString("Id");
                String ShipOrder = json.getString("ShipOrder");
                String ShipLine = json.getString("ShipLine");
                String ShipCompany  = json .getString("ShipCompany");
                String ShipName = json.getString("ShipName");
                String DestinationPort = json.getString("DestinationPort");
                mlist.add(new inquire(Id,ShipCompany,ShipLine,ShipName,ShipOrder,DestinationPort,InPortTime));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
    }

    private void initTitle() {
        TitleView titleView = (TitleView) findViewById(R.id.inquire_title);
        titleView.setLeftViewVisible(true);
        titleView.setMiddleTextVisible(true);

        titleView.setWineText(R.string.O_pic1);
        titleView.setMiddleText(R.string.query);

        View view =findViewById(R.id.title_back);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
