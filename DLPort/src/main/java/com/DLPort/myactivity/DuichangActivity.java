package com.DLPort.myactivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.DLPort.R;
import com.DLPort.myadapter.DuiAdapter;
import com.DLPort.mydata.Duichang;
import com.DLPort.myview.TitleView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/16.
 */
public class DuichangActivity extends BaseActivity {
    private static final String TAG="DuichangActivity";
    private List<Duichang> datas = new ArrayList<Duichang>();
    private ListView listView;
    private DuiAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.duichuang_name);
        initTitle();
        init();
    }

    private void init() {
        final Intent intent = getIntent();
        final Bundle bundle =intent.getExtras();
        String data = bundle.getString("data");

        Log.d(TAG,data);
        listView = (ListView) findViewById(R.id.duichang_list);
        adapter = new DuiAdapter(this, R.layout.dui_chang_name_list,datas);
        listView.setAdapter(adapter);
        display(data);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Duichang data= datas.get(position);
                Intent  intent1 = new Intent();
                Bundle bundle1= new Bundle();
                bundle1.putString("StorageYardNmae",data.getStorageYardNmae());
                bundle1.putString("Address",data.getAddress());
                bundle1.putString("Price",data.getPrice());
                intent1.putExtras(bundle1);
                setResult(RESULT_OK,intent1);
                finish();
            }
        });

    }

    private void display(String data) {

        try {
            JSONArray jsonArray = new JSONArray(data);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                Log.d(TAG,json.toString());
                int i1 = json.getInt("Id");
                String StorageYardNmae = json.getString("StorageYardNmae");
                String Address = json .getString("Address");
                String Price =json.getString("Price");
                datas.add(new Duichang(i1,StorageYardNmae,Address,Price));
            }

        }catch(JSONException e){
            e.printStackTrace();
        }

    }
    private void initTitle() {
        TitleView titleView = (TitleView) findViewById(R.id.duchuang_title);
        titleView.setLeftViewVisible(true);
        titleView.setMiddleTextVisible(true);
        titleView.setWineText(R.string.O_pic2);
        titleView.setMiddleText(R.string.Duichangname);
        View view =findViewById(R.id.title_back);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
