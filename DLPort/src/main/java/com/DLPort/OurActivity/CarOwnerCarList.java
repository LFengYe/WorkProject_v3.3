package com.DLPort.OurActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.DLPort.R;
import com.DLPort.app.Constant;
import com.DLPort.myactivity.BaseActivity;
import com.DLPort.myadapter.CarListAdapter;
import com.DLPort.mydata.CarInfo;
import com.DLPort.mytool.GlobalParams;
import com.DLPort.mytool.MyHandler;
import com.DLPort.mytool.MyThread;
import com.DLPort.mytool.MyToast;
import com.DLPort.myview.TitleView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by fuyzh on 16/6/1.
 */
public class CarOwnerCarList extends BaseActivity {

    private ListView carListView;
    private CarListAdapter adapter;
    private ArrayList<CarInfo> carInfoList;
    private int deletePosition;

    private Handler handler = new MyHandler(this) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                try {
                    JSONObject object = new JSONObject((String) msg.obj);
                    Log.i("CarListAdapter", object.toString());
                    MyToast.makeText(CarOwnerCarList.this, object.getString("Message"));
                    if (object.getInt("Status") == 0) {
                        carInfoList.remove(deletePosition);
                        adapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_list);
        initTitle();
        init();
    }

    private void initTitle() {
        TitleView titleView = (TitleView) findViewById(R.id.car_list_title);
        titleView.setLeftViewVisible(true);
        titleView.setMiddleTextVisible(true);
        titleView.setWineText(R.string.account);
        titleView.setMiddleText(R.string.account_car_list);
        View view =findViewById(R.id.title_back);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void init() {
        carListView = (ListView) findViewById(R.id.car_list_list);
//        String[] carNumberList = getIntent().getStringArrayExtra("carNumberList");
        carInfoList = getIntent().getParcelableArrayListExtra("carInfoList");
//        SimpleAdapter adapter = new SimpleAdapter(this, getData(carNumberList),
//                R.layout.car_list_item, new String[] {"carNumber"}, new int[] {R.id.car_number});
        adapter = new CarListAdapter(this, carInfoList, R.layout.car_list_item,
                handler, new CarListAdapter.DeleteBtnClick() {
            @Override
            public void deleteClick(final int position) {
                final CarInfo carInfo = carInfoList.get(position);
                new AlertDialog.Builder(CarOwnerCarList.this)
                        .setTitle(R.string.promote)
                        .setMessage(R.string.confirm_delete)
                        .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteCarInfo(carInfo.getCarId());
                                deletePosition = position;
                            }
                        })
                        .setNegativeButton(R.string.cancel, null)
                        .show();
            }
        });
        carListView.setAdapter(adapter);
        carListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
//                intent.putParcelableArrayListExtra("carInfoList", carInfoList);
                intent.putExtra("position", position);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private List<HashMap<String, String>> getData(String[] carNumberList) {
        List<HashMap<String, String>> data = new ArrayList<>();
        for (int i = 0; i < carNumberList.length; i++) {
            HashMap<String, String> map = new HashMap<>();
            map.put("carNumber", carNumberList[i]);
            data.add(map);
        }
        return data;
    }

    private void deleteCarInfo(int carId) {
        if (GlobalParams.isNetworkAvailable(this)) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("CarId", carId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            new MyThread(Constant.URL_CarOwnerPostDeleteCar, handler, jsonObject, this).start();
        } else {
            MyToast.makeText(this, "亲,网络未连接");

        }
    }
}
