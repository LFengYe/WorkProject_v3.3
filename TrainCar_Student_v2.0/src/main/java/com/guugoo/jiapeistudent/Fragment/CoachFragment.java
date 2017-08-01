package com.guugoo.jiapeistudent.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.alibaba.fastjson.JSONObject;
import com.guugoo.jiapeistudent.Adapter.CoachAdapter;
import com.guugoo.jiapeistudent.App.Constant;
import com.guugoo.jiapeistudent.Data.Coach;
import com.guugoo.jiapeistudent.Data.ReturnData;
import com.guugoo.jiapeistudent.MainActivity.ReserveTrainActivity;
import com.guugoo.jiapeistudent.R;
import com.guugoo.jiapeistudent.Tools.DES;
import com.guugoo.jiapeistudent.Tools.MyHandler;
import com.guugoo.jiapeistudent.Tools.MyThread;
import com.guugoo.jiapeistudent.Tools.MyToast;
import com.guugoo.jiapeistudent.Tools.Utils;
import com.guugoo.jiapeistudent.Views.PullToRefreshLayout;
import com.guugoo.jiapeistudent.Views.PullableListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2016/8/8.
 */
public class CoachFragment extends Fragment{
    private static final String TAG = "CoachFragment";

    private FragmentManager manager;
    private FragmentTransaction ft;

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

    private PopupWindow popupWindow;
    private float alpha;
    private int timeType = 0;
    private int levelType = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = getFragmentManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_coach,container,false);
        handler = new MyHandler(getActivity()){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what == 1){
                    ReturnData data= JSONObject.parseObject((String) msg.obj,ReturnData.class);
                    if(data.getStatus() == 0){
                        List<Coach> coachs = JSONObject.parseArray(data.getData(),Coach.class);
                        if (coachs.size()==0) {
                            switch (requestType){
                                case 1:
                                    layout.refreshFinish(PullToRefreshLayout.SUCCEED);
                                    break;
                                case 2:
                                    listView.noMoreLoading();
                                    break;
                            }
                        }else {
                            if(currentPage == 1){
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

    @Override
    public void onHiddenChanged(boolean hidden) {
        Log.i(TAG, "onHiddenChanged");
        super.onHiddenChanged(hidden);
        if (!hidden) {
            firstLoaded();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //ft.hide(timeFragment);
    }

    private void findById() {
        listView  = (PullableListView) fragmentView.findViewById(R.id.coach_list);
        layout = (PullToRefreshLayout) fragmentView.findViewById(R.id.coach_layout);
        search = (ImageView) fragmentView.findViewById(R.id.search_image);
        search_text = (EditText) fragmentView.findViewById(R.id.search_text);

        fragmentView.findViewById(R.id.coach_start_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow(v, 1);
            }
        });
        fragmentView.findViewById(R.id.teaching_age_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow(v, 2);
            }
        });

        sp = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
    }

    private void init() {
        listData = new ArrayList<>();
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
                Bundle bundle = new Bundle();
                bundle.putInt("type",2);
                bundle.putString("Branch",coach.getBranch());
                bundle.putString("Name",coach.getName());
                bundle.putInt("TeacherId",coach.getTId());
                bundle.putString("VehNof",coach.getVehNof());
                bundle.putString("Tel",coach.getTel());
                Log.d(TAG, "onItemClick: "+coach.getTId());
                TimeFragment timeFragment = ((ReserveTrainActivity)getActivity()).getTimeFragment();
                //timeFragment.setArguments(bundle);
                timeFragment.setBundle(bundle);

                ft = manager.beginTransaction();
                ft.hide(CoachFragment.this);
                ft.show(timeFragment);
                ft.addToBackStack(null);
                ft.commit();
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

    public void firstLoaded() {
        if (Utils.isNetworkAvailable(getContext())) {
            currentPage = 1;
            getTeacher();
        } else {
            MyToast.makeText(getContext(), R.string.Toast_internet);
        }
    }

    private void getTeacher(){
        JSONObject json= new JSONObject(true);
        json.put("Subject",sp.getInt("CurrentSubject", 0));
        json.put("SchoolId",sp.getInt("SchoolId",0));
        json.put("PageIndex", currentPage);
        json.put("PageSize", 10);
        json.put("StudentId",sp.getInt("Id",0));
        json.put("TeacherName",search_text.getText());
        json.put("ComprehensiveLevelType", levelType);
        json.put("TeacherTimeType", timeType);
        Log.d(TAG, "getTeacher: "+json.toString());
        isCompleated=false;
        new MyThread(Constant.URL_Teacher, handler, DES.encryptDES(json.toString())).start();
    }

    private void showPopupWindow(View view, final int type) {
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_list_popup, null);
        popupWindow = new PopupWindow(contentView
                , ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.WRAP_CONTENT);

        ListView listView = (ListView) contentView.findViewById(R.id.list_content);
        String[] subjects = {"不限", "从低到高", "从高到低"};
        List<String> list = new ArrayList<>(Arrays.asList(subjects));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (type == 1) {
                    levelType = position;
                }
                if (type == 2) {
                    timeType = position;
                }
                firstLoaded();
                popupWindow.dismiss();
            }
        });
        int[] location = new int[2];
        view.getLocationOnScreen(location);

        //popupWindow.update();
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());//不添加这一句, popupwindow消失不了
        popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, location[0], location[1] + view.getHeight());
        popupWindow.setOnDismissListener(new CoachFragment.PopupDismissListener());

        alpha = 1f;
        backgroundChange();
    }

    /**
     * 返回或者点击空白位置的时候将背景透明度改回来
     */
    class PopupDismissListener implements PopupWindow.OnDismissListener{

        @Override
        public void onDismiss() {
            new Thread(new Runnable(){
                @Override
                public void run() {
                    //此处while的条件alpha不能<= 否则会出现黑屏
                    while(alpha < 1f){
                        try {
                            Thread.sleep(4);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Message msg = mHandler.obtainMessage();
                        msg.what = 1;
                        alpha += 0.01f;
                        msg.obj = alpha ;
                        mHandler.sendMessage(msg);
                    }
                }

            }).start();
        }
    }

    private void backgroundChange() {
        new Thread(new Runnable(){
            @Override
            public void run() {
                while(alpha > 0.5f){
                    try {
                        //4是根据弹出动画时间和减少的透明度计算
                        Thread.sleep(4);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Message msg = mHandler.obtainMessage();
                    msg.what = 1;
                    //每次减少0.01，精度越高，变暗的效果越流畅
                    alpha -= 0.01f;
                    msg.obj = alpha ;
                    mHandler.sendMessage(msg);
                }
            }

        }).start();
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    backgroundAlpha((float)msg.obj);
                    break;
            }
        }
    };

    /**
     * 设置添加屏幕的背景透明度
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getActivity().getWindow().setAttributes(lp);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }
}
