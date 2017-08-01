package com.guugoo.jiapeiteacher.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.guugoo.jiapeiteacher.R;
import com.guugoo.jiapeiteacher.activity.CirclesSharingActivity;
import com.guugoo.jiapeiteacher.activity.LawsActivity;
import com.guugoo.jiapeiteacher.activity.MyNewsActivity;
import com.guugoo.jiapeiteacher.activity.MyReservationActivity;
import com.guugoo.jiapeiteacher.activity.MyRecommendActivity;
import com.guugoo.jiapeiteacher.activity.ScanConfirmActivity;
import com.guugoo.jiapeiteacher.activity.WorkbenchActivity;
import com.guugoo.jiapeiteacher.base.BaseAsyncTask;
import com.guugoo.jiapeiteacher.base.Constants;
import com.guugoo.jiapeiteacher.bean.LoginInfo;
import com.guugoo.jiapeiteacher.bean.TotalData;
import com.guugoo.jiapeiteacher.util.EncryptUtils;
import com.guugoo.jiapeiteacher.util.HttpUtil;
import com.guugoo.jiapeiteacher.view.MyDialog;
import com.senter.mobilereader.ReadCardInfoActivity;
import com.xys.libzxing.zxing.activity.CaptureActivity;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;


public class HomeFragment extends Fragment implements View.OnClickListener{

    private static final int READ_CARDINFO_REQUEST_CODE = 101;
    private static final int CODE_SCAN_REQUEST_CODE = 102;
    private static final int CONFIRM_LOGIN_REQUEST_CODE = 103;

    private ImageView iv_point;
    private int startState;
    private LoginInfo loginInfo;

    private String token;
    private String cardNo;
    private int coachId;

    public HomeFragment() {
        super();
    }

    public static HomeFragment newInstance(int startState, LoginInfo loginInfo) {
        HomeFragment homeFragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putInt("startState", startState);
        args.putParcelable("loginInfo", loginInfo);
        homeFragment.setArguments(args);
        return homeFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            startState = args.getInt("startState");
            loginInfo = args.getParcelable("loginInfo");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        LinearLayout ll_my_reservation = (LinearLayout) view.findViewById(R.id.ll_my_reservation);
        LinearLayout ll_workbench = (LinearLayout) view.findViewById(R.id.ll_workbench);
        LinearLayout ll_circles_sharing = (LinearLayout) view.findViewById(R.id.ll_circles_sharing);
        LinearLayout ll_my_news = (LinearLayout) view.findViewById(R.id.ll_my_news);
        LinearLayout ll_my_recommend = (LinearLayout) view.findViewById(R.id.ll_my_recommend);
        LinearLayout ll_laws = (LinearLayout) view.findViewById(R.id.ll_laws);
        LinearLayout ll_login = (LinearLayout) view.findViewById(R.id.ll_coach_login);
        LinearLayout ll_logout = (LinearLayout) view.findViewById(R.id.ll_coach_logout);

        iv_point = (ImageView) view.findViewById(R.id.iv_point);
        setBallState(Constants.ballState);
        ll_my_reservation.setOnClickListener(this);
        ll_workbench.setOnClickListener(this);
        ll_circles_sharing.setOnClickListener(this);
        ll_my_news.setOnClickListener(this);
        ll_my_recommend.setOnClickListener(this);
        ll_laws.setOnClickListener(this);
        ll_login.setOnClickListener(this);
        ll_logout.setOnClickListener(this);


        if (startState == 1) {
            Intent intent = new Intent();
            intent.setClass(getActivity(), CirclesSharingActivity.class);
            startActivity(intent);
        }
        if (startState == 7) {
            Intent intent = new Intent();
            intent.setClass(getActivity(), WorkbenchActivity.class);
            intent.putExtra("startState", startState);
            startActivity(intent);
        }
        if (startState == 8) {
            Intent intent = new Intent();
            intent.setClass(getActivity(), MyNewsActivity.class);
            startActivity(intent);
        }

        SharedPreferences prefs = getActivity().getSharedPreferences(Constants.SHARED_NAME, MODE_PRIVATE);
        token = prefs.getString("token", "");
        cardNo = prefs.getString("CardNo", "");
        coachId = prefs.getInt("Id", 0);
    }

