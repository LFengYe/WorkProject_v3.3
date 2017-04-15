package cn.guugoo.jiapeistudent.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;

import com.alibaba.fastjson.JSONObject;
import cn.guugoo.jiapeistudent.Adapter.CoachAdapter;
import cn.guugoo.jiapeistudent.App.Constant;
import cn.guugoo.jiapeistudent.Data.Coach;
import cn.guugoo.jiapeistudent.Data.ReturnData;
import cn.guugoo.jiapeistudent.MinorActivity.SelectTimeActivity;
import cn.guugoo.jiapeistudent.R;
import cn.guugoo.jiapeistudent.Tools.DES;
import cn.guugoo.jiapeistudent.Tools.MyHandler;
import cn.guugoo.jiapeistudent.Tools.MyThread;
import cn.guugoo.jiapeistudent.Tools.MyToast;
import cn.guugoo.jiapeistudent.Tools.Utils;
import cn.guugoo.jiapeistudent.Views.PullToRefreshLayout;
import cn.guugoo.jiapeistudent.Views.PullableListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/8.
 */
public class CoachFragment extends Fragment{
    private static final String TAG = "CoachFragment";

    private View fragmentView;
    private List<Coach> listData ;
    private PullableListView listView;
    private PullToRefreshLayout layout;
    private CoachAdapter adapter;
    private SharedPreferences sp;
    private boolean isCompleated = true; //是否正在加载
    private int currentPage = 1;  //页数
    private int requestType = 0;   //请求的种类 0:第一次请求 ，1：下拉刷新，2：上拉加载
    private ImageView search;
    private EditText search_text;
    private Handler handler;


//    protected Handler handler = new MyHandler(getContext()){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            if(msg.what == 1){
//                Log.d(TAG, "handleMessage: "+msg.obj);
//                ReturnData data= JSONObject.parseObject((String) msg.obj,ReturnData.class);
//                if(data.getStatus()==0){
//                    List<Coach> coachs = JSONObject.parseArray(data.getData(),Coach.class);
//                    Log.d(TAG, "handleMessage: "+coachs.size());
//                    if (coachs.size()==0){
//                        switch (requestType){
//                            case 1:
//                                layout.refreshFinish(PullToRefreshLayout.SUCCEED);
//                                break;
//                            case 2:
//                                listView.noMoreLoading();
//                                break;
//                        }
//                    }else {
//                        if(currentPage==1){
//                            listData.clear();
//                        }
//                        listData.addAll(coachs);
//                        currentPage +=1;
//                        adapter.notifyDataSetChanged();
//                        switch (requestType){
//                            case 1:
//                                layout.refreshFinish(PullToRefreshLayout.SUCCEED);
//                                break;
//                            case 2:
//                                if (coachs.size() > 0)
//                                    listView.finishLoading();
//                                else
//                                    listView.noMoreLoading();
//                                break;
//                        }
//                    }
//                    isCompleated=true;
//                }else {
//                    MyToast.makeText(getContext(),data.getMessage());
//                    isCompleated = true;
//                    switch (requestType){
//                        case 1:
//                            layout.refreshFinish(PullToRefreshLayout.FAIL);
//                            break;
//                        case 2:
//                            listView.errorLoading();
//                            break;
//                    }
//                }
//            }
//        }
//    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentView =inflater.inflate(R.layout.fragment_coach,container,false);
        handler = new MyHandler(getActivity()){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what == 1){
                    Log.d(TAG, "handleMessage: "+msg.obj);
                    ReturnData data= JSONObject.parseObject((String) msg.obj,ReturnData.class);
                    if(data.getStatus()==0){
                        List<Coach> coachs = JSONObject.parseArray(data.getData(),Coach.class);
                        Log.d(TAG, "handleMessage: "+coachs.size());
                        if (coachs.size()==0){
                            switch (requestType){
                                case 1:
                                    layout.refreshFinish(PullToRefreshLayout.SUCCEED);
                                    break;
                                case 2:
                                    listView.noMoreLoading();
                                    break;
                            }
                        }else {
                            if(currentPage==1){
                                listData.clear();
                            }
                            listData.addAll(coachs);
                            currentPage +=1;
                            adapter.notifyDataSetChanged();
                            switch (requestType){
                                case 1:
                                    layout.refreshFinish(PullToRefreshLayout.SUCCEED);
                                    break;
                                case 2:
                                    if (coachs.size() > 0)
                                        listView.finishLoading();
                                    else
                                        listView.noMoreLoading();
                                    break;
                            }
                        }
                        isCompleated=true;
                    }else {
                        MyToast.makeText(getContext(),data.getMessage());
                        isCompleated = true;
                        switch (requestType){
                            case 1:
                                layout.refreshFinish(PullToRefreshLayout.FAIL);
                                break;
                            case 2:
                                listView.errorLoading();
                                break;
                        }
                    }
                }
            }
        };
        findById();
        init();
        return fragmentView;
    }
    private void findById() {
        listView  = (PullableListView) fragmentView.findViewById(R.id.coach_list);
        layout = (PullToRefreshLayout) fragmentView.findViewById(R.id.coach_layout);
        search = (ImageView) fragmentView.findViewById(R.id.search_image);
        search_text = (EditText) fragmentView.findViewById(R.id.search_text);
        sp = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
    }

    private void init() {
        listData = new ArrayList<Coach>();
        search_text.setHint("请输入教练名称");
        adapter = new CoachAdapter(R.layout.adapte_coach,getContext(),listData);
        listView.setAdapter(adapter);
        firstLoaded();


        layout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                if (Utils.isNetworkAvailable(getContext())) {
                    if (isCompleated) {
                        listData.clear();
                        adapter.notifyDataSetChanged();
                        currentPage = 1;
                        requestType = 1;
                        getTeacher();
                        if (!listView.mStateTextView.getText().equals(R.string.more)) {
                            listView.mStateTextView.setText(R.string.more);
                        }
                    } else {
                        pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
                        MyToast.makeText(getContext(), R.string.Toast_loading);
                    }
                } else {
                    isCompleated = true;
                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
                    MyToast.makeText(getContext(), R.string.Toast_internet);
                }
            }
        });
        listView.setOnLoadListener(new PullableListView.OnLoadListener() {
            @Override
            public void onLoad(PullableListView pullableListView) {

                if (Utils.isNetworkAvailable(getContext())) {
                    if (isCompleated) {
                        requestType = 2;
                        getTeacher();
                    } else {
                        pullableListView.finishLoading();
                        MyToast.makeText(getContext(), R.string.Toast_loading);
                    }

                } else {
                    pullableListView.finishLoading();
                    MyToast.makeText(getContext(), R.string.Toast_internet);
                }

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Coach coach= listData.get(position);
                Intent intent = new Intent(getActivity(), SelectTimeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("Branch",coach.getBranch());
                bundle.putString("Name",coach.getName());
                bundle.putInt("TeacherId",coach.getTId());
                bundle.putString("VehNof",coach.getVehNof());
                bundle.putString("Tel",coach.getTel());
                Log.d(TAG, "onItemClick: "+coach.getTId());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        search_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                select();
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select();
            }
        });

    }

    public void select(){
        if(isCompleated==true){
            listData.clear();
            currentPage = 1;
            JSONObject json= new JSONObject(true);
            Log.d(TAG, "getTimeTable: "+sp.getInt("CurrentSubject", 0));
            json.put("Subject",sp.getInt("CurrentSubject", 0));
            json.put("SchoolId",sp.getInt("SchoolId",0));
            json.put("PageIndex", currentPage);
            json.put("PageSize", 10);
            json.put("TeacherName",search_text.getText());
            json.put("StudentId",sp.getInt("Id",0));
            Log.d(TAG, "getTeacher: "+json.toString());
            isCompleated=false;
            new MyThread(Constant.URL_Teacher, handler, DES.encryptDES(json.toString())).start();
        }


    }

    private void firstLoaded() {
        if (Utils.isNetworkAvailable(getContext())) {
            getTeacher();
        } else {
            MyToast.makeText(getContext(), R.string.Toast_internet);
        }
    }

    private void getTeacher(){
        JSONObject json= new JSONObject(true);
        Log.d(TAG, "getTimeTable: "+sp.getInt("CurrentSubject", 0));
        json.put("Subject",sp.getInt("CurrentSubject", 0));
        json.put("SchoolId",sp.getInt("SchoolId",0));
        json.put("PageIndex", currentPage);
        json.put("PageSize", 10);
        json.put("StudentId",sp.getInt("Id",0));
        json.put("TeacherName",search_text.getText());
        Log.d(TAG, "getTeacher: "+json.toString());
        isCompleated=false;
        new MyThread(Constant.URL_Teacher, handler, DES.encryptDES(json.toString())).start();
    }
}
