package cn.guugoo.jiapeiteacher.fragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import cn.guugoo.jiapeiteacher.R;
import cn.guugoo.jiapeiteacher.activity.StudentDetailsActivity;
import cn.guugoo.jiapeiteacher.activity.TeacherCommentActivity;
import cn.guugoo.jiapeiteacher.activity.WorkbenchActivity;
import cn.guugoo.jiapeiteacher.adapter.ReservationAdapter;
import cn.guugoo.jiapeiteacher.adapter.StudentAdapter;
import cn.guugoo.jiapeiteacher.adapter.StudentCommentAdapter;
import cn.guugoo.jiapeiteacher.bean.Booking;
import cn.guugoo.jiapeiteacher.bean.Reservation;
import cn.guugoo.jiapeiteacher.bean.ReservationStudent;
import cn.guugoo.jiapeiteacher.bean.TotalPaperData;
import cn.guugoo.jiapeiteacher.base.BaseAsyncTask;
import cn.guugoo.jiapeiteacher.util.EncryptUtils;
import cn.guugoo.jiapeiteacher.util.HttpUtil;
import cn.guugoo.jiapeiteacher.view.XRecyclerView;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */

public class ReservationFragment extends Fragment {

    private XRecyclerView lv_reservation_msg;
    private ArrayList<Reservation> reservations;
    private ReservationAdapter reservationAdapter;
    private PopupWindow popupWindow;
    private int teacherId;
    private int schoolId;
    private int status;
    private int TotalPage;
    private int CurrentPage = 1;
    private String token;
    private float alpha;


    public static ReservationFragment newInstance(int teacherId, int schoolId, int status,String token) {
        ReservationFragment reservationFragment = new ReservationFragment();
        Bundle args = new Bundle();
        args.putInt("teacherId", teacherId);
        args.putInt("schoolId", schoolId);
        args.putInt("status", status);
        args.putString("token",token);
        reservationFragment.setArguments(args);
        return reservationFragment;
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            teacherId = args.getInt("teacherId");
            schoolId = args.getInt("schoolId");
            status = args.getInt("status");
            token = args.getString("token");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(cn.guugoo.jiapeiteacher.R.layout.fragment_reservation, container, false);
        lv_reservation_msg = (XRecyclerView) view.findViewById(cn.guugoo.jiapeiteacher.R.id.lv_reservation_msg);


        reservations = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        lv_reservation_msg.setLayoutManager(layoutManager);
        lv_reservation_msg.setRefreshing(true);
        lv_reservation_msg.setEmptyView(view.findViewById(cn.guugoo.jiapeiteacher.R.id.tv_empty));

        JsonObject jsonObject = getJsonObject(1);
        new GetBookingAsyncTask(getActivity(),HttpUtil.url_bookings,token).execute(jsonObject);

        lv_reservation_msg.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                JsonObject jsonObject = getJsonObject(1);
                new GetRefreshBookAsyncTask(getActivity(),HttpUtil.url_bookings,token).execute(jsonObject);
            }


