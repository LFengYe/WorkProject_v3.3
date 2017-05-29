package cn.guugoo.jiapeiteacher.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import cn.guugoo.jiapeiteacher.activity.TeacherCommentActivity;
import cn.guugoo.jiapeiteacher.adapter.ReservationAdapter;
import cn.guugoo.jiapeiteacher.bean.Reservation;
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
    private int teacherId;
    private int schoolId;
    private int status;
    private int TotalPage;
    private int CurrentPage = 1;
    private String token;


    public static ReservationFragment newInstance(int teacherId, int schoolId, int status,String token) {
        ReservationFragment reservationFragment = new ReservationFragment();
        Bundle args = new Bundle();
        args.putInt("teacherId", teacherId);
        args.putInt("status", status);
        args.putString("token",token);
        reservationFragment.setArguments(args);
        return reservationFragment;
    }


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
        System.out.println("paramter:" + jsonObject.toString());
        return jsonObject;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 48 && resultCode == getActivity().RESULT_OK) {
            JsonObject jsonObject = getJsonObject(1);
            new GetRefreshBookAsyncTask(getActivity(),HttpUtil.url_bookings,token).execute(jsonObject);
        }

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
                    public void onClick(String bookId, int position) {
                        Intent intent = new Intent(getActivity(), TeacherCommentActivity.class);
                        intent.putExtra("bookingId", bookId);
                        intent.putExtra("token", token);
                        startActivityForResult(intent, 48);
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