    public void setBallState(boolean ballState) {
        if (!ballState) {
            iv_point.setVisibility(View.INVISIBLE);
        } else {
            iv_point.setVisibility(View.VISIBLE);
        }

    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.ll_my_reservation:
                intent.setClass(getActivity(), MyReservationActivity.class);
                startActivityForResult(intent,19);
                break;
            case R.id.ll_workbench:
                intent.setClass(getActivity(), WorkbenchActivity.class);
                intent.putExtra("startState", startState);
                startActivityForResult(intent,19);
                break;
            case R.id.ll_circles_sharing:
                intent.setClass(getActivity(), CirclesSharingActivity.class);
                startActivityForResult(intent,19);
                break;
            case R.id.ll_my_news:
                intent.setClass(getActivity(), MyNewsActivity.class);
                startActivityForResult(intent,19);
                break;
            case R.id.ll_my_recommend:
                intent.setClass(getActivity(), MyRecommendActivity.class);
                startActivityForResult(intent,19);
                break;
            case R.id.ll_laws:
                intent.setClass(getActivity(), LawsActivity.class);
                startActivityForResult(intent,19);
                break;
            case R.id.ll_coach_login:
                Intent loginIntent = new Intent(getActivity(), ReadCardInfoActivity.class);
                loginIntent.putExtra("studentCardId", cardNo);
                loginIntent.putExtra("readType", 2);
                //loginIntent.putExtra("studentCardId", "420322199008197236");
                startActivityForResult(loginIntent, READ_CARDINFO_REQUEST_CODE);
                break;
            case R.id.ll_coach_logout:
                MyDialog.promoteDialog(getActivity(),
                        getString(R.string.coach_logout_confirm),
                        getString(R.string.promote),
                        new MyDialog.PromoteListener() {
                    @Override
                    public void okBtnClick() {
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("CoachID", coachId);
                        //Log.i("签退发送参数", jsonObject.toString());
                        jsonObject = EncryptUtils.encryptDES(jsonObject.toString());
                        new CoachLogOut(getActivity(), HttpUtil.url_CoachLogOut, token).execute(jsonObject);
                    }
                }).show();
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 19:
                if (resultCode == RESULT_OK)
                    setBallState(Constants.ballState);
                break;
            case READ_CARDINFO_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Intent intent = new Intent(getActivity(), CaptureActivity.class);
                    intent.putExtra("scanType", 2);
                    startActivityForResult(intent, CODE_SCAN_REQUEST_CODE);
                }
                break;
            case CODE_SCAN_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    String result = bundle.getString("result");
                    if (!TextUtils.isEmpty(result)) {
                        String[] info = result.split(",");
                        if (info.length < 3) {
                            Toast.makeText(getActivity(), getString(R.string.scan_code_error), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (loginInfo.getSchoolId() != Integer.valueOf(info[0])) {
                            Toast.makeText(getActivity(), getString(R.string.coach_auth_failed), Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intent = new Intent(getActivity(), ScanConfirmActivity.class);
                            intent.putExtra("VehNof", info[2]);
                            intent.putExtra("loginInfo", loginInfo);
                            startActivityForResult(intent, CONFIRM_LOGIN_REQUEST_CODE);
                        }
                    } else {
                        Toast.makeText(getActivity(), getString(R.string.scan_code_failed), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case CONFIRM_LOGIN_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    //签到成功, 返回到这里
                }
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    class CoachLogOut extends BaseAsyncTask {

        public CoachLogOut(Context mContext, String url, String token) {
            super(mContext, url, token);
        }

        @Override
        protected void dealResults(String s) {
            Log.i("签退返回", s);
            if (s.isEmpty()) {
                Toast.makeText(getActivity(), R.string.servlet_error, Toast.LENGTH_SHORT).show();
                return;
            }
            Gson gson = new Gson();
            TotalData totalData = gson.fromJson(s, TotalData.class);
            Toast.makeText(getActivity(), totalData.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
