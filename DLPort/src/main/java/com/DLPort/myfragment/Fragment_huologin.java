package com.DLPort.myfragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.DLPort.R;
import com.DLPort.app.Constant;
import com.DLPort.myactivity.MainActivity;
import com.DLPort.myactivity.SignIn;
import com.DLPort.mytool.GlobalParams;
import com.DLPort.mytool.MyThread;
import com.DLPort.mytool.MyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by Administrator on 2016/3/25.
 */
public class Fragment_huologin  extends Fragment {
    public static final String TAG = "Fragment_huologin";
    private View view;
    private Button Loginin;
    private TextView signin;
    private SharedPreferences sp;
    private EditText huo_user;
    private EditText huo_password;
    private ProgressDialog dialog;
    private CheckBox autologin,rememberpass;


    private Handler handler =new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                try {
                    JSONObject jsonUser = new JSONObject((String) msg.obj);
                    Log.d(TAG, "Login的响应" + jsonUser.toString());
                    int status = jsonUser.getInt("Status");
                    Log.i(TAG, "状态" + status);
                    if (0 == status) {
                        sp.edit().putBoolean("LOGINOK",true).apply();
                        String s = jsonUser.getString("Message");
                        Log.i(TAG, s);
                        String data = jsonUser.getString("Data");
                        JSONObject jsondata = new JSONObject(data);
                        Log.d(TAG, jsondata.toString());
                        SharedPreferences.Editor edit = sp.edit();
                        edit.putString("UserId", jsondata.getString("UserId"));
                        edit.putString("Companyname", jsondata.getString("Companyname"));
                        edit.putString("Principal", jsondata.getString("Principal"));
                        edit.putString("Telephoen",jsondata.getString("Telephoen"));
                        edit.putString("LoginName", jsondata.getString("LoginName"));
                        edit.putString("Integral", jsondata.getString("Integral"));
                        edit.putString("Address", jsondata.getString("Address"));
                        edit.putString("Token", jsondata.getString("Token"));
                        edit.putString("Password", huo_password.getText().toString());
                        edit.apply();
                        dialog.dismiss();

                        /**
                         * 登录成功, 设置极光推送标签和别名
                         */
                        HashSet<String> tags = new HashSet<String>();
                        tags.add("goodsOwner");
                        JPushInterface.setAliasAndTags(getContext(), jsondata.getString("LoginName"), tags, new TagAliasCallback() {

                            @Override
                            public void gotResult(int arg0, String arg1, Set<String> arg2) {

                            }
                        });

                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("Type",1);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        getActivity().finish();

                    }else{
                        dialog.dismiss();
                        sp.edit().putBoolean("LOGINOK", false).apply();
                        switch (status){
                            case -1:
                                MyToast.makeText(getActivity(), "登陆出错");
                                break;
                            case 1:
                                MyToast.makeText(getActivity(),"用户名不存在");
                                break;
                            case 2:
                                MyToast.makeText(getActivity(),"密码错误");
                                break;
                            case 3:
                                MyToast.makeText(getActivity(),"用户已登陆");
                        }
                    }
                } catch (JSONException e) {
                    Log.d(TAG,"异常——————————————————————————————");
                    dialog.dismiss();
                    e.printStackTrace();
                }
            }else {
                Log.d(TAG,msg.what+" 服务器异常");
                dialog.dismiss();
                MyToast.makeText(getActivity(), msg.what + " 服务器异常");
            }


        }

    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view =inflater.inflate(R.layout.login_huo,container,false);
        findById();
        init();
        return view;
    }

    private void init() {
        Loginin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
            }
        });
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SignIn.class);
                Bundle bundle = new Bundle();
                bundle.putInt("Type", 1);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });
        autologin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (autologin.isChecked()) {
                    sp.edit().putBoolean("AUTO_ISCHECK", true).apply();
                } else {
                    sp.edit().putBoolean("AUTO_ISCHECK", false).apply();
                }
            }
        });
        rememberpass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (rememberpass.isChecked()) {
                    sp.edit().putBoolean("REMEMBER_ISCHECK", true).apply();
                } else {
                    sp.edit().putBoolean("REMEMBER_ISCHECK", false).apply();
                }
            }
        });

    }

    private void findById() {
        Loginin = (Button) view.findViewById(R.id.huo_login);
        signin = (TextView) view.findViewById(R.id.H_sign_in);
        huo_user = (EditText) view.findViewById(R.id.huo_id);
        huo_password = (EditText) view.findViewById(R.id.huo_password);
        autologin = (CheckBox) view.findViewById(R.id.huo_auto);
        rememberpass = (CheckBox) view.findViewById(R.id.huo_remember);
        dialog =new ProgressDialog(getContext());
        sp =getContext().getSharedPreferences("huo", Context.MODE_PRIVATE);
        huo_user.setText(sp.getString("UserName", ""));
        if(sp.getBoolean("LOGINOK",false)){
            huo_password.setText(sp.getString("Password", ""));
            Login();
            /*
            Intent intent = new Intent(getActivity(), MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("Type",1);
            intent.putExtras(bundle);
            startActivity(intent);
            getActivity().finish();
            */
        }else {
            if(sp.getBoolean("REMEMBER_ISCHECK",false)){
                rememberpass.setChecked(true);
                huo_password.setText(sp.getString("PassWord",""));
                if(sp.getBoolean("AUTO_ISCHECK",false)){
                    autologin.setChecked(true);
                }
            }
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);

        } catch (IllegalAccessException e) {

            throw new RuntimeException(e);
        }
    }

    public void Login(){

        final String password = huo_password.getText().toString().trim();
        String user = huo_user.getText().toString().trim();

        if (TextUtils.isEmpty(user) || TextUtils.isEmpty(password)) {
            Toast.makeText(getActivity(), "请,完善信息", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
            return;
        }else {
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("UserName",user);
            editor.putString("PassWord",password);
            editor.apply();
        }
        if(GlobalParams.isNetworkAvailable(getContext())) {

            dialog.setMessage("正在登录...");
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.show();
            try {
                JSONObject json = new JSONObject();
                json.put("LoginName", user);
                json.put("PassWork", password);
                int type = 1;
                json.put("LoginType", type);
                Log.d(TAG, json.toString());
                new MyThread(Constant.URL_UserLOGIN, handler, json, getActivity()).start();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            dialog.dismiss();
            Toast.makeText(getActivity(), "亲,网络未连接",
                    Toast.LENGTH_SHORT).show();
        }
    }

}