            @Override
            public void onLoadMore() {
                CurrentPage++;
                if (CurrentPage > TotalPage) {
                    lv_reservation_msg.loadMoreComplete();
                    lv_reservation_msg.setNoMore(true);
                    return;
                }
                JsonObject jsonObject = getJsonObject(CurrentPage);
                new GetLoadBookAsyncTask(getActivity(),HttpUtil.url_bookings,token).execute(jsonObject);
            }
        });

        return view;
    }

    private JsonObject getJsonObject(int PageIndex) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("TeacherId", teacherId);
        jsonObject.addProperty("SchoolId", schoolId);
        jsonObject.addProperty("Status", status);
        jsonObject.addProperty("PageIndex", PageIndex);
        jsonObject.addProperty("PageSize", 15);
        jsonObject = EncryptUtils.encryptDES(jsonObject.toString());
        return jsonObject;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            popupWindow = null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 48 && resultCode == getActivity().RESULT_OK) {
            JsonObject jsonObject = getJsonObject(1);
            new GetRefreshBookAsyncTask(getActivity(),HttpUtil.url_bookings,token).execute(jsonObject);
        }

    }

    private void showPopupWindow(View view, final Reservation reservation) {
        final ArrayList<ReservationStudent> students = reservation.getStudentList();
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.layout_list_popup, null);

        popupWindow = new PopupWindow(contentView, 300,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        ListView listView = (ListView) contentView.findViewById(R.id.list_content);
        StudentCommentAdapter adapter = new StudentCommentAdapter(students, getContext());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ReservationStudent student = students.get(position);
                if (student.getStatus() == 2) {
                    Intent intent = new Intent(getContext(), TeacherCommentActivity.class);
                    intent.putExtra("studentId", Integer.valueOf(student.getStudentId()));
                    intent.putExtra("token", token);
                    intent.putParcelableArrayListExtra("studentList", students);
                    intent.putExtra("selectedIndex", position);
                    intent.putExtra("bookingId", reservation.getBookingId());
                    startActivityForResult(intent, 48);
                }
                //startActivity(intent);
            }
        });

        int[] location = new int[2];
        view.getLocationOnScreen(location);

        popupWindow.update();
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, location[0], location[1] + view.getHeight());
        popupWindow.setOnDismissListener(new ReservationFragment.PopupDismissListener());

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

    class GetBookingAsyncTask extends BaseAsyncTask {


        public GetBookingAsyncTask(Context mContext, String url, String token) {
            super(mContext, url, token);
        }

        @Override
        protected void dealResults(String s) {
            if (s.isEmpty()) {
                Toast.makeText(getActivity(), cn.guugoo.jiapeiteacher.R.string.servlet_error, Toast.LENGTH_SHORT).show();
                return;
            }
            //System.out.println(s);
            Gson gson = new Gson();
            TotalPaperData totalPaperData = gson.fromJson(s, TotalPaperData.class);
            if (totalPaperData.getStatus() == 0) {
                Type listType = new TypeToken<ArrayList<Reservation>>() {
                }.getType();
                TotalPage = totalPaperData.getTotalPage();
                reservations = gson.fromJson(totalPaperData.getData(), listType);
                reservationAdapter = new ReservationAdapter(reservations, getActivity());
                lv_reservation_msg.setAdapter(reservationAdapter);
                reservationAdapter.setCommentClickListener(new ReservationAdapter.commentClickListener() {

                    @Override
                    public void onClick(View view, Reservation reservation) {
                        showPopupWindow(view, reservation);
                    }
                });

            } else {
                Toast.makeText(getActivity(), totalPaperData.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    class GetRefreshBookAsyncTask extends BaseAsyncTask {


        public GetRefreshBookAsyncTask(Context mContext, String url, String token) {
            super(mContext, url, token);
        }


        @Override
        protected void dealResults(String s) {
            if (s.isEmpty()) {
                Toast.makeText(getActivity(), cn.guugoo.jiapeiteacher.R.string.servlet_error, Toast.LENGTH_SHORT).show();
                lv_reservation_msg.refreshComplete("fail");
                return;
            }
            //System.out.println(s);
            Gson gson = new Gson();
            TotalPaperData totalPaperData = gson.fromJson(s, TotalPaperData.class);
            if (totalPaperData.getStatus() == 0) {
                lv_reservation_msg.refreshComplete("success");
                Type listType = new TypeToken<ArrayList<Reservation>>() {
                }.getType();
                reservations.clear();
                CurrentPage = 1;
                ArrayList<Reservation> new_reservations = gson.fromJson(totalPaperData.getData(), listType);
                reservations.addAll(new_reservations);
                reservationAdapter.notifyDataSetChanged();
            } else {
                lv_reservation_msg.refreshComplete("fail");
                Toast.makeText(getActivity(), totalPaperData.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    class GetLoadBookAsyncTask extends BaseAsyncTask{


        public GetLoadBookAsyncTask(Context mContext, String url, String token) {
            super(mContext, url, token);
        }

        @Override
        protected void dealResults(String s) {
            lv_reservation_msg.loadMoreComplete();
            if (s.isEmpty()) {
                Toast.makeText(getActivity(), cn.guugoo.jiapeiteacher.R.string.servlet_error, Toast.LENGTH_SHORT).show();
                return;
            }
            //System.out.println(s);
            Gson gson = new Gson();
            TotalPaperData totalPaperData = gson.fromJson(s, TotalPaperData.class);
            if (totalPaperData.getStatus() == 0) {
                Type listType = new TypeToken<ArrayList<Reservation>>() {
                }.getType();
                ArrayList<Reservation> Load_reservations = gson.fromJson(totalPaperData.getData(), listType);
                reservations.addAll(Load_reservations);
                System.out.println(reservations.size());
                reservationAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getActivity(), totalPaperData.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }
    }

}
